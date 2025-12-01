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

-- Tabla de asignaturas
CREATE TABLE IF NOT EXISTS asignaturas (
    asignatura_id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    creditos INTEGER NOT NULL CHECK (creditos > 0),
    descripcion TEXT,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    tenant_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tenant_id) REFERENCES tenants(tenant_id)
);

-- Tabla de grupos
CREATE TABLE IF NOT EXISTS grupos (
    grupo_id BIGSERIAL PRIMARY KEY,
    asignatura_id BIGINT NOT NULL,
    docente_id BIGINT,
    periodo VARCHAR(20) NOT NULL,
    cupo INTEGER NOT NULL CHECK (cupo > 0),
    horario VARCHAR(255) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (asignatura_id) REFERENCES asignaturas(asignatura_id),
    FOREIGN KEY (docente_id) REFERENCES usuarios(usuario_id)
);

-- Tabla de componentes de evaluación
CREATE TABLE IF NOT EXISTS componentes_evaluacion (
    componente_id BIGSERIAL PRIMARY KEY,
    asignatura_id BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    porcentaje DECIMAL(5,2) NOT NULL CHECK (porcentaje > 0 AND porcentaje <= 100),
    descripcion TEXT,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (asignatura_id) REFERENCES asignaturas(asignatura_id),
    UNIQUE(asignatura_id, nombre)
);

-- Tabla de matrículas
CREATE TABLE IF NOT EXISTS matriculas (
    matricula_id BIGSERIAL PRIMARY KEY,
    estudiante_id BIGINT NOT NULL,
    grupo_id BIGINT NOT NULL,
    fecha_matricula TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (estudiante_id) REFERENCES usuarios(usuario_id),
    FOREIGN KEY (grupo_id) REFERENCES grupos(grupo_id),
    UNIQUE(estudiante_id, grupo_id)
);

-- Tabla de notas
CREATE TABLE IF NOT EXISTS notas (
    nota_id BIGSERIAL PRIMARY KEY,
    matricula_id BIGINT NOT NULL,
    componente_id BIGINT NOT NULL,
    valor DECIMAL(3,2) NOT NULL CHECK (valor >= 0.0 AND valor <= 5.0),
    justificacion TEXT,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP,
    modificado_por BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (matricula_id) REFERENCES matriculas(matricula_id),
    FOREIGN KEY (componente_id) REFERENCES componentes_evaluacion(componente_id),
    FOREIGN KEY (modificado_por) REFERENCES usuarios(usuario_id),
    UNIQUE(matricula_id, componente_id)
);

-- Tabla de historial de notas (opcional para auditoría)
CREATE TABLE IF NOT EXISTS historial_notas (
    historial_id BIGSERIAL PRIMARY KEY,
    nota_id BIGINT NOT NULL,
    valor_anterior DECIMAL(3,2) NOT NULL,
    valor_nuevo DECIMAL(3,2) NOT NULL,
    justificacion TEXT NOT NULL,
    modificado_por BIGINT NOT NULL,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (nota_id) REFERENCES notas(nota_id),
    FOREIGN KEY (modificado_por) REFERENCES usuarios(usuario_id)
);

-- Índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_asignaturas_codigo ON asignaturas(codigo);
CREATE INDEX IF NOT EXISTS idx_asignaturas_tenant_id ON asignaturas(tenant_id);
CREATE INDEX IF NOT EXISTS idx_grupos_asignatura_id ON grupos(asignatura_id);
CREATE INDEX IF NOT EXISTS idx_grupos_docente_id ON grupos(docente_id);
CREATE INDEX IF NOT EXISTS idx_grupos_periodo ON grupos(periodo);
CREATE INDEX IF NOT EXISTS idx_componentes_asignatura_id ON componentes_evaluacion(asignatura_id);
CREATE INDEX IF NOT EXISTS idx_matriculas_estudiante_id ON matriculas(estudiante_id);
CREATE INDEX IF NOT EXISTS idx_matriculas_grupo_id ON matriculas(grupo_id);
CREATE INDEX IF NOT EXISTS idx_notas_matricula_id ON notas(matricula_id);
CREATE INDEX IF NOT EXISTS idx_notas_componente_id ON notas(componente_id);
CREATE INDEX IF NOT EXISTS idx_historial_nota_id ON historial_notas(nota_id);

-- Triggers para actualizar updated_at
CREATE TRIGGER update_asignaturas_updated_at BEFORE UPDATE ON asignaturas
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_grupos_updated_at BEFORE UPDATE ON grupos
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_notas_updated_at BEFORE UPDATE ON notas
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Trigger para actualizar updated_at en tenants
CREATE TRIGGER update_tenants_updated_at
    BEFORE UPDATE ON tenants
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

