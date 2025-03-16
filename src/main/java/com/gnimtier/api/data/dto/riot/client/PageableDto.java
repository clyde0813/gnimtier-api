package com.gnimtier.api.data.dto.riot.client;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class PageableDto {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PageableResponseDto<T> {
        private List<T> data;
        private String sortBy;
        private int pageSize;
        private int page;

        private Boolean hasNext;
        private Boolean hasPrevious;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PlainRequestDto {
        private String sortBy;
        private int page;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PageableRequestDto<T> {
        private List<T> data;
        private String sortBy;
        private int page;
        private int pageSize;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PageableIDRequestDto {
        private String id;
        private String sortBy;
        private int page;
    }
}
