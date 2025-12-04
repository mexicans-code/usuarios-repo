package com.idgs12.auth.auth.dto;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String type = "Bearer";
    private UsuarioInfoDTO usuario;
}