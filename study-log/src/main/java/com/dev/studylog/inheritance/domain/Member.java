package com.dev.studylog.inheritance.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract  class Member {

    @Id
    @GeneratedValue
    private Long mayoId;

    private String summonerName;
    private Long win = 0L;
    private Long lose = 0L;

    private Long kills = 0L;
    private Long deaths = 0L;
    private Long assists = 0L;

    private Date lastGamePlayed;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<LeagueResult> leagueResults = new ArrayList<>();

}
