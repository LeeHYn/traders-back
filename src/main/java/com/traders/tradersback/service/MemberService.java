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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    public Member register(Member member) {
        // 비밀번호 해싱
        String hashedPassword = passwordEncoder.encode(member.getMemberPassword());
        member.setMemberPassword(hashedPassword);

        return memberRepository.save(member);
    }

    public AuthResponse login(String loginInput, String password) {
        // 로그인 입력값 로그 출력
        System.out.println("Login attempt with input: " + loginInput);

        Member member = null;

        if (loginInput.contains("@")) {
            member = memberRepository.findByMemberEmail(loginInput);
            // 이메일로 멤버 조회 로그
            System.out.println("Attempting to find member by email: " + loginInput);
        } else {
            member = memberRepository.findByMemberId(loginInput);
            // 아이디로 멤버 조회 로그
            System.out.println("Attempting to find member by ID: " + loginInput);
        }

        if (member != null) {
            // 멤버 정보 로그 (비밀번호는 제외)
            System.out.println("Member found: " + member.getMemberId());

            if (passwordEncoder.matches(password, member.getMemberPassword())) {
                // 비밀번호 일치 로그
                System.out.println("Password matches for member: " + member.getMemberId());

                String accessToken = createTokenForMember(member);
                createAndSaveRefreshTokenForMember(member); // 리프레시 토큰은 생성하되 클라이언트에 반환하지 않음

                // AuthResponse 객체 생성 시 memberNum도 포함하여 생성
                return new AuthResponse(member.getMemberNum(), member.getMemberId(), accessToken); // memberNum 추가
            } else {
                // 비밀번호 불일치 로그
                System.out.println("Password mismatch for member: " + member.getMemberId());
            }
        } else {
            // 멤버 찾기 실패 로그
            System.out.println("No member found with input: " + loginInput);
        }
        throw new RuntimeException("Login failed");
    }



    private String createTokenForMember(Member member) {
        try {
            Date expiration = new Date(System.currentTimeMillis() + 36000000); // 10시간 후 만료
            String token = JWT.create()
                    .withSubject(member.getMemberId().toString())
                    .withExpiresAt(expiration)
                    .sign(Algorithm.HMAC256(jwtSecret));

            // 토큰 생성 성공 로그
            System.out.println("Token created successfully for member: " + member.getMemberId());
            System.out.println("Token: " + token);

            return token;
        } catch (Exception e) {
            // 토큰 생성 실패 로그
            System.out.println("Failed to create token for member: " + member.getMemberId());
            e.printStackTrace(); // 오류 스택 추적 출력

            // 여기서 적절한 예외 처리를 수행할 수 있습니다.
            throw e; // 또는 적절한 예외를 던지세요.
        }
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
        createAndSaveRefreshTokenForMember(member); // 새 리프레시 토큰 생성하지만 클라이언트에 반환하지 않음
        String newAccessToken = createTokenForMember(member);

        return new AuthResponse(member.getMemberId(), newAccessToken); // 새 엑세스 토큰만 반환
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
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

}