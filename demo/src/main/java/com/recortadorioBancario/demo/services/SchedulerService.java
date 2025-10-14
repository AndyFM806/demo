package com.recortadorioBancario.demo.services;

import com.recortadorioBancario.demo.entidades.Cuota;
import com.recortadorioBancario.demo.repository.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private CuotaRepository cuotaRepository;

    @Autowired
    private EmailService emailService;

    // Se ejecuta cada 24 horas
    @Scheduled(cron = "0 0 9 * * *") // cada día a las 9 AM
    public void verificarCuotasPorVencer() {
        List<Cuota> cuotasPendientes = cuotaRepository.findByEstado(Cuota.EstadoCuota.PENDIENTE);

        for (Cuota c : cuotasPendientes) {
            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), c.getFechaVencimiento());

            if (diasRestantes <= 2 && diasRestantes >= 0) {
                String email = c.getPrestamo().getUsuario().getEmail();
                String asunto = "Recordatorio de pago - Cuota " + c.getNumero();
                String cuerpo = "Hola, este es un recordatorio: su cuota #" + c.getNumero() +
                        " vence el " + c.getFechaVencimiento() +
                        ". Monto: " + c.getMonto() + " Soles.";
                emailService.enviarCorreo(email, asunto, cuerpo);
            }
        }
    }
}
