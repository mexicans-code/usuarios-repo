package com.idgs12.grupos.grupos.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.idgs12.grupos.grupos.dto.UsuarioDTO;

@FeignClient(name = "usuarios", url = "https://usuarios-repository-production.up.railway.app")
public interface UsuarioFeignClient {

    @GetMapping("/usuarios/{id}")
    UsuarioDTO getUsuarioById(@PathVariable("id") Long id);
}
