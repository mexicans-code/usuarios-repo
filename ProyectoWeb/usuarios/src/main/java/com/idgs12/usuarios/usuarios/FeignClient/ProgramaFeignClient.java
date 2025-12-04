package com.idgs12.usuarios.usuarios.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.idgs12.usuarios.usuarios.dto.ProgramaUsuarioDTO;

import java.util.List;

@FeignClient(name = "programaseducativos")
public interface ProgramaFeignClient {

    @GetMapping("/programas/{id}")
    ProgramaUsuarioDTO getProgramaById(@PathVariable("id") Long id);

    @PostMapping("/programas/by-ids")
    List<ProgramaUsuarioDTO> obtenerProgramasPorIds(@RequestBody List<Long> ids);
}
