package com.okay.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter

public class Paging {
    private Long presentPage;
    private Long startAt;
    private Long endBy;
    private Long totalElement;
    private Long totalPage;

    private final Long commentQty = 10L;
    private final Long pageQty = 5L;

    public Paging() {
    }

    public Paging(Long presentPage, Long startAt, Long endBy, Long totalElement, Long totalPage) {
        this.presentPage = presentPage;
        this.startAt = startAt;
        this.endBy = endBy;
        this.totalElement = totalElement;
        this.totalPage = totalPage;
    }
}
