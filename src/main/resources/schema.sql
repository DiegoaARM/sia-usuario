-- Script de inicialización para R2DBC PostgreSQL
-- Ejecutar este script en tu base de datos PostgreSQL

-- Tabla de tenants
CREATE TABLE IF NOT EXISTS tenants (
    tenant_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    domain VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(50) NOT NULL,
    country VARCHAR(100) NOT NULL,
    language VARCHAR(50) NOT NULL,
    timezone VARCHAR(100) NOT NULL,
    state VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    usuario_id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE,
    given_name VARCHAR(255) NOT NULL,
    family_name VARCHAR(255) NOT NULL,
    id_type VARCHAR(10) NOT NULL,
    id_number VARCHAR(50) NOT NULL,
    role_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    birthdate DATE NOT NULL,
    genre VARCHAR(20) NOT NULL,
    state VARCHAR(20) NOT NULL,
    last_login TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tenant_id BIGINT,
    FOREIGN KEY (tenant_id) REFERENCES tenants(tenant_id)
);

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_usuarios_email ON usuarios(email);
CREATE INDEX IF NOT EXISTS idx_usuarios_tenant_id ON usuarios(tenant_id);
CREATE INDEX IF NOT EXISTS idx_tenants_domain ON tenants(domain);

-- Función para actualizar updated_at automáticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Trigger para actualizar updated_at en tenants
CREATE TRIGGER update_tenants_updated_at
    BEFORE UPDATE ON tenants
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

