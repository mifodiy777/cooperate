package com.cooperate;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * User: Kuzmin K.A.
 */
public class Utils {

    /**
     * Преобразование коллекции элементов в JSON массив с именем aaData для таблиц
     */
    public static ResponseEntity<String> convertListToJson(GsonBuilder gson, Iterable collection) {
        gson.serializeNulls();
        JsonObject obj = new JsonObject();
        obj.add("aaData", gson.create().toJsonTree(collection));
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(new MediaType("application", "json", Charset.forName("utf-8")));
        return new ResponseEntity<String>(obj.toString(), responseHeaders, HttpStatus.OK);
    }

    /**
     * Преобразование коллекции элементов в JSON массив с именем aaData для таблиц
     */
    public static ResponseEntity<String> convertObjectToJson(GsonBuilder gson, Serializable entity) {
        gson.serializeNulls();
        JsonObject obj = new JsonObject();
        JsonArray elements = new JsonArray();
        elements.add(gson.create().toJsonTree(entity));
        obj.add("aaData", elements);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(new MediaType("application", "json", Charset.forName("utf-8")));
        return new ResponseEntity<String>(obj.toString(), responseHeaders, HttpStatus.OK);
    }

    /**
     * Создание JSON ответа
     */
    public static ResponseEntity<String> createJsonResponse(GsonBuilder gson, Object obj) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(new MediaType("application", "json", Charset.forName("utf-8")));
        return new ResponseEntity<String>(gson.create().toJson(obj), responseHeaders, HttpStatus.OK);
    }

    public static String formatDate(Calendar calendar) {
        return new SimpleDateFormat("dd.MM.yyyy").format(calendar.getTime());
    }
}
