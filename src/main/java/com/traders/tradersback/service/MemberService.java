package com.traders.tradersback.service;


import com.traders.tradersback.dto.AuthResponse;
import com.traders.tradersback.model.Member;
import com.traders.tradersback.model.PasswordResetToken;
import com.traders.tradersback.repository.MemberRepository;
import com.traders.tradersback.repository.PasswordResetTokenRepository;
import com.traders.tradersback.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    public Member register(Member member) {
        return memberRepository.save(member);
    }

    public AuthResponse login(String loginInput, String password) {
        Member member = null;

        // 이메일 형식인지 확인하여 분기 처리
        if (loginInput.contains("@")) {
            member = memberRepository.findByMemberEmail(loginInput);
        } else {
            member = memberRepository.findByMemberId(loginInput);
        }

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

    public String findMemberIdByEmail(String email) {
        Member member = memberRepository.findByMemberEmail(email);
        return member != null ? member.getMemberId() : null;
    }

    public void resetPassword(String token, String newPassword) {

        // 토큰으로 PasswordResetToken 엔티티를 찾음
        Optional<PasswordResetToken> passwordResetTokenOptional = passwordResetTokenRepository.findByToken(token);

        if (!passwordResetTokenOptional.isPresent()) {
            throw new RuntimeException("Invalid password reset token.");
        }

        PasswordResetToken resetToken = passwordResetTokenOptional.get();

        // 토큰 만료 여부 확인
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password reset token is expired.");
        }

        // 회원 정보를 조회하여 비밀번호 업데이트
        Member member = memberRepository.findByMemberEmail(resetToken.getEmail());
        if (member == null) {
            throw new RuntimeException("No member found with the given email.");
        }

        // 새 비밀번호 설정 (여기서 비밀번호는 해싱되어야 함)
        System.out.println("Received new password: " + newPassword); // 로그 추가
        String hashedPassword = hashPassword(newPassword);
        System.out.println("Hashed password: " + hashedPassword); // 로그 추가
        member.setMemberPassword(hashedPassword);
        memberRepository.save(member);
    }

    // 비밀번호 해싱 메소드 (예시)
    private String hashPassword(String password) {
        // 여기에 비밀번호 해싱 로직을 구현 (예: BCrypt)
        return password; // 임시로 반환 (실제로는 해싱된 비밀번호 반환)
    }
}