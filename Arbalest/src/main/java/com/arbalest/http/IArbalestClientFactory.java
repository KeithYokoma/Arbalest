package com.arbalest.http;

import com.arbalest.exception.ArbalestNetworkException;

/**
 * Created by yokomakukeishin on 2013/12/10.
 */
public interface IArbalestClientFactory {
    public ArbalestClient create(String url) throws ArbalestNetworkException;
}
