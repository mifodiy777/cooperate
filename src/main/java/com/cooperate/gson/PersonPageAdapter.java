package com.cooperate.gson;

import com.cooperate.entity.Garag;
import com.cooperate.entity.Person;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PersonPageAdapter implements JsonSerializer<Person> {

    public JsonElement serialize(Person src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", src.getId());
        jsonObject.addProperty("fio", src.getFIO());
        jsonObject.addProperty("phone", src.getTelephone());
        jsonObject.addProperty("benefits", src.getBenefits());
        jsonObject.addProperty("address", src.getAddress().getAddr());
        JsonArray garags = new JsonArray();
        for (Garag g : src.getGaragList()) {
            JsonObject garag = new JsonObject();
            garag.add("garag", new JsonPrimitive(g.getFullName()));
            garag.add("garagId", new JsonPrimitive(g.getId()));
            garags.add(garag);
        }
        jsonObject.add("garags", garags);
        return jsonObject;
    }
}
