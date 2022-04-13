package com.dev.studylog.inheritance.repository;

import com.dev.studylog.inheritance.domain.Member;
import com.dev.studylog.inheritance.domain.MemberV2;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberV2Repository extends MemberRepository<MemberV2, Long> {

    @Query("select m.id from MemberV2 m where m.summonerName = :summonerName")
    Optional<Long> findIdBySummonerName(@Param("summonerName") String summonerName);

    @Query("select m from MemberV2 m where m.oAuthId = :oAuthId")
    MemberV2 findByOAuthId(@Param("oAuthId") Long oAuthId);

    Optional<MemberV2> findByPuuid(String puuid);

    Optional<MemberV2> findById(Long id);
}