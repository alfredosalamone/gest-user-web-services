package com.xantrix.webapp.exceptions;

public class DuplicateException extends Exception{
    private static final long serialVersionUID = -7158897347652444389L;

    private String message;

    public DuplicateException(){
        super();
    }

    public DuplicateException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
