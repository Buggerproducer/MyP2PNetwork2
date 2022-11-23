package default_package;

import java.security.MessageDigest;

public class HashKit {
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    public static String sha1(String srcStr){
        return hash("SHA-1", srcStr);
    }

    public static String hash(String algorithm, String srcStr) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = messageDigest.digest(srcStr.getBytes("utf-8"));
            return toHex(bytes);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toHex(byte[] bytes) {
        StringBuilder rBuilder = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            rBuilder.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            rBuilder.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return rBuilder.toString();
    }

}