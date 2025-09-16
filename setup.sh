#!/bin/bash

# ========= setup.sh =========
# Script to setup the project structure and configuration files
# Author: Xavier Nhagumbe
# Date: 2025-09-15

echo "🚀 Setting up SIGA SaaS API project structure..."

# Create directory structure
echo "📁 Creating directories..."
mkdir -p config
mkdir -p scripts
mkdir -p logs
mkdir -p src/main/resources/db/migration
mkdir -p src/main/resources/db/tenant
mkdir -p src/test/resources

# Create prometheus configuration
echo "📝 Creating Prometheus configuration..."
cat > config/prometheus.yml << 'EOF'
global:
  scrape_interval: 15s
  evaluation_interval: 15s

scrape_configs:
  - job_name: 'spring-boot'
    metrics_path: '/api/management/prometheus'
    static_configs:
      - targets: ['host.docker.internal:8080']
EOF

# Create .env.example file
echo "📝 Creating .env.example file..."
cat > .env.example << 'EOF'
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=school_management
DB_USERNAME=admin
DB_PASSWORD=admin

# Redis Configuration
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# JWT Configuration
JWT_SECRET=mySecretKeyForDevelopmentPurposesOnlyChangeInProduction

# Jaeger Configuration
JAEGER_HOST=localhost
JAEGER_PORT=4317

# CORS Configuration
CORS_ORIGINS=http://localhost:4200

# Spring Profile
SPRING_PROFILES_ACTIVE=dev
EOF


# Create README.md
echo "📝 Creating README.md..."
cat > README.md << 'EOF'
# SIGA SaaS API - Sistema de Gestão Escolar

Sistema de gestão escolar multi-tenant desenvolvido com Spring Boot, seguindo arquitetura hexagonal e DDD.

## 🏗️ Arquitetura

- **Spring Modulith**: Arquitetura modular
- **Hexagonal Architecture**: Separação clara entre domínio e infraestrutura
- **Domain-Driven Design (DDD)**: Agregados, Value Objects, Domain Events
- **Multi-tenancy**: Isolamento por schema PostgreSQL

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

## 🧪 Testes

```bash
# Executar todos os testes
./mvnw test

# Testes de arquitetura
./mvnw test -Dtest=ModularityTests

# Testes com coverage
./mvnw test jacoco:report
```

## 📦 Módulos

- **common**: Componentes compartilhados (kernel)
- **tenant**: Gestão multi-tenant
- **security**: Autenticação e autorização
- **observability**: Métricas e monitoramento

## 👨‍💻 Autor

Xavier Nhagumbe
EOF

echo "✅ Setup completed successfully!"
echo ""
echo "📋 Next steps:"
echo "1. Review and copy .env.example to .env"
echo "2. Run: docker compose up -d"
echo "3. Run: ./mvnw spring-boot:run"
echo ""
echo "🎉 Happy coding!"