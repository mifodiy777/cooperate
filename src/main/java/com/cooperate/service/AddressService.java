package com.cooperate.service;

import com.cooperate.entity.Address;

import java.util.List;

public interface AddressService {

    void saveOrUpdate(Address address);

    void delete(Integer id);

    List<Address> getAddressAll();

    Address getAddress(Integer id);
}
