package com.sena.cipa.web;

import com.sena.cipa.modelo.Instructor;
import com.sena.cipa.modelo.InstructorDAO;
import com.sena.cipa.modelo.InstructorDAOImpl;
import com.sena.cipa.modelo.UsuarioDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// CONTROLADOR web de instructores (mismas reglas que InstructorControlador en Swing)
@WebServlet("/instructores")
public class InstructorServlet extends ServletBase {

    private final InstructorDAO dao = new InstructorDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Instructor actual = null;
        if (!campo(req, "id").isEmpty()) {
            actual = dao.leerPorId(entero(req, "id"));
        } else if (!campo(req, "documento").isEmpty()) {
            actual = dao.buscarPorDocumento(campo(req, "documento"));
            if (actual == null) {
                req.setAttribute("error", "No existe un instructor con el documento " + campo(req, "documento"));
            }
        }
        mostrar(req, resp, actual);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = campo(req, "accion");
        int id = entero(req, "id");

        if ("inactivar".equals(accion)) {
            if (dao.eliminar(id)) {
                aviso(req, "Instructor inactivado", true);
            } else {
                aviso(req, "No se pudo inactivar el instructor", false);
            }
            ir(req, resp, "/instructores");
            return;
        }

        Instructor i = new Instructor();
        i.setId(id);
        i.setUsuarioId(entero(req, "usuarioId"));
        i.setEspecialidad(campo(req, "especialidad"));
        i.setEstado(campo(req, "estado"));
        String error = null;
        if (i.getUsuarioId() == 0) {
            error = "Primero registra un usuario con rol INSTRUCTOR";
        } else if (i.getEspecialidad().isEmpty()) {
            error = "Completa la especialidad del instructor";
        }
        if (error != null) {
            req.setAttribute("error", error);
            mostrar(req, resp, i);
            return;
        }

        boolean nuevo = !"modificar".equals(accion) || id == 0;
        boolean ok = nuevo ? dao.registrar(i) : dao.actualizar(i);
        if (!ok) {
            req.setAttribute("error", "No se pudo guardar. Revisa que ese usuario no sea ya instructor");
            mostrar(req, resp, i);
            return;
        }
        aviso(req, nuevo ? "Instructor registrado" : "Instructor modificado", true);
        ir(req, resp, "/instructores");
    }

    private void mostrar(HttpServletRequest req, HttpServletResponse resp, Instructor actual)
            throws ServletException, IOException {
        req.setAttribute("actual", actual);
        req.setAttribute("lista", dao.listarTodos());
        // la lista de usuarios se llena solo con los que tienen rol INSTRUCTOR
        req.setAttribute("usuarios", new UsuarioDAOImpl().listarPorRol("INSTRUCTOR"));
        req.setAttribute("estados", new String[]{"ACTIVO", "INACTIVO"});
        vista(req, resp, "instructores");
    }
}
