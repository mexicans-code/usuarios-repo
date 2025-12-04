package com.idgs12.usuarios.usuarios.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.idgs12.usuarios.usuarios.entity.ProgramaUsuario;

public interface ProgramaUsuarioRepository extends JpaRepository<ProgramaUsuario, Integer> {

    @Transactional
    void deleteByUsuario_Id(int usuarioId);

}
