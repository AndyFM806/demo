package com.recortadorioBancario.demo.controller;

import com.recortadorioBancario.demo.entidades.Cuota;
import com.recortadorioBancario.demo.entidades.Prestamo;
import com.recortadorioBancario.demo.entidades.Usuario;
import com.recortadorioBancario.demo.repository.UsuarioRepository;
import com.recortadorioBancario.demo.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/email")
public class emailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // POST /api/email/test/usuario/1
   @PostMapping("/test/usuario/{id}")
public String enviarTestAUsuario(@PathVariable Long id) {

    Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

    // 🔍 Normaliza fechas (sin horas)
    LocalDate hoy = LocalDate.now();

    int totalCorreos = 0;

    for (Prestamo prestamo : usuario.getPrestamos()) {
        for (Cuota cuota : prestamo.getCuotas()) {
            if (cuota.getEstado() == Cuota.EstadoCuota.PENDIENTE) {
                LocalDate fechaVenc = cuota.getFechaVencimiento();

                long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaVenc);

                // ✅ Solo enviar si vence en 2 días o menos (y aún no venció)
                if (diasRestantes <= 2 && diasRestantes >= 0) {

                    String asunto = "💰 Cuota próxima a vencer el " + fechaVenc;

                    String cuerpo = """
                        Hola %s,

                        Te recordamos que tienes una cuota pendiente que vence pronto.

                        📘 Detalles:
                        - Préstamo ID: %d
                        - Cuota N°: %d
                        - Monto: S/. %.2f
                        - Fecha de vencimiento: %s

                        Por favor, realiza el pago antes de la fecha indicada para evitar cargos adicionales.

                        Gracias por confiar en Recordatorio Bancario.
                        """.formatted(
                                usuario.getNombre(),
                                prestamo.getId(),
                                cuota.getNumero(),
                                cuota.getMonto(),
                                fechaVenc
                        );

                    emailService.enviarCorreo(usuario.getEmail(), asunto, cuerpo);
                    totalCorreos++;
                }
            }
        }
    }

    if (totalCorreos == 0) {
        return "📭 No hay cuotas próximas a vencer para el usuario " + usuario.getEmail();
    } else {
        return "✅ Se enviaron " + totalCorreos + " recordatorios a " + usuario.getEmail();
    }
}
}