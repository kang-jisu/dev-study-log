package com.dev.studylog.mappedsuperclass.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@AttributeOverride(name="id", column = @Column(name = "account_id" ))
@Entity
@Table(name = "members")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Setter
@Getter
public class MemberV1 extends Member {

    private String currentTier;
    private String highestTier;
}
