package com.example.springdemo1221.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_base_person")
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "person_name")
    private String personName;

    @Column(name = "sex")
    private String sex;

    @Column(name = "age")
    private int age;

    @Column(name = "login_name")
    private String loginName;

    @Column(name = "password")
    private String password;
}
