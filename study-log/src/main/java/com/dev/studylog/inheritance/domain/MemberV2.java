package com.dev.studylog.inheritance.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "members_v2")
@Setter
@Getter
public class MemberV2 extends Member {

    private Long oAuthId;
    private String oAuthNickname;

    private String puuid;
    private Integer profileIconId;

}
