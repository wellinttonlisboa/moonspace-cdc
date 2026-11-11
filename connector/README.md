# Connector

API que cadastra drivers e conectores de CDC e publica cada mudança no Kafka.

Pacote: `br.com.moonspace.cdc.connector`. Aplicação: `StartConnectorApplication`. Contexto HTTP: `/api`. Porta: `18701`.

## O que este módulo faz

- Expõe CRUD de drivers em `/api/v1/drivers` e de conectores em `/api/v1/connectors`, com listagem e página (`/pageable`).
- Grava os agregados no MongoDB, banco `connector`.
- Publica `ConnectorRecord` no tópico `MOONSPACE-CDC-Connector` com serializer Avro e Schema Registry.

Swagger UI: `http://localhost:18701/api/doc/connector/v1/api.html`.

## Layout

```
src/main/java/br/com/moonspace/cdc/connector
  core/domain/entities     Connector e Driver
  core/usecases            um caso de uso por operação
  ports/inbound            comandos, queries e APIs de entrada
  ports/outbound           portas de banco e de fila
  adapters/inbound/api     controllers
  adapters/outbound        Mongo e Kafka
  infrastructure           contratos HTTP, repositórios, config
src/main/resources/schema  ConnectorRecord.avsc
```

O código em `br.com.moonspace.schema.registry` é gerado a partir do `.avsc` na compilação.

## Como rodar

Requisito: JDK 21 no `JAVA_HOME`. O Gradle 8.7 não sobe no Java 25.

```bash
export JAVA_HOME="$HOME/.sdkman/candidates/java/21.0.13.0+9-tem"
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew test
./gradlew bootRun
```

`gradle.properties` aponta `core_folder` e `common_folder` para `../framework`. Mongo, Redis e Kafka usam os defaults de `src/main/resources/application.yml`. A imagem Docker espera `build/libs/app.jar` e o agent em `build/agent`, produzidos por `./gradlew bootJar`.

Variáveis úteis: `PORT`, `MONGODB_HOST`, `MONGODB_PORT`, `REDIS_HOST`, `REDIS_PORT`, `REDIS_PASS`, `KAFKA_SERVER`, `SCHEMA_REGISTRY`.

Não rode este Gradle ao mesmo tempo que o do `router`. Os dois compilam `framework/core` e `framework/commons`.
