package arbalest.rest.callback;

import arbalest.rest.exception.ParseRestException;

public interface ParseCallback<T> {
    public void onSuccess(T result);
    public void onFailure(ParseRestException exp);
}