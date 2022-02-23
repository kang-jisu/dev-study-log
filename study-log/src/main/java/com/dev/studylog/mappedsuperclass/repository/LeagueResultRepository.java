package com.dev.studylog.mappedsuperclass.repository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeagueResultRepository<T,ID> extends JpaRepository<T,ID> {
}
