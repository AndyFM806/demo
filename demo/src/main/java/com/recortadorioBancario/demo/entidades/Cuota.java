package com.recortadorioBancario.demo.entidades;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
@Table(name = "cuotas")
public class Cuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numero;
    private double monto;
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    private EstadoCuota estado;

    @ManyToOne
    @JoinColumn(name = "prestamo_id")
    @JsonBackReference
    private Prestamo prestamo;


    public enum EstadoCuota {
        PENDIENTE,
        PAGADA
    }
}
