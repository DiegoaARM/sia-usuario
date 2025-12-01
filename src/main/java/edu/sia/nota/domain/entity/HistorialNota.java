package edu.sia.nota.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("historial_notas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialNota {

    @Id
    @Column("historial_id")
    private Long historialId;

    @Column("nota_id")
    private Long notaId;

    @Column("valor_anterior")
    private BigDecimal valorAnterior;

    @Column("valor_nuevo")
    private BigDecimal valorNuevo;

    @Column("justificacion")
    private String justificacion;

    @Column("modificado_por")
    private Long modificadoPor;

    @Column("fecha_modificacion")
    private LocalDateTime fechaModificacion;
}

