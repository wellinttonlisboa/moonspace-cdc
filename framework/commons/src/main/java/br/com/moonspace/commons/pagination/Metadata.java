package br.com.moonspace.commons.pagination;

public record Metadata(
        int currentPage,
        int perPage,
        long total
) {

}