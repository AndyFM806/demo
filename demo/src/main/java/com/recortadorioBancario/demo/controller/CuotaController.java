package com.recortadorioBancario.demo.controller;


import com.recortadorioBancario.demo.entidades.Cuota;
import com.recortadorioBancario.demo.repository.CuotaRepository;
import com.recortadorioBancario.demo.services.CuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/cuotas")
@CrossOrigin(origins = "*")
public class CuotaController {

    @Autowired
    private CuotaService cuotaService;

    @Autowired
    private CuotaRepository cuotaRepository;

    // 🔹 Listar todas las cuotas pendientes (dashboard general)
    @GetMapping("/pendientes")
    public List<Cuota> listarPendientes() {
        return cuotaService.listarPendientes();
    }

    // 🔹 Listar cuotas pendientes de un usuario específico (para dashboard personal)
    @GetMapping("/pendientes/usuario/{usuarioId}")
    public List<Cuota> listarPendientesPorUsuario(@PathVariable Long usuarioId) {
        return cuotaRepository.findByPrestamoUsuarioIdAndEstado(usuarioId, Cuota.EstadoCuota.PENDIENTE);
    }

    // 🔹 Listar cuotas de un préstamo específico
    @GetMapping("/prestamo/{prestamoId}")
    public List<Cuota> listarPorPrestamo(@PathVariable Long prestamoId) {
        return cuotaRepository.findByPrestamoId(prestamoId);
    }

    // 🔹 Marcar una cuota como pagada
    @PutMapping("/{id}/pagar")
    public Cuota marcarComoPagada(@PathVariable Long id) {
        return cuotaService.marcarComoPagada(id);
    }
}
