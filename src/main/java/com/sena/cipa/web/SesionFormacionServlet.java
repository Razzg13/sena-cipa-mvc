package com.sena.cipa.web;

import com.sena.cipa.modelo.FichaDAOImpl;
import com.sena.cipa.modelo.SesionFormacion;
import com.sena.cipa.modelo.SesionFormacionDAO;
import com.sena.cipa.modelo.SesionFormacionDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;

// CONTROLADOR web de sesiones de formacion (mismas reglas que SesionFormacionControlador en Swing)
@WebServlet("/sesiones")
public class SesionFormacionServlet extends ServletBase {

    private final SesionFormacionDAO dao = new SesionFormacionDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SesionFormacion actual = null;
        if (!campo(req, "id").isEmpty()) {
            actual = dao.leerPorId(entero(req, "id"));
        } else if (!campo(req, "tema").isEmpty()) {
            actual = dao.buscarPorTema(campo(req, "tema"));
            if (actual == null) {
                req.setAttribute("error", "No existe una sesion con el tema " + campo(req, "tema"));
            }
        }
        mostrar(req, resp, actual);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = campo(req, "accion");
        int id = entero(req, "id");

        if ("eliminar".equals(accion)) {
            if (dao.eliminar(id)) {
                aviso(req, "Sesion eliminada", true);
            } else {
                aviso(req, "No se pudo eliminar la sesion. Revisa que no tenga asistencias registradas", false);
            }
            ir(req, resp, "/sesiones");
            return;
        }

        SesionFormacion s = new SesionFormacion();
        s.setId(id);
        s.setFichaId(entero(req, "fichaId"));
        s.setFecha(campo(req, "fecha"));
        s.setHoraInicio(campo(req, "horaInicio"));
        s.setHoraFin(campo(req, "horaFin"));
        s.setTema(campo(req, "tema"));
        String error = null;
        if (s.getFichaId() == 0) {
            error = "Primero registra una ficha";
        } else if (s.getTema().isEmpty()) {
            error = "Completa el tema de la sesion";
        } else if (!fechaValida(s.getFecha())) {
            error = "La fecha debe existir y escribirse como aaaa-mm-dd";
        } else if (!horaValida(s.getHoraInicio()) || !horaValida(s.getHoraFin())) {
            error = "Las horas deben escribirse como HH:mm";
        } else if (!LocalTime.parse(s.getHoraFin()).isAfter(LocalTime.parse(s.getHoraInicio()))) {
            error = "La hora de fin debe ser posterior a la hora de inicio";
        }
        if (error != null) {
            req.setAttribute("error", error);
            mostrar(req, resp, s);
            return;
        }
        s.setHoraInicio(normalizarHora(s.getHoraInicio()));
        s.setHoraFin(normalizarHora(s.getHoraFin()));

        boolean nuevo = !"modificar".equals(accion) || id == 0;
        boolean ok = nuevo ? dao.registrar(s) : dao.actualizar(s);
        if (!ok) {
            req.setAttribute("error", "No se pudo guardar la sesion");
            mostrar(req, resp, s);
            return;
        }
        aviso(req, nuevo ? "Sesion registrada" : "Sesion modificada", true);
        ir(req, resp, "/sesiones");
    }

    // la base guarda la hora como TIME: se envia siempre "HH:mm:00"
    private String normalizarHora(String texto) {
        LocalTime hora = LocalTime.parse(texto);
        return String.format("%02d:%02d:00", hora.getHour(), hora.getMinute());
    }

    private void mostrar(HttpServletRequest req, HttpServletResponse resp, SesionFormacion actual)
            throws ServletException, IOException {
        req.setAttribute("actual", actual);
        req.setAttribute("lista", dao.listarTodos());
        req.setAttribute("fichas", new FichaDAOImpl().listarTodos());
        vista(req, resp, "sesiones");
    }
}
