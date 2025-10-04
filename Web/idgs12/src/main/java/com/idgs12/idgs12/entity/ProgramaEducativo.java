package com.idgs12.idgs12.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "programas_educativos")
public class ProgramaEducativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String programa;
    private boolean activo;

    @ManyToOne
    @JoinColumn(name = "division_id")
    private DivisionEntity division;
}