package com.minishop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    @GetMapping("/cart")
    public ResponseEntity<Object> getCart(HttpSession session) {
        String username = getUsername(session);
        if (username == null) {
            Map<String, String> error = Map.of("error", "Vui lòng đăng nhập để xem giỏ hàng");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        List<Map<String, Object>> cart = getCartFromSession(session);
        return ResponseEntity.ok(Map.of("username", username, "cart", cart));
    }

    @PostMapping("/cart")
    public ResponseEntity<Object> addToCart(@RequestParam("productId") int productId,
                                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                                            HttpSession session) {
        String username = getUsername(session);
        if (username == null) {
            Map<String, String> error = Map.of("error", "Vui lòng đăng nhập để thêm vào giỏ hàng");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        Map<String, Object> product = findProduct(productId);
        if (product == null) {
            Map<String, String> error = Map.of("error", "Sản phẩm không tồn tại");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        List<Map<String, Object>> cart = getCartFromSession(session);
        Map<String, Object> cartItem = new HashMap<>(product);
        cartItem.put("quantity", quantity);
        cart.add(cartItem);
        session.setAttribute("cart", cart);

        return ResponseEntity.ok(Map.of(
            "message", "Đã thêm sản phẩm vào giỏ hàng!",
            "username", username,
            "cart", cart
        ));
    }

    @GetMapping("/admin")
    public ResponseEntity<Object> adminPage(HttpSession session) {
        String username = getUsername(session);
        if (username == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Cần đăng nhập để truy cập trang admin"));
        }
        if (!"admin".equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "Chỉ admin mới truy cập được trang quản trị"));
        }
        return ResponseEntity.ok(Map.of(
            "message", "Chào mừng admin!",
            "stats", "100 đơn hàng hôm nay"
        ));
    }

    private String getUsername(HttpSession session) {
        return session == null ? null : (String) session.getAttribute("username");
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getCartFromSession(HttpSession session) {
        Object cartAttr = session.getAttribute("cart");
        if (cartAttr instanceof List) {
            return (List<Map<String, Object>>) cartAttr;
        }
        List<Map<String, Object>> cart = new ArrayList<>();
        session.setAttribute("cart", cart);
        return cart;
    }

    private Map<String, Object> findProduct(int productId) {
        return getProducts().stream()
            .filter(product -> Integer.valueOf(productId).equals(product.get("id")))
            .findFirst()
            .orElse(null);
    }
}
