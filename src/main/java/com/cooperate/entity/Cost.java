package com.cooperate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

/**
 * Сущность описывающая затраты, расходы
 */
@Entity
public class Cost implements Serializable{

    /**
     * Идентификатор расхода
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * Тип расхода
     */
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "id_type")
    private CostType type;

    /**
     * Дополнительное описание расхода
     */
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * Дата расхода
     */
    @OrderBy("date desc ")
    @Column(name = "date", nullable = false)
    private Calendar date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CostType getType() {
        return type;
    }

    public void setType(CostType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cost cost = (Cost) o;

        if (id != null ? !id.equals(cost.id) : cost.id != null) return false;
        if (description != null ? !description.equals(cost.description) : cost.description != null) return false;
        return date != null ? date.equals(cost.date) : cost.date == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
