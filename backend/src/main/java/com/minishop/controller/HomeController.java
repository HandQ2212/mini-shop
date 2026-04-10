package com.minishop.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public Map<String, Object> home(HttpSession session, 
                                    @CookieValue(value = "theme", defaultValue = "light") String theme) {
        Map<String, Object> response = new HashMap<>();
        response.put("title", "Mini Profile App");
        response.put("theme", theme);
        
        String username = (String) session.getAttribute("username");
        if (username != null) {
            response.put("message", "Xin chào, " + username);
        } else {
            response.put("message", "Bạn chưa đăng nhập");
        }
        return response;
    }

    @PostMapping("/set-theme/{theme}")
    public Map<String, String> setTheme(@PathVariable String theme, HttpServletResponse response) {
        Map<String, String> result = new HashMap<>();
        if (!"light".equals(theme) && !"dark".equals(theme)) {
            result.put("error", "Chỉ chấp nhận light hoặc dark");
            return result;
        }

        Cookie cookie = new Cookie("theme", theme);
        cookie.setMaxAge(600); // 10 phút
        cookie.setPath("/");
        response.addCookie(cookie);

        result.put("message", "Lưu theme thành công");
        result.put("theme", theme);
        return result;
    }
}
