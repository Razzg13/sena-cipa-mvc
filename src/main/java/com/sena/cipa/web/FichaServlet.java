package com.sena.cipa.web;

import com.sena.cipa.modelo.Ficha;
import com.sena.cipa.modelo.FichaDAO;
import com.sena.cipa.modelo.FichaDAOImpl;
import com.sena.cipa.modelo.ProgramaDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

// CONTROLADOR web de fichas (mismas reglas que FichaControlador en Swing)
@WebServlet("/fichas")
public class FichaServlet extends ServletBase {

    private final FichaDAO dao = new FichaDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Ficha actual = null;
        if (!campo(req, "id").isEmpty()) {
            actual = dao.leerPorId(entero(req, "id"));
        } else if (!campo(req, "numero").isEmpty()) {
            actual = dao.buscarPorNumero(campo(req, "numero"));
            if (actual == null) {
                req.setAttribute("error", "No existe una ficha con el numero " + campo(req, "numero"));
            }
        }
        mostrar(req, resp, actual);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = campo(req, "accion");
        int id = entero(req, "id");

        // una ficha no se borra: se cancela, porque sus matriculas quedan en el historial
        if ("cancelar".equals(accion)) {
            if (dao.eliminar(id)) {
                aviso(req, "Ficha cancelada", true);
            } else {
                aviso(req, "No se pudo cancelar la ficha", false);
            }
            ir(req, resp, "/fichas");
            return;
        }

        Ficha f = new Ficha();
        f.setId(id);
        f.setNumero(campo(req, "numero"));
        f.setProgramaId(entero(req, "programaId"));
        f.setJornada(campo(req, "jornada"));
        f.setFechaInicio(campo(req, "fechaInicio"));
        f.setFechaFin(campo(req, "fechaFin"));
        f.setAmbiente(campo(req, "ambiente"));
        f.setEstado(campo(req, "estado"));
        String error = validar(f);
        if (error != null) {
            req.setAttribute("error", error);
            mostrar(req, resp, f);
            return;
        }

        boolean nuevo = !"modificar".equals(accion) || id == 0;
        boolean ok = nuevo ? dao.registrar(f) : dao.actualizar(f);
        if (!ok) {
            req.setAttribute("error", "No se pudo guardar. Revisa que el numero de ficha no este repetido");
            mostrar(req, resp, f);
            return;
        }
        aviso(req, nuevo ? "Ficha registrada" : "Ficha modificada", true);
        ir(req, resp, "/fichas");
    }

    private String validar(Ficha f) {
        if (f.getProgramaId() == 0) {
            return "Elige un programa de formacion (si no hay, registra primero un programa)";
        }
        if (f.getNumero().isEmpty() || f.getAmbiente().isEmpty()) {
            return "Completa el numero de ficha y el ambiente";
        }
        if (!fechaValida(f.getFechaInicio()) || !fechaValida(f.getFechaFin())) {
            return "Las fechas deben existir y escribirse como aaaa-mm-dd";
        }
        if (LocalDate.parse(f.getFechaFin()).isBefore(LocalDate.parse(f.getFechaInicio()))) {
            return "La fecha de fin no puede ser anterior a la fecha de inicio";
        }
        return null;
    }

    private void mostrar(HttpServletRequest req, HttpServletResponse resp, Ficha actual)
            throws ServletException, IOException {
        req.setAttribute("actual", actual);
        req.setAttribute("lista", dao.listarTodos());
        req.setAttribute("programas", new ProgramaDAOImpl().listarActivos());
        req.setAttribute("jornadas", new String[]{"MANANA", "TARDE", "NOCHE", "MIXTA", "FIN_DE_SEMANA"});
        req.setAttribute("estados", new String[]{"EN_EJECUCION", "APLAZADA", "TERMINADA", "CANCELADA"});
        vista(req, resp, "fichas");
    }
}
