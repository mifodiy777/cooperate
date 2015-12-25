package com.cooperate.service.impl;

import com.cooperate.dao.AddressDAO;
import com.cooperate.entity.Address;
import com.cooperate.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressDAO addressDAO;

        public void saveOrUpdate(Address address) {
        addressDAO.save(address);
    }

    public void delete(Integer id) {
        addressDAO.delete(id);
    }

    public List<Address> getAddressAll() {
        return addressDAO.findAll();
    }

    public Address getAddress(Integer id) {
        return addressDAO.getOne(id);
    }
}

