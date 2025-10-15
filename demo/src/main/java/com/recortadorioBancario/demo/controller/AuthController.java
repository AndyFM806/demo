package com.recortadorioBancario.demo.controller;


import com.recortadorioBancario.demo.entidades.Usuario;
import com.recortadorioBancario.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/register")
    public Usuario register(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario usuario) {
        return usuarioRepository.findByEmailAndPassword(usuario.getEmail(), usuario.getPassword())
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
    }
}
