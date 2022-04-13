package com.dev.studylog.inheritance.service;

import com.dev.studylog.inheritance.domain.Member;
import com.dev.studylog.inheritance.repository.MemberRepository;

import java.util.List;

public abstract class MemberService {

    protected final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void insert(Member member) {
        memberRepository.save(member);
    }

    public void get() {
        List<Member> members = memberRepository.findAll();
        for (Member member : members) {
            System.out.println(member);
        }

    }

    public void delete(Long id) {

    }

    public void update(Member member) {

    }
}