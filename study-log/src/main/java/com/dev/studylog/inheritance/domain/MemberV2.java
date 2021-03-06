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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<LeagueResultV2> leagueResults = new ArrayList<>();

    @Override
    public void addLeagueResult(LeagueResult leagueResult) {
        this.leagueResults.add((LeagueResultV2) leagueResult);
        leagueResult.setMember(this);
    }

    @Override
    public void removeLeagueResult(LeagueResult leagueResult) {
        this.leagueResults.remove(leagueResult);
        leagueResult.setMember(null);
    }

    @Override
    public List<LeagueResult> getLeagueResult() {
        List<LeagueResult> result = new ArrayList<>();
        for(LeagueResultV2 leagueResultV2 : leagueResults) {
            result.add(leagueResultV2);
        }
        return result;
    }
}