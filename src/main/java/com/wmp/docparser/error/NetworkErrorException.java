package com.wmp.docparser.error;

public class NetworkErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public NetworkErrorException() {
        super();
    }
    public NetworkErrorException(String s) {
        super(s);
    }

    public NetworkErrorException(String s, Throwable t) {
        super(s, t);
    }
}
