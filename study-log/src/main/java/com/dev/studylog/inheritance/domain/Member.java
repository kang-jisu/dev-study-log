package com.dev.studylog.inheritance.domain;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.List;

@Data
@MappedSuperclass
public abstract class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String summonerName;
    private Long win = 0L;
    private Long lose = 0L;

    private Long kills = 0L;
    private Long deaths = 0L;
    private Long assists = 0L;

    public abstract void addLeagueResult(LeagueResult leagueResult);
    public abstract void removeLeagueResult(LeagueResult leagueResult);
    public abstract List<LeagueResult> getLeagueResult();
}