package com.idgs12.grupos.grupos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import com.idgs12.grupos.grupos.entity.GrupoUsuario;
import java.util.List;

public interface GrupoUsuarioRepository extends JpaRepository<GrupoUsuario, Integer> {

    List<GrupoUsuario> findByGrupo_Id(int grupoId);

    @Transactional
    void deleteByGrupo_Id(int grupoId);

    boolean existsByGrupo_IdAndUsuarioId(int grupoId, Long usuarioId);

    List<GrupoUsuario> findByUsuarioId(Long usuarioId);
}
