package arbalest.rest.net.json;

import android.util.Log;

import repack.com.google.gson.FieldNamingStrategy;
import repack.com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public enum DefaultFieldNamingStrategy implements FieldNamingStrategy {
    PARSE_API_CAMEL_CASE() {
        @Override
        protected String fieldNameToJsonName(String fieldName) {
            return fieldName;
        }
    };

    private static final String TAG = DefaultFieldNamingStrategy.class.getSimpleName();
    private static final String MEMBER_NAME_PREFIX = "m";

    @Override
    public String translateName(Field field) {
        StringBuilder fieldName = new StringBuilder(field.getName());
        int fieldModifiers = field.getModifiers();

        if ((!field.getDeclaringClass().isMemberClass()) && (!Modifier.isStatic(fieldModifiers))
                && (!Modifier.isPublic(fieldModifiers)) && field.getAnnotation(SerializedName.class) == null) {
            if ((fieldName.length() >= MEMBER_NAME_PREFIX.length())
                    && MEMBER_NAME_PREFIX.equals(fieldName.substring(0, MEMBER_NAME_PREFIX.length()))) {
                fieldName.delete(0, MEMBER_NAME_PREFIX.length());

                if (fieldName.length() > 0)
                    fieldName.setCharAt(0, Character.toLowerCase(fieldName.charAt(0)));
            } else {
                Log.i(TAG, "The following field naming doesn't follow Rima guidelines : " + field);
            }
        }

        return fieldNameToJsonName(fieldName.toString());
    }

    protected abstract String fieldNameToJsonName(String fieldName);
}
