# AGENTS.md

Complementa o [AGENTS.md](../AGENTS.md) da raiz. Vale para tudo dentro de `connector/`.

## Fronteiras

- `Connector` e `Driver` estendem `AggregateRoot` e validam no construtor com `Notification`. Nome nulo, em branco ou com tamanho fora de 3 a 255 caracteres (depois do `trim`) é rejeitado. `properties` do conector não pode ser nulo nem vazio.
- Caso de uso chama porta. A única exceção já existente é o update de conector, que depende do adaptador concreto `ProduceConnectorOutboundAdapter`. Novos casos de uso dependem da porta, não do adaptador.
- Controller implementa a interface em `infrastructure/api` e devolve o record de saída do caso de uso. Não coloque regra de negócio no adapter.
- `PageableConnectorOutboundAdapter` monta o driver com `entity.name()`. Os testes afirmam esse mapeamento. Só troque se a tarefa for corrigir a página.

## Avro e cobertura

- Mude `src/main/resources/schema/registry/v1/connector/ConnectorRecord-schema.avsc`. A task `avroCodeGeneration` reescreve `src/main/java/br/com/moonspace/schema/registry`. O router tem o próprio schema; mantenha os dois alinhados quando o contrato mudar.
- JaCoCo exige 1.0 de linha e de ramo. Exclusões: `StartConnectorApplication` e `br/com/moonspace/schema/registry/**`.
- Testes novos seguem `SubjectTest` / `should<Outcome>When<Condition>`, AssertJ, sem `@SpringBootTest`. Mockito só em porta ou repositório Spring Data.

## Build

JDK 21. Um Gradle por vez entre este módulo e o `router`.

```bash
export JAVA_HOME="$HOME/.sdkman/candidates/java/21.0.13.0+9-tem"
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew test
```
