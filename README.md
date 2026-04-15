# 可爱闹钟 (Cute Alarm)

一款基于 Kotlin 和 Jetpack Compose 构建的 Android 闹钟应用。

## 功能介绍

- 创建和管理多个闹钟
- 支持重复模式设置（每天、工作日、自定义）
- 简洁美观的 Material Design 3 界面
- 铃声响起时显示提醒界面
- 本地数据库存储，持久化保存闹钟数据

## 技术栈

- **语言**: Kotlin
- **UI 框架**: Jetpack Compose
- **最低 SDK**: Android 8.0 (API 26)
- **目标 SDK**: Android 14 (API 34)
- **数据库**: Room
- **架构**: MVVM + Compose

## 项目结构

```
app/
├── src/main/
│   ├── java/com/cutealarm/android/
│   │   ├── data/
│   │   │   ├── database/     # Room 数据库（Entity, DAO, Database）
│   │   │   └── repository/   # 数据仓库
│   │   ├── ui/
│   │   │   ├── alarm/        # 闹钟响铃界面
│   │   │   ├── screens/      # Compose 界面
│   │   │   ├── theme/        # 主题配置
│   │   │   └── viewmodel/    # ViewModel
│   │   └── util/             # 工具类
│   └── res/                  # 资源文件
```

## 构建

### 前置条件

- Android Studio Hedgehog 或更高版本
- JDK 17
- Android SDK API 34

### 本地构建

```bash
# 安装依赖
./gradlew assembleDebug

# 运行 debug 版本
./gradlew installDebug

# 运行测试
./gradlew test
```

### 构建输出

Debug APK 位置: `app/build/outputs/apk/debug/app-debug.apk`

## 版本历史

- **v1.0.0**: 初始版本，包含基本闹钟功能

## 注意事项

- 应用需要闹钟权限才能正常运作
- 首次使用时请授权通知和闹钟权限
- 确保应用在后台保持运行以确保闹钟准时响铃
