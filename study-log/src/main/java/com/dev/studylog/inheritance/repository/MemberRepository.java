package com.dev.studylog.inheritance.repository;

import com.dev.studylog.inheritance.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface MemberRepository<T extends Member, ID> extends JpaRepository<T, ID> {

    Optional<Member> findByName(String name);
}