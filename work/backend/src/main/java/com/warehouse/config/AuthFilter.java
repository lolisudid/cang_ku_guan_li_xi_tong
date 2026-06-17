package com.warehouse.config;

import com.warehouse.dao.StaffDao;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class AuthFilter implements Filter {
  private final StaffDao staffDao;

  public AuthFilter(StaffDao staffDao) { this.staffDao = staffDao; }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;
    String path = req.getRequestURI();

    // 静态资源和公开 API 无需鉴权
    if (!path.startsWith("/api/") || path.startsWith("/api/auth/login")) {
      chain.doFilter(request, response);
      return;
    }

    String auth = req.getHeader("Authorization");
    if (auth == null || !auth.startsWith("Bearer ")) {
      res.setStatus(401);
      res.getWriter().write("{\"code\":401,\"message\":\"未登录或登录已过期\"}");
      return;
    }

    String staffId = auth.substring(7);
    if (!staffDao.existsById(staffId)) {
      res.setStatus(401);
      res.getWriter().write("{\"code\":401,\"message\":\"无效的用户\"}");
      return;
    }
    chain.doFilter(request, response);
  }
}