package arbalest.utils;

import java.io.IOException;

/**
 * 
 * @author keishin.yokomaku
 * @hide
 */
public final class StringUtils {
    private static final String DEFAULT_CHARSET = "UTF-8";

    private StringUtils() {}

    public static final byte[] getBytes(String s) {
        try {
            return s.getBytes(DEFAULT_CHARSET);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}