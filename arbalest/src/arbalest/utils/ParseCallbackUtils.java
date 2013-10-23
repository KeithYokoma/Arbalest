package arbalest.utils;

import arbalest.rest.callback.ParseCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class ParseCallbackUtils {
    private ParseCallbackUtils() {}

    public static final <T> Type getType(ParseCallback<T> callback) {
        if (callback == null) {
            return null;
        }

        return ((ParameterizedType) callback.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }
}