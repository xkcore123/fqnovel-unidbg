package com.anjia.unidbgserver.dto;

import lombok.Data;

@Data
public class LegadoCommentRequest {

    private String bookId;

    private String chapterId;

    private Integer paraIndex;

    private Integer count = 20;

    private String cursor;
}
