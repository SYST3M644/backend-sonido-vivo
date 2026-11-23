package cl.sonidovivo.catalogo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ImagenService {

    private static final List<String> PERMITIDOS = List.of("image/jpeg", "image/png", "image/webp");

    private final Path carpeta;

    public ImagenService(@Value("${app.uploads.carpeta}") String ruta) {
        this.carpeta = Paths.get(ruta).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.carpeta);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear la carpeta de imagenes", e);
        }
    }

    public byte[] leer(String nombre) {
        Path archivo = resolver(nombre);

        if (!Files.exists(archivo)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen no encontrada: " + nombre);
        }

        try {
            return Files.readAllBytes(archivo);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo leer la imagen");
        }
    }

    public String guardar(MultipartFile archivo) {
        if (archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se recibio ningun archivo");
        }

        String tipo = archivo.getContentType();

        if (tipo == null || !PERMITIDOS.contains(tipo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se aceptan imagenes JPG, PNG o WEBP");
        }

        String nombre = System.currentTimeMillis() + "-" + archivo.getOriginalFilename();

        try {
            Files.copy(archivo.getInputStream(), resolver(nombre));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo guardar la imagen");
        }

        return nombre;
    }

    private Path resolver(String nombre) {
        Path archivo = carpeta.resolve(nombre).normalize();

        if (!archivo.startsWith(carpeta)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nombre de archivo no valido");
        }

        return archivo;
    }
}
