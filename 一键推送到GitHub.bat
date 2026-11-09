@echo off
chcp 65001 >nul
title 推送海信电视守护系统到 GitHub 云端打包
echo =======================================================
echo    海信 65E3G-PRO 电视学习守护系统 - 云端打包推送工具
echo =======================================================
echo.
echo 目标仓库: https://github.com/laowang-laowang-laowang/hisense-study-guard.git
echo.
echo 正在推送到 GitHub，请稍候...
echo （若弹出浏览器登录窗口，请点击 Authorize 允许授权）
echo.

git branch -M main
git remote set-url origin https://github.com/laowang-laowang-laowang/hisense-study-guard.git
git push -u origin main

echo.
if %errorlevel% equ 0 (
    echo =======================================================
    echo [成功] 全部代码与 500 道题库已成功推送到 GitHub！
    echo.
    echo 云端服务器（GitHub Actions）已自动开始编译 APK！
    echo 大约 2 分钟后完成，请在下方网址下载现成 APK：
    echo https://github.com/laowang-laowang-laowang/hisense-study-guard/actions
    echo =======================================================
) else (
    echo =======================================================
    echo [提示] 推送未完成，如果提示认证失败，请在对话框中发给助手 Token 进行静默推送。
    echo =======================================================
)
echo.
pause
