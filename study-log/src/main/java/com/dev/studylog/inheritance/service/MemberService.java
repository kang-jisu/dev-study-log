package com.dev.studylog.inheritance.service;

import com.dev.studylog.inheritance.domain.LeagueResult;
import com.dev.studylog.inheritance.domain.Member;
import com.dev.studylog.inheritance.repository.MemberRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public abstract class MemberService {

    protected final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member saveMember(Member member) {
        return (Member) memberRepository.save(member);
    }

    @Transactional
    public Member getMemberById(Long id) throws Throwable {
        Member member = (Member) memberRepository.findById((Long) id)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 ID입니다 :"+ id));

        return member;
    }

    @Transactional
    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Transactional
    public void saveLeagueResult(LeagueResult leagueResult, String summonerName) {
        Member member = (Member) memberRepository.findBySummonerName(summonerName).get();
        member.addLeagueResult(leagueResult);
        memberRepository.save(member);
    }

}