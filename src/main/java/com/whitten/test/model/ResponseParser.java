package com.whitten.test.model;

import com.google.gson.Gson;

public class ResponseParser {
    private static Gson gson = new Gson();

    // TODO: 11/21/17 move into the service?
    public static <T> T getResponseObject(Class<T> objectClass, String response) {
        return gson.fromJson(response, objectClass);
    }
}
