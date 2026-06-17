@echo off
echo =======================================
echo  超市货物仓库管理系统 V1.0 — 启动脚本
echo =======================================
echo.
echo 请确保已安装：
echo   - MySQL 8.0+
echo   - Java 17+
echo   - Node.js 18+
echo   - Maven 3.8+
echo.
echo 步骤1：初始化数据库
echo   运行 database\init.sql 导入 MySQL
echo.
echo 步骤2：启动后端 (需要新开一个终端)
echo   cd backend ^&^& mvn spring-boot:run
echo.
echo 步骤3：启动前端 (需要新开一个终端)
echo   cd frontend ^&^& npm install ^&^& npm run dev
echo.
echo 步骤4：打开浏览器访问 http://localhost:5173
echo.
echo 测试账号：SA001 / 111111（管理员）
echo            CK001 / 111111（仓库管理员）
echo            CG001 / 111111（采购员）
echo.
pause
