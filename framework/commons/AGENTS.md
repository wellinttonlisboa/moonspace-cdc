# AGENTS.md

Complementa o [AGENTS.md](../../AGENTS.md) da raiz. Vale para `framework/commons`.

## Papel

Código de infraestrutura usado pelos dois apps. O handler HTTP dos apps repete o contrato de `exceptionhandler/GlobalExceptionHandler`; ao mudar um método aqui, confira as cópias em `connector` e `router`.

- `KafkaErrorHandler` trata `RecordDeserializationException` avançando o offset e dando commit. Outras falhas não mexem no consumer.
- `@Cascade` nos callbacks de Mongo cobre save e delete de referência simples e de coleção.
- `Operation` em `kafka/models/connect` é o opcode curto do Debezium (`C`, `U`, `D`). O enum Avro `br.com.moonspace.schema.registry.v1.connector.Operation` é outro tipo e vive nos apps.

## Testes

Cobertura de linha e ramo em 1.0, sem exclusão. Records e métodos `from` entram na conta: o JaCoCo deste projeto não os filtra.

- Teste solitário. Construa a `@Configuration` e chame o `@Bean`. `@Value` entra por `ReflectionTestUtils`.
- `RedisConfig` pode criar um `LettuceConnectionFactory` sem conectar; feche com `destroy()` no teste.
- Classe `SubjectTest`, método `should<Outcome>When<Condition>`, AssertJ, `@Tag("unit")`.

Mantenha `lombok.config` com `lombok.addLombokGeneratedAnnotation = true` e `config.stopBubbling = true`.

```bash
export JAVA_HOME="$HOME/.sdkman/candidates/java/21.0.13.0+9-tem"
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew test
```

Não rode este Gradle em paralelo com `connector` ou `router`.
