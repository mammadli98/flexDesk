package flexDesk.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtil {

  public static String hashPassword(String password) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
      messageDigest.update(password.getBytes());
      byte[] hash = messageDigest.digest();
      return new String(hash);
    } catch (NoSuchAlgorithmException e) {
      System.err.println(e.getCause() + e.getMessage());
      return "";
    }
  }
}
