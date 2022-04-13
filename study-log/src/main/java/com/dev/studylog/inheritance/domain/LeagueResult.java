package com.dev.studylog.inheritance.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public abstract class LeagueResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "league_result_id")
    private Long id;

    private Long ranking;
    private Long ordinalNum;

    public abstract void setMember(Member member);
    public abstract Member getMember();
}
