package com.recortadorioBancario.demo.entidades;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@Table(name = "prestamos")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double monto;
    private double interes;
    private int numeroCuotas;
    private LocalDate fechaEmision;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonBackReference
    private Usuario usuario;


    @OneToMany(mappedBy = "prestamo", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Cuota> cuotas;
}
