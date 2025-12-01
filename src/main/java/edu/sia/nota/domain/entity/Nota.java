package edu.sia.nota.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("notas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Nota {

    @Id
    @Column("nota_id")
    private Long notaId;

    @Column("matricula_id")
    private Long matriculaId;

    @Column("componente_id")
    private Long componenteId;

    @Column("valor")
    private BigDecimal valor;

    @Column("justificacion")
    private String justificacion;

    @Column("fecha_registro")
    private LocalDateTime fechaRegistro;

    @Column("fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column("modificado_por")
    private Long modificadoPor;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}

