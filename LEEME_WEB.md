# SENA CIPA - version web

La aplicacion paso de Swing (escritorio) a web con **Servlets + JSP sobre Tomcat 10.1**.
El modelo (paquete `modelo`: entidades, DAO y conexion) es el mismo de antes.

| Capa | Escritorio (antes) | Web (ahora) |
|---|---|---|
| Modelo | `modelo/*` | `modelo/*` (sin cambios, salvo `Conexion` que acepta variables de entorno) |
| Controlador | `controlador/*Controlador.java` | `web/*Servlet.java` |
| Vista | `vista/*Vista.java` (Swing) | `src/main/webapp/WEB-INF/vistas/*.jsp` |

La version de escritorio se conserva y sigue funcionando (`mvnw exec:java`).
Respaldo del proyecto original: `respaldo/version_escritorio_2026-10-13.tgz`.

## Como correrla

1. Si ya tienes la base `sena_basico`, NO ejecutes `sena_basico.sql` (empieza con DROP DATABASE y borra tus datos).
   Solo ejecuta `base_de_datos/usuario_admin_web.sql` para crear el administrador (solo agrega una fila).
   Si la base no existe todavia, ahi si ejecuta primero `sena_basico.sql`.
2. Opcion A (sin instalar nada): `mvnw jetty:run` y abrir http://localhost:8081/
3. Opcion B (NetBeans): agregar un servidor **Apache Tomcat 10.1** en la pestana Services,
   en Propiedades del proyecto > Run elegir ese servidor y darle Run. Abre http://localhost:8080/sena-cipa/
4. Entrar con documento `9999999` y clave `sena1234` (ADMINISTRADOR).
   Los usuarios de ejemplo (instructores y aprendices) tambien entran con `sena1234`.

## Base de datos en un servidor

Por defecto usa `jdbc:mysql://localhost:3306/`, usuario `root`, clave `123456`.
En un servidor se cambian con las variables de entorno `DB_URL` (terminada en `/`), `DB_USUARIO` y `DB_CLAVE`.

## Estado

Los 11 modulos de la version de escritorio ya estan en web: Programas, Fichas, Instructores, Aprendices,
Matriculas, Asignaciones, Sesiones, Asistencias, Actividades, Evaluaciones y Usuarios (este ultimo solo ADMINISTRADOR).
