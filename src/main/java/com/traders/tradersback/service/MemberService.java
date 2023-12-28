package com.traders.tradersback.service;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.traders.tradersback.dto.AuthResponse;
import com.traders.tradersback.model.Member;
import com.traders.tradersback.model.PasswordResetToken;
import com.traders.tradersback.model.RefreshToken;
import com.traders.tradersback.repository.MemberRepository;
import com.traders.tradersback.repository.PasswordResetTokenRepository;
import com.traders.tradersback.repository.RefreshTokenRepository;
import com.traders.tradersback.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.refreshSecret}")
    private String jwtRefreshSecret;

    @Value("${jwt.refreshTokenValidityInMs}")
    private Long refreshTokenValidityInMs;

    public Member register(Member member) {
        return memberRepository.save(member);
    }

    public AuthResponse login(String loginInput, String password) {
        Member member = null;

        if (loginInput.contains("@")) {
            member = memberRepository.findByMemberEmail(loginInput);
        } else {
            member = memberRepository.findByMemberId(loginInput);
        }

        if (member != null && member.getMemberPassword().equals(password)) {
            String accessToken = createTokenForMember(member);
            RefreshToken refreshToken = createAndSaveRefreshTokenForMember(member);
            return new AuthResponse(member.getMemberId(), accessToken, refreshToken.getToken());
        }
        throw new RuntimeException("Login failed");
    }

    private String createTokenForMember(Member member) {
        Date expiration = new Date(System.currentTimeMillis() + 36000000);
        return JWT.create()
                .withSubject(member.getMemberId().toString())
                .withExpiresAt(expiration)
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    private RefreshToken createAndSaveRefreshTokenForMember(Member member) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setMember(member);
        refreshToken.setToken(generateRefreshTokenString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenValidityInMs));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    private String generateRefreshTokenString() {
        return UUID.randomUUID().toString();
    }

    public AuthResponse refreshToken(String refreshTokenString) throws Exception {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenString)
                .orElseThrow(() -> new Exception("Invalid refresh token"));

        Member member = refreshToken.getMember();
        refreshTokenRepository.delete(refreshToken);
        RefreshToken newRefreshToken = createAndSaveRefreshTokenForMember(member);
        String newAccessToken = createTokenForMember(member);

        return new AuthResponse(member.getMemberId(), newAccessToken, newRefreshToken.getToken());
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