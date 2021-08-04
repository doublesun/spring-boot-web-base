package com.ywrain.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * <pre>
 * 加密解密处理工具类
 * </pre>
 *
 * @author caixiwei@youcheyihou.com
 * @since 版本:1.2.0
 */
@SuppressWarnings("deprecation")
public class DigestUtil extends DigestUtils {

    //    /**
    //     * AES 名称字符串
    //     */
    //    public static final String AES_NAME = "AES";
    //    /**
    //     * <pre>
    //     * AES默认算法: AES/CBC/PKCS7Padding
    //     * 模式: CBC
    //     * 补码方式:PKCS7Padding
    //     * </pre>
    //     */
    //    public static final String AES_ALGORITHM_DEFAULT = AES_NAME + "/CBC/PKCS7Padding";

    //    /**
    //     * <pre>
    //     * 需要增加依赖: <dependency><groupId>org.bouncycastle</groupId><artifactId>bcprov-jdk15on</artifactId><version>1.61</version></dependency>
    //     * </pre>
    //     */
    //    static {
    //        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    //    }

    //    /**
    //     * AES解密
    //     * 
    //     * @param algorithm 默认:AES/CBC/PKCS7Padding,使用模式:CBC,补码规则:PKCS7Padding
    //     * @param keyBytes AES的密钥
    //     * @param ivBytes 加盐值
    //     * @param datas 加密数据
    //     * @return 解密后数据
    //     * @throws NoSuchAlgorithmException 不支持算法异常
    //     * @throws NoSuchPaddingException 不支持补码模式异常
    //     * @throws InvalidAlgorithmParameterException 非法算法参数异常
    //     * @throws InvalidKeyException 非法算法key值异常
    //     * @throws BadPaddingException 错误的补码异常
    //     * @throws IllegalBlockSizeException 非法块大小异常
    //     */
    //    public byte[] AesDecrypt(String algorithm, byte[] keyBytes, byte[] ivBytes, byte[] datas) throws NoSuchAlgorithmException, NoSuchPaddingException,
    //            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    //        if (algorithm == null || algorithm.trim().length() == 0) {
    //            algorithm = AES_ALGORITHM_DEFAULT;
    //        }
    //        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, AES_NAME);
    //        Cipher cipher = Cipher.getInstance(algorithm);
    //        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(ivBytes));
    //        return cipher.doFinal(datas);
    //    }

    //    /**
    //     * AES加密
    //     * 
    //     * @param algorithm 默认:AES/CBC/PKCS7Padding,使用模式:CBC,补码规则:PKCS7Padding
    //     * @param keyBytes AES的密钥
    //     * @param ivBytes 加盐值
    //     * @param datas 需要加密数据
    //     * @return 加密后数据
    //     * @throws NoSuchAlgorithmException 不支持算法异常
    //     * @throws NoSuchPaddingException 不支持补码模式异常
    //     * @throws InvalidAlgorithmParameterException 非法算法参数异常
    //     * @throws InvalidKeyException 非法算法key值异常
    //     * @throws BadPaddingException 错误的补码异常
    //     * @throws IllegalBlockSizeException 非法块大小异常
    //     */
    //    public byte[] AesEncrypt(String algorithm, byte[] keyBytes, byte[] ivBytes, byte[] datas) throws NoSuchAlgorithmException, NoSuchPaddingException,
    //            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
    //        if (algorithm == null || algorithm.trim().length() == 0) {
    //            algorithm = AES_ALGORITHM_DEFAULT;
    //        }
    //        SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, AES_NAME);
    //        Cipher cipher = Cipher.getInstance(algorithm);
    //        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(ivBytes));
    //        return cipher.doFinal(datas);
    //    }
}
