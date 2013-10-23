package arbalest.rest.net.converter;

import arbalest.rest.exception.ParseRestNetworkException;
import arbalest.rest.exception.ParseRestResponseException;
import arbalest.rest.net.json.DefaultFieldNamingStrategy;
import arbalest.utils.CloseableUtils;
import repack.com.google.gson.Gson;
import repack.com.google.gson.GsonBuilder;
import repack.com.google.gson.JsonSyntaxException;
import repack.com.google.gson.stream.JsonReader;
import repack.com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonConverter implements Converter {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private final GsonBuilder mDefaultGsonBuilder = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
            .setFieldNamingStrategy(DefaultFieldNamingStrategy.PARSE_API_CAMEL_CASE);

    @Override
    public String convert(Object object) {
        return mDefaultGsonBuilder.create().toJson(object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void convert(OutputStream out, T object) throws ParseRestNetworkException {
        if (object.getClass().isArray()) {
            convertArray(out, (T[]) object);
        } else {
            convertObject(out, object);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(InputStream in, Type type) throws ParseRestNetworkException, ParseRestResponseException {
        Class<?> clazz;
        if (type instanceof ParameterizedType) {
            clazz = (Class<?>) ((ParameterizedType) type).getRawType();
        } else {
            clazz = (Class<?>) type;
        }
        return (T) (clazz.isArray() ? convertArray(in, clazz) : convertObject(in, type));
    }

    private <T> void convertObject(OutputStream out, T object) throws ParseRestNetworkException {
        JsonWriter writer = null;
        try {
            writer = new JsonWriter(new OutputStreamWriter(out, DEFAULT_CHARSET));
            Gson gson = mDefaultGsonBuilder.create();
            gson.toJson(object, object.getClass(), writer);
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new ParseRestNetworkException(e);
        } finally {
            CloseableUtils.close(writer);
        }
    }

    private Object convertObject(InputStream in, Type type) throws ParseRestNetworkException, ParseRestResponseException {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(in, DEFAULT_CHARSET));
            Gson gson = mDefaultGsonBuilder.create();
            return gson.fromJson(reader, type);
        } catch (JsonSyntaxException e) {
            throw new ParseRestResponseException(e);
        } catch (IOException e) {
            throw new ParseRestNetworkException(e);
        } finally {
            CloseableUtils.close(reader);
        }
    }

    private void convertArray(OutputStream out, Object[] objects) throws ParseRestNetworkException {
        JsonWriter writer = null;
        try {
            writer = new JsonWriter(new OutputStreamWriter(out, DEFAULT_CHARSET));
            writer.beginArray();

            Gson gson = mDefaultGsonBuilder.create();
            for (Object object : objects) {
                gson.toJson(object, object.getClass(), writer);
            }

            writer.endArray();
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new ParseRestNetworkException(e);
        } finally {
            CloseableUtils.close(writer);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T convertArray(InputStream in, Class<T> clazz) throws ParseRestNetworkException, ParseRestResponseException {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(in, DEFAULT_CHARSET));
            Class<?> type = clazz.getComponentType();
            Gson gson = mDefaultGsonBuilder.create();
            List<Object> list = new ArrayList<Object>();

            reader.beginArray();

            while (reader.hasNext()) {
                list.add(gson.fromJson(reader, type));
            }

            reader.endArray();

            return (T) list.toArray((T[]) Array.newInstance(type, list.size()));
        } catch (JsonSyntaxException e) {
            throw new ParseRestResponseException(e);
        } catch (IOException e) {
            throw new ParseRestNetworkException(e);
        } finally {
            CloseableUtils.close(reader);
        }
    }
}