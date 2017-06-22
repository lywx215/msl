package services;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class XptoUtils {

    private static SecretKey skey;
    private static KeySpec ks;
    private static PBEParameterSpec ps;
    private static final String algorithm = "PBEWithMD5AndDES";
    private static BASE64Encoder enc = new BASE64Encoder();
    private static BASE64Decoder dec = new BASE64Decoder();

    static {
	try {
	    SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
	    ps = new PBEParameterSpec(new byte[] { 3, 1, 4, 1, 5, 9, 2, 6 }, 20);
	    ks = new PBEKeySpec("EAlGeEen3/m8/YkO".toCharArray());
	    skey = skf.generateSecret(ks);
	} catch (java.security.NoSuchAlgorithmException ex) {
	    ex.printStackTrace();
	} catch (java.security.spec.InvalidKeySpecException ex) {
	    ex.printStackTrace();
	}
    }

    public static final String k(final String text) {
	try {
	    final Cipher cipher = Cipher.getInstance(algorithm);
	    cipher.init(Cipher.ENCRYPT_MODE, skey, ps);
	    return enc.encode(cipher.doFinal(text.getBytes()));
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return "";
    }

    public static final String j(final String text) {
	try {
	    final Cipher cipher = Cipher.getInstance(algorithm);
	    cipher.init(Cipher.DECRYPT_MODE, skey, ps);

	    return new String(cipher.doFinal(dec.decodeBuffer(text)));
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	
	return "";
    }

}