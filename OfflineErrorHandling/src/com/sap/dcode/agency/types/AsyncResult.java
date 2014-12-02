package com.sap.dcode.agency.types;

public class AsyncResult<T> {

    public static final Void VOID_RESULT = (Void) null;

    private Exception exception;
    private T data;

    public AsyncResult(Exception exception) {
        this.exception = exception;
    }

    public AsyncResult(T data) {
        this.data = data;
    }

    public Exception getException() {
        return exception;
    }

    public T getData() {
        return data;
    }

}
