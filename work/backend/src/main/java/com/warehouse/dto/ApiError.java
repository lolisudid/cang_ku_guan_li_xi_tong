package com.warehouse.dto;

public record ApiError(int code, String message) {
  public static ApiError of(String message) { return new ApiError(400, message); }
  public static ApiError validation(String message) { return new ApiError(400, message); }
}
