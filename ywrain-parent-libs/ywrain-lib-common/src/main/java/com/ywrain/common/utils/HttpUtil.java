package com.ywrain.common.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ywrain.common.support.CommonException;

import okhttp3.ConnectionPool;
import okhttp3.Dns;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Headers.Builder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * HTTP请求工具类，内部使用OkHttp3
 * 
 * <pre>
 *     默认共用OkHttpClient单实例
 *     默认链接超时: 2s
 *     默认读超时时间: 10s
 *     默认最大并发请求数: 64
 *     默认每个Host的最大并发数: [nCpu * 2, 16]
 * </pre>
 *
 * @author weipengfei@youcheyihou.com
 * @since 1.2.0
 */
public class HttpUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    private static MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private static Map<String, List<InetAddress>> mapLocalDns;

    private static OkHttpClient client;

    static {
        int nCpu = Runtime.getRuntime().availableProcessors();
        int maxReqPerHost = nCpu * 2;
        maxReqPerHost = maxReqPerHost < 16 ? maxReqPerHost : 16;
        client = new OkHttpClient.Builder().dns(new HttpUtil.LocalOkHttpDns()).connectTimeout(2, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS)
                .build();
        // 扩容默认的单HOST最大并发数
        client.dispatcher().setMaxRequestsPerHost(maxReqPerHost);
        // 初始化DNS映射表
        mapLocalDns = new ConcurrentHashMap<>(1);
    }

    /**
     * 本地的DNS配置
     *
     * @author weipengfei@youcheyihou.com
     */
    public static class LocalOkHttpDns implements Dns {

        @Override
        public List<InetAddress> lookup(String hostname) throws UnknownHostException {
            if (!MapUtil.isEmpty(mapLocalDns)) {
                List<InetAddress> inetAddresses = mapLocalDns.get(hostname);
                if (CollectionUtil.isNotEmpty(inetAddresses)) {
                    return inetAddresses;
                }
            }

            return Dns.SYSTEM.lookup(hostname);
        }
    }

    private static String doRequest(Request request) throws IOException {
        Response response = null;
        response = client.newCall(request).execute();
        if (response.isSuccessful() && response.body() != null) {
            String result = response.body().string();
            if (response != null) {
                response.close();
            }
            return result;
        }
        return null;
    }

    private static HttpResult doRequestResult(Request request) throws IOException {
        Response response = null;
        response = client.newCall(request).execute();
        HttpResult result = new HttpResult();
        result.setStatusCode(response.code());
        if (response.isSuccessful() && response.body() != null) {
            ResponseBody body = response.body();
            result.setResult(body.string());
            result.setResultByte(body.bytes());
            if (response != null) {
                response.close();
            }
            return result;
        }
        return null;
    }

    private static Headers createHeader(Map<String, String> headers) {
        if (headers != null && !headers.isEmpty()) {
            Builder headersBuilder = new Builder();
            headers.forEach((k, v) -> headersBuilder.add(k, v));
            return headersBuilder.build();
        }
        return null;
    }

    /**
     * 增加本地的DnsHost配置
     *
     * @param map 映射表 host -> ip1...ip2
     */
    public static void addDnsHost(String hostname, String... ips) throws UnknownHostException {
        synchronized (mapLocalDns) {
            List<InetAddress> inetAddresses = mapLocalDns.get(hostname);
            if (CollectionUtil.isEmpty(inetAddresses)) {
                inetAddresses = new ArrayList<>(2);
                mapLocalDns.put(hostname, inetAddresses);
            }
            for (String ip : ips) {
                inetAddresses.add(InetAddress.getByName(ip));
            }
        }
    }

    /**
     * 设置本地的DnsHost配置
     *
     * @param map 映射表 host -> ip1...ip2
     */
    public static void setDnsHost(String hostname, String... ips) throws UnknownHostException {
        synchronized (mapLocalDns) {
            List<InetAddress> inetAddresses = new ArrayList<>(2);
            for (String ip : ips) {
                inetAddresses.add(InetAddress.getByName(ip));
            }
            mapLocalDns.put(hostname, inetAddresses);
        }
    }

    /**
     * 设置当前httpClient的超时配置，单位：秒
     *
     * @param connTimeout 链接超时
     * @param readTimeout 读取超时
     */
    public static void setTimeout(int connTimeout, int readTimeout) {
        client = client.newBuilder().connectTimeout(connTimeout, TimeUnit.SECONDS).readTimeout(readTimeout, TimeUnit.SECONDS).build();
    }


    /**
     * 设置当前httpClient的超时配置，单位：秒
     *
     * @param connTimeout 链接超时
     * @param readTimeout 读取超时
     */
    public static void setConnectionPool(ConnectionPool pool) {
        client = client.newBuilder().connectionPool(pool).build();
    }

    /**
     * 设置最大并发数
     *
     * @param maxRequestsPerHost 最大并发数
     */
    public static void setMaxRequest(int maxRequests) {
        client.dispatcher().setMaxRequests(maxRequests);
    }

    /**
     * 设置单个请求HOST对应的最大并发数
     *
     * @param maxRequestsPerHost 每个Host对应的最大并发数
     */
    public static void setMaxConcurrentRequestPerHost(int maxRequestsPerHost) {
        client.dispatcher().setMaxRequestsPerHost(maxRequestsPerHost);
    }

    /**
     * get方式请求地址，返回字符串
     *
     * @param url URL地址
     * @return 地址对应的字符串
     */
    public static String get(String url) {
        try {
            String val = doRequest(new Request.Builder().url(url).build());
            return val;
        } catch (IOException e) {
            throw new CommonException("[HttpUtil]get error", e);
        }
    }

    /**
     * get方式请求地址，返回二进制数据，如图片、小视频等
     *
     * @param url URL地址
     * @return 返回的bytes二进制数据
     */
    public static byte[] getBytes(String url) {
        Request request = new Request.Builder().url(url).get().build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().bytes();
            }
        } catch (IOException e) {
            throw new CommonException("HttpUtil getBytes error", e);
        }
        return null;
    }

    /**
     * get方式请求地址，返回二进制数据，如图片、小视频等
     *
     * @param url URL地址
     * @return 返回的bytes二进制数据
     */
    public static HttpResult getResult(String url) {
        Request request = new Request.Builder().url(url).get().build();
        try {
            Response response = client.newCall(request).execute();
            HttpResult httpResult = new HttpResult();
            httpResult.setStatusCode(response.code());
            if (response.isSuccessful() && response.body() != null) {
                httpResult.setResult(response.body().string());
                httpResult.setResultByte(response.body().bytes());
                if (response != null) {
                    response.close();
                }
                return httpResult;
            } else {
                throw new CommonException("HttpUtil getBytes error");
            }
        } catch (IOException e) {
            throw new CommonException("HttpUtil getBytes error", e);
        }
    }

    /**
     * post方式请求，提交Json Body内容, 返回字符串
     *
     * @param url URL
     * @param jsonBody 请求传输的数据
     * @return 返回字符串
     */
    public static String postJson(String url, String jsonBody) {
        return postJson(url, null, jsonBody);
    }

    /**
     * post方式请求，提交Json Body内容, 返回响应体
     * 
     * @param url
     * @param jsonBody
     * @return
     */
    public static HttpResult postJsonResult(String url, String jsonBody) {
        return postJsonResult(url, null, jsonBody);
    }

    /**
     * post方式请求，指定Headers，提交Json Body内容, 返回字符串
     *
     * @param url URL
     * @param headers HTTP头
     * @param jsonBody 请求传输的数据
     * @return 返回字符串
     */
    public static String postJson(String url, Map<String, String> headers, String jsonBody) {
        if (url == null || jsonBody == null) {
            throw new CommonException("HttpUtil post url or json body cant be null");
        }
        try {
            RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, jsonBody);
            Headers httpHeaders = createHeader(headers);
            Request request;
            if (httpHeaders != null) {
                request = new Request.Builder().headers(httpHeaders).url(url).post(body).build();
            } else {
                request = new Request.Builder().url(url).post(body).build();
            }
            return doRequest(request);
        } catch (IOException e) {
            throw new CommonException("[HttpUtil]post JsonBody error, url=" + url, e);
        }
    }

    /**
     * post方式请求，指定Headers，提交Json Body内容, 返回响应体
     *
     * @param url URL
     * @param headers HTTP头
     * @param jsonBody 请求传输的数据
     * @return 返回字符串
     */
    public static HttpResult postJsonResult(String url, Map<String, String> headers, String jsonBody) {
        if (url == null || jsonBody == null) {
            throw new CommonException("HttpUtil post url or json body cant be null");
        }
        try {
            RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, jsonBody);
            Headers httpHeaders = createHeader(headers);
            Request request;
            if (httpHeaders != null) {
                request = new Request.Builder().headers(httpHeaders).url(url).post(body).build();
            } else {
                request = new Request.Builder().url(url).post(body).build();
            }
            return doRequestResult(request);
        } catch (IOException e) {
            throw new CommonException("[HttpUtil]post JsonBody error, url=" + url, e);
        }
    }


    /**
     * 以表单提交内容使用post方式请求, 返回JSON字符串转换为实体类
     *
     * @param url 请求地址
     * @param params 参数
     * @return 返回字符串
     */
    public static String postForm(String url, Map<String, String> params) {
        return postForm(url, null, params);
    }

    /**
     * 以表单提交内容使用post方式请求, 返回JSON字符串转换为实体类
     *
     * @param url 请求地址
     * @param headers HTTP头
     * @param mapParams 参数
     * @return 返回字符串
     */
    public static String postForm(String url, Map<String, String> headers, Map<String, String> mapParams) {
        if (url == null || mapParams == null) {
            throw new CommonException("HttpUtil post url or params cant be null");
        }
        try {
            Headers httpHeaders = createHeader(headers);
            FormBody.Builder requetBuilder = new okhttp3.FormBody.Builder();
            mapParams.forEach((k, v) -> requetBuilder.add(k, v));
            RequestBody requestBody = requetBuilder.build();
            Request request;
            if (httpHeaders != null) {
                request = new Request.Builder().headers(httpHeaders).url(url).post(requestBody).build();
            } else {
                request = new Request.Builder().url(url).post(requestBody).build();
            }
            return doRequest(request);
        } catch (IOException e) {
            throw new CommonException("[HttpUtil]post from error, url=" + url, e);
        }
    }
}
