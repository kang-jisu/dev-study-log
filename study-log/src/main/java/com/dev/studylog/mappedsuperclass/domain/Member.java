package com.dev.studylog.mappedsuperclass.domain;

import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Setter
abstract public class Member {

    @Id
    @GeneratedValue
    @Column(name="member_id")
    private Long id;

    private String summonerName;
    private Long win = 0L;
    private Long lose = 0L;

    private Long kills = 0L;
    private Long deaths = 0L;
    private Long assists = 0L;

    private Date lastGamePlayed;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<LeagueResult> leagueResults = new ArrayList<>();

    public void addLeagueResult(LeagueResult leagueResultEntity) {
        this.leagueResults.add(leagueResultEntity);
        leagueResultEntity.setMember(this);
    }

    public void removeLeagueResult(LeagueResult leagueResultEntity) {
        this.leagueResults.remove(leagueResultEntity);
    }
}
