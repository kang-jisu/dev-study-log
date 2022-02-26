package com.dev.studylog.inheritance.repository;

import com.dev.studylog.inheritance.domain.MemberV1;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberV1Repository extends MemberRepository<MemberV1,Long>{
}
