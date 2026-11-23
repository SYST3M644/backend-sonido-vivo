ALTER TABLE producto
ADD COLUMN categoria VARCHAR(30) NOT NULL DEFAULT 'Accesorios';

UPDATE producto SET categoria = 'Cuerdas'    WHERE id IN (1, 2, 3, 6, 9);
UPDATE producto SET categoria = 'Teclados'   WHERE id IN (4, 7);
UPDATE producto SET categoria = 'Vientos'    WHERE id IN (5, 8);
UPDATE producto SET categoria = 'Percusión'  WHERE id = 10;
UPDATE producto SET categoria = 'Audio'      WHERE id = 11;
UPDATE producto SET categoria = 'Accesorios' WHERE id = 12;
