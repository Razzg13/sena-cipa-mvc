package com.sena.cipa.web;

import com.sena.cipa.modelo.ActividadEvaluativaDAOImpl;
import com.sena.cipa.modelo.Evaluacion;
import com.sena.cipa.modelo.EvaluacionDAO;
import com.sena.cipa.modelo.EvaluacionDAOImpl;
import com.sena.cipa.modelo.MatriculaDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// CONTROLADOR web de evaluaciones (mismas reglas que EvaluacionControlador en Swing)
@WebServlet("/evaluaciones")
public class EvaluacionServlet extends ServletBase {

    private final EvaluacionDAO dao = new EvaluacionDAOImpl();

    // GET: con ?documento= la tabla muestra solo las evaluaciones de ese aprendiz
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Evaluacion actual = campo(req, "id").isEmpty() ? null : dao.leerPorId(entero(req, "id"));
        mostrar(req, resp, actual);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = campo(req, "accion");
        int id = entero(req, "id");

        if ("eliminar".equals(accion)) {
            if (dao.eliminar(id)) {
                aviso(req, "Evaluacion eliminada", true);
            } else {
                aviso(req, "No se pudo eliminar la evaluacion", false);
            }
            ir(req, resp, "/evaluaciones");
            return;
        }

        Evaluacion e = new Evaluacion();
        e.setId(id);
        e.setActividadId(entero(req, "actividadId"));
        e.setMatriculaId(entero(req, "matriculaId"));
        e.setCalificacion(campo(req, "calificacion").replace(',', '.'));
        e.setJuicio(campo(req, "juicio"));
        e.setRetroalimentacion(campo(req, "retroalimentacion"));
        e.setFechaEvaluacion(campo(req, "fechaEvaluacion"));
        String error = validar(e);
        if (error != null) {
            req.setAttribute("error", error);
            mostrar(req, resp, e);
            return;
        }

        boolean nuevo = !"modificar".equals(accion) || id == 0;
        boolean ok = nuevo ? dao.registrar(e) : dao.actualizar(e);
        if (!ok) {
            req.setAttribute("error", "No se pudo guardar. Revisa que ese aprendiz no tenga ya nota en esa actividad");
            mostrar(req, resp, e);
            return;
        }
        aviso(req, nuevo ? "Evaluacion registrada" : "Evaluacion modificada", true);
        ir(req, resp, "/evaluaciones");
    }

    private String validar(Evaluacion e) {
        if (e.getActividadId() == 0) {
            return "Todavia no hay actividades evaluativas. Registra primero una actividad.";
        }
        if (e.getMatriculaId() == 0) {
            return "Todavia no hay matriculas activas. Registra primero una matricula.";
        }
        if (!e.getCalificacion().isEmpty()) {
            try {
                double valor = Double.parseDouble(e.getCalificacion());
                if (valor < 0 || valor > 5) {
                    return "La calificacion debe estar entre 0.00 y 5.00";
                }
            } catch (NumberFormatException ex) {
                return "La calificacion debe ser un numero, por ejemplo 4.50";
            }
        }
        if (!fechaValida(e.getFechaEvaluacion())) {
            return "La fecha de evaluacion debe escribirse como aaaa-mm-dd";
        }
        return null;
    }

    private void mostrar(HttpServletRequest req, HttpServletResponse resp, Evaluacion actual)
            throws ServletException, IOException {
        String documento = campo(req, "documento");
        List<Evaluacion> lista = documento.isEmpty() ? dao.listarTodos() : dao.listarPorDocumento(documento);
        req.setAttribute("actual", actual);
        req.setAttribute("lista", lista);
        req.setAttribute("filtro", documento);
        req.setAttribute("actividades", new ActividadEvaluativaDAOImpl().listarTodos());
        req.setAttribute("matriculas", new MatriculaDAOImpl().listarActivas());
        req.setAttribute("juicios", new String[]{"APROBADO", "NO_APROBADO", "POR_MEJORAR"});
        vista(req, resp, "evaluaciones");
    }
}
