package com.dev.studylog.inheritance.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@MappedSuperclass
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String summonerName;
    private Long win = 0L;
    private Long lose = 0L;

    private Long kills = 0L;
    private Long deaths = 0L;
    private Long assists = 0L;

}