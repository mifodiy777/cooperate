package com.cooperate.gson;

import com.cooperate.entity.Cost;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

/**
 * Адаптер расходов
 */
public class CostAdapter implements JsonSerializer<Cost> {

    private static SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    public JsonElement serialize(Cost src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("id", src.getId());
        json.addProperty("name", src.getType().getName());
        json.addProperty("date", formatter.format(src.getDate().getTime()));
        json.addProperty("money", src.getMoney());
        json.addProperty("description", src.getDescription());
        return json;
    }
}
