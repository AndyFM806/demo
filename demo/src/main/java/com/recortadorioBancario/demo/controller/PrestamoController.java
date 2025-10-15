package com.recortadorioBancario.demo.controller;


import com.recortadorioBancario.demo.entidades.Prestamo;
import com.recortadorioBancario.demo.entidades.Usuario;
import com.recortadorioBancario.demo.repository.UsuarioRepository;
import com.recortadorioBancario.demo.services.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 🔹 Crear un nuevo préstamo asociado a un usuario
    @PostMapping("/crear")
    public Prestamo crearPrestamo(@RequestParam Long usuarioId, @RequestBody Prestamo prestamo) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        prestamo.setUsuario(usuario);
        Prestamo guardado = prestamoService.registrarPrestamo(prestamo);
        return guardado;
    }

    // 🔹 Listar préstamos de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public List<Prestamo> listarPorUsuario(@PathVariable Long usuarioId) {
        return prestamoService.listarPrestamosPorUsuario(usuarioId);
    }

    // 🔹 Listar todos los préstamos (opcional)
    @GetMapping("/listar")
    public List<Prestamo> listarTodos() {
        return prestamoService.listarPrestamosPorUsuario(null);
    }
}
