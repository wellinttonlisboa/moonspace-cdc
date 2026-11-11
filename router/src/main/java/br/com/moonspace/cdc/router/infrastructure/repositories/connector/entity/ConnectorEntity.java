package br.com.moonspace.cdc.router.infrastructure.repositories.connector.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document("connectors")
@TypeAlias("Connectors")
public record ConnectorEntity(
        @Id
        String id,
        String name,
        String driverId,
        String driverName,
        List<String> properties
) implements Serializable {

        public static ConnectorEntity from(String id, String name, String driverId, String driverName,
                                           List<String> properties) {
                return new ConnectorEntity(id, name, driverId, driverName, properties);
        }

}