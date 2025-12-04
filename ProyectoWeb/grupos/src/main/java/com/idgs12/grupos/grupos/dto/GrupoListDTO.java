package com.idgs12.grupos.grupos.dto;

import lombok.Data;

@Data
public class GrupoListDTO {
    private int id;
    private String nombre;
    private String cuatrimestre;
    private Boolean estado;
    private int cantidadAlumnos;
    private int cantidadProfesores;
}