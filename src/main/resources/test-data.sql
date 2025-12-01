-- Script de datos de prueba para el sistema académico
-- Ejecutar después de schema.sql

-- 1. Insertar tenant de prueba
INSERT INTO tenants (name, domain, email, phone_number, country, language, timezone, state)
VALUES ('Universidad Demo', 'universidad.edu', 'info@universidad.edu', '+57300000000', 'Colombia', 'es', 'America/Bogota', 'ACTIVE')
ON CONFLICT DO NOTHING;

-- 2. Insertar asignaturas
INSERT INTO asignaturas (codigo, nombre, creditos, descripcion, estado, tenant_id)
VALUES
    ('PR-101', 'Programación Reactiva', 3, 'Fundamentos de programación reactiva con Spring WebFlux', 'ACTIVE', 1),
    ('BDA-201', 'Bases de Datos Avanzadas', 4, 'Bases de datos NoSQL y optimización de consultas', 'ACTIVE', 1),
    ('DS-301', 'Arquitectura de Software', 3, 'Patrones y principios de diseño de software', 'ACTIVE', 1)
ON CONFLICT (codigo) DO NOTHING;

-- 3. Insertar docentes
INSERT INTO usuarios (email, given_name, family_name, id_type, id_number, role_name, phone_number, birthdate, genre, state, tenant_id)
VALUES
    ('juan.perez@universidad.edu', 'Juan', 'Pérez', 'CC', '1234567890', 'DOCENTE', '+573001234567', '1980-05-15', 'M', 'ACTIVE', 1),
    ('ana.martinez@universidad.edu', 'Ana', 'Martínez', 'CC', '2345678901', 'DOCENTE', '+573002345678', '1985-08-20', 'F', 'ACTIVE', 1),
    ('carlos.gomez@universidad.edu', 'Carlos', 'Gómez', 'CC', '3456789012', 'DOCENTE', '+573003456789', '1978-03-10', 'M', 'ACTIVE', 1)
ON CONFLICT (email) DO NOTHING;

-- 4. Insertar estudiantes
INSERT INTO usuarios (email, given_name, family_name, id_type, id_number, role_name, phone_number, birthdate, genre, state, tenant_id)
VALUES
    ('maria.garcia@universidad.edu', 'María', 'García', 'CC', '9876543210', 'ESTUDIANTE', '+573009876543', '2000-08-20', 'F', 'ACTIVE', 1),
    ('pedro.lopez@universidad.edu', 'Pedro', 'López', 'CC', '8765432109', 'ESTUDIANTE', '+573008765432', '2001-02-15', 'M', 'ACTIVE', 1),
    ('laura.rodriguez@universidad.edu', 'Laura', 'Rodríguez', 'CC', '7654321098', 'ESTUDIANTE', '+573007654321', '2000-11-30', 'F', 'ACTIVE', 1),
    ('andres.sanchez@universidad.edu', 'Andrés', 'Sánchez', 'CC', '6543210987', 'ESTUDIANTE', '+573006543210', '2001-06-25', 'M', 'ACTIVE', 1),
    ('sofia.ramirez@universidad.edu', 'Sofía', 'Ramírez', 'CC', '5432109876', 'ESTUDIANTE', '+573005432109', '2000-09-12', 'F', 'ACTIVE', 1)
ON CONFLICT (email) DO NOTHING;

-- 5. Crear grupos (obtener IDs de asignaturas y docentes)
INSERT INTO grupos (asignatura_id, docente_id, periodo, cupo, horario, estado)
SELECT
    a.asignatura_id,
    d.usuario_id,
    '2025-1',
    30,
    'Lunes y Miércoles 8:00-10:00',
    'ACTIVE'
FROM asignaturas a
CROSS JOIN usuarios d
WHERE a.codigo = 'PR-101'
  AND d.email = 'juan.perez@universidad.edu'
ON CONFLICT DO NOTHING;

INSERT INTO grupos (asignatura_id, docente_id, periodo, cupo, horario, estado)
SELECT
    a.asignatura_id,
    d.usuario_id,
    '2025-1',
    25,
    'Martes y Jueves 14:00-16:00',
    'ACTIVE'
FROM asignaturas a
CROSS JOIN usuarios d
WHERE a.codigo = 'BDA-201'
  AND d.email = 'ana.martinez@universidad.edu'
ON CONFLICT DO NOTHING;

INSERT INTO grupos (asignatura_id, docente_id, periodo, cupo, horario, estado)
SELECT
    a.asignatura_id,
    d.usuario_id,
    '2025-1',
    35,
    'Viernes 10:00-14:00',
    'ACTIVE'
FROM asignaturas a
CROSS JOIN usuarios d
WHERE a.codigo = 'DS-301'
  AND d.email = 'carlos.gomez@universidad.edu'
ON CONFLICT DO NOTHING;

-- 6. Crear componentes evaluativos para Programación Reactiva
INSERT INTO componentes_evaluacion (asignatura_id, nombre, porcentaje, descripcion, estado)
SELECT
    a.asignatura_id,
    'Parcial 1',
    30.00,
    'Primer examen parcial - Fundamentos de Reactor',
    'ACTIVE'
FROM asignaturas a
WHERE a.codigo = 'PR-101'
ON CONFLICT (asignatura_id, nombre) DO NOTHING;

INSERT INTO componentes_evaluacion (asignatura_id, nombre, porcentaje, descripcion, estado)
SELECT
    a.asignatura_id,
    'Parcial 2',
    30.00,
    'Segundo examen parcial - Spring WebFlux',
    'ACTIVE'
FROM asignaturas a
WHERE a.codigo = 'PR-101'
ON CONFLICT (asignatura_id, nombre) DO NOTHING;

INSERT INTO componentes_evaluacion (asignatura_id, nombre, porcentaje, descripcion, estado)
SELECT
    a.asignatura_id,
    'Proyecto Final',
    40.00,
    'Proyecto integrador - API Reactiva completa',
    'ACTIVE'
FROM asignaturas a
WHERE a.codigo = 'PR-101'
ON CONFLICT (asignatura_id, nombre) DO NOTHING;

-- 7. Crear componentes evaluativos para Bases de Datos Avanzadas
INSERT INTO componentes_evaluacion (asignatura_id, nombre, porcentaje, descripcion, estado)
SELECT
    a.asignatura_id,
    'Quiz 1',
    15.00,
    'Evaluación NoSQL',
    'ACTIVE'
FROM asignaturas a
WHERE a.codigo = 'BDA-201'
ON CONFLICT (asignatura_id, nombre) DO NOTHING;

INSERT INTO componentes_evaluacion (asignatura_id, nombre, porcentaje, descripcion, estado)
SELECT
    a.asignatura_id,
    'Quiz 2',
    15.00,
    'Evaluación Optimización',
    'ACTIVE'
FROM asignaturas a
WHERE a.codigo = 'BDA-201'
ON CONFLICT (asignatura_id, nombre) DO NOTHING;

INSERT INTO componentes_evaluacion (asignatura_id, nombre, porcentaje, descripcion, estado)
SELECT
    a.asignatura_id,
    'Parcial',
    35.00,
    'Examen teórico-práctico',
    'ACTIVE'
FROM asignaturas a
WHERE a.codigo = 'BDA-201'
ON CONFLICT (asignatura_id, nombre) DO NOTHING;

INSERT INTO componentes_evaluacion (asignatura_id, nombre, porcentaje, descripcion, estado)
SELECT
    a.asignatura_id,
    'Proyecto',
    35.00,
    'Diseño e implementación de base de datos',
    'ACTIVE'
FROM asignaturas a
WHERE a.codigo = 'BDA-201'
ON CONFLICT (asignatura_id, nombre) DO NOTHING;

-- 8. Matricular estudiantes en grupos
-- Matricular 3 estudiantes en Programación Reactiva
INSERT INTO matriculas (estudiante_id, grupo_id, estado)
SELECT
    u.usuario_id,
    g.grupo_id,
    'ACTIVE'
FROM usuarios u
CROSS JOIN grupos g
INNER JOIN asignaturas a ON g.asignatura_id = a.asignatura_id
WHERE u.email IN ('maria.garcia@universidad.edu', 'pedro.lopez@universidad.edu', 'laura.rodriguez@universidad.edu')
  AND a.codigo = 'PR-101'
ON CONFLICT (estudiante_id, grupo_id) DO NOTHING;

-- Matricular 2 estudiantes en Bases de Datos Avanzadas
INSERT INTO matriculas (estudiante_id, grupo_id, estado)
SELECT
    u.usuario_id,
    g.grupo_id,
    'ACTIVE'
FROM usuarios u
CROSS JOIN grupos g
INNER JOIN asignaturas a ON g.asignatura_id = a.asignatura_id
WHERE u.email IN ('maria.garcia@universidad.edu', 'andres.sanchez@universidad.edu')
  AND a.codigo = 'BDA-201'
ON CONFLICT (estudiante_id, grupo_id) DO NOTHING;

-- Matricular 1 estudiante en Arquitectura de Software
INSERT INTO matriculas (estudiante_id, grupo_id, estado)
SELECT
    u.usuario_id,
    g.grupo_id,
    'ACTIVE'
FROM usuarios u
CROSS JOIN grupos g
INNER JOIN asignaturas a ON g.asignatura_id = a.asignatura_id
WHERE u.email = 'sofia.ramirez@universidad.edu'
  AND a.codigo = 'DS-301'
ON CONFLICT (estudiante_id, grupo_id) DO NOTHING;

-- 9. Registrar notas para María García en Programación Reactiva
INSERT INTO notas (matricula_id, componente_id, valor, fecha_registro)
SELECT
    m.matricula_id,
    ce.componente_id,
    4.5,
    CURRENT_TIMESTAMP
FROM matriculas m
INNER JOIN grupos g ON m.grupo_id = g.grupo_id
INNER JOIN asignaturas a ON g.asignatura_id = a.asignatura_id
INNER JOIN componentes_evaluacion ce ON ce.asignatura_id = a.asignatura_id
INNER JOIN usuarios u ON m.estudiante_id = u.usuario_id
WHERE u.email = 'maria.garcia@universidad.edu'
  AND a.codigo = 'PR-101'
  AND ce.nombre = 'Parcial 1'
ON CONFLICT (matricula_id, componente_id) DO NOTHING;

INSERT INTO notas (matricula_id, componente_id, valor, fecha_registro)
SELECT
    m.matricula_id,
    ce.componente_id,
    4.8,
    CURRENT_TIMESTAMP
FROM matriculas m
INNER JOIN grupos g ON m.grupo_id = g.grupo_id
INNER JOIN asignaturas a ON g.asignatura_id = a.asignatura_id
INNER JOIN componentes_evaluacion ce ON ce.asignatura_id = a.asignatura_id
INNER JOIN usuarios u ON m.estudiante_id = u.usuario_id
WHERE u.email = 'maria.garcia@universidad.edu'
  AND a.codigo = 'PR-101'
  AND ce.nombre = 'Parcial 2'
ON CONFLICT (matricula_id, componente_id) DO NOTHING;

INSERT INTO notas (matricula_id, componente_id, valor, fecha_registro)
SELECT
    m.matricula_id,
    ce.componente_id,
    4.3,
    CURRENT_TIMESTAMP
FROM matriculas m
INNER JOIN grupos g ON m.grupo_id = g.grupo_id
INNER JOIN asignaturas a ON g.asignatura_id = a.asignatura_id
INNER JOIN componentes_evaluacion ce ON ce.asignatura_id = a.asignatura_id
INNER JOIN usuarios u ON m.estudiante_id = u.usuario_id
WHERE u.email = 'maria.garcia@universidad.edu'
  AND a.codigo = 'PR-101'
  AND ce.nombre = 'Proyecto Final'
ON CONFLICT (matricula_id, componente_id) DO NOTHING;

-- 10. Registrar notas para Pedro López en Programación Reactiva
INSERT INTO notas (matricula_id, componente_id, valor, fecha_registro)
SELECT
    m.matricula_id,
    ce.componente_id,
    3.8,
    CURRENT_TIMESTAMP
FROM matriculas m
INNER JOIN grupos g ON m.grupo_id = g.grupo_id
INNER JOIN asignaturas a ON g.asignatura_id = a.asignatura_id
INNER JOIN componentes_evaluacion ce ON ce.asignatura_id = a.asignatura_id
INNER JOIN usuarios u ON m.estudiante_id = u.usuario_id
WHERE u.email = 'pedro.lopez@universidad.edu'
  AND a.codigo = 'PR-101'
  AND ce.nombre = 'Parcial 1'
ON CONFLICT (matricula_id, componente_id) DO NOTHING;

INSERT INTO notas (matricula_id, componente_id, valor, fecha_registro)
SELECT
    m.matricula_id,
    ce.componente_id,
    4.2,
    CURRENT_TIMESTAMP
FROM matriculas m
INNER JOIN grupos g ON m.grupo_id = g.grupo_id
INNER JOIN asignaturas a ON g.asignatura_id = a.asignatura_id
INNER JOIN componentes_evaluacion ce ON ce.asignatura_id = a.asignatura_id
INNER JOIN usuarios u ON m.estudiante_id = u.usuario_id
WHERE u.email = 'pedro.lopez@universidad.edu'
  AND a.codigo = 'PR-101'
  AND ce.nombre = 'Parcial 2'
ON CONFLICT (matricula_id, componente_id) DO NOTHING;

-- Verificar datos insertados
SELECT 'Tenants' as tabla, COUNT(*) as registros FROM tenants
UNION ALL
SELECT 'Asignaturas', COUNT(*) FROM asignaturas
UNION ALL
SELECT 'Usuarios', COUNT(*) FROM usuarios
UNION ALL
SELECT 'Grupos', COUNT(*) FROM grupos
UNION ALL
SELECT 'Componentes Evaluación', COUNT(*) FROM componentes_evaluacion
UNION ALL
SELECT 'Matrículas', COUNT(*) FROM matriculas
UNION ALL
SELECT 'Notas', COUNT(*) FROM notas;

