package com.cooperate.gson;

import com.cooperate.entity.Garag;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class GaragAdapter implements JsonSerializer<Garag> {

    public JsonElement serialize(Garag src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObject = new JsonObject();
        if (src != null) {
          jsonObject.addProperty("garag", " Ряд: " + src.getSeries() + " Номер: " + src.getNumber());
        }
        return jsonObject;
    }

}
