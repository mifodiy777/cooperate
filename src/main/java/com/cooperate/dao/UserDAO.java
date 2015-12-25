package com.cooperate.dao;


import org.springframework.data.repository.CrudRepository;
import com.cooperate.entity.User;


/**
 * Created with IntelliJ IDEA.
 * User: velievvm
 * Date: 16.07.15
 * Time: 19:44
 * To change this template use File | Settings | File Templates.
 */
public interface UserDAO extends CrudRepository<User, Integer> {

    public User findByLogin(String login);
}