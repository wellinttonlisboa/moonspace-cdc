# moonspace-commons

Infraestrutura compartilhada pelo connector e pelo router: resposta HTTP, paginação, Kafka, Redis, criptografia e callbacks do MongoDB.

Pacote: `br.com.moonspace.commons`. Versão: `1.0.0`. Depende de `moonspace-core`. O `bootJar` está desligado; o artefato da biblioteca é `build/libs/commons-1.0.0-plain.jar`.

## O que tem aqui

| Pacote | Conteúdo |
| --- | --- |
| `rest` | `ApiResponseEntity`, `ApiError`, download |
| `pagination` | `Pagination` e `Metadata` |
| `exceptionhandler` | `GlobalExceptionHandler` |
| `kafka` | `KafkaConfig`, `KafkaErrorHandler`, modelo Debezium Connect |
| `mongo` | `@Cascade` e callbacks de save, delete e `@Id` |
| `configurations` | Redis e criptografia |
| `utils` | `GenId`, `InstantUtils`, `CollectionUtils` |
| `keycloak` | records do cliente de token |

`settings.gradle` inclui `:core` pelo `core_folder` de `gradle.properties`.

## Como testar

JDK 21.

```bash
export JAVA_HOME="$HOME/.sdkman/candidates/java/21.0.13.0+9-tem"
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew test
```

JaCoCo exige 100% de linha e de ramo, sem exclusão. Relatório em `build/reports/jacoco/test/html`.

Connector e router recompilam este módulo no mesmo `build/`. Rode um Gradle por vez.
