# Router

Consome o catálogo de conectores publicado pelo connector e mantém a cópia no MongoDB.

Pacote: `br.com.moonspace.cdc.router`. Aplicação: `StartRouterApplication`. Porta HTTP: `18702`. Contexto: `/api`. O fluxo principal não é HTTP: é o listener do tópico `MOONSPACE-CDC-Connector`, grupo `audit-connector-group`.

## O que este módulo faz

`ConnectorInboundAdapter` lê um `ConnectorRecord` e despacha pelo campo `operation`:

| Operação | Efeito |
| --- | --- |
| `CREATED` | grava o conector no banco `router` |
| `UPDATED` | atualiza o conector já gravado |
| `DELETED` | remove pelo id |

O agregado local é `core/domain/entities/connector/Connector`. Ele não é o mesmo `Connector` do módulo connector: aqui a identidade é uma `String`, e a validação de `id` e `name` usa `ThrowsValidationHandler`.

As dependências Camel e Debezium estão no `build.gradle`. Não há rota Camel no código.

## Layout

```
src/main/java/br/com/moonspace/cdc/router
  core/domain/entities/connector
  core/usecases/connector          create, update, delete
  ports                            comandos e portas de banco
  adapters/inbound/queue           listener Kafka
  adapters/outbound/db             Mongo
  infrastructure                   repositório, config, OpenAPI
src/main/resources/schema          ConnectorRecord.avsc
```

## Como rodar

Requisito: JDK 21 no `JAVA_HOME`. O Gradle 8.7 não sobe no Java 25.

```bash
export JAVA_HOME="$HOME/.sdkman/candidates/java/21.0.13.0+9-tem"
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew test
./gradlew bootRun
```

O connector precisa estar publicando no mesmo Kafka e no mesmo Schema Registry. Defaults em `src/main/resources/application.yml`: Mongo `localhost:27017` banco `router`, Redis `localhost:6379` senha `root`, Kafka `localhost:9092`, Schema Registry `http://localhost:8081`.

`gradle.properties` aponta `core_folder` e `common_folder` para `../framework`. Não rode este Gradle junto com o do `connector`.

A imagem Docker espera `build/libs/app.jar` e o agent em `build/agent` (`./gradlew bootJar`). No `docker-compose.yml` o serviço da aplicação ainda se chama `connector`.
