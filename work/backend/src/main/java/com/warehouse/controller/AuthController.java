package com.warehouse.controller;

import com.warehouse.dto.LoginRequest;
import com.warehouse.dto.LoginResponse;
import com.warehouse.entity.Staff;
import com.warehouse.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) { this.authService = authService; }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    Staff staff = authService.login(request.staffId(), request.password());
    if (staff != null) {
      return ResponseEntity.ok(new LoginResponse(staff.getId(), staff.getId(), staff.getName(), staff.getRole()));
    }
    return ResponseEntity.status(401).body(Map.of("message", "账号或密码错误"));
  }
}
