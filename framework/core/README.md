# moonspace-core

Tipos de domínio usados pelo connector, pelo router e pelo commons. Não depende de Spring.

Pacote: `br.com.moonspace.core`. Versão: `1.0.0`. Artefato: `build/libs/core-1.0.0.jar`.

## O que tem aqui

| Pacote | Conteúdo |
| --- | --- |
| `architecture.basic.ddd` | `Entity`, `AggregateRoot`, `ValueObject`, `Identifier`, eventos |
| `architecture.basic.ddd.exceptions` | `DomainException`, `NotificationException`, `NotFoundException` e as demais falhas de domínio |
| `architecture.basic.cqrs` | `DomainCommand` e `DomainQuery` |
| `validation` | `Validator`, `Error`, `Notification` e `ThrowsValidationHandler` |
| `messages` | `Message` |

`Notification` acumula erros. `ThrowsValidationHandler` lança `DomainException` no primeiro `append`.

## Como testar

JDK 21. Este módulo tem o próprio `gradlew` e não inclui os outros projetos.

```bash
export JAVA_HOME="$HOME/.sdkman/candidates/java/21.0.13.0+9-tem"
export PATH="$JAVA_HOME/bin:$PATH"
./gradlew test
```

O gate do JaCoCo exige 100% de linha e de ramo, sem exclusão. Relatório em `build/reports/jacoco/test/html`.

Connector e router também compilam este código, porque o `settings.gradle` deles aponta `projectDir` para esta pasta. Evite rodar o Gradle deles ao mesmo tempo que este.
