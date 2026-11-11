package br.com.moonspace.cdc.connector.infrastructure.repositories.connector.entity;

import br.com.moonspace.cdc.connector.infrastructure.repositories.driver.entity.DriverEntity;
import br.com.moonspace.commons.mongo.Cascade;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document("connectors")
@TypeAlias("Connectors")
public record ConnectorEntity(
        @Id
        String id,
        String name,
        List<String> properties,
        @DBRef
        @Cascade
        DriverEntity driver
) implements Serializable {

        public static ConnectorEntity from(String id, String name, List<String> properties, DriverEntity driver) {
                return new ConnectorEntity(id, name, properties, driver);
        }

}