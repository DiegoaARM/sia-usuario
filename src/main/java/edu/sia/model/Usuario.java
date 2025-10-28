package edu.sia.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuarioId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;

    private String email;
    private String nombreUsuario;
    private String apellido;
    private String tipoDocumento;
    private String numeroDocumento;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String genero;
    private String estado;
    private Boolean emailVerificado;
    private LocalDateTime ultimoLogin;
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<UsuarioRol> usuarioRoles;
}
