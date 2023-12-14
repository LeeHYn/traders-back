package com.traders.tradersback.controller;



import com.traders.tradersback.dto.AuthResponse;
import com.traders.tradersback.dto.MemberLoginDto;
import com.traders.tradersback.dto.PasswordResetDto;
import com.traders.tradersback.dto.PasswordResetRequestDTO;
import com.traders.tradersback.model.Member;
import com.traders.tradersback.service.EmailService;
import com.traders.tradersback.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;


@RestController
@RequestMapping("/api/members")
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);
    @Autowired
    private MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Member member) {
        try {
            Member registeredMember = memberService.register(member);
            return ResponseEntity.ok(registeredMember);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginDto memberLoginDto) {
        try {
            AuthResponse response = memberService.login(memberLoginDto.getMemberId(), memberLoginDto.getMemberPassword());
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body("Login failed");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("/find-id")
    public ResponseEntity<?> findMemberId(@RequestParam String memberEmail) {
        try {
            String memberId = memberService.findMemberIdByEmail(memberEmail);
            if (memberId != null) {
                return ResponseEntity.ok(memberId);
            } else {
                return ResponseEntity.badRequest().body("No member found with the given email.");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @Autowired
    private EmailService emailService;

    @PostMapping("/reset-password-request")
    public ResponseEntity<?> requestPasswordReset(@RequestBody PasswordResetRequestDTO requestDto) {
        try {
            emailService.sendPasswordResetEmail(requestDto.getEmail());
            return ResponseEntity.ok("비밀번호 재설정 이메일을 발송했습니다.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 재설정 이메일 발송 중 오류가 발생했습니다: " + ex.getMessage());
        }
    }
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDto resetDto) {
        try {
            logger.info("Reset Password request received for token: {}", resetDto.getToken());
            memberService.resetPassword(resetDto.getToken(), resetDto.getNewPassword());
            logger.info("Password reset successful for token: {}", resetDto.getNewPassword());
            return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");
        } catch (Exception ex) {
            logger.error("Error during password reset for token: {}", resetDto.getToken(), ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경 중 오류가 발생했습니다: " + ex.getMessage());
        }
    }

}
