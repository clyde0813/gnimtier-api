package com.gnimtier.api.data.dto.riot.client.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageableResponseDto<T> {
    private List<T> data;
    private String sortBy;
    private int pageSize;
    private int page;

    private Boolean hasNext;
    private Boolean hasPrevious;
}