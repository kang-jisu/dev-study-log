package com.dev.studylog.mappedsuperclass.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@AttributeOverride(name="id", column = @Column(name = "mayo_id" ))
@Entity
@Table(name = "members_v2")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Setter
@Getter
public class MemberV2 extends Member{

    private Long oAuthId;
    private String oAuthNickname;

    private String puuid;
    private Integer profileIconId;
}
