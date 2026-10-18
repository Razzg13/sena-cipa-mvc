package com.sena.cipa.web;

import com.sena.cipa.modelo.Conexion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

// CONTROLADOR del menu (reemplaza a MenuControlador): verifica la base y muestra las tarjetas.
@WebServlet(urlPatterns = {"/inicio", ""})
public class InicioServlet extends ServletBase {

    // modulos del menu en orden: ruta -> {titulo, explicacion, listo en web}
    static final Map<String, String[]> MODULOS = new LinkedHashMap<>();

    static {
        MODULOS.put("programas", new String[]{"Programas de formacion", "Catalogo de programas", "si"});
        MODULOS.put("fichas", new String[]{"Fichas", "Grupos de aprendices", "si"});
        MODULOS.put("instructores", new String[]{"Instructores", "Datos del instructor", "si"});
        MODULOS.put("aprendices", new String[]{"Aprendices", "Datos del aprendiz", "si"});
        MODULOS.put("matriculas", new String[]{"Matriculas", "Aprendiz en una ficha", "si"});
        MODULOS.put("asignaciones", new String[]{"Asignaciones", "Instructor por ficha", "si"});
        MODULOS.put("sesiones", new String[]{"Sesiones", "Clases dictadas", "si"});
        MODULOS.put("asistencias", new String[]{"Asistencias", "Control de asistencia", "si"});
        MODULOS.put("actividades", new String[]{"Actividades", "Talleres y pruebas", "si"});
        MODULOS.put("evaluaciones", new String[]{"Evaluaciones", "Notas y juicios", "si"});
        MODULOS.put("usuarios", new String[]{"Usuarios", "Cuentas, roles y claves", "si"});
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Conexion conexion = new Conexion();
        req.setAttribute("conectada", conexion.probar());
        req.setAttribute("bd", conexion.getBd());
        req.setAttribute("errorBd", conexion.getUltimoError());
        req.setAttribute("modulos", MODULOS);
        vista(req, resp, "inicio");
    }
}
