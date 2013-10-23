package arbalest.rest.net.converter;

import arbalest.rest.exception.ParseRestNetworkException;
import arbalest.rest.exception.ParseRestResponseException;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * Policy enumeration for converting entity object with the serialized form.
 * @author keishin.yokomaku
 */
public enum EntityConversionPolicy implements EntityConversionStrategy {
    DEFAULT_GSON() {
        private GsonConverter mConverter = new GsonConverter();

        @Override
        public String convert(Object object) throws ParseRestNetworkException {
            return mConverter.convert(object);
        }

        @Override
        public void convert(OutputStream out, Object object) throws ParseRestNetworkException {
            mConverter.convert(out, object);
        }

        @Override
        public Object convert(InputStream in, Type type) throws ParseRestNetworkException, ParseRestResponseException {
            return mConverter.convert(in, type);
        }
    },
    RAW_JSON() {
        private RawJsonConverter mConverter = new RawJsonConverter();

        @Override
        public String convert(Object object) throws ParseRestNetworkException {
            return mConverter.convert(object);
        }

        @Override
        public void convert(OutputStream out, Object object) throws ParseRestNetworkException {
            mConverter.convert(out, object);
        }

        @Override
        public Object convert(InputStream in, Type type) throws ParseRestNetworkException, ParseRestResponseException {
            return mConverter.convert(in, type);
        }
    },
    BINARY() {
        private BinaryConverter mConverter = new BinaryConverter();

        @Override
        public String convert(Object object) throws ParseRestNetworkException {
            return mConverter.convert(object);
        }

        @Override
        public void convert(OutputStream out, Object object) throws ParseRestNetworkException {
            mConverter.convert(out, object);
        }

        @Override
        public Object convert(InputStream in, Type type) throws ParseRestNetworkException, ParseRestResponseException  {
            return mConverter.convert(in, type);
        }
    };
}