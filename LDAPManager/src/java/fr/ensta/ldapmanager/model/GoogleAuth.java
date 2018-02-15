/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.ensta.ldapmanager.model;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base32;

/**
 *
 * @author eleve
 */
public class GoogleAuth {
    
    private int secretSize;

    public GoogleAuth() {
        secretSize = 10;
    }

    
    public String generateKey(){
        
        byte[] buffer;
        buffer = new byte[secretSize];
        new Random().nextBytes(buffer);
        Base32 codec = new Base32();
        byte[] secretKey = Arrays.copyOf(buffer, secretSize);
        byte[] bEncodedKey = codec.encode(secretKey);
        String encodedKey = new String(bEncodedKey);
        return encodedKey;
    }
    
    public String getQRBarcodeURL(String user, String host, String secret) {
        
        String format = "https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=otpauth://totp/%s@%s%%3Fsecret%%3D%s";
        return String.format(format, user, host, secret);
        
      }
    
    public boolean check_code(String secret,long code,long t) throws NoSuchAlgorithmException, InvalidKeyException {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);

        // Window is used to check codes generated in the near past.
        // You can use this value to tune how far you're willing to go. 
        int window = 3;
        for (int i = -window; i <= window; ++i) {
          long hash = verify_code(decodedKey, t + i);
          if (hash == code) {
            return true;
          }
        }

        // The validation code is invalid.
        return false;
  }

    public int verify_code(byte[] key,long t) throws InvalidKeyException, NoSuchAlgorithmException {
      byte[] data = new byte[8];
      long value = t;
      for (int i = 8; i-- > 0; value >>>= 8) {
        data[i] = (byte) value;
      }

      SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
      Mac mac = Mac.getInstance("HmacSHA1");
      mac.init(signKey);
      byte[] hash = mac.doFinal(data);

      int offset = hash[20 - 1] & 0xF;

      // We're using a long because Java hasn't got unsigned int.
      long truncatedHash = 0;
      for (int i = 0; i < 4; ++i) {
        truncatedHash <<= 8;
        // We are dealing with signed bytes:
        // we just keep the first byte.
        truncatedHash |= (hash[offset + i] & 0xFF);
      }

      truncatedHash &= 0x7FFFFFFF;
      truncatedHash %= 1000000;

      return (int) truncatedHash;
    }
    
}
