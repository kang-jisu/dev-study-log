package com.dev.studylog.inheritance.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;
}