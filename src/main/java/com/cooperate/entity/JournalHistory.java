package com.cooperate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

//Журнал событий
@Entity
public class JournalHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_history", nullable = false)
    private Integer id;

    /*Дата события*/
    @Column(name = "date_event", nullable = false)
    private Calendar dateEvent;

    //Событие
    @Column(name = "evented", nullable = false)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JournalHistory)) return false;

        JournalHistory that = (JournalHistory) o;

        if (dateEvent != null ? !dateEvent.equals(that.dateEvent) : that.dateEvent != null) return false;
        if (evented != null ? !evented.equals(that.evented) : that.evented != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dateEvent != null ? dateEvent.hashCode() : 0);
        result = 31 * result + (evented != null ? evented.hashCode() : 0);
        return result;
    }
}
