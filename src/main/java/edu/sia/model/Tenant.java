package edu.sia.model;

import edu.sia.users.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tenants")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tenantId;

    private String nombreTenant;
    private String dominio;
    private String emailContacto;
    private String telefonoContacto;
    private String pais;
    private String idioma;
    private String zonaHoraria;
    private String estado;
    private Integer maxUsuarios;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "tenantId", fetch = FetchType.LAZY)
    private List<User> users;

    @OneToMany(mappedBy = "tenant", fetch = FetchType.LAZY)
    private List<Rol> roles;
}
