package com.traders.tradersback.repository;

import com.traders.tradersback.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberId(String memberId);
    Member findByMemberEmail(String memberEmail); // 이메일을 기반으로 회원을 찾는 메서드 추가
}
