# AGENTS.md

Complementa o [AGENTS.md](../AGENTS.md) da raiz. Vale para tudo dentro de `router/`.

## Fronteiras

- A entrada do catálogo é `ConnectorInboundAdapter.on`. `CREATED` e `UPDATED` chamam o caso de uso correspondente. Qualquer outro `Operation`, inclusive `DELETED`, cai no `default` e apaga pelo id.
- `core/domain/entities/connector/Connector` valida `id` e depois `name` com `ThrowsValidationHandler` no construtor e no `update`. O primeiro erro lança `DomainException` e o segundo `if` não corre. Para cobrir as duas linhas de `append`, chame `validate` com um `Notification` depois de ajustar os campos.
- Repositório ausente lança `DomainException.of(new Error("Aggregate not found"))`. Não use `NotFoundException` aqui; esse tipo pertence ao connector.
- Não há rota Camel nem Debezium no fonte, apesar das dependências no Gradle. Não crie uma rota sem um pedido explícito.
- O `docker-compose.yml` ainda nomeia o serviço da aplicação como `connector`. A porta da aplicação é a de `application.yml` (`18702`).

## Avro e cobertura

- O contrato é `src/main/resources/schema/registry/v1/connector/ConnectorRecord-schema.avsc`. A compilação regenera `br.com.moonspace.schema.registry`. O connector tem outra cópia; mude as duas quando o registro mudar.
- JaCoCo exige 1.0 de linha e de ramo. Exclusões: `StartRouterApplication` e `br/com/moonspace/schema/registry/**`.
- Testes seguem `SubjectTest` / `should<Outcome>When<Condition>`, AssertJ, sem `@SpringBootTest`. Mockito só em porta ou gateway de repositório.

## Build

JDK 21. Um Gradle por vez entre este módulo e o `connector`.

```bash
export JAVA_HOME="$HOME/.sdkman/candidates/java/21.0.13.0+9-tem"
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew test
```
