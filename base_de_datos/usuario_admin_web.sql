-- Crea un usuario ADMINISTRADOR para entrar a la version web.
-- Documento: 9999999   Clave: sena1234 (la misma de los usuarios de ejemplo)
-- Se ejecuta una sola vez, despues de sena_basico.sql
USE sena_basico;
INSERT INTO usuarios (documento, nombre_completo, correo, telefono, clave, rol) VALUES
    ('9999999', 'Administrador del sistema', 'admin@sena.edu.co', NULL,
     '$2a$10$buhwo33DjLN6NqtIOQhksuqYLjPoaFdwkBTHDqTRa3YPXK8vIyVW2', 'ADMINISTRADOR');
