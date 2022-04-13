package com.dev.studylog.inheritance.repository;

import com.dev.studylog.inheritance.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@NoRepositoryBean
public interface MemberRepository<T extends Member, Long> extends JpaRepository<T, Long> {

    Optional<Member> findBySummonerName(String summonerName);
}