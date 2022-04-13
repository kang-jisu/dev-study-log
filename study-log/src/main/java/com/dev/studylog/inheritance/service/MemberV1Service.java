package com.dev.studylog.inheritance.service;

import com.dev.studylog.inheritance.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MemberV1Service extends MemberService {

    public MemberV1Service(@Qualifier("memberV1Repository") MemberRepository memberRepository) {
        super(memberRepository);
    }
}