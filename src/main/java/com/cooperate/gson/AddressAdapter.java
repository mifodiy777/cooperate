package com.cooperate.gson;

import com.cooperate.entity.Address;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AddressAdapter implements JsonSerializer<Address> {

    public JsonElement serialize(Address src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        if (src.getCity().isEmpty() || src.getStreet().isEmpty()) {
            jsonObject.addProperty("address", "");
        } else {
            if (src.getApartment().isEmpty()) {
                jsonObject.addProperty("address", "г." + src.getCity() + " ул." + src.getStreet() +
                        " д." + src.getHome());
            } else {
                jsonObject.addProperty("address", "г." + src.getCity() + " ул." + src.getStreet() +
                        " д." + src.getHome() + " кв." + src.getApartment());
            }
        }
        return jsonObject;
    }
}
