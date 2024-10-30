package org.productos.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductosEntity {


    private String id;
    private String nombre;
    private String fecha;
    private Integer unidades;

    private CategoriaEnum categoria;
    private boolean disponible;
}
