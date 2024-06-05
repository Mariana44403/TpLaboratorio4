package com.final_laboratorio4.final_laboratorio4.DTO;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class PrestamoDTO {
    private Long id;
    private Integer id_usuario;
    private String nombreUsuario;
    private Long id_libro;
    private String tituloLibro;
    private LocalDate fecha_prestamo;
    private LocalDate fecha_devolucion;
    private String estado;

}
