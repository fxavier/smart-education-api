# SIGA SaaS API - Sistema de Gestão Escolar

Sistema de gestão escolar multi-tenant desenvolvido com Spring Boot, seguindo arquitetura hexagonal e DDD com **módulos totalmente acessíveis** e **arquitetura testada e validada**.

## 🏗️ Arquitetura

- **Spring Modulith 1.4.1**: Arquitetura modular com boundaries testadas
- **Hexagonal Architecture**: Separação clara entre domínio e infraestrutura
- **Domain-Driven Design (DDD)**: Agregados, Value Objects, Domain Events
- **Multi-tenancy**: Isolamento por schema PostgreSQL
- **Módulo Common Acessível**: Todos os pacotes do módulo common expostos para outros módulos
- **Arquitetura Validada**: ArchUnit + Spring Modulith garantem compliance arquitetural

## 🚀 Quick Start

### Pré-requisitos

- Java 21
- Docker e Docker Compose
- Maven 3.8+

### Setup

1. Clone o repositório
2. Execute o script de setup:

   ```bash
   chmod +x setup.sh
   ./setup.sh
   ```

3. Inicie os serviços:

   ```bash
   docker compose up -d
   ```

4. Execute a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```

## 📊 Serviços

- **API**: http://localhost:8080/api
- **Swagger**: http://localhost:8080/api/swagger-ui.html
- **Actuator**: http://localhost:8080/api/management
- **Prometheus**: http://localhost:9090
- **Grafana**: http://localhost:3000 (admin/admin)
- **Jaeger**: http://localhost:16686

## 🧪 Comprehensive Test Suite

### 📋 Test Categories (7 tipos)

#### 1. **Testes de Modularidade (Spring Modulith)**

```bash
./mvnw test -Dtest=ModularityTests
```

- Validação de boundaries dos módulos
- Verificação de dependências circulares
- Acessibilidade do módulo common

#### 2. **Testes de Arquitetura (ArchUnit)**

```bash
./mvnw test -Dtest=ArchitectureTests
```

- Regras de dependência entre camadas
- Validação de packages e naming conventions
- Compliance arquitetural (13 regras)

#### 3. **Testes de Acesso ao Módulo Common**

```bash
./mvnw test -Dtest=CommonModuleAccessTest
```

- Verificação de acessibilidade dos componentes
- Injeção de dependências
- APIs expostas

#### 4. **Testes de Fluxo de Eventos de Domínio**

```bash
./mvnw test -Dtest=DomainEventFlowTests
```

- Publicação e handling de eventos
- Eventos síncronos e assíncronos
- Preservação de metadata

#### 5. **Testes de Integração de Módulos**

```bash
./mvnw test -Dtest=ModuleIntegrationTests
```

- Interação entre módulos
- Configuração Spring Boot
- Value Objects e Entities

#### 6. **Testes de Contrato da API**

```bash
./mvnw test -Dtest=CommonModuleApiContractTests
```

- Validação de interfaces públicas
- Estabilidade de contratos
- Compatibilidade backwards

#### 7. **Testes de Performance**

```bash
./mvnw test -Dtest=CommonModulePerformanceTests
```

- Event publishing: >100 events/sec
- Concurrent processing: >50 events/sec
- Value object creation: >1000 objects/sec

### 🎯 **Comandos Principais**

```bash
# Executar todos os testes
./mvnw test

# Ver sumário das suites de teste
./mvnw test -Dtest=TestSuiteRunner

# Testes específicos por categoria
./mvnw test -Dtest="*ModularityTests,*ArchitectureTests"

# Testes com coverage
./mvnw test jacoco:report

# Testes de performance
./mvnw test -Dtest="*PerformanceTests"
```

### 📊 **Métricas de Qualidade**

- ✅ **13 Regras Arquiteturais** validadas com ArchUnit
- ✅ **Module Boundaries** verificados com Spring Modulith
- ✅ **Performance Benchmarks** com thresholds definidos
- ✅ **API Contracts** estáveis e testados
- ✅ **Event Flow** completamente testado

📋 **Documentação Completa**: [TEST-SUITE-DOCUMENTATION.md](./TEST-SUITE-DOCUMENTATION.md)

## 📦 Módulos

### 🛠️ **Common Module (Totalmente Acessível)**

**Status**: ✅ **Implementado e Testado**

- **Domain Layer**: Entities, Value Objects, Domain Events
- **Application Layer**: Event Listeners, Services
- **Infrastructure Layer**: Event Publishers, Repositories
- **API Layer**: Interfaces públicas para outros módulos
- **Accessibility**: Todos os packages expostos via `package-info.java`
- **Testing**: 6 categorias de teste implementadas

```bash
# Verificar acessibilidade do módulo common
./mvnw test -Dtest=CommonModuleAccessTest

# Validar boundaries do módulo
./mvnw test -Dtest=ModularityTests
```

### 🚧 **Módulos Planejados**

- **tenant**: Gestão multi-tenant (Planejado)
- **security**: Autenticação e autorização (Planejado)
- **observability**: Métricas e monitoramento (Planejado)

### 📁 **Estrutura do Common Module**

```
src/main/java/com/xavier/smarteducationapi/common/
├── api/                    # 🔗 Interfaces públicas
├── application/            # 🎯 Camada de aplicação
│   └── event/             # Event listeners
├── domain/                # 💎 Camada de domínio
│   ├── entity/           # Entities e Aggregate Roots
│   ├── event/            # Domain Events system
│   └── valueobject/      # Value Objects compartilhados
├── infrastructure/        # 🔧 Implementações
│   └── event/            # Event publishers
└── package-info.java     # 📋 Configuração de módulo
```

## 🔧 Tecnologias e Versões

### Core Framework

- **Java**: 21
- **Spring Boot**: 3.5.5
- **Spring Modulith**: 1.4.1

### Testing Stack

- **JUnit 5**: Testing framework
- **ArchUnit 1.3.0**: Architecture testing
- **Spring Modulith Test**: Module boundary testing
- **Mockito**: Mocking framework
- **Testcontainers**: Integration testing

### Database & Infrastructure

- **PostgreSQL 17.6**: Database principal
- **Flyway**: Database migrations
- **Docker Compose**: Environment setup

## 📈 Status do Projeto

### ✅ **Implementado**

- [x] Common Module com total acessibilidade
- [x] Domain Events system
- [x] Spring Modulith integration
- [x] Comprehensive Test Suite (7 categorias)
- [x] Architecture validation (ArchUnit)
- [x] Performance benchmarking
- [x] API contract testing
- [x] Multi-tenant database schema
- [x] Observability (Prometheus/Grafana)

### 🚧 **Em Desenvolvimento**

- [ ] Tenant module implementation
- [ ] Security module with authentication
- [ ] Observability module enhancements
- [ ] Advanced performance optimizations

## 🎯 **Quality Gates**

### Architectural Compliance

- ✅ **13/13** ArchUnit rules passing
- ✅ **Module boundaries** validated
- ✅ **Layer dependencies** enforced
- ✅ **Package organization** verified

### Testing Coverage

- ✅ **Module accessibility** tested
- ✅ **Event flow** end-to-end tested
- ✅ **Performance thresholds** met
- ✅ **API contracts** validated

### Performance Benchmarks

- ✅ **Event Publishing**: >100 events/sec
- ✅ **Concurrent Processing**: >50 events/sec
- ✅ **Value Object Creation**: >1000 objects/sec
- ✅ **Memory Stability**: Pressure tested

## 👨‍💻 Autor

**Xavier Nhagumbe**  
_Senior Software Architect_

---

## 📋 Links Úteis

- 📖 [Test Suite Documentation](./TEST-SUITE-DOCUMENTATION.md)
- 🏗️ [Architecture Decision Records](./docs/adr/) (Coming Soon)
- 🚀 [Deployment Guide](./docs/deployment/) (Coming Soon)
- 📊 [Performance Reports](./docs/performance/) (Coming Soon)
