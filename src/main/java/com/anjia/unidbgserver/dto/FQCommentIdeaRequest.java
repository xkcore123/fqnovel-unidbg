package com.anjia.unidbgserver.dto;

import lombok.Data;

/**
 * 段评统计请求 DTO
 * 对应接口: POST /novel/commentapi/idea/list/{chapter_id}/v1
 */
@Data
public class FQCommentIdeaRequest {

    /** 章节ID */
    private String chapterId;

    /** 应用ID (query param) */
    private String aid;

    /** 安装ID (query param) */
    private String iid;

    /** 项目版本 (body param) */
    private String itemVersion = "1";
}
