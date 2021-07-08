package com.lzc.util;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Liang Zhancheng
 * @date 2021/6/28 9:40
 * @description 创建 md5 加密类，进行密码加密
 */
@Component
public class Md5Utils {
    /**
     * @param str 输入字符串
     * @return 返回加密的字符串
     * @description md5算法进行密码加密
     */
    public static String encode(String str) {
        try {
            //1.获取MessageDigest对象  生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            /*
            str.getBytes()
            * 使用平台的默认字符集将此 String 编码为 byte 序列，并将结果存储到一个新的 byte 数组中.
            此方法多用在字节流中，用与将字符串转换为字节。
            * */

            // 计算md5函数 使用指定的字节数组更新摘要md
            md.update(str.getBytes());
            /*
             * digest()最后确定返回md5 hash值，返回值为8的字符串。
             * 因为md5 hash值是16位的hex值，实际上就是8位的
             * */
            byte[] byteDigest = md.digest();
            int i;
            StringBuilder buf = new StringBuilder("");
            //遍历byteDigest
            //加密逻辑，可以debug自行了解一下加密逻辑
            for (byte b : byteDigest) {
                i = b;
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}


