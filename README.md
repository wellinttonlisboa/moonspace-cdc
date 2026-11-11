# Moonspace CDC

Change Data Capture da Moonspace. O **connector** cadastra drivers e conectores e publica cada alteração. O **router** consome esses eventos e mantém a cópia local do catálogo.

O contrato entre os dois é o tópico Kafka `MOONSPACE-CDC-Connector`, com o registro Avro `ConnectorRecord` (`CREATED`, `UPDATED`, `DELETED`).

## Módulos

Não existe um Gradle na raiz. Cada diretório abaixo é um projeto com o próprio `gradlew`.

| Módulo | O que faz |
| --- | --- |
| [connector](connector/README.md) | API HTTP de drivers e conectores. Persiste no MongoDB e produz o evento. Porta `18701`. |
| [router](router/README.md) | Consome o tópico e aplica create, update e delete no MongoDB. Porta `18702`. |
| [framework/core](framework/core/README.md) | Tipos de domínio: agregado, validação, comandos e exceções. Sem Spring. |
| [framework/commons](framework/commons/README.md) | REST, paginação, Kafka, Redis e callbacks do Mongo. Depende do core. |

`connector` e `router` incluem `core` e `commons` pelos caminhos em `gradle.properties` (`core_folder` e `common_folder`).

## Requisitos

- JDK 21. O Gradle 8.7 deste repositório não sobe no Java 25. Se o `java` padrão da máquina for 25, aponte `JAVA_HOME` para um JDK 21 antes do `./gradlew` e deixe o padrão do sistema como está.
- MongoDB em `localhost:27017`, Redis em `localhost:6379` (senha `root`) e Kafka com Schema Registry em `localhost:8081` para subir as aplicações. Os testes unitários não precisam desses serviços.

## Build e testes

Rode um módulo por vez. Connector e router compilam `framework/core` e `framework/commons` nos mesmos diretórios `build`.

```bash
export JAVA_HOME="$HOME/.sdkman/candidates/java/21.0.13.0+9-tem"
export PATH="$JAVA_HOME/bin:$PATH"

cd connector && ./gradlew test
cd ../router && ./gradlew test
```

`test` gera o relatório JaCoCo e falha se linha ou ramo ficarem abaixo de 100% no código escrito à mão. HTML em `build/reports/jacoco/test/html`.

O artefato executável é `build/libs/app.jar` em `connector` e em `router`.

## Fluxo

1. Um cliente chama a API do connector em `/api/v1/drivers` ou `/api/v1/connectors`.
2. O caso de uso valida o agregado e grava no MongoDB.
3. O connector publica um `ConnectorRecord` em `MOONSPACE-CDC-Connector`.
4. O router lê o registro e cria, atualiza ou remove o conector no próprio MongoDB.

Documentação HTTP do connector: `http://localhost:18701/api/doc/connector/v1/api.html`.

Contato do projeto: `contato@moonspace.com.br`.
