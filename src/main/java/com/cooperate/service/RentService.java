package com.cooperate.service;

import com.cooperate.entity.Rent;

import java.util.*;

public interface RentService {

    Boolean checkRent(Integer year);

    void saveOrUpdate(Rent rent);

    List<Rent> getRents();

    Rent getRent(Integer id);

    void delete(Integer id);

    void createNewPeriod(Rent rent);

    Rent findByYear(Integer year);
}
