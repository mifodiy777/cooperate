package com.cooperate.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Тип расходов
 */
@Entity
public class CostType implements Serializable {

    /**
     * Идентификатор типа расхода
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * Наименование типа расхода
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CostType costType = (CostType) o;

        if (id != null ? !id.equals(costType.id) : costType.id != null) return false;
        return name != null ? name.equals(costType.name) : costType.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
