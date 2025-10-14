package com.recortadorioBancario.demo.services;

import com.recortadorioBancario.demo.entidades.*;
import com.recortadorioBancario.demo.repository.*;
import com.recortadorioBancario.demo.util.FechaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;


@Service
public class PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private CuotaRepository cuotaRepository;

    public Prestamo registrarPrestamo(Prestamo prestamo) {
        Prestamo nuevo = prestamoRepository.save(prestamo);

        double montoPorCuota = (prestamo.getMonto() * (1 + prestamo.getInteres() / 100)) / prestamo.getNumeroCuotas();
        List<Cuota> cuotas = new ArrayList<>();

        for (int i = 1; i <= prestamo.getNumeroCuotas(); i++) {
            Cuota cuota = new Cuota();
            cuota.setNumero(i);
            cuota.setMonto(montoPorCuota);
            cuota.setFechaVencimiento(FechaUtil.sumarMeses(prestamo.getFechaEmision(), i));
            cuota.setEstado(Cuota.EstadoCuota.PENDIENTE);
            cuota.setPrestamo(nuevo);
            cuotas.add(cuota);
        }

        cuotaRepository.saveAll(cuotas);
        return nuevo;
    }

    public List<Prestamo> listarPrestamosPorUsuario(Long usuarioId) {
        return prestamoRepository.findByUsuarioId(usuarioId);
    }
}

