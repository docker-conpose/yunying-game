package com.joolun.mall.util;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;

public class EncryptUtil {
    // 字符串默认键值
    private static String strDefaultKey = "2022@#$%^&";

    //加密工具
    private Cipher encryptCipher = null;

    // 解密工具
    private Cipher decryptCipher = null;

    /**
     * 默认构造方法，使用默认密钥
     */
    public EncryptUtil() throws Exception {
        this(strDefaultKey);
    }

    /**
     * 指定密钥构造方法
     */

    public EncryptUtil(String strKey) throws Exception {

        Key key = getKey(strKey.getBytes());
        encryptCipher = Cipher.getInstance("DES");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);
        decryptCipher = Cipher.getInstance("DES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813，和public static byte[]
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用2个字符才能表示，所以字符串的长度是数组长度的2倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }

    /**
     * 将表示16进制值的字符串转换为byte数组，和public static String byteArr2HexStr(byte[] arrB)
     */
    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }

    /**
     * 返回计算结果为十六进制字符串的异或校验
     *
     * @str 十六进制字符串
     * @return 异或校验结果
     */
    public static String getXor2String(String str) {
        byte[] bytes = hexStringToByteArray(str);
        byte xor = getXor(bytes);
        //字节转十六进制
        String s = Integer.toHexString(0xFF & xor);
        return s;
    }

    public static String getHexString(String str) {
        StringBuilder hex = new StringBuilder();
        for (char c : str.toCharArray()) {
            hex.append(Integer.toHexString((int) c));
        }
        return hex.toString();
    }
    public static byte getXor(byte[] datas){
        byte temp=datas[0];
        for (int i = 1; i <datas.length; i++) {
            temp ^=datas[i];
        }
        return temp;
    }
    /**
     * 16进制表示的字符串转换为字节数组
     * 返回的字节数组表示为十进制
     * @param hexString 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }

    /**
     * 加密字节数组
     */
    public byte[] encrypt(byte[] arrB) throws Exception {
        return encryptCipher.doFinal(arrB);
    }

    /**
     * 加密字符串
     */
    public String encrypt(String strIn) throws Exception {
        return byteArr2HexStr(encrypt(strIn.getBytes()));
    }

    /**
     * 解密字节数组
     */
    public byte[] decrypt(byte[] arrB) throws Exception {
        return decryptCipher.doFinal(arrB);
    }

    /**
     * 解密字符串
     */
    public String decrypt(String strIn) throws Exception {
        return new String(decrypt(hexStr2ByteArr(strIn)));
    }

    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     */
    private Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }

    public static String openContainerCode(int cRow) {
        int head=138;
        int address=0;
        int open= 17;
        int verify=head^address^cRow^open;
        String code ;
        code =  "8A000"+ Integer.toHexString(cRow) + "11" +
                Integer.toHexString(verify);

        return code.toUpperCase();
    }

    public static String openNewContainerCode(int cRow,String code) {
        String head="E5";
        String end="5E";
        String type= "02";
        String cId="0" + Integer.toHexString(cRow-1);
        String packageLength="0" +Integer.toHexString((code.length()+type.length()+cId.length()+2+end.length())/2);
        String xor2String = EncryptUtil.getXor2String(head + packageLength + code + type + cId);
        if (xor2String.length()==1){
            xor2String="0"+xor2String;
        }
        return (head+packageLength+code+type+cId+xor2String+end).toUpperCase();
    }

    public static String heartbeatEcho(String code) {
        String head="E5";
        String end="5E";
        String type= "05";
        String heartbeatEcho= "01";
        String packageLength="0" +Integer.toHexString((code.length()+type.length()+2+heartbeatEcho.length()+end.length())/2);
        String xor2String = EncryptUtil.getXor2String(head + heartbeatEcho+packageLength + code + type );
        if (xor2String.length()==1){
            xor2String="0"+xor2String;
        }
        return (head+packageLength+code+type+heartbeatEcho+xor2String+end).toUpperCase();
    }

    public static String HexStrToByteArray(String hexStr){
        try {
        int strLen=hexStr.length();
        if (strLen % 2 !=0) {
            return null;
        }
        byte[] conver = new byte[strLen/2];;
        for (int i = 0; i < strLen; i++) {
            if (i%2==0){
                conver[i/2] = (byte)Integer.parseInt(hexStr.substring(i,i+2),16);
            }
        }

            return new String(conver,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        System.out.println(EncryptUtil.getXor2String("E71411383633313633303430313733393432000101"));
        ;
    }
}
