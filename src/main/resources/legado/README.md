# FQNovel Legado 书源配置

本目录包含用于 @gedoor/legado 阅读3 的 FQNovel API 书源配置文件。

## 书源文件

### 1. fqnovel.json
- **名称**: FQNovel-unidbg
- **类型**: 标准书源
- **功能**: 支持搜索、发现（热门推荐/最近更新）、详情获取、目录浏览、单章节内容获取、搜索分页
- **特点**: 稳定可靠，适合日常使用；禁用 CookieJar 减少无关请求；多发现分类

### 2. fqnovel-batch-booksource.json  
- **名称**: FQNovel-unidbg (批量)
- **类型**: 标准书源（批量优化版）
- **功能**: 与标准版相同，超时时间更长（300s），适合连续阅读场景
- **特点**: 可与标准版同时导入，互为备用；启用后可在 Legado 中灵活切换

## 使用方法

### 1. 启动 FQNovel API 服务
```bash
# 确保服务在 localhost:8099 运行（默认端口）
java -jar target/unidbg-boot-server-0.0.1-SNAPSHOT.jar
```

### 2. 导入书源到 Legado
1. 打开 Legado 阅读 APP
2. 进入「书源管理」
3. 选择「导入书源」 
4. 复制对应的 JSON 配置文件内容
5. 粘贴并导入

### 3. 书源配置说明

#### API 端点映射
- **搜索**: `/api/fqsearch/books` 
- **书籍详情**: `/api/fqnovel/book/{bookId}`
- **书籍目录**: `/api/fqsearch/directory/{bookId}`
- **章节内容**: `/api/fqnovel/item_id/{itemId}` 或 `/api/fqnovel/chapter/{bookId}/{chapterId}`
- **批量章节**: `/api/fqnovel/chapters/batch`

#### 关键参数
- `bookId`: 书籍唯一标识
- `itemId`: 章节唯一标识  
- `chapterRange`: 章节范围 (如 "1-30")
- `chapterIds`: 章节 ID 列表 (如 [
  "7271262165057667646",
  "7271262274424144446"
  ])
- `query`: 搜索关键词
- `offset`: 分页偏移量
- `count`: 每页数量

### 3. 段评接口（书源专用聚合）
- **接口**: `POST /api/legado/comment`
- **请求体**:
  - `bookId` 书籍ID
  - `chapterId` 章节ID
  - `paraIndex` 段落索引（从1开始）
  - `count` 可选，默认20
  - `cursor` 可选，分页游标
- **返回结构**:
  - `data.comments`: 段评文本数组
  - `data.commentCount`: 本次返回数量
  - `data.hasMore`: 是否有下一页
  - `data.nextCursor`: 下一页游标


### 修改服务地址
如果 FQNovel API 服务部署在其他地址，需要修改以下字段:
- `bookSourceUrl`
- `exploreUrl` 
- `searchUrl`
- `ruleBookInfo.tocUrl`
- `ruleExplore.bookUrl`
- `ruleSearch.bookUrl`