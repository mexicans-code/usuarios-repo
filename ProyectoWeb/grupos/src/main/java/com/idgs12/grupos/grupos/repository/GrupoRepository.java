package com.idgs12.grupos.grupos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.idgs12.grupos.grupos.entity.GruposEntity;

public interface GrupoRepository extends JpaRepository<GruposEntity, Integer> {
}
