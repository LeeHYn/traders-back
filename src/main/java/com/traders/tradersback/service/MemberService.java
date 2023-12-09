package com.traders.tradersback.service;


import com.traders.tradersback.dto.AuthResponse;
import com.traders.tradersback.model.Member;
import com.traders.tradersback.repository.MemberRepository;
import com.traders.tradersback.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public Member register(Member member) {
        return memberRepository.save(member);
    }

    public AuthResponse login(String memberId, String password) {
        Member member = memberRepository.findByMemberId(memberId);
        if (member != null && member.getMemberPassword().equals(password)) {
            // JWT 토큰 생성
            String token = createTokenForMember(member);
            return new AuthResponse(member.getMemberId(), token);
        }
        throw new RuntimeException("Login failed"); // 혹은 적절한 예외 처리
    }
    private String createTokenForMember(Member member) {
        // 토큰 만료 시간 설정 (예: 10시간)
        Date expiration = new Date(System.currentTimeMillis() + 36000000);

        return JWT.create()
                .withSubject(member.getMemberId().toString()) // 'subject' 클레임 설정
                .withExpiresAt(expiration) // 만료 시간 설정
                .sign(Algorithm.HMAC256("secret")); // 여기서 'secret'은 서명에 사용되는 비밀 키입니다.
    }
}