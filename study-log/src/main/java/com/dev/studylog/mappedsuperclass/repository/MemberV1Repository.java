package com.dev.studylog.mappedsuperclass.repository;

import com.dev.studylog.mappedsuperclass.domain.Member;
import com.dev.studylog.mappedsuperclass.domain.MemberV1;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberV1Repository extends MemberRepository<MemberV1,Long>{
}
