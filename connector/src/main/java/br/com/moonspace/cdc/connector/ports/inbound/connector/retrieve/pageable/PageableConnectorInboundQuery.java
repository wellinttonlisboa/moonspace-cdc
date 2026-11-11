package br.com.moonspace.cdc.connector.ports.inbound.connector.retrieve.pageable;

public record PageableConnectorInboundQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {

    public static PageableConnectorInboundQuery from(int page,
                                                     int perPage,
                                                     String terms,
                                                     String sort,
                                                     String direction) {
        return new PageableConnectorInboundQuery(page,
                perPage,
                terms,
                sort,
                direction
        );
    }

}