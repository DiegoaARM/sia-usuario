Feature: Gestion Academica
  Como docente o estudiante
  Quiero que el sistema gestione la información académica
  Para garantizar disponibilidad, coherencia y acceso seguro a los datos

  Scenario: Consulta exitosa de asignaturas
    Given que el usuario ha iniciado sesion
    When realizo la solicitud para ver todas las asignaturas
    Then puedo visualizar la lista completa de asignaturas

  Scenario: Creación de un grupo académico
    Given que el usuario ha iniciado sesion
    When envío la solicitud para crear un nuevo grupo
    Then el grupo se crea correctamente y recibo la confirmación

  Scenario: Registro de una nota para un estudiante
    Given que el usuario ha iniciado sesion
    When envío la nota de un estudiante en un componente evaluativo
    Then la nota queda registrada en el sistema exitosamente

  Scenario: Actualización de una nota existente
    Given que el usuario ha iniciado sesion
    When actualizo la nota de un estudiante indicando una justificación
    Then el sistema guarda el nuevo valor y registra la justificación del cambio

  Scenario: Consulta de calificaciones consolidadas
    Given que el usuario ha iniciado sesion
    When consulto todas las calificaciones de un estudiante
    Then el sistema me muestra las notas consolidadas por período académico
