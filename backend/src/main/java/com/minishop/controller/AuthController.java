package com.minishop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class AuthController {

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestParam("username") String username, HttpSession session) {
        if (username == null || username.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Username không được để trống");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        session.setAttribute("username", username.trim());
        session.setAttribute("loginTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        session.setAttribute("viewCount", 0);
        session.setMaxInactiveInterval(1800); // Session sống 30 phút

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Đăng nhập thành công");
        response.put("username", username.trim());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> profile(HttpSession session) {
        if (!isLoggedIn(session)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Chưa đăng nhập, vui lòng chuyển về /login");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        int viewCount = session.getAttribute("viewCount") == null ? 0 : (Integer) session.getAttribute("viewCount");
        viewCount++;
        session.setAttribute("viewCount", viewCount);

        String username = (String) session.getAttribute("username");
        String loginTime = (String) session.getAttribute("loginTime");

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("loginTime", loginTime);
        response.put("viewCount", viewCount);

        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/logout", method = {RequestMethod.POST, RequestMethod.GET})
    public ResponseEntity<Object> logout(HttpSession session) {
        if (!isLoggedIn(session)) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Bạn chưa đăng nhập");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        session.invalidate();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đăng xuất thành công");
        return ResponseEntity.ok(response);
    }

    private boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("username") != null;
    }
}