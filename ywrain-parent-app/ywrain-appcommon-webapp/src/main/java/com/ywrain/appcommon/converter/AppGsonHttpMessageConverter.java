/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ywrain.appcommon.converter;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.ywrain.appcommon.utils.RespUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.Assert;

/**
 * Implementation of {@link org.springframework.http.converter.HttpMessageConverter} that can read and write JSON using the <a
 * href="https://code.google.com/p/google-gson/">Google Gson</a> library's {@link Gson} class.
 *
 * <p>
 * This converter can be used to bind to typed beans or untyped {@code HashMap}s. By default, it supports {@code application/json} and {@code
 * application/*+json} with {@code UTF-8} character set.
 *
 * <p>
 * Tested against Gson 2.6; compatible with Gson 2.0 and higher.
 *
 * @author Roy Clarkson
 * @see #setGson
 * @see #setSupportedMediaTypes
 * @since 4.1
 */
public class AppGsonHttpMessageConverter extends AbstractGenericHttpMessageConverter<Object> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private Gson gson = RespUtil.gson;

    private String jsonPrefix;

    /**
     * Construct a new {@code GsonHttpMessageConverter}.
     */
    public AppGsonHttpMessageConverter() {
        super(MediaType.APPLICATION_JSON, new MediaType("application", "*+json"));
        this.setDefaultCharset(DEFAULT_CHARSET);
    }


    /**
     * Set the {@code Gson} instance to use. If not set, a default {@link Gson#Gson() Gson} instance is used.
     * <p>
     * Setting a custom-configured {@code Gson} is one way to take further control of the JSON serialization process.
     */
    public void setGson(Gson gson) {
        Assert.notNull(gson, "'gson' is required");
        this.gson = gson;
    }

    /**
     * Return the configured {@code Gson} instance for this converter.
     */
    public Gson getGson() {
        return this.gson;
    }

    /**
     * Specify a custom prefix to use for JSON output. Default is none.
     *
     * @see #setPrefixJson
     */
    public void setJsonPrefix(String jsonPrefix) {
        this.jsonPrefix = jsonPrefix;
    }

    /**
     * Indicate whether the JSON output by this view should be prefixed with ")]}', ". Default is {@code false}.
     * <p>
     * Prefixing the JSON string in this manner is used to help prevent JSON Hijacking. The prefix renders the string syntactically invalid as a
     * script so that it cannot be hijacked. This prefix should be stripped before parsing the string as JSON.
     *
     * @see #setJsonPrefix
     */
    public void setPrefixJson(boolean prefixJson) {
        this.jsonPrefix = (prefixJson ? ")]}', " : null);
    }


    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return canRead(mediaType);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return canWrite(mediaType);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        // should not be called, since we override canRead/Write instead
        throw new UnsupportedOperationException();
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        TypeToken<?> token = getTypeToken(clazz);
        return readTypeToken(token, inputMessage);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        TypeToken<?> token = getTypeToken(type);
        return readTypeToken(token, inputMessage);
    }

    /**
     * Return the Gson {@link TypeToken} for the specified type.
     * <p>
     * The default implementation returns {@code TypeToken.get(type)}, but this can be overridden in subclasses to allow for custom generic collection
     * handling. For instance:
     *
     * <pre class="code">
     * protected TypeToken<?> getTypeToken(Type type) {
     *     if (type instanceof Class && List.class.isAssignableFrom((Class<?>) type)) {
     *         return new TypeToken<ArrayList<MyBean>>() {};
     *     } else {
     *         return super.getTypeToken(type);
     *     }
     * }
     * </pre>
     *
     * @param type the type for which to return the TypeToken
     * @return the type token
     */
    protected TypeToken<?> getTypeToken(Type type) {
        return TypeToken.get(type);
    }


    private Object readTypeToken(TypeToken<?> token, HttpInputMessage inputMessage) throws IOException {
        Reader json = new InputStreamReader(inputMessage.getBody(), getCharset(inputMessage.getHeaders()));
        try {
            if (String.class.isAssignableFrom(token.getRawType())) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputMessage.getBody(), getCharset(inputMessage.getHeaders())));
                StringBuffer buffer = new StringBuffer();
                String tmp = null;
                while ((tmp = reader.readLine()) != null) {
                    buffer.append(tmp);
                }
                return buffer.toString();
            } else {
                return this.gson.fromJson(json, token.getType());
            }
        } catch (JsonParseException ex) {
            throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }

    private Charset getCharset(HttpHeaders headers) {
        if (headers == null || headers.getContentType() == null || headers.getContentType().getCharset() == null) {
            return DEFAULT_CHARSET;
        }
        return headers.getContentType().getCharset();
    }

    @Override
    protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        Charset charset = getCharset(outputMessage.getHeaders());
        OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), charset);
        try {
            writePrefix(writer, object);
            Object value = object;
            if (object instanceof MappingGsonValue) {
                MappingGsonValue container = (MappingGsonValue) object;
                value = container.getValue();
            }
            if (type != null) {
                if ("java.lang.String".equals(type.getTypeName())) {
                    writer.append(value.toString());
                } else {
                    this.gson.toJson(value, type, writer);
                }
            } else {
                this.gson.toJson(value, writer);
            }
            writeSuffix(writer, object);
            writer.close();
        } catch (JsonIOException ex) {
            throw new HttpMessageNotWritableException("Could not write JSON: " + ex.getMessage(), ex);
        }
    }

    protected void writePrefix(OutputStreamWriter writer, Object object) throws IOException {
        if (this.jsonPrefix != null) {
            writer.append(this.jsonPrefix);
        }
        String jsonpFunction = (object instanceof MappingGsonValue ? ((MappingGsonValue) object).getJsonpFunction() : null);
        if (jsonpFunction != null) {
            writer.append(jsonpFunction + "(");
        }
    }

    protected void writeSuffix(OutputStreamWriter writer, Object object) throws IOException {
        String jsonpFunction = (object instanceof MappingGsonValue ? ((MappingGsonValue) object).getJsonpFunction() : null);
        if (jsonpFunction != null) {
            writer.append(");");
        }
    }

}
