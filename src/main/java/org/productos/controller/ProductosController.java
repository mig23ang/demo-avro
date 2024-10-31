package org.productos.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.productos.avro.ProductosAvro;
import org.productos.dao.ProductosEntity;
import org.productos.service.ProductosSerializer;

@Path("/productos") // Debes definir la ruta aquí
public class ProductosController {

    @Inject
    ProductosSerializer productosSerializer;
    @Inject
    Logger logger;

    @POST
    @Produces("application/json")
    public Response productos(ProductosEntity productosEntity) {
        logger.info("Inicia productos");
        try {
            ProductosAvro serializedProductos = productosSerializer.serializeProductos(productosEntity);
            logger.info("Productos serializados correctamente");
            return Response.ok(serializedProductos).build();
        } catch (Exception e) {
            logger.error("Error al serializar productos" + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al serializar productos").build();
        }
    }
}
