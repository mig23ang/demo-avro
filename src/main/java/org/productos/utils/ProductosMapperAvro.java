package org.productos.utils;

import jakarta.inject.Singleton;
import org.productos.avro.ProductosAvro;
import org.productos.dao.ProductosEntity;
import org.productos.dao.CategoriaEnum;
import org.productos.avro.CategoriaEnumAvro;

@Singleton
public class ProductosMapperAvro {

    public ProductosEntity toProductosEntity(ProductosAvro productosAvro) {
        return new ProductosEntity(
                productosAvro.getId().toString(),
                productosAvro.getNombre().toString(),
                productosAvro.getFecha().toString(),
                productosAvro.getUnidades(),
                mapCategoriaEnum(productosAvro.getCategoria()),
                productosAvro.getDisponible()
        );
    }

    //Metodo de conversión de CategoriaEnumAvro a CategoriaEnum
    private CategoriaEnum mapCategoriaEnum(CategoriaEnumAvro categoriaAvro) {
        return CategoriaEnum.valueOf(categoriaAvro.name());
    }

    public ProductosAvro toProductosAvro(ProductosEntity productosEntity) {
        return ProductosAvro.newBuilder()
                .setId(productosEntity.getId())
                .setNombre(productosEntity.getNombre())
                .setFecha(productosEntity.getFecha())
                .setUnidades(productosEntity.getUnidades())
                .setCategoria(mapCategoriaEnumA(productosEntity.getCategoria()))
                .setDisponible(productosEntity.isDisponible())
                .build();
    }

    //Metodo de conversión de CategoriaEnum a CategoriaEnumAvro
    private CategoriaEnumAvro mapCategoriaEnumA(CategoriaEnum categoria) {
        return CategoriaEnumAvro.valueOf(categoria.name());
    }
}
