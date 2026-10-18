package com.sena.cipa.web;

import com.sena.cipa.modelo.Asistencia;
import com.sena.cipa.modelo.AsistenciaDAO;
import com.sena.cipa.modelo.AsistenciaDAOImpl;
import com.sena.cipa.modelo.MatriculaDAOImpl;
import com.sena.cipa.modelo.SesionFormacionDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// CONTROLADOR web de asistencias (mismas reglas que AsistenciaControlador en Swing)
@WebServlet("/asistencias")
public class AsistenciaServlet extends ServletBase {

    private final AsistenciaDAO dao = new AsistenciaDAOImpl();

    // GET: con ?documento= la tabla muestra solo las asistencias de ese aprendiz
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Asistencia actual = campo(req, "id").isEmpty() ? null : dao.leerPorId(entero(req, "id"));
        mostrar(req, resp, actual);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = campo(req, "accion");
        int id = entero(req, "id");

        if ("eliminar".equals(accion)) {
            if (dao.eliminar(id)) {
                aviso(req, "Asistencia eliminada", true);
            } else {
                aviso(req, "No se pudo eliminar la asistencia", false);
            }
            ir(req, resp, "/asistencias");
            return;
        }

        Asistencia a = new Asistencia();
        a.setId(id);
        a.setSesionId(entero(req, "sesionId"));
        a.setMatriculaId(entero(req, "matriculaId"));
        a.setEstado(campo(req, "estado"));
        a.setObservacion(campo(req, "observacion"));
        String error = null;
        if (a.getSesionId() == 0) {
            error = "Todavia no hay sesiones de formacion. Registra primero una sesion.";
        } else if (a.getMatriculaId() == 0) {
            error = "Todavia no hay matriculas activas. Registra primero una matricula.";
        }
        if (error != null) {
            req.setAttribute("error", error);
            mostrar(req, resp, a);
            return;
        }

        boolean nuevo = !"modificar".equals(accion) || id == 0;
        boolean ok = nuevo ? dao.registrar(a) : dao.actualizar(a);
        if (!ok) {
            req.setAttribute("error", "No se pudo guardar. Revisa que ese aprendiz no tenga ya asistencia en esa sesion");
            mostrar(req, resp, a);
            return;
        }
        aviso(req, nuevo ? "Asistencia registrada" : "Asistencia modificada", true);
        ir(req, resp, "/asistencias");
    }

    private void mostrar(HttpServletRequest req, HttpServletResponse resp, Asistencia actual)
            throws ServletException, IOException {
        String documento = campo(req, "documento");
        List<Asistencia> lista = documento.isEmpty() ? dao.listarTodos() : dao.listarPorDocumento(documento);
        req.setAttribute("actual", actual);
        req.setAttribute("lista", lista);
        req.setAttribute("filtro", documento);
        req.setAttribute("sesiones", new SesionFormacionDAOImpl().listarTodos());
        req.setAttribute("matriculas", new MatriculaDAOImpl().listarActivas());
        req.setAttribute("estados", new String[]{"PRESENTE", "AUSENTE", "TARDANZA", "EXCUSADO"});
        vista(req, resp, "asistencias");
    }
}
