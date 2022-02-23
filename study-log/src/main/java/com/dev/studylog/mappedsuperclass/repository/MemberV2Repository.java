package com.dev.studylog.mappedsuperclass.repository;

import com.dev.studylog.mappedsuperclass.domain.MemberV2;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberV2Repository extends MemberRepository<MemberV2,Long>{
}
