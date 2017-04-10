package Utilities;

import java.security.MessageDigest;

public class Hash {
    /**
     * Hases the received string
     * @param fileID received string
     * @return hashed string
     */
    public static String sha256(String fileID){
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(fileID.getBytes("UTF-8"));
            byte[] digest = md.digest();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();

        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
