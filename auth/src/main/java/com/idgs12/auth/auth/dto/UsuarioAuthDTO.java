package com.idgs12.auth.auth.dto;

import lombok.Data;

@Data
public class UsuarioAuthDTO {
    private Integer id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String correo;
    private String matricula;
    private String rol;
    private String contrasena;
}
