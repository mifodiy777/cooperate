package com.cooperate.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Entity
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_person", nullable = false)
    private Integer id;

    @Column(name = "name_person")
    private String name;

    @Column(name = "lastname_person")
    private String lastName;

    @Column(name = "fathername_person")
    private String fatherName;

    @Column(name = "telephone")
    private String telephone;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "benefits")
    private String benefits;

    @OneToMany(mappedBy = "person")
    private List<Garag> garagList;

    @Column(name = "member_board")
    private Boolean memberBoard;

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public List<Garag> getGaragList() {
        return garagList;
    }

    public void setGaragList(List<Garag> garagList) {
        this.garagList = garagList;
    }

    public Boolean getMemberBoard() {
        return memberBoard;
    }

    public void setMemberBoard(Boolean memberBoard) {
        this.memberBoard = memberBoard;
    }

    public String getFIO() {
        return this.lastName + " " + this.name + " " + this.fatherName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person person = (Person) o;

        if (address != null ? !address.equals(person.address) : person.address != null) return false;
        if (benefits != null ? !benefits.equals(person.benefits) : person.benefits != null) return false;
        if (fatherName != null ? !fatherName.equals(person.fatherName) : person.fatherName != null) return false;
        if (garagList != null ? !garagList.equals(person.garagList) : person.garagList != null) return false;
        if (id != null ? !id.equals(person.id) : person.id != null) return false;
        if (lastName != null ? !lastName.equals(person.lastName) : person.lastName != null) return false;
        if (memberBoard != null ? !memberBoard.equals(person.memberBoard) : person.memberBoard != null) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        if (telephone != null ? !telephone.equals(person.telephone) : person.telephone != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (fatherName != null ? fatherName.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (benefits != null ? benefits.hashCode() : 0);
        result = 31 * result + (garagList != null ? garagList.hashCode() : 0);
        result = 31 * result + (memberBoard != null ? memberBoard.hashCode() : 0);
        return result;
    }
}
