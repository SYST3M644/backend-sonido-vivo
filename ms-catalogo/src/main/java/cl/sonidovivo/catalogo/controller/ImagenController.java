package cl.sonidovivo.catalogo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cl.sonidovivo.catalogo.service.ImagenService;

@RestController
@RequestMapping("api/productos/imagenes")
@CrossOrigin(origins = "http://localhost:5173")
public class ImagenController {
    private final ImagenService service;

    public ImagenController(ImagenService service) {
        this.service = service;
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<byte[]> ver(@PathVariable String nombre) {
        byte[] imagen = service.leer(nombre);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imagen);
    }

    @PostMapping
    public ResponseEntity<String> subir(@RequestParam("archivo") MultipartFile archivo) {
        String nombre = service.guardar(archivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nombre);
    }
}
