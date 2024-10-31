package org.productos.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.jboss.logging.Logger;
import org.productos.avro.ProductosAvro;
import org.productos.dao.ProductosEntity;
import org.productos.utils.ProductosMapperAvro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@ApplicationScoped
public class ProductosSerializer {

    @Inject
    ProductosMapperAvro mapper;

    @Inject
    Logger logger;



    public ProductosAvro serializeProductos(ProductosEntity productosEntity) {
        logger.info("Inicia serializeProductos en service");
        ProductosAvro productosAvro = mapper.toProductosAvro(productosEntity);

        try {
            byte[] avroData = serializeToByteArray(productosAvro);
            sendAvroData(avroData);
            logger.info("Productos serializados correctamente en service");
        } catch (IOException e) {
            logger.error("Error al serializar el producto a bytes: ", e);
        } catch (Exception e) {
            logger.error("Error al enviar los datos: ", e);
        }

        return productosAvro;
    }

    public void sendAvroData(byte[] avroData) throws IOException {
        logger.info("Enviando datos a la cola en sendAvroData");
        URL url = new URL("xxxxxxxxxxxxxxxxxx");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.setDoOutput(true);

        // Enviar los datos serializados
        try (OutputStream os = connection.getOutputStream()) {
            logger.info("Enviando datos a la cola serializados");
            os.write(avroData);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            logger.info("Datos enviados correctamente.");
        } else {
            logger.error("Error al enviar datos: " + responseCode);
        }
    }

    public byte[] serializeToByteArray(ProductosAvro productosAvro) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        SpecificDatumWriter<ProductosAvro> datumWriter = new SpecificDatumWriter<>(ProductosAvro.class);
        DataFileWriter<ProductosAvro> dataFileWriter = new DataFileWriter<>(datumWriter);

        dataFileWriter.create(ProductosAvro.getClassSchema(), outputStream);
        dataFileWriter.append(productosAvro);
        dataFileWriter.close(); // Cerrar

        return outputStream.toByteArray();
    }
}
