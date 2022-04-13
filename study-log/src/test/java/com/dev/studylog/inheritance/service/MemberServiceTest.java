package com.dev.studylog.inheritance.service;

import com.dev.studylog.inheritance.domain.*;
import com.dev.studylog.inheritance.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        memberV1Repository.save(member1);

        MemberV2 member2 = new MemberV2();
        member2.setSummonerName("v2");
        memberV2Repository.save(member2);
    }

    @Test
    @DisplayName("멤버 저장 테스트")
    void serviceTest() {
        MemberV2 member2 = new MemberV2();
        member2.setSummonerName("v2");

        memberService.saveMember(member2);
    }

    @Test
    @DisplayName("ID로 조회 테스트")
    void getMemberByIdTest() throws Throwable {
        MemberV2 memberV2 = new MemberV2();
        memberV2.setSummonerName("감귤or가씨");
        memberV2.setOAuthId(1L);
        memberV2.setWin(2L);
        memberV2.setLose(3L);

        Member member = memberService.saveMember(memberV2);
        Member memberById = memberService.getMemberById(member.getId());

        assertThat(memberById.getSummonerName()).isEqualTo("감귤or가씨");
    }

    @Test
    @DisplayName("리그 결과 저장 테스트")
    void saveLeagueResultTest() throws Throwable {
        MemberV2 memberV2 = new MemberV2();
        memberV2.setSummonerName("감귤or가씨");
        memberV2.setOAuthId(1L);
        memberV2.setWin(2L);
        memberV2.setLose(3L);

        Member member = memberService.saveMember(memberV2);

        LeagueResult leagueResult = new LeagueResultV2();
        leagueResult.setRanking(1L);
        leagueResult.setOrdinalNum(1L);
        memberService.saveLeagueResult(leagueResult, "감귤or가씨");


        List<Member> allMembers = memberService.findAllMembers();
        assertThat(allMembers.get(0).getLeagueResult().get(0).getMember().getSummonerName()).isEqualTo("감귤or가씨");
    }
}