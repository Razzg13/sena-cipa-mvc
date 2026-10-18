package com.sena.cipa.web;

import com.sena.cipa.modelo.ActividadEvaluativa;
import com.sena.cipa.modelo.ActividadEvaluativaDAO;
import com.sena.cipa.modelo.ActividadEvaluativaDAOImpl;
import com.sena.cipa.modelo.FichaDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// CONTROLADOR web de actividades evaluativas (mismas reglas que ActividadEvaluativaControlador en Swing)
@WebServlet("/actividades")
public class ActividadEvaluativaServlet extends ServletBase {

    private final ActividadEvaluativaDAO dao = new ActividadEvaluativaDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ActividadEvaluativa actual = null;
        if (!campo(req, "id").isEmpty()) {
            actual = dao.leerPorId(entero(req, "id"));
        } else if (!campo(req, "nombre").isEmpty()) {
            actual = dao.buscarPorNombre(campo(req, "nombre"));
            if (actual == null) {
                req.setAttribute("error", "No existe una actividad con el nombre " + campo(req, "nombre"));
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
                aviso(req, "Actividad eliminada", true);
            } else {
                aviso(req, "No se pudo eliminar la actividad. Revisa que no tenga evaluaciones registradas", false);
            }
            ir(req, resp, "/actividades");
            return;
        }

        ActividadEvaluativa a = new ActividadEvaluativa();
        a.setId(id);
        a.setFichaId(entero(req, "fichaId"));
        a.setNombre(campo(req, "nombre"));
        String descripcion = campo(req, "descripcion");
        // la descripcion puede quedar vacia: en la base se guarda como null
        a.setDescripcion(descripcion.isEmpty() ? null : descripcion);
        a.setFechaLimite(campo(req, "fechaLimite"));
        String error = null;
        if (a.getFichaId() == 0) {
            error = "Primero registra una ficha";
        } else if (a.getNombre().isEmpty()) {
            error = "Completa el nombre de la actividad";
        } else if (!fechaValida(a.getFechaLimite())) {
            error = "La fecha limite debe existir y escribirse como aaaa-mm-dd";
        }
        if (error != null) {
            req.setAttribute("error", error);
            mostrar(req, resp, a);
            return;
        }

        boolean nuevo = !"modificar".equals(accion) || id == 0;
        boolean ok = nuevo ? dao.registrar(a) : dao.actualizar(a);
        if (!ok) {
            req.setAttribute("error", "No se pudo guardar la actividad");
            mostrar(req, resp, a);
            return;
        }
        aviso(req, nuevo ? "Actividad registrada" : "Actividad modificada", true);
        ir(req, resp, "/actividades");
    }

    private void mostrar(HttpServletRequest req, HttpServletResponse resp, ActividadEvaluativa actual)
            throws ServletException, IOException {
        req.setAttribute("actual", actual);
        req.setAttribute("lista", dao.listarTodos());
        req.setAttribute("fichas", new FichaDAOImpl().listarTodos());
        vista(req, resp, "actividades");
    }
}
