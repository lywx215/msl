package services;

public class StringUtils {
    
    public static String getStringOrEmpty(String ctx) {
	return ctx == null ? "" : ctx; 
    }

}
