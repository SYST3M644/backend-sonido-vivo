package cl.sonidovivo.catalogo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cl.sonidovivo.catalogo.service.ImagenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Imágenes", description = "Fotos de los productos")
@RequestMapping("api/productos/imagenes")
public class ImagenController {
    private final ImagenService service;

    public ImagenController(ImagenService service) {
        this.service = service;
    }

    @Operation(summary = "Ver imagen", description = "Devuelve la foto de un producto por su nombre de archivo. Es pública.")
    @GetMapping("/{nombre}")
    public ResponseEntity<byte[]> ver(@PathVariable String nombre) {
        byte[] imagen = service.leer(nombre);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(imagen);
    }

    @Operation(summary = "Subir imagen", description = "Sube una foto JPG, PNG o WEBP y devuelve el nombre con que se guardó.")
    @PostMapping
    public ResponseEntity<String> subir(@RequestParam("archivo") MultipartFile archivo) {
        String nombre = service.guardar(archivo);
        return ResponseEntity.status(HttpStatus.CREATED).body(nombre);
    }
}
