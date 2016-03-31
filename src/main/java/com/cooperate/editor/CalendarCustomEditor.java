package com.cooperate.editor;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.beans.PropertyEditorSupport;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarCustomEditor extends PropertyEditorSupport {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd.MM.yyyy");

    @Override
    public String getAsText() {
        Calendar value = (Calendar) getValue();
        DateTime dateTime = new DateTime(value);
        return (value != null ? dateTime.toString(this.dateTimeFormatter) : "");
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(new SimpleDateFormat("dd.MM.yyyy").parse(text).getTime()));
            setValue(calendar);
        } catch (ParseException e) {
            setValue(null);
        }
    }
}
