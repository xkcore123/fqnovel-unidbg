package com.anjia.unidbgserver.dto;

import lombok.Data;

/**
 * 段评详情请求 DTO
 * 对应接口: POST /novel/commentapi/comment/list/:group_id/v1/
 */
@Data
public class FQCommentListRequest {

    /** 章节ID（对应API路径参数 group_id） */
    private String chapterId;

    // ===== business_param =====

    /** 书籍ID */
    private String bookId;

    /** 段落索引 */
    private Integer paraIndex;

    /** 请求类型（可选） */
    private Integer reqType;

    // ===== body 通用参数 =====

    /** 评论来源，默认2(段评 NovelParaComment) */
    private Integer commentSource = 2;

    /** 排序模式: 1=SmartHot, 2=TimeAsc, 3=TimeDesc，默认1 */
    private Integer commentType = 1;

    /** 每页数量 */
    private Integer count = 20;

    /** 分组类型，默认15(Item) */
    private Integer groupType = 15;

    /** 游标(分页) */
    private String cursor;
}
