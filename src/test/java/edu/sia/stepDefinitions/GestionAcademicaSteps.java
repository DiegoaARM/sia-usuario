package edu.sia.stepDefinitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.Assert.*;

public class GestionAcademicaSteps {

    private Response response;
    private String token = "Bearer test-token"; // Simulación rápida

    @Given("que el usuario ha iniciado sesion")
    public void usuario_ha_iniciado_sesion() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    // 1. CONSULTA DE ASIGNATURAS
    @When("realizo la solicitud para ver todas las asignaturas")
    public void consultarAsignaturas() {
        response = RestAssured
                .given()
                .header("Authorization", token)
                .when()
                .get("/api/asignaturas");
    }

    @Then("puedo visualizar la lista completa de asignaturas")
    public void validarAsignaturas() {
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("nombre"));
    }

    // 2. CREAR GRUPO
    @When("envío la solicitud para crear un nuevo grupo")
    public void crearGrupo() {
        String body = """
        {
          "nombre": "Grupo A",
          "asignaturaId": 1
        }
        """;

        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .body(body)
                .when()
                .post("/api/grupos");
    }

    @Then("el grupo se crea correctamente y recibo la confirmación")
    public void validarGrupoCreado() {
        assertEquals(201, response.getStatusCode());
    }

    // 3. REGISTRAR NOTA
    @When("envío la nota de un estudiante en un componente evaluativo")
    public void registrarNota() {

        String body = """
        {
          "estudianteId": 1,
          "componente": "Parcial 1",
          "nota": 4.5
        }
        """;

        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .body(body)
                .when()
                .post("/api/notas");
    }

    @Then("la nota queda registrada en el sistema exitosamente")
    public void validarNotaCreada() {
        assertEquals(201, response.getStatusCode());
    }

    // 4. ACTUALIZAR NOTA
    @When("actualizo la nota de un estudiante indicando una justificación")
    public void actualizarNota() {

        String body = """
        {
          "nota": 4.8,
          "justificacion": "Corrección por error de cálculo"
        }
        """;

        response = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .body(body)
                .when()
                .put("/api/notas/1");
    }

    @Then("el sistema guarda el nuevo valor y registra la justificación del cambio")
    public void validarActualizacion() {
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("Corrección"));
    }

    // 5. CONSULTA DE NOTAS
    @When("consulto todas las calificaciones de un estudiante")
    public void consultarNotas() {
        response = RestAssured
                .given()
                .header("Authorization", token)
                .when()
                .get("/api/estudiantes/1/notas");
    }

    @Then("el sistema me muestra las notas consolidadas por período académico")
    public void validarNotas() {
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("periodo"));
    }
}

