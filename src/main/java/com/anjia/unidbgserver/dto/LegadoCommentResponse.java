package com.anjia.unidbgserver.dto;

import lombok.Data;

import java.util.List;

@Data
public class LegadoCommentResponse {

    private List<String> comments;

    private Integer commentCount;

    private Boolean hasMore;

    private String nextCursor;
}
