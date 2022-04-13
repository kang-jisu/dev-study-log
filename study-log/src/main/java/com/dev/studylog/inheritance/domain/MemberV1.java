package com.dev.studylog.inheritance.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "accound_id"))
})
public class MemberV1 extends Member {

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<LeagueResultV1> leagueResults = new ArrayList<>();

    @Override
    public void addLeagueResult(LeagueResult leagueResult) {
        this.leagueResults.add((LeagueResultV1) leagueResult);
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
        for(LeagueResultV1 leagueResultV1 : leagueResults) {
            result.add(leagueResultV1);
        }
        return result;
    }
}