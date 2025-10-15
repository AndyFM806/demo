package com.recortadorioBancario.demo.controller;

import com.recortadorioBancario.demo.entidades.Usuario;
import com.recortadorioBancario.demo.repository.UsuarioRepository;
import com.recortadorioBancario.demo.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/email")
public class emailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // POST /api/email/test/usuario/1
    @PostMapping("/test/usuario/{id}")
    public String enviarTestAUsuario(
            @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {

        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        String para = u.getEmail();
        String asunto = (body != null && body.get("asunto") != null)
                ? body.get("asunto")
                : "Prueba de Recordatorio Bancario";
        String mensaje = (body != null && body.get("mensaje") != null)
                ? body.get("mensaje")
                : "Hola " + u.getNombre() + ", este es un correo de prueba desde el backend.";

        emailService.enviarCorreo(para, asunto, mensaje);
        return "Correo enviado (o intentado) a " + para;
    }
}
