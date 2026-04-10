package com.minishop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ShopController {

    @GetMapping("/products")
    public List<Map<String, Object>> getProducts() {
        return Arrays.asList(
            Map.of("id", 1, "name", "Áo thun nam", "price", 150000, "stock", 100),
            Map.of("id", 2, "name", "Quần jean nam", "price", 350000, "stock", 50),
            Map.of("id", 3, "name", "Giày thể thao", "price", 500000, "stock", 20)
        );
    }

    @PostMapping("/cart")
    public Map<String, Object> addToCart(@RequestParam("productId") int productId, 
                                         @RequestParam(value="quantity", defaultValue="1") int quantity,
                                         HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return Map.of("error", "Vui lòng đăng nhập để thêm vào giỏ hàng");
        }
        
        return Map.of(
            "message", "Đã thêm sản phẩm vào giỏ hàng!",
            "username", username,
            "productId", productId,
            "quantity", quantity
        );
    }
    
    @GetMapping("/admin")
    public Map<String, Object> adminPage(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (!"admin".equals(username)) {
            return Map.of("error", "Phải là admin mới đổi vô được trang quản trị!");
        }
        return Map.of("message", "Chào mừng admin!", "stats", "100 đơn hàng hôm nay");
    }
}
