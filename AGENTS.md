# AGENTS.md - fqnovel-unidbg

## 项目概述

Spring Boot 2.6.3 + Unidbg 0.9.8 服务，通过模拟 Android so 库执行来实现番茄小说接口签名与数据获取。

## 构建与运行

```bash
# 推荐方式：Maven Wrapper 编译
./mvnw clean package -DskipTests
java -jar target/unidbg-boot-server-0.0.1-SNAPSHOT.jar

# 快捷脚本（使用 Java 17，路径 /usr/lib/jvm/java-17-openjdk）
./run.sh
```

- `pom.xml` 目标为 Java 1.8，但 **推荐用 Java 11/17 运行**；Java 21+ 有 bug 不处理
- `pom.xml` 中 `<maven.test.skip>true</maven.test.skip>`，测试默认跳过
- 默认端口 `8099`（`application.yml`），监听 `0.0.0.0`

## 关键架构

### 6 个 API 路由前缀

| 前缀 | 控制器 | 职责 |
|------|--------|------|
| `/api/fqnovel` | `FQNovelController` | 书籍搜索、目录、章节（单章/批量） |
| `/api/fqsearch` | `FQSearchController` | REST 风格搜索与目录接口 |
| `/api/fullbook` | `FullBookDownloadController` | 全本下载（流式、进度、自动恢复） |
| `/api/device` | `DeviceManagementController` | 设备注册、设备池管理 |
| `/api/fq-signature` | `FQEncryptController` | so 签名生成（调用 unidbg 模拟 `libmetasec_ml.so`） |
| `/api/cache` | `CacheController` | Redis 缓存查看/删除 |

### Unidbg 核心

- `IdleFQ`（`unidbg/IdleFQ.java`）：核心 unidbg 引擎，加载 `com/dragon/read/oversea/gp/lib/libmetasec_ml.so`，通过 `generateSignature(url, header)` 调用 native 函数 `module.callFunction(emulator, 0x168c80, ...)` 生成签名
- `FQEncryptService`：单例包装 `IdleFQ`，`engineLock` 串行化调用
- `FQEncryptServiceWorker`（bean 名 `fqEncryptWorker`）：封装工作池模式。根据 `application.unidbg.async` 决定走 **同步单实例** 还是 **异步 WorkerPool**（池大小来自 `${spring.task.execution.pool.core-size}`，默认 4）
- 需要的 Android 资源放在 `src/main/resources/com/dragon/read/oversea/gp/`，首次加载时 `TempFileUtils` 将其提取到 `/tmp/`
- `application.unidbg.verbose=true` 会开启 unidbg vm 的详细日志，非常慢，生产应关闭

### 设备池

- `fq.api.device-pool.enabled=true`（默认）时启动设备池轮询，`size: 5`
- `DevicePoolService` 用 `CopyOnWriteArrayList` + `AtomicInteger` 轮询取设备
- `/api/device/register` 注册设备并更新 `application.yml` 中的 `fq.api.cookie` + `fq.api.device.*`
- `/api/device/restart` 通过 `DeviceManagementService.restartApp()` 尝试系统级重启进程（依赖 `ProcessBuilder` 调用）

### 全本下载

- 需要 Redis 支持，否则日志会有连接错误
- `AutoResumeTaskService` 定时扫描下载任务并自动恢复
- 缓存文件存放在 `results/` 目录

## Profile

- 激活的 profile：`prod`（`spring.profiles.active=prod`）
- `application-dev.yml` 存在但不启用，可用 `--spring.profiles.active=dev` 切换
- `redis.host=0.0.0.0` 表示未配置真实 Redis 地址，如需使用请改为 `127.0.0.1` 等

## 目录速查

```
src/main/java/com/anjia/unidbgserver/
  web/          — 控制器（6 个 Controller 对应 6 个路由前缀）
  service/      — 业务逻辑（FQEncryptService, DevicePoolService, FullBookDownloadService...）
  unidbg/       — IdleFQ，unidbg 模拟核心
  config/       — Spring 配置类（FQApiProperties, UnidbgProperties, JacksonConfig）
  dto/          — 请求/响应 DTO
  utils/        — TempFileUtils, FQApiUtils

src/main/resources/
  com/dragon/read/oversea/gp/  — unidbg 运行需要的 APK、so、rootfs
  legado/                      — Legado（阅读3）书源配置

tools/                         — Python 辅助脚本（设备注册、全本下载合并等）
results/                       — 全本下载输出
```

## 注意事项

- **避免高频调用** `/api/fqnovel/chapter/{bookId}/{chapterId}`，优先用 `/api/fqnovel/chapters/batch`
- `restart.sh` 硬编码了原作者本机路径 `/Users/edy/code/...`，本地运行需修改
- 设备注册接口 `/api/device/register` 为空参数时会随机生成真实设备
- `tools/` 目录下的 Python 脚本可能需要 `python3` + `requests`
