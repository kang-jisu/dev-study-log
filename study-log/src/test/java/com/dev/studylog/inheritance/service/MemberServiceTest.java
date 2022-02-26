package com.dev.studylog.inheritance.service;

import com.dev.studylog.inheritance.domain.LeagueResultV2;
import com.dev.studylog.inheritance.domain.MemberV2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("V2 entity")
    public void V2Entity() {
        MemberV2 memberV2 = new MemberV2();
        memberV2.setOAuthNickname("123");
        memberV2.setOAuthId(123L);
        memberService.saveMember(memberV2);
        System.out.println(memberV2.getOAuthId());
        System.out.println(memberV2.getMayoId());


        LeagueResultV2 leagueResult = new LeagueResultV2();
        leagueResult.setRanking(1L);
        leagueResult.setOrdinalNum(1L);

    }
}