package com.cooperate.gson;

import com.cooperate.entity.Payment;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class PaymentAdapter implements JsonSerializer<Payment> {

    public JsonElement serialize(Payment src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("id", src.getId());
        json.addProperty("number", src.getNumber());
        json.addProperty("datePay", src.getDatePay());
        json.addProperty("garag", src.getGarag().getFullName());
        json.addProperty("fio", src.getFio());
        json.addProperty("pay", src.getSumPay().intValue() + " руб.");
        return json;
    }
}
