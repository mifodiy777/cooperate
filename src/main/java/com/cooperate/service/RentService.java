package com.cooperate.service;

import com.cooperate.entity.Rent;

import java.util.*;

public interface RentService {

    Boolean checkRent(Integer year);

    void saveOrUpdate(Rent rent);

    List<Rent> getRents();

    void createNewPeriod(Rent rent);

    Rent findByYear(Integer year);
}
