package com.dev.studylog.inheritance.service;

import com.dev.studylog.inheritance.domain.MemberV1;
import com.dev.studylog.inheritance.domain.MemberV2;
import com.dev.studylog.inheritance.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("다형성 테스트")
class MemberServiceTest {
    @Autowired
    @Qualifier("memberV1Repository")
    MemberRepository memberV1Repository;

    @Autowired
    @Qualifier("memberV2Repository")
    MemberRepository memberV2Repository;

    @Autowired
    @Qualifier("memberV2Service")
    MemberService memberService;

    @Test
    @DisplayName("엔티티와 레파지토리가 매핑되는지 테스트")
    void entityTest() {
        MemberV1 member1 = new MemberV1();
        member1.setSummonerName("v1");
        member1.setOne("one");
        memberV1Repository.save(member1);

        MemberV2 member2 = new MemberV2();
        member2.setSummonerName("v2");
        member2.setTwo("two");
        memberV2Repository.save(member2);
    }

    @Test
    @DisplayName("서비스와 엔티티가 매핑되는지 테스트")
    void serviceTest() {
        MemberV2 member2 = new MemberV2();
        member2.setSummonerName("v2");
        member2.setTwo("two");

        memberService.insert(member2);
        memberService.get();
    }
}