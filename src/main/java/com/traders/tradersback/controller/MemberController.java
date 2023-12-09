package com.traders.tradersback.controller;



import com.traders.tradersback.dto.AuthResponse;
import com.traders.tradersback.dto.MemberLoginDto;
import com.traders.tradersback.model.Member;
import com.traders.tradersback.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {
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
}
