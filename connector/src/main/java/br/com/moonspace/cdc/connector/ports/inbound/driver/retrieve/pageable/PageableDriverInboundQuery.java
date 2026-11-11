package br.com.moonspace.cdc.connector.ports.inbound.driver.retrieve.pageable;

public record PageableDriverInboundQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {

    public static PageableDriverInboundQuery from(int page,
                                                  int perPage,
                                                  String terms,
                                                  String sort,
                                                  String direction) {
        return new PageableDriverInboundQuery(page,
                perPage,
                terms,
                sort,
                direction
        );
    }

}