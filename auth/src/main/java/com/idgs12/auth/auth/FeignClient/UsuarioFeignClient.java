package com.idgs12.auth.auth.FeignClient;

import com.idgs12.auth.auth.dto.UsuarioAuthDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "usuarios", url = "https://usuarios-repository-production.up.railway.app")
public interface UsuarioFeignClient {

    @GetMapping("/usuarios/correo/{correo}")
    UsuarioAuthDTO getUsuarioByCorreo(@PathVariable("correo") String correo);

    @PostMapping("/usuarios/register")
    UsuarioAuthDTO createUsuarioWithPassword(@RequestBody UsuarioAuthDTO dto);
}