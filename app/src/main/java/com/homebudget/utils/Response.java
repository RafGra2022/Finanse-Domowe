package com.homebudget.utils;

public class Response {

    private int HttpCode;
    private String response;

    public Response() {
    }

    public Response(Integer httpCode, String response) {
        this.HttpCode = httpCode;
        this.response = response;
    }

    public int getHttpCode() {
        return HttpCode;
    }

    public String getResponse() {
        return response;
    }
}
