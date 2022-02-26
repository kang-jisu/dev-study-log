package com.dev.studylog.inheritance.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "members")
@Setter
@Getter
public class MemberV1 extends Member {

    private Long accountId;
    private String currentTier;
    private String highestTier;

}
