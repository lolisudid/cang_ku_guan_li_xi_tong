package com.warehouse.dto;

public record LoginResponse(String token, String staffId, String staffName, String role) {}
