package com.dev.studylog.inheritance.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
public class LeagueResultV2 extends LeagueResult {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mayo_id")
    private MemberV2 member;
}
