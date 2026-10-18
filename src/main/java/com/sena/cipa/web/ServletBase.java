package com.sena.cipa.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

// Ayudante comun de los controladores web (hace el papel que tenia VentanaCrud en Swing):
// leer campos del formulario, mostrar mensajes y abrir la vista JSP.
public abstract class ServletBase extends HttpServlet {

    // texto de un campo del formulario, sin espacios a los lados ("" si no vino)
    protected String campo(HttpServletRequest req, String nombre) {
        String v = req.getParameter(nombre);
        return v == null ? "" : v.trim();
    }

    // numero entero de un campo, o 0 si no es un numero
    protected int entero(HttpServletRequest req, String nombre) {
        try {
            return Integer.parseInt(campo(req, nombre));
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    // true si el texto es una fecha que existe, escrita como aaaa-mm-dd
    protected boolean fechaValida(String texto) {
        try {
            LocalDate.parse(texto);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    // true si el texto es una hora escrita como HH:mm (o HH:mm:ss)
    protected boolean horaValida(String texto) {
        try {
            LocalTime.parse(texto);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }

    // mensaje que se muestra una sola vez en la siguiente pagina (ok = verde, si no = rojo)
    protected void aviso(HttpServletRequest req, String mensaje, boolean ok) {
        HttpSession sesion = req.getSession();
        sesion.setAttribute("aviso", mensaje);
        sesion.setAttribute("avisoOk", ok);
    }

    // abre la vista /WEB-INF/vistas/<nombre>.jsp
    protected void vista(HttpServletRequest req, HttpServletResponse resp, String nombre)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/vistas/" + nombre + ".jsp").forward(req, resp);
    }

    // despues de guardar se redirige, asi recargar la pagina no repite el registro
    protected void ir(HttpServletRequest req, HttpServletResponse resp, String ruta) throws IOException {
        resp.sendRedirect(req.getContextPath() + ruta);
    }
}
