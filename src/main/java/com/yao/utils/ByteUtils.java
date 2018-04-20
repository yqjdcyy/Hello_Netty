package com.yao.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/1/4.
 */
public class ByteUtils {

    public static void main(String[] args) {

        System.out.println(bytes2Hex());
    }

    public static String bytes2Hex() {

        String output = "";
        String input = "7E01183B04201612120201201612220001271000448B9B8054A37B000000000000000000000000201612220001278000000000280103010190020302189C01030302580203041E7801030529CC02030626AC01030727740203081770897E";
        char[] DIGITS_UPPER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        byte[] bytes = new byte[input.length()];
        for (int i = 0; i < input.length(); i++) {
            bytes[i] = (byte) input.charAt(i);
        }

        return bytesToHexString(bytes);
    }

    protected static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
            out[j++] = toDigits[0x0F & data[i]];
        }
        return out;
    }
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    private static void prod() {

        //进行数据格式转换
        System.out.println("{data=data, proc=controller.method}: " + decodeBytes(encodeBytes("controller.method", "data")));
        System.out.println("{data=null, proc=controller.method}: " + decodeBytes(encodeBytes(null, "data")));
        System.out.println("{data=data, proc=null}: " + decodeBytes(encodeBytes("controller.method", null)));
        System.out.println("{data=null, proc=null}: " + decodeBytes(encodeBytes(null, null)));

        //方法一
        //int对象转换后的字节大小
        byte[] bytes = ByteUtils.intToByteArray(4, new Integer(999999999));
        System.out.println("Integer(999999999).length= " + bytes.length + "\tRecovery.int= " + ByteUtils.byteArrayToInt(bytes) + "\t" + bytes);
        //Integer对象转换后的字节大小
        bytes = ByteUtils.intToByteArray(4, new Integer(-999999999));
        System.out.println("Integer(-999999999).length= " + bytes.length + "\tRecovery.int= " + ByteUtils.byteArrayToInt(bytes) + "\t" + bytes);

        //方法二
        //int对象转换后的字节大小
        bytes = ByteUtils.int2Bytes(new Integer(999999999));
        System.out.println("Integer(999999999).length= " + bytes.length + "\tRecovery.int= " + ByteUtils.bytes2Int(bytes) + "\t" + bytes);
        //Integer对象转换后的字节大小
        bytes = ByteUtils.int2Bytes(new Integer(-999999999));
        System.out.println("Integer(-999999999).length= " + bytes.length + "\tRecovery.int= " + ByteUtils.bytes2Int(bytes) + "\t" + bytes);

        //正常对象类型转换
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("proc", "className.methodName");
        bytes = ByteUtils.obj2Bytes(map);
        System.out.println("Obj= " + map.toString() + "\tBytes.length= " + bytes.length + "\tObj= " + ByteUtils.bytes2Obj(bytes));
    }

    /**
     * 将对象转换为字节数组
     *
     * @param obj
     * @return
     */
    public static byte[] obj2Bytes(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    /**
     * 将字节数组转换为对象
     *
     * @param bytes
     * @return
     */
    public static Object bytes2Obj(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    /**
     * 将整数转换为固定四位的字节数据
     *
     * @param num
     * @return
     */
    public static byte[] int2Bytes(int num) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (num >> (8 * i));
        }

        return bytes;
    }

    /**
     * 将字节数据转换为整数
     *
     * @param bytes
     * @return
     */
    public static int bytes2Int(byte[] bytes) {
        int num = 0;
        if (null != bytes && bytes.length > 0) {
            for (int i = 0; i < bytes.length; i++) {
                num += (bytes[i] & 0xff) << (8 * i);
            }
        }

        return num;
    }

    public static byte[] intToByteArray(int byteLen, int intVal) {
        return ByteBuffer.allocate(byteLen).putInt(intVal).array();
    }

    public static int byteArrayToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    /**
     * 将 调用方法（类名+ 方法名） 和 传入参数 转化为整体的字节数组
     *
     * @param proc
     * @param data
     * @return
     */
    public static byte[] encodeBytes(String proc, Object data) {
        byte[] procBytes = ByteUtils.obj2Bytes(proc);
        byte[] dataBytes = ByteUtils.obj2Bytes(data);
        ByteBuffer buffer = ByteBuffer.allocate(4 + 4 + (null == procBytes ? 0 : procBytes.length) + (null == dataBytes ? 0 : dataBytes.length));
        int idx = 0;

        buffer.put(int2Bytes((null == procBytes ? 0 : procBytes.length)));
        buffer.put(procBytes);
        buffer.put(int2Bytes(null == dataBytes ? 0 : dataBytes.length));
        buffer.put(dataBytes);

        return buffer.array();
    }

    /**
     * 将字节数组自动转换出用户传入的参数格式
     * byte[int, proc, int, ]
     *
     * @param bytes
     * @return
     */
    public static HashMap decodeBytes(byte[] bytes) {
        HashMap<String, Object> map = null;
        if (null != bytes && bytes.length > 8) {
            map = new HashMap<String, Object>();
            int idx = 4, num = bytes2Int(cutBytes(bytes, 0, 4));
            map.put("proc", 0 == num ? null : bytes2Obj(cutBytes(bytes, idx, num)));
            idx += num;
            num = bytes2Int(cutBytes(bytes, idx, 4));
            idx += 4;
            map.put("data", 0 == num ? null : bytes2Obj(cutBytes(bytes, idx, num)));
        }
        return map;
    }

    /**
     * 进行字节数据合并
     *
     * @param array1
     * @param array2
     * @return
     */
    private static byte[] combineByteArray(byte[] array1, byte[] array2) {
        byte[] combined = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, combined, 0, array1.length);
        System.arraycopy(array2, 0, combined, array1.length, array2.length);
        return combined;
    }

    /**
     * 自动截取【指定位置】开始的【指定位数】的【字节数组】
     *
     * @param bytes
     * @param start
     * @param length
     * @return
     */
    public static byte[] cutBytes(byte[] bytes, int start, int length) {
        byte[] result = new byte[length];
        for (int i = start, idx = 0; i < bytes.length && i < start + length; i++, idx++) {
            result[idx] = bytes[i];
        }
        return result;
    }
}























