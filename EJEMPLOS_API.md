# Ejemplos de API - Sistema de Gestión Académica

Este documento contiene ejemplos de requests y responses para todas las funcionalidades implementadas.

---

## HU-004: Crear grupos de una asignatura

**Endpoint:** `POST /api/grupos`

### Request
```json
{
  "asignaturaId": 1,
  "periodo": "2025-1",
  "cupo": 30,
  "horario": "Lunes y Miércoles 8:00-10:00"
}
```

### Response (201 Created)
```json
{
  "grupoId": 1,
  "asignaturaId": 1,
  "asignaturaNombre": "Programación Reactiva",
  "asignaturaCodigo": "PR-101",
  "docenteId": null,
  "docenteNombre": null,
  "periodo": "2025-1",
  "cupo": 30,
  "matriculados": 0,
  "horario": "Lunes y Miércoles 8:00-10:00",
  "estado": "ACTIVE",
  "createdAt": "2025-12-01T10:30:00"
}
```

### Errores posibles
```json
// 404 Not Found - Asignatura no existe
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Asignatura no encontrada con ID: 1"
}

// 400 Bad Request - Validación fallida
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Error de validación en los campos",
  "validationErrors": {
    "cupo": "El cupo debe ser mayor a 0",
    "periodo": "El periodo debe tener el formato YYYY-1 o YYYY-2"
  }
}
```

---

## HU-005: Asignar docentes a grupos

**Endpoint:** `PUT /api/grupos/{grupoId}/docente`

### Request
```json
{
  "docenteId": 5
}
```

### Response (200 OK)
```json
{
  "grupoId": 1,
  "asignaturaId": 1,
  "asignaturaNombre": "Programación Reactiva",
  "asignaturaCodigo": "PR-101",
  "docenteId": 5,
  "docenteNombre": "Juan Pérez",
  "periodo": "2025-1",
  "cupo": 30,
  "matriculados": 0,
  "horario": "Lunes y Miércoles 8:00-10:00",
  "estado": "ACTIVE",
  "createdAt": "2025-12-01T10:30:00"
}
```

### Errores posibles
```json
// 404 Not Found - Grupo no existe
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Grupo no encontrado con ID: 1"
}

// 404 Not Found - Docente no existe
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Docente no encontrado con ID: 5"
}

// 400 Bad Request - Usuario no es docente
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El usuario no tiene rol de docente"
}
```

---

## HU-007: Configurar esquema de evaluación

**Endpoint:** `POST /api/asignaturas/{asignaturaId}/evaluacion`

### Request - Primer componente
```json
{
  "nombre": "Parcial 1",
  "porcentaje": 30.00,
  "descripcion": "Primer examen parcial"
}
```

### Response (201 Created)
```json
{
  "componenteId": 1,
  "asignaturaId": 1,
  "nombre": "Parcial 1",
  "porcentaje": 30.00,
  "descripcion": "Primer examen parcial",
  "estado": "ACTIVE",
  "createdAt": "2025-12-01T10:30:00"
}
```

### Request - Segundo componente
```json
{
  "nombre": "Parcial 2",
  "porcentaje": 30.00,
  "descripcion": "Segundo examen parcial"
}
```

### Request - Tercer componente
```json
{
  "nombre": "Proyecto Final",
  "porcentaje": 40.00,
  "descripcion": "Proyecto integrador"
}
```

### Errores posibles
```json
// 400 Bad Request - Suma excede 100%
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "La suma de porcentajes no puede exceder 100%. Actual: 80%, Nuevo componente: 30%"
}

// 400 Bad Request - Validación
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Error de validación en los campos",
  "validationErrors": {
    "porcentaje": "El porcentaje no puede exceder 100"
  }
}
```

### Listar componentes
**Endpoint:** `GET /api/asignaturas/{asignaturaId}/evaluacion`

### Response (200 OK)
```json
[
  {
    "componenteId": 1,
    "asignaturaId": 1,
    "nombre": "Parcial 1",
    "porcentaje": 30.00,
    "descripcion": "Primer examen parcial",
    "estado": "ACTIVE",
    "createdAt": "2025-12-01T10:30:00"
  },
  {
    "componenteId": 2,
    "asignaturaId": 1,
    "nombre": "Parcial 2",
    "porcentaje": 30.00,
    "descripcion": "Segundo examen parcial",
    "estado": "ACTIVE",
    "createdAt": "2025-12-01T10:31:00"
  },
  {
    "componenteId": 3,
    "asignaturaId": 1,
    "nombre": "Proyecto Final",
    "porcentaje": 40.00,
    "descripcion": "Proyecto integrador",
    "estado": "ACTIVE",
    "createdAt": "2025-12-01T10:32:00"
  }
]
```

---

## HU-009: Ver grupos asignados y estudiantes matriculados

**Endpoint:** `GET /api/docentes/{docenteId}/grupos`

### Response (200 OK)
```json
[
  {
    "grupoId": 1,
    "asignaturaNombre": "Programación Reactiva",
    "asignaturaCodigo": "PR-101",
    "periodo": "2025-1",
    "cupo": 30,
    "matriculados": 2,
    "horario": "Lunes y Miércoles 8:00-10:00",
    "estudiantes": [
      {
        "estudianteId": 10,
        "nombreCompleto": "María García López",
        "email": "maria.garcia@universidad.edu",
        "idNumber": "1234567890",
        "fechaMatricula": "2025-11-15T09:00:00",
        "estado": "ACTIVE"
      },
      {
        "estudianteId": 11,
        "nombreCompleto": "Carlos Rodríguez Silva",
        "email": "carlos.rodriguez@universidad.edu",
        "idNumber": "0987654321",
        "fechaMatricula": "2025-11-16T10:30:00",
        "estado": "ACTIVE"
      }
    ]
  },
  {
    "grupoId": 2,
    "asignaturaNombre": "Bases de Datos Avanzadas",
    "asignaturaCodigo": "BDA-201",
    "periodo": "2025-1",
    "cupo": 25,
    "matriculados": 1,
    "horario": "Martes y Jueves 14:00-16:00",
    "estudiantes": [
      {
        "estudianteId": 10,
        "nombreCompleto": "María García López",
        "email": "maria.garcia@universidad.edu",
        "idNumber": "1234567890",
        "fechaMatricula": "2025-11-17T11:00:00",
        "estado": "ACTIVE"
      }
    ]
  }
]
```

### Errores posibles
```json
// 404 Not Found - Docente no existe
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Docente no encontrado con ID: 5"
}

// 400 Bad Request - Usuario no es docente
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El usuario no tiene rol de docente"
}
```

---

## HU-010: Ingresar notas por estudiante y componente

**Endpoint:** `POST /api/grupos/{grupoId}/notas`

### Request
```json
{
  "estudianteId": 10,
  "componenteId": 1,
  "valor": 4.5
}
```

### Response (201 Created)
```json
{
  "notaId": 1,
  "matriculaId": 1,
  "estudianteId": 10,
  "estudianteNombre": "María García López",
  "componenteId": 1,
  "componenteNombre": "Parcial 1",
  "componentePorcentaje": 30.00,
  "valor": 4.5,
  "justificacion": null,
  "fechaRegistro": "2025-12-01T10:30:00",
  "fechaModificacion": null,
  "modificadoPor": null,
  "modificadoPorNombre": null
}
```

### Errores posibles
```json
// 400 Bad Request - Estudiante no matriculado
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El estudiante no está matriculado en este grupo"
}

// 400 Bad Request - Componente no pertenece a la asignatura
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El componente no pertenece a la asignatura del grupo"
}

// 400 Bad Request - Nota ya existe
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Ya existe una nota para este componente"
}

// 400 Bad Request - Validación
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Error de validación en los campos",
  "validationErrors": {
    "valor": "La nota no puede exceder 5.0"
  }
}
```

---

## HU-013: Modificar una nota con justificación

**Endpoint:** `PUT /api/notas/{notaId}`

### Request
```json
{
  "valor": 4.8,
  "justificacion": "Corrección por error en la digitación inicial. El estudiante presentó reclamación con evidencia de su trabajo."
}
```

### Response (200 OK)
```json
{
  "notaId": 1,
  "matriculaId": 1,
  "estudianteId": 10,
  "estudianteNombre": "María García López",
  "componenteId": 1,
  "componenteNombre": "Parcial 1",
  "componentePorcentaje": 30.00,
  "valor": 4.8,
  "justificacion": "Corrección por error en la digitación inicial. El estudiante presentó reclamación con evidencia de su trabajo.",
  "fechaRegistro": "2025-12-01T10:30:00",
  "fechaModificacion": "2025-12-01T15:45:00",
  "modificadoPor": 5,
  "modificadoPorNombre": "Juan Pérez"
}
```

### Errores posibles
```json
// 404 Not Found - Nota no existe
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Nota no encontrada con ID: 1"
}

// 400 Bad Request - Validación
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Error de validación en los campos",
  "validationErrors": {
    "justificacion": "La justificación debe tener entre 10 y 500 caracteres"
  }
}
```

---

## HU-021: Consultar calificaciones por asignatura y periodo

**Endpoint:** `GET /api/estudiantes/{estudianteId}/calificaciones`

### Response (200 OK)
```json
[
  {
    "asignaturaId": 1,
    "asignaturaNombre": "Programación Reactiva",
    "asignaturaCodigo": "PR-101",
    "grupoId": 1,
    "periodo": "2025-1",
    "notaFinal": 4.52,
    "estado": "ACTIVE"
  },
  {
    "asignaturaId": 2,
    "asignaturaNombre": "Bases de Datos Avanzadas",
    "asignaturaCodigo": "BDA-201",
    "grupoId": 2,
    "periodo": "2025-1",
    "notaFinal": 4.20,
    "estado": "ACTIVE"
  }
]
```

**Nota:** La nota final es calculada automáticamente sumando las notas ponderadas de cada componente.

**Cálculo ejemplo:**
- Parcial 1 (30%): 4.5 → 1.35
- Parcial 2 (30%): 4.8 → 1.44
- Proyecto Final (40%): 4.3 → 1.72
- **Nota Final: 4.51**

---

## HU-023: Detalle de notas por componente

**Endpoint:** `GET /api/estudiantes/{estudianteId}/calificaciones/{asignaturaId}`

### Response (200 OK)
```json
{
  "asignaturaId": 1,
  "asignaturaNombre": "Programación Reactiva",
  "asignaturaCodigo": "PR-101",
  "periodo": "2025-1",
  "componentes": [
    {
      "componenteId": 1,
      "componenteNombre": "Parcial 1",
      "porcentaje": 30.00,
      "valor": 4.5,
      "valorPonderado": 1.35
    },
    {
      "componenteId": 2,
      "componenteNombre": "Parcial 2",
      "porcentaje": 30.00,
      "valor": 4.8,
      "valorPonderado": 1.44
    },
    {
      "componenteId": 3,
      "componenteNombre": "Proyecto Final",
      "porcentaje": 40.00,
      "valor": 4.3,
      "valorPonderado": 1.72
    }
  ],
  "notaFinal": 4.51
}
```

### Errores posibles
```json
// 404 Not Found - Asignatura no existe
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Asignatura no encontrada con ID: 1"
}

// 404 Not Found - Estudiante no existe
{
  "timestamp": "2025-12-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Estudiante no encontrado con ID: 10"
}
```

---

## Códigos de estado HTTP utilizados

- **200 OK**: Operación exitosa (GET, PUT)
- **201 Created**: Recurso creado exitosamente (POST)
- **400 Bad Request**: Error de validación o lógica de negocio
- **404 Not Found**: Recurso no encontrado
- **409 Conflict**: Conflicto de recursos (duplicados, etc.)
- **500 Internal Server Error**: Error inesperado del servidor

---

## Validaciones implementadas

### Grupo (CreateGrupoDto)
- ✅ `asignaturaId`: Obligatorio, debe existir
- ✅ `periodo`: Obligatorio, formato YYYY-1 o YYYY-2
- ✅ `cupo`: Obligatorio, entre 1 y 100
- ✅ `horario`: Obligatorio, máximo 255 caracteres

### Docente (AsignarDocenteDto)
- ✅ `docenteId`: Obligatorio, debe existir y tener rol DOCENTE

### Componente Evaluación (CreateComponenteEvaluacionDto)
- ✅ `nombre`: Obligatorio, máximo 100 caracteres
- ✅ `porcentaje`: Obligatorio, entre 0.01 y 100
- ✅ Suma de porcentajes no puede exceder 100%

### Nota (CreateNotaDto)
- ✅ `estudianteId`: Obligatorio, debe estar matriculado
- ✅ `componenteId`: Obligatorio, debe pertenecer a la asignatura
- ✅ `valor`: Obligatorio, entre 0.0 y 5.0, máximo 2 decimales

### Nota Update (UpdateNotaDto)
- ✅ `valor`: Obligatorio, entre 0.0 y 5.0
- ✅ `justificacion`: Obligatoria, entre 10 y 500 caracteres

---

## Notas técnicas

1. **Transacciones**: Todas las operaciones de escritura usan `@Transactional` para garantizar consistencia.

2. **Reactividad**: Todos los endpoints retornan `Mono<T>` o `Flux<T>` para operaciones reactivas.

3. **Logging**: Se registran todas las operaciones importantes con nivel DEBUG/INFO.

4. **Seguridad**: El sistema usa JWT de AWS Cognito (configurar en variables de entorno).

5. **Validación automática**: Spring Validation con anotaciones Jakarta (`@Valid`, `@NotNull`, etc.).

6. **Historial de notas**: Se guarda automáticamente en tabla `historial_notas` al modificar una nota.

7. **Cálculo de nota final**: Se calcula en tiempo real sumando las notas ponderadas de cada componente.

