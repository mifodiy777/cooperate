package com.cooperate.editor;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
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
        if (!StringUtils.hasText(text)) {
            setValue(null);
        } else {
            DateTime dateTime = this.dateTimeFormatter.parseDateTime(text);
            setValue(dateTime.toGregorianCalendar());
        }
    }
}
