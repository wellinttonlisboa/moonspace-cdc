package br.com.moonspace.cdc.connector.infrastructure.repositories.driver.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document("drivers")
@TypeAlias("Drivers")
public record DriverEntity(
        @Id
        String id,
        String name
) implements Serializable {

        public static DriverEntity from(String id, String name) {
                return new DriverEntity(id, name);
        }

}