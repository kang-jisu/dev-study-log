package com.dev.studylog.inheritance.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "mayo_id"))
})
public class MemberV2 extends Member {

    private Long oAuthId;
    private String puuid;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<LeagueResultV2> leagueResults = new ArrayList<>();
}