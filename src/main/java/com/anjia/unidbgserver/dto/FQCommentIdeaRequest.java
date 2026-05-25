package com.anjia.unidbgserver.dto;

import lombok.Data;

/**
 * 段评统计请求 DTO
 * 对应接口: POST /novel/commentapi/idea/list/:item_id/v1/
 */
@Data
public class FQCommentIdeaRequest {

    /** 章节ID（对应API路径参数 item_id） */
    private String chapterId;

    /** 书籍ID（用于business_param） */
    private String bookId;

    /** 评论来源，默认2(段评 NovelParaComment) */
    private Integer commentSource = 2;

    /** 分组类型，默认15(Item) */
    private Integer groupType = 15;
}
