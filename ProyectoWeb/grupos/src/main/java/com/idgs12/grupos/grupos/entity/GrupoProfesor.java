package com.idgs12.grupos.grupos.entity;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "grupo_profesor")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class GrupoProfesor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id", nullable = false)
    private GruposEntity grupo;

    @Column(name = "profesor_id", nullable = false)
    private Long profesorId;
}