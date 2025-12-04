package com.idgs12.grupos.grupos.entity;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "grupos")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class GruposEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String cuatrimestre;
    private Boolean estado;
}
