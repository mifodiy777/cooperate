package com.cooperate.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.Calendar;

//Журнал событий
@Entity
public class JournalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_history", nullable = false)
    private Integer id;

    /*Дата события*/
    @Column(name = "date_event")
    private Calendar dateEvent;

    //Событие
    @Column(name = "evented")
    private String evented;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Calendar getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Calendar dateEvent) {
        this.dateEvent = dateEvent;
    }

    public String getEvented() {
        return evented;
    }

    public void setEvented(String evented) {
        this.evented = evented;
    }
}
