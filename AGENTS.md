# AGENTS.md

Regras para quem altera este repositório. Leia também o `AGENTS.md` do módulo em que for trabalhar.

## Layout

A raiz `cdc` não é um projeto Gradle. São quatro raízes independentes:

- `connector/`
- `router/`
- `framework/core/`
- `framework/commons/`

`connector/settings.gradle` e `router/settings.gradle` incluem `:core` e `:commons` pelos caminhos absolutos de `gradle.properties`. Esses caminhos apontam para `framework/` deste workspace. Ao mover o repositório, atualize `core_folder` e `common_folder` nos `gradle.properties` de `connector`, `router` e `framework/commons`.

Pacote de aplicação: `br.com.moonspace`. Grupo Maven dos apps: `br.com.moonspace.cdc`. Core e commons: `br.com.moonspace`.

## Java e Gradle

- Código-fonte em Java 21. Wrapper Gradle 8.7. Spring Boot 3.2.5 nos apps e no commons.
- O Gradle 8.7 não executa no Java 25. Antes de `./gradlew`, exporte `JAVA_HOME` para um JDK 21. Não altere o Java padrão da máquina nem o `current` do SDKMAN.
- Rode o Gradle de `connector` e o de `router` em sequência. Os dois escrevem em `framework/core/build` e `framework/commons/build`.
- Comando de verificação: `./gradlew test` dentro do módulo. A task `test` finaliza em `jacocoTestReport` e `jacocoTestCoverageVerification`. `check` depende da verificação.

## Arquitetura

Connector e router seguem portas e adaptadores.

- `core/domain` guarda agregados e validadores. Não importa Spring, Kafka nem Mongo.
- `core/usecases` depende só de portas de saída.
- `ports/inbound` define o contrato que o adaptador de entrada chama. `ports/outbound` é o que o caso de uso chama.
- `adapters` implementa as portas. `infrastructure` liga Spring, repositório e OpenAPI.

O core de domínio compartilhado fica em `framework/core`. Infraestrutura compartilhada fica em `framework/commons`.

O schema Avro está em `src/main/resources/schema/registry/v1/connector/ConnectorRecord-schema.avsc` do connector e do router. A task `avroCodeGeneration` gera `br.com.moonspace.schema.registry` dentro de `src/main/java` antes de compilar. Altere o `.avsc` e deixe a task regenerar. Os dois módulos têm cópia própria do schema; uma mudança de contrato precisa das duas.

## Testes

Padrão já usado no repositório:

- JUnit 5 e AssertJ. Classe `SubjectTest`. Método `should<Outcome>When<Condition>` com `@DisplayName`. Corpo em arrange, act, assert.
- `@Tag("unit")` ou a meta-anotação `@UnitTest` do módulo.
- Teste solitário: sem `@SpringBootTest`. Chame métodos de `@Configuration` direto. Preencha `@Value` com `ReflectionTestUtils`.
- Mockito apenas em portas e em repositórios Spring Data. Não simule agregado, value object nem record de domínio.
- JaCoCo 0.8.12 exige razão 1.0 de `LINE` e de `BRANCH`.
- Fora da conta: `StartConnectorApplication`, `StartRouterApplication` e `br/com/moonspace/schema/registry/**`. Core e commons não têm essa exclusão.
- `lombok.config` de cada módulo precisa manter `lombok.addLombokGeneratedAnnotation = true` e `config.stopBubbling = true`, para o JaCoCo ignorar o que o Lombok gera.
- Cada ramo conta, inclusive cada argumento nulo de `Objects.requireNonNull` e cada lado de um `||`.

Afirme o comportamento que o código tem hoje. Em `PageableConnectorOutboundAdapter` o nome do driver na página sai de `entity.name()`, o nome do conector. Mantenha isso, a menos que a tarefa seja corrigir esse mapeamento.

## O que deixar quieto

- Não faça commit, push nem altere `git config`, a menos que o pedido seja esse.
- Não edite `build/`, `.gradle/` nem os logs em `connector/logs` e `router/logs`.
- Não suba dependência, plugin ou versão do Java sem pedido explícito.
- O `docker-compose.yml` de cada app descreve a malha local. A porta da aplicação está em `application.yml` (`18701` e `18702`). O serviço do compose do router ainda se chama `connector`.
