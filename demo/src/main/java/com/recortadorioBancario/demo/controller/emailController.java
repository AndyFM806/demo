package com.recortadorioBancario.demo.controller;

import com.recortadorioBancario.demo.entidades.Cuota;
import com.recortadorioBancario.demo.entidades.Prestamo;
import com.recortadorioBancario.demo.entidades.Usuario;
import com.recortadorioBancario.demo.repository.UsuarioRepository;
import com.recortadorioBancario.demo.services.EmailService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestController
@RequestMapping("/email")
@CrossOrigin(
        origins = {
                "http://localhost:5501",
                "http://127.0.0.1:5501",
                "http://localhost:5500",
                "https://frontrecordatorio.onrender.com"
        },
        allowCredentials = "true"
)
public class emailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Envia un correo POR CADA cuota PENDIENTE que vence en <= 2 días (y no está vencida) para el usuario {id}.
     * Responde con un JSON que resume el envío.
     *
     * Ejemplo prueba (Thunder/Front):
     * POST https://<tu-backend>/api/email/test/usuario/1
     * Body: {}
     */
    @PostMapping("/test/usuario/{id}")
    @Transactional
    public Map<String, Object> enviarTestAUsuario(@PathVariable Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));

        // Usa zona horaria de Lima (tu proyecto)
        LocalDate hoy = LocalDate.now(ZoneId.of("America/Lima"));

        int totalCorreos = 0;
        List<Map<String, Object>> detalles = new ArrayList<>();

        // Itera préstamos y cuotas
        for (Prestamo prestamo : usuario.getPrestamos()) {
            for (Cuota cuota : prestamo.getCuotas()) {
                if (cuota.getEstado() == Cuota.EstadoCuota.PENDIENTE) {
                    LocalDate fechaVenc = cuota.getFechaVencimiento();
                    long diasRestantes = ChronoUnit.DAYS.between(hoy, fechaVenc);

                    // Enviar si vence en 2 días o menos y aún no venció
                    if (diasRestantes <= 2 && diasRestantes >= 0) {

                        // Asunto solicitado:
                        // CUOTA PROXIMA A VENCER EN DIA (YYYY-MM-DD)
                        String asunto = "CUOTA PROXIMA A VENCER EN DIA (" + fechaVenc + ")";

                        String cuerpo = """
                                Hola %s,

                                Te recordamos que tienes una cuota pendiente próxima a vencer.

                                📘 Detalles:
                                - Préstamo ID: %d
                                - Cuota N°: %d
                                - Monto: S/. %.2f
                                - Fecha de vencimiento: %s
                                - Días restantes: %d

                                Por favor, realiza el pago antes de la fecha indicada para evitar cargos adicionales.

                                Gracias por confiar en RecordatorioBancario.
                                """.formatted(
                                usuario.getNombre(),
                                prestamo.getId(),
                                cuota.getNumero(),
                                cuota.getMonto(),
                                fechaVenc,
                                diasRestantes
                        );

                        // Enviar (tu EmailService ya setea el From con "RecordatorioBancario")
                        emailService.enviarCorreo(usuario.getEmail(), asunto, cuerpo);
                        totalCorreos++;

                        // Agregar al resumen
                        Map<String, Object> fila = new LinkedHashMap<>();
                        fila.put("prestamoId", prestamo.getId());
                        fila.put("cuotaId", cuota.getId());
                        fila.put("cuotaNumero", cuota.getNumero());
                        fila.put("monto", cuota.getMonto());
                        fila.put("fechaVencimiento", fechaVenc.toString());
                        fila.put("diasRestantes", diasRestantes);
                        detalles.add(fila);
                    }
                }
            }
        }

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("usuario", Map.of(
                "id", usuario.getId(),
                "nombre", usuario.getNombre(),
                "email", usuario.getEmail()
        ));
        respuesta.put("totalCorreosEnviados", totalCorreos);
        respuesta.put("detalles", detalles);

        if (totalCorreos == 0) {
            respuesta.put("mensaje", "No hay cuotas próximas a vencer (<=2 días) para este usuario.");
        } else {
            respuesta.put("mensaje", "Se enviaron " + totalCorreos + " recordatorios.");
        }

        return respuesta;
    }
}
