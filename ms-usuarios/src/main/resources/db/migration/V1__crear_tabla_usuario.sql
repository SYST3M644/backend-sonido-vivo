CREATE TABLE usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(150) NOT NULL,
    correo VARCHAR(150) NOT NULL,
    clave VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    activo BIT(1) NOT NULL DEFAULT b'1',
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuario_correo (correo)
);
