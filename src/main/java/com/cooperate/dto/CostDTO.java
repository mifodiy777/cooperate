package com.cooperate.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Класс обертка для получения данных по расходам
 */
public class CostDTO {

    /**
     * Тип расхода
     */
    private String type;

    /**
     * кол-во расходов
     */
    private BigInteger count;

    /**
     * Общая сумма расхода
     */
    private BigDecimal sum;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigInteger getCount() {
        return count;
    }

    public void setCount(BigInteger count) {
        this.count = count;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public void setSum(BigDecimal sum) {
        this.sum = sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CostDTO dto = (CostDTO) o;

        if (type != null ? !type.equals(dto.type) : dto.type != null) return false;
        if (count != null ? !count.equals(dto.count) : dto.count != null) return false;
        return sum != null ? sum.equals(dto.sum) : dto.sum == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (sum != null ? sum.hashCode() : 0);
        return result;
    }
}
