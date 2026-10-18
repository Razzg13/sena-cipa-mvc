package com.sena.cipa.web;

import com.sena.cipa.modelo.Programa;
import com.sena.cipa.modelo.ProgramaDAO;
import com.sena.cipa.modelo.ProgramaDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// CONTROLADOR web de programas: las mismas reglas de ProgramaControlador (Swing),
// pero recibe formularios HTML en vez de clics de botones.
@WebServlet("/programas")
public class ProgramaServlet extends ServletBase {

    private final ProgramaDAO dao = new ProgramaDAOImpl();

    // GET: muestra la tabla; con ?id= carga ese registro y con ?codigo= lo busca
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Programa actual = null;
        if (!campo(req, "id").isEmpty()) {
            actual = dao.leerPorId(entero(req, "id"));
        } else if (!campo(req, "codigo").isEmpty()) {
            actual = dao.buscarPorCodigo(campo(req, "codigo"));
            if (actual == null) {
                req.setAttribute("error", "No existe un programa con el codigo " + campo(req, "codigo"));
            }
        }
        mostrar(req, resp, actual);
    }

    // POST: registrar, modificar o inactivar, segun el boton que se oprimio
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = campo(req, "accion");
        int id = entero(req, "id");

        if ("inactivar".equals(accion)) {
            if (dao.eliminar(id)) {
                aviso(req, "Programa inactivado", true);
            } else {
                aviso(req, "No se pudo inactivar el programa", false);
            }
            ir(req, resp, "/programas");
            return;
        }

        Programa p = new Programa();
        p.setId(id);
        p.setCodigo(campo(req, "codigo"));
        p.setNombre(campo(req, "nombre"));
        p.setNivel(campo(req, "nivel"));
        p.setVersion(campo(req, "version"));
        p.setEstado(campo(req, "estado"));
        String error = validar(req, p);
        if (error != null) {
            req.setAttribute("error", error);
            mostrar(req, resp, p);
            return;
        }

        boolean nuevo = !"modificar".equals(accion) || id == 0;
        boolean ok = nuevo ? dao.registrar(p) : dao.actualizar(p);
        if (!ok) {
            req.setAttribute("error", "No se pudo guardar. Revisa que el codigo no este repetido");
            mostrar(req, resp, p);
            return;
        }
        aviso(req, nuevo ? "Programa registrado" : "Programa modificado", true);
        ir(req, resp, "/programas");
    }

    private String validar(HttpServletRequest req, Programa p) {
        String horas = campo(req, "duracion");
        if (p.getCodigo().isEmpty() || p.getNombre().isEmpty() || p.getVersion().isEmpty() || horas.isEmpty()) {
            return "Completa el codigo, el nombre, la version y la duracion";
        }
        try {
            p.setDuracionHoras(Integer.parseInt(horas));
        } catch (NumberFormatException ex) {
            return "La duracion debe ser un numero entero";
        }
        return null;
    }

    private void mostrar(HttpServletRequest req, HttpServletResponse resp, Programa actual)
            throws ServletException, IOException {
        req.setAttribute("actual", actual);
        req.setAttribute("lista", dao.listarTodos());
        req.setAttribute("niveles", new String[]{
            "AUXILIAR", "COMPLEMENTARIA", "ESPECIALIZACION_TECNOLOGICA", "OPERARIO", "TECNICO", "TECNOLOGO"});
        req.setAttribute("estados", new String[]{"ACTIVO", "INACTIVO"});
        vista(req, resp, "programas");
    }
}
