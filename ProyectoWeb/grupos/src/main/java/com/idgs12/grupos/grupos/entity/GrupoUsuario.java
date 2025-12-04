package com.idgs12.grupos.grupos.entity;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "grupo_usuario")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

public class GrupoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id", nullable = false)
    private GruposEntity grupo;

    private Long usuarioId;
}
