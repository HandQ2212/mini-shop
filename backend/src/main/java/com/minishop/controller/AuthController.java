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
    public Map<String, Object> login(@RequestParam("username") String username, HttpSession session) {
        session.setAttribute("username", username);
        session.setAttribute("loginTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        session.setAttribute("viewCount", 0);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Đăng nhập thành công");
        response.put("username", username);
        return response;
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> profile(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Chưa đăng nhập, yêu cầu chuyển về /login");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        int viewCount = (Integer) session.getAttribute("viewCount");
        viewCount++;
        session.setAttribute("viewCount", viewCount);

        String loginTime = (String) session.getAttribute("loginTime");

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("loginTime", loginTime);
        response.put("viewCount", viewCount);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public Map<String, String> logout(HttpSession session) {
        session.invalidate();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Đăng xuất thành công");
        return response;
    }
}
