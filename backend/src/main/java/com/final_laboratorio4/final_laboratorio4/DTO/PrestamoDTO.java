package com.final_laboratorio4.final_laboratorio4.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoDTO {
    private Long id;
    private Integer id_usuario;
    private Long id_libro;
    private LocalDate fecha_prestamo;
    private LocalDate fecha_devolucion;
    private String estado;

}
