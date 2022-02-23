package com.dev.studylog.mappedsuperclass.service;

import com.dev.studylog.mappedsuperclass.domain.Member;
import com.dev.studylog.mappedsuperclass.repository.LeagueResultRepository;
import com.dev.studylog.mappedsuperclass.repository.MemberRepository;
import com.dev.studylog.mappedsuperclass.repository.MemberV2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

public class MemberService {
    private final MemberRepository memberRepository;

    MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional(readOnly = true)
    public Member getMemberById(Long id) throws Throwable {
        return (Member) memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
    }

    @Transactional
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

}
