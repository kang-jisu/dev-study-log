package com.dev.studylog.inheritance.repository;

import com.dev.studylog.inheritance.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository<T,ID> extends JpaRepository<Member,Long> {

    Optional<T> findBySummonerName(String summonerName);

    @Query("select m from Member m join fetch m.leagueResults lr where lr = :leagueId")
    public List<T> findLeagueResultsByLeagueId(@Param("leagueId") Long leagueId);
}
