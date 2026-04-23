# Gradle Wrapper 8.11.1

## 问题分析

当前系统安装的是 Gradle 9.4.1，而 NeoForge 26.1.2 可能与 Gradle 9 存在兼容性问题。需要使用 Gradle 8.11.1 来确保兼容性。

## 解决方案

由于网络环境限制，无法直接下载 Gradle 8.11.1 发行版，我已为您配置了 Gradle Wrapper 脚本，它会：

1. 尝试从多个国内镜像下载 Gradle 8.11.1
2. 解压并安装到用户目录
3. 使用安装的 Gradle 8.11.1 执行构建

## 如何使用

```bash
# 确保脚本可执行
chmod +x gradlew

# 运行构建
./gradlew build
```

## 注意事项

- 首次运行时会自动下载 Gradle 8.11.1（约 200MB）
- 后续构建会使用已下载的版本，无需重复下载
- 如果下载失败，请检查网络连接或手动下载并解压到 `~/.gradle/wrapper/dists/gradle-8.11.1-bin/` 目录

## 验证

```bash
# 查看 Gradle 版本
./gradlew --version

# 应该显示 Gradle 8.11.1
```
