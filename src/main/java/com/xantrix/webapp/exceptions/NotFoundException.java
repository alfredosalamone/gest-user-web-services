package com.xantrix.webapp.exceptions;

public class NotFoundException extends Exception {

    private static final long serialVersionUID = -6227907101307598844L;

    private String messaggio;

    public NotFoundException(String messaggio) {
        super(messaggio);
        this.messaggio = messaggio;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }
}
