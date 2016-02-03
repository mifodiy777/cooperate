package com.cooperate.gson;

import com.cooperate.entity.Person;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class PersonAdapter implements JsonSerializer<Person> {

    @Override
    public JsonElement serialize(Person src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObject = new JsonObject();
        if (src != null) {
            jsonObject.addProperty("personId", src.getId());
            jsonObject.addProperty("fio", src.getLastName() + ' ' + src.getName() + ' ' + src.getFatherName());
            jsonObject.addProperty("phone", src.getTelephone());
            jsonObject.addProperty("benefits", src.getBenefits());
            if (src.getAddress().getStreet().isEmpty()) {
                jsonObject.addProperty("address", "");
            } else {
                if (src.getAddress().getApartment().isEmpty()) {
                    jsonObject.addProperty("address", "г." + src.getAddress().getCity() + " ул." + src.getAddress().getStreet() +
                            " д." + src.getAddress().getHome());
                } else {
                    jsonObject.addProperty("address", "г." + src.getAddress().getCity() + " ул." + src.getAddress().getStreet() +
                            " д." + src.getAddress().getHome() + " кв." + src.getAddress().getApartment());
                }
            }
        }
        return jsonObject;
    }
}
