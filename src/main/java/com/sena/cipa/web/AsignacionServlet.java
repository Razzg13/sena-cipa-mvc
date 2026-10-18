package com.sena.cipa.web;

import com.sena.cipa.modelo.Asignacion;
import com.sena.cipa.modelo.AsignacionDAO;
import com.sena.cipa.modelo.AsignacionDAOImpl;
import com.sena.cipa.modelo.FichaDAOImpl;
import com.sena.cipa.modelo.InstructorDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

// CONTROLADOR web de asignaciones de instructor (mismas reglas que AsignacionControlador en Swing)
@WebServlet("/asignaciones")
public class AsignacionServlet extends ServletBase {

    private final AsignacionDAO dao = new AsignacionDAOImpl();

    // GET: con ?ficha= la tabla muestra solo las asignaciones de esa ficha
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Asignacion actual = campo(req, "id").isEmpty() ? null : dao.leerPorId(entero(req, "id"));
        mostrar(req, resp, actual);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = campo(req, "accion");
        int id = entero(req, "id");

        if ("eliminar".equals(accion)) {
            if (dao.eliminar(id)) {
                aviso(req, "Asignacion eliminada", true);
            } else {
                aviso(req, "No se pudo eliminar la asignacion", false);
            }
            ir(req, resp, "/asignaciones");
            return;
        }

        Asignacion a = new Asignacion();
        a.setId(id);
        a.setFichaId(entero(req, "fichaId"));
        a.setInstructorId(entero(req, "instructorId"));
        a.setFechaInicio(campo(req, "fechaInicio"));
        a.setFechaFin(campo(req, "fechaFin"));
        String error = null;
        if (a.getFichaId() == 0) {
            error = "Primero registra una ficha";
        } else if (a.getInstructorId() == 0) {
            error = "Primero registra un instructor";
        } else if (!fechaValida(a.getFechaInicio()) || !fechaValida(a.getFechaFin())) {
            error = "Las fechas deben existir y escribirse como aaaa-mm-dd";
        } else if (LocalDate.parse(a.getFechaFin()).isBefore(LocalDate.parse(a.getFechaInicio()))) {
            error = "La fecha de fin no puede ser anterior a la fecha de inicio";
        }
        if (error != null) {
            req.setAttribute("error", error);
            mostrar(req, resp, a);
            return;
        }

        boolean nuevo = !"modificar".equals(accion) || id == 0;
        boolean ok = nuevo ? dao.registrar(a) : dao.actualizar(a);
        if (!ok) {
            req.setAttribute("error", "No se pudo guardar la asignacion");
            mostrar(req, resp, a);
            return;
        }
        aviso(req, nuevo ? "Asignacion registrada" : "Asignacion modificada", true);
        ir(req, resp, "/asignaciones");
    }

    private void mostrar(HttpServletRequest req, HttpServletResponse resp, Asignacion actual)
            throws ServletException, IOException {
        String ficha = campo(req, "ficha");
        List<Asignacion> lista = ficha.isEmpty() ? dao.listarTodos() : dao.listarPorFicha(ficha);
        req.setAttribute("actual", actual);
        req.setAttribute("lista", lista);
        req.setAttribute("filtro", ficha);
        // cualquier ficha puede tener instructor, pero solo instructores activos
        req.setAttribute("fichas", new FichaDAOImpl().listarTodos());
        req.setAttribute("instructores", new InstructorDAOImpl().listarActivos());
        vista(req, resp, "asignaciones");
    }
}
