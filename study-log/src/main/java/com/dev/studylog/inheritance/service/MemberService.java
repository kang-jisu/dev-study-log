package com.dev.studylog.inheritance.service;

import com.dev.studylog.inheritance.domain.*;
import com.dev.studylog.inheritance.repository.LeagueResultV1Repository;
import com.dev.studylog.inheritance.repository.LeagueResultV2Repository;
import com.dev.studylog.inheritance.repository.MemberV2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    // 여기서 사용할 Repository 의존성만 바꿔주면 그대로 쓸 수 있음
    private final MemberV2Repository memberRepository;
    private final LeagueResultV2Repository leagueResultV2Repository;
    private final LeagueResultV1Repository leagueResultV1Repository;

    @Transactional(readOnly = true)
    public Member getMemberById(Long id) throws Throwable {
        return (Member) memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
    }

    @Transactional(readOnly = true)
    public List<MemberV2> findLeagueResult(Long leagueId){
        return  memberRepository.findLeagueResultsByLeagueId(leagueId);
    }

    @Transactional
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }


}
