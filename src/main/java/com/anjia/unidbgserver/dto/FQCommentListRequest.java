package com.anjia.unidbgserver.dto;

import lombok.Data;

/**
 * 段评详情请求 DTO
 * 对应接口: POST /novel/commentapi/comment/list/{chapter_id}/v1
 */
@Data
public class FQCommentListRequest {

    /** 章节ID */
    private String chapterId;

    // ===== business_param =====

    /** 书籍ID */
    private String bookId;

    /** 项目版本 */
    private String itemVersion;

    /** 段落索引 */
    private Integer paraIndex;

    // ===== query params =====

    /** 应用ID */
    private String aid;

    /** 安装ID */
    private String iid;

    // ===== body 通用参数 =====

    /** 评论来源 */
    private Integer commentSource = 2;

    /** 评论类型 */
    private Integer commentType = 1;

    /** 每页数量 */
    private Integer count = 20;

    /** 分组类型 */
    private Integer groupType = 15;

    /** 排序方式 */
    private Integer sort = 0;
}
