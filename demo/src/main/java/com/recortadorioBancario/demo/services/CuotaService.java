package com.recortadorioBancario.demo.services;

import com.recortadorioBancario.demo.entidades.Cuota;
import com.recortadorioBancario.demo.repository.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CuotaService {

    @Autowired
    private CuotaRepository cuotaRepository;

    @Autowired
    private EmailService emailService;

    // 🔹 Listar cuotas pendientes (para dashboard)
    public List<Cuota> listarPendientes() {
        return cuotaRepository.findByEstado(Cuota.EstadoCuota.PENDIENTE);
    }

    // 🔹 Cambiar estado a "PAGADA"
    public Cuota marcarComoPagada(Long id) {
        Cuota c = cuotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuota no encontrada"));
        c.setEstado(Cuota.EstadoCuota.PAGADA);
        return cuotaRepository.save(c);
    }

    // 🔹 Verificar cuotas por vencer y enviar recordatorios (llamado desde Scheduler)
    public void enviarRecordatoriosPorVencer() {
        List<Cuota> pendientes = listarPendientes();

        for (Cuota c : pendientes) {
            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), c.getFechaVencimiento());
            if (diasRestantes <= 2 && diasRestantes >= 0) {
                String email = c.getPrestamo().getUsuario().getEmail();
                String asunto = "Recordatorio de pago - Cuota #" + c.getNumero();
                String cuerpo = "Hola, " + c.getPrestamo().getUsuario().getNombre() + ",\n\n" +
                        "Le recordamos que su cuota #" + c.getNumero() +
                        " del préstamo por $" + c.getPrestamo().getMonto() +
                        " vence el " + c.getFechaVencimiento() + ".\n\n" +
                        "Monto a pagar: $" + c.getMonto() + "\n\n" +
                        "Por favor realice su pago a tiempo para evitar recargos.\n\nAtentamente,\nRecordatorio Bancario";

                emailService.enviarCorreo(email, asunto, cuerpo);
            }
        }
    }
}
