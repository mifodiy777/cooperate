package com.cooperate.gson;

import com.cooperate.entity.Person;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class PersonAdapter implements JsonSerializer<Person> {


    public JsonElement serialize(Person src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObject = new JsonObject();
        if (src != null) {
            jsonObject.addProperty("personId", src.getId());
            jsonObject.addProperty("fio", src.getFIO());
            jsonObject.addProperty("phone", src.getTelephone());
            jsonObject.addProperty("benefits", src.getBenefits());
            jsonObject.addProperty("address", src.getAddress().getAddr());
            jsonObject.addProperty("memberBoard", src.getMemberBoard());
        }
        return jsonObject;
    }
}
