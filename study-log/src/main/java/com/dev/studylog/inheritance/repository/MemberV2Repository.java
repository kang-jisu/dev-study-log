package com.dev.studylog.inheritance.repository;

import com.dev.studylog.inheritance.domain.MemberV2;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberV2Repository extends MemberRepository<MemberV2,Long>{
}
