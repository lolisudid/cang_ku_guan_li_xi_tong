@echo off
chcp 65001 >nul
echo.
echo =======================================
echo  超市货物仓库管理系统 V1.0
echo =======================================
echo.
echo 正在启动后端服务...
start "WarehouseServer" /B "F:\JDK 17\bin\java.exe" -jar "F:\zuomian\SQL_work\work\backend\target\supermarket-warehouse-1.0.0.jar" --server.port=9090
echo 等待服务启动...
timeout /t 10 /nobreak >nul
echo.
echo =======================================
echo  系统已就绪！
echo.
echo  访问地址：http://localhost:9090
echo.
echo  测试账号：
echo    SA001 / 111111 — 管理员
echo    CK001 / 111111 — 仓库管理员
echo    CG001 / 111111 — 采购员
echo.
echo  按任意键停止服务...
pause >nul
taskkill /f /im java.exe >nul 2>&1
echo 服务已停止。
pause
