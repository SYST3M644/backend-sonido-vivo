CREATE TABLE pedido (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cliente VARCHAR(150) NOT NULL,
    fecha DATE NOT NULL,
    total INT NOT NULL,
    entrega VARCHAR(30) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'en preparacion',
    PRIMARY KEY (id)
);
