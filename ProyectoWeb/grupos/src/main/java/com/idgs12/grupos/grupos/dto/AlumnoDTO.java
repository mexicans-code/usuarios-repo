package com.idgs12.grupos.grupos.dto;

import lombok.Data;

@Data
public class AlumnoDTO {
    private Long id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;
}
