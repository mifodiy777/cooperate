package com.cooperate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

@Entity
public class HistoryGarag implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_history", nullable = false)
    private Integer id;

    @Column(name = "date_record")
    private Calendar dateRecord;

    @Column(name = "fio_last_person")
    private String fioLastPerson;

    @Column(name = "reason")
    private String reason;

    @ManyToOne
    private Garag garag;

    public HistoryGarag() {
    }

    public HistoryGarag(Calendar dateRecord, String fioLastPerson, String reason, Garag garag) {
        this.dateRecord = dateRecord;
        this.fioLastPerson = fioLastPerson;
        this.reason = reason;
        this.garag = garag;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Calendar getDateRecord() {
        return dateRecord;
    }

    public void setDateRecord(Calendar dateRecord) {
        this.dateRecord = dateRecord;
    }

    public String getFioLastPerson() {
        return fioLastPerson;
    }

    public void setFioLastPerson(String fioLastPerson) {
        this.fioLastPerson = fioLastPerson;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Garag getGarag() {
        return garag;
    }

    public void setGarag(Garag garag) {
        this.garag = garag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HistoryGarag)) return false;

        HistoryGarag that = (HistoryGarag) o;

        if (dateRecord != null ? !dateRecord.equals(that.dateRecord) : that.dateRecord != null) return false;
        if (fioLastPerson != null ? !fioLastPerson.equals(that.fioLastPerson) : that.fioLastPerson != null)
            return false;
        if (garag != null ? !garag.equals(that.garag) : that.garag != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (reason != null ? !reason.equals(that.reason) : that.reason != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (dateRecord != null ? dateRecord.hashCode() : 0);
        result = 31 * result + (fioLastPerson != null ? fioLastPerson.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (garag != null ? garag.hashCode() : 0);
        return result;
    }
}
