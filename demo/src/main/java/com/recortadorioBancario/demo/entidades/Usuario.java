package com.recortadorioBancario.demo.entidades;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String email;
    private String password;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Prestamo> prestamos;

}
