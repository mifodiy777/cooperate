package com.cooperate.entity;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: velievvm
 * Date: 16.07.15
 * Time: 19:45
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "role_id")
    private Integer id;

    @Column(name = "name", unique = true)
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
}
