package com.idgs12.usuarios.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.idgs12.usuarios.usuarios.entity.UsuarioEntity;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    Optional<UsuarioEntity> findByMatricula(String matricula);

    UsuarioEntity findByCorreo(String correo);

}
