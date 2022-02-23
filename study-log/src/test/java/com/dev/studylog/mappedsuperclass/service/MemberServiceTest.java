package com.dev.studylog.mappedsuperclass.service;

import com.dev.studylog.mappedsuperclass.domain.MemberV2;
import com.dev.studylog.mappedsuperclass.repository.MemberRepository;
import com.dev.studylog.mappedsuperclass.repository.MemberV1Repository;
import com.dev.studylog.mappedsuperclass.repository.MemberV2Repository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MemberServiceTest {

    @Autowired
    private MemberV2Repository memberRepository;

    private final MemberService memberService = new MemberService(memberRepository);

    @Test
    @DisplayName("V2 entity")
    public void V2Entity(){
        MemberV2 memberV2 = new MemberV2();
        memberV2.setOAuthNickname("123");
        memberV2.setOAuthId(123L);
        memberService.saveMember(memberV2);
        System.out.println(memberV2.getOAuthId());
    }
}