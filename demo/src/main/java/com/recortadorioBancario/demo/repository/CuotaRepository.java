package com.recortadorioBancario.demo.repository;

import com.recortadorioBancario.demo.entidades.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {

    List<Cuota> findByEstado(Cuota.EstadoCuota estado);

    List<Cuota> findByPrestamoId(Long prestamoId);

    List<Cuota> findByPrestamoUsuarioIdAndEstado(Long usuarioId, Cuota.EstadoCuota estado);
}
