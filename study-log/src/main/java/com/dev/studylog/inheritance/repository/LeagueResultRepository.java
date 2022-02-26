package com.dev.studylog.inheritance.repository;

import com.dev.studylog.inheritance.domain.LeagueResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueResultRepository<T,ID> extends JpaRepository<LeagueResult,Long> {
}
