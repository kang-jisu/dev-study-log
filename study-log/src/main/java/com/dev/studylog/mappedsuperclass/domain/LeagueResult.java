package com.dev.studylog.mappedsuperclass.domain;

import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
abstract public class LeagueResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "league_result_id")
    private Long id;

    // 제 n회 대회
    private Long ordinalNum;
    // 결과 순위 (우승:1, 준우승:2, ... )
    private Long ranking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

}
