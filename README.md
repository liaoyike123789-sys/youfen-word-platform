# 优分单词平台 Youfen Word Platform

> 一个基于 **Spring Boot** 的单词学习 Web / 小程序后端 + 完整接口测试实践工程。
> 项目覆盖：用户认证、单词查询、学习记录、错题本、打卡与学习数据统计，共 **32 个核心接口**。
> 围绕本项目完成了 **86 条测试用例设计、JUnit5 + RestAssured 自动化、Postman / Fiddler 接口调试、JMeter 压测、禅道缺陷跟踪与 Linux 日志排查** 的完整测试流程。

- 作者：廖奕科
- 技术栈：Spring Boot 3.2 / JDK 17 / Spring Data JPA / H2(MySQL 兼容) / springdoc-openapi
- 测试栈：JUnit 5 / RestAssured / Postman / Fiddler / JMeter / 禅道
- 演示周期：2026.04 – 2026.05

---

## 目录结构

```
youfen-word-platform/
├── backend/                     # 被测后端（Spring Boot 工程）
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/youfen/word/
│       │   ├── YoufenWordApplication.java
│       │   ├── common/          # 统一返回 Result / 全局异常
│       │   ├── config/          # CORS 配置
│       │   ├── entity/          # User / Word / WordCategory / LearningRecord / ReviewItem
│       │   ├── repository/
│       │   ├── service/
│       │   └── controller/     # Auth / Word / Learning / Stats / Health
│       └── resources/
│           ├── application.yml  # H2 开箱即跑，附 MySQL 切换注释
│           └── data.sql         # 初始化 4 个分类 + 30 个单词
│
├── test-automation/             # 接口自动化测试工程（JUnit5 + RestAssured）
│   ├── pom.xml
│   └── src/test/java/com/youfen/word/
│       ├── BaseApiTest.java
│       ├── SmokeTest.java
│       ├── AuthApiTest.java
│       ├── WordApiTest.java
│       ├── LearningApiTest.java
│       └── StatsApiTest.java
│
├── test-docs/                  # 测试文档与过程资产
│   ├── 测试计划.md
│   ├── 测试用例清单.xlsx        # 86 条用例 + 统计
│   ├── 禅道缺陷记录.md          # 12 条典型 bug 样例
│   ├── 测试报告.md
│   └── 问题排查日志.md          # 8 个真实排查案例
│
├── postman/
│   └── Youfen-Word-Platform.postman_collection.json   # 32 个接口一键导入
│
└── jmeter/
    └── 单词列表压测.jmx          # 50 并发 × 30s 压 /api/words
```

---

## 30 秒跑起来

### 1. 启动后端

要求：JDK 17+、Maven 3.8+。

```bash
cd backend
mvn spring-boot:run
```

启动后访问：

- 健康检查：http://localhost:8080/api/health
- Swagger UI：http://localhost:8080/swagger-ui.html
- H2 控制台：http://localhost:8080/h2-console （JDBC URL 见 `application.yml`）

数据库默认用 H2 内存库，**启动即自动建表 + 灌入 30 个单词 / 4 个分类**，无需任何额外配置。

切 MySQL 的方式见 `backend/src/main/resources/application.yml` 顶部注释。

### 2. 跑接口自动化测试

先保持后端在 8080 运行，再开一个终端：

```bash
cd test-automation
mvn test
```

### 3. 导入 Postman

Postman → Import → 选择 `postman/Youfen-Word-Platform.postman_collection.json`。
环境变量已内置：`baseUrl=http://localhost:8080`；登录后把返回的 token 写入集合变量 `token`，其余鉴权接口自动带上。

### 4. 跑 JMeter 压测

JMeter → 打开 `jmeter/单词列表压测.jmx` → 启动。场景：50 并发压 `GET /api/words` 30 秒。

---

## 32 个核心接口一览

| # | 模块 | 方法 | 路径 | 说明 |
|---|---|---|---|---|
| 1 | 认证 | POST | /api/auth/register | 注册 |
| 2 | 认证 | POST | /api/auth/login | 登录，返回 token |
| 3 | 认证 | POST | /api/auth/logout | 登出 |
| 4 | 认证 | GET | /api/auth/me | 当前用户 |
| 5 | 认证 | PUT | /api/auth/password | 修改密码 |
| 6 | 认证 | PUT | /api/auth/profile | 完善资料 |
| 7 | 单词 | GET | /api/words | 分页查询 |
| 8 | 单词 | GET | /api/words/{id} | 单词详情 |
| 9 | 单词 | GET | /api/words/category/{cid} | 按分类查 |
| 10 | 单词 | GET | /api/words/search?keyword= | 搜索 |
| 11 | 单词 | GET | /api/words/letter/{letter} | 按字母分组 |
| 12 | 单词 | POST | /api/words | 新增单词 |
| 13 | 单词 | PUT | /api/words/{id} | 修改单词 |
| 14 | 单词 | DELETE | /api/words/{id} | 删除单词 |
| 15 | 单词 | GET | /api/categories | 分类列表 |
| 16 | 单词 | POST | /api/categories | 新增分类 |
| 17 | 单词 | DELETE | /api/categories/{id} | 删除分类 |
| 18 | 学习 | GET | /api/learning/today | 今日学习队列 |
| 19 | 学习 | POST | /api/learning/review | 标记认识 / 不认识 |
| 20 | 学习 | GET | /api/learning/records | 学习记录 |
| 21 | 学习 | GET | /api/learning/wrongbook | 错题本 |
| 22 | 学习 | DELETE | /api/learning/wrongbook/{wordId} | 移出错题本 |
| 23 | 学习 | POST | /api/learning/checkin | 打卡 |
| 24 | 学习 | GET | /api/learning/checkin/status | 今日打卡状态 |
| 25 | 学习 | GET | /api/learning/history | 历史趋势 |
| 26 | 统计 | GET | /api/stats/overview | 总览 |
| 27 | 统计 | GET | /api/stats/today | 今日数据 |
| 28 | 统计 | GET | /api/stats/trend | 学习趋势 |
| 29 | 统计 | GET | /api/stats/mastery | 掌握分布 |
| 30 | 统计 | GET | /api/stats/streak | 连续打卡天数 |
| 31 | 统计 | GET | /api/stats/accuracy | 今日正确率 |
| 32 | 其他 | GET | /api/health | 健康检查 |

---

## 测试资产速查

| 资产 | 位置 | 数量 / 说明 |
|---|---|---|
| 测试计划 | `test-docs/测试计划.md` | 范围 / 策略 / 准入准出 |
| 测试用例 | `test-docs/测试用例清单.xlsx` | **86 条**（认证 18 + 单词 30 + 学习 20 + 统计 12 + 兼容性能 6） |
| 缺陷记录 | `test-docs/禅道缺陷记录.md` | 12 条典型 bug（含 1 致命 / 3 严重） |
| 测试报告 | `test-docs/测试报告.md` | 通过率 95.3%，性能 90% < 500ms |
| 排查日志 | `test-docs/问题排查日志.md` | 8 个真实排查案例（端口占用 / MySQL 连接 / 中文乱码 / Fiddler 抓包 / Linux 404 等） |
| 自动化 | `test-automation/` | JUnit5 + RestAssured，覆盖冒烟 + 4 个模块 |
| Postman | `postman/*.json` | 32 个接口一键导入 |
| JMeter | `jmeter/*.jmx` | 50 并发压测脚本 |

---

## 设计说明 / 已知边界

- Token 存内存 `ConcurrentHashMap`，重启即失效；生产环境应替换为 Redis。
- 密码当前为明文存储，仅用于演示；生产环境必须换 BCrypt。
- 数据库默认 H2（MySQL 兼容模式），保证开箱即跑；切 MySQL 的步骤写在 `application.yml` 注释里。
- 初始 30 个单词为演示数据，正式上线需替换为完整词库。

---

## 关于作者

- 廖奕科 · 湛江科技学院 · 计算机科学与技术 · 2027 届
- 求职方向：技术支持 / 软件测试
- GitHub：https://github.com/liaoyike123789-sys
