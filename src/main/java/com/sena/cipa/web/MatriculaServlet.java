package com.sena.cipa.web;

import com.sena.cipa.modelo.AprendizDAOImpl;
import com.sena.cipa.modelo.FichaDAOImpl;
import com.sena.cipa.modelo.Matricula;
import com.sena.cipa.modelo.MatriculaDAO;
import com.sena.cipa.modelo.MatriculaDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// CONTROLADOR web de matriculas (mismas reglas que MatriculaControlador en Swing)
@WebServlet("/matriculas")
public class MatriculaServlet extends ServletBase {

    private final MatriculaDAO dao = new MatriculaDAOImpl();

    // GET: con ?documento= la tabla muestra solo las matriculas de ese aprendiz
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Matricula actual = campo(req, "id").isEmpty() ? null : dao.leerPorId(entero(req, "id"));
        mostrar(req, resp, actual);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = campo(req, "accion");
        int id = entero(req, "id");

        if ("cancelar".equals(accion)) {
            if (dao.eliminar(id)) {
                aviso(req, "Matricula cancelada", true);
            } else {
                aviso(req, "No se pudo cancelar la matricula", false);
            }
            ir(req, resp, "/matriculas");
            return;
        }

        Matricula m = new Matricula();
        m.setId(id);
        m.setAprendizId(entero(req, "aprendizId"));
        m.setFichaId(entero(req, "fichaId"));
        m.setFechaMatricula(campo(req, "fechaMatricula"));
        m.setEstado(campo(req, "estado"));
        String error = null;
        if (m.getAprendizId() == 0) {
            error = "Primero registra un aprendiz";
        } else if (m.getFichaId() == 0) {
            error = "Primero registra una ficha en ejecucion";
        } else if (!fechaValida(m.getFechaMatricula())) {
            error = "La fecha de matricula debe existir y escribirse como aaaa-mm-dd";
        }
        if (error != null) {
            req.setAttribute("error", error);
            mostrar(req, resp, m);
            return;
        }

        boolean nuevo = !"modificar".equals(accion) || id == 0;
        boolean ok = nuevo ? dao.registrar(m) : dao.actualizar(m);
        if (!ok) {
            req.setAttribute("error", "No se pudo guardar. Revisa que el aprendiz no este ya matriculado en esa ficha");
            mostrar(req, resp, m);
            return;
        }
        aviso(req, nuevo ? "Matricula registrada" : "Matricula modificada", true);
        ir(req, resp, "/matriculas");
    }

    private void mostrar(HttpServletRequest req, HttpServletResponse resp, Matricula actual)
            throws ServletException, IOException {
        String documento = campo(req, "documento");
        List<Matricula> lista = documento.isEmpty() ? dao.listarTodos() : dao.listarPorDocumento(documento);
        req.setAttribute("actual", actual);
        req.setAttribute("lista", lista);
        req.setAttribute("filtro", documento);
        // el aprendiz debe estar activo y la ficha tiene que estar en ejecucion
        req.setAttribute("aprendices", new AprendizDAOImpl().listarActivos());
        req.setAttribute("fichas", new FichaDAOImpl().listarEnEjecucion());
        req.setAttribute("estados", new String[]{"ACTIVA", "CANCELADA"});
        vista(req, resp, "matriculas");
    }
}
