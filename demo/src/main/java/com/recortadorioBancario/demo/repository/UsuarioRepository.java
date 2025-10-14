package com.recortadorioBancario.demo.repository;


import com.recortadorioBancario.demo.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmailAndPassword(String email, String password);
}
