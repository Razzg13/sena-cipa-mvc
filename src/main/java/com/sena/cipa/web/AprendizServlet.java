package com.sena.cipa.web;

import com.sena.cipa.modelo.Aprendiz;
import com.sena.cipa.modelo.AprendizDAO;
import com.sena.cipa.modelo.AprendizDAOImpl;
import com.sena.cipa.modelo.UsuarioDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// CONTROLADOR web de aprendices (mismas reglas que AprendizControlador en Swing)
@WebServlet("/aprendices")
public class AprendizServlet extends ServletBase {

    private final AprendizDAO dao = new AprendizDAOImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Aprendiz actual = null;
        if (!campo(req, "id").isEmpty()) {
            actual = dao.leerPorId(entero(req, "id"));
        } else if (!campo(req, "documento").isEmpty()) {
            actual = dao.buscarPorDocumento(campo(req, "documento"));
            if (actual == null) {
                req.setAttribute("error", "No existe un aprendiz con el documento " + campo(req, "documento"));
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
                aviso(req, "Aprendiz inactivado", true);
            } else {
                aviso(req, "No se pudo inactivar el aprendiz", false);
            }
            ir(req, resp, "/aprendices");
            return;
        }

        Aprendiz i = new Aprendiz();
        i.setId(id);
        i.setUsuarioId(entero(req, "usuarioId"));
        i.setEstadoFormativo(campo(req, "estadoFormativo"));
        i.setEstado(campo(req, "estado"));
        String error = null;
        if (i.getUsuarioId() == 0) {
            error = "Primero registra un usuario con rol APRENDIZ";
        }
        if (error != null) {
            req.setAttribute("error", error);
            mostrar(req, resp, i);
            return;
        }

        boolean nuevo = !"modificar".equals(accion) || id == 0;
        boolean ok = nuevo ? dao.registrar(i) : dao.actualizar(i);
        if (!ok) {
            req.setAttribute("error", "No se pudo guardar. Revisa que ese usuario no sea ya aprendiz");
            mostrar(req, resp, i);
            return;
        }
        aviso(req, nuevo ? "Aprendiz registrado" : "Aprendiz modificado", true);
        ir(req, resp, "/aprendices");
    }

    private void mostrar(HttpServletRequest req, HttpServletResponse resp, Aprendiz actual)
            throws ServletException, IOException {
        req.setAttribute("actual", actual);
        req.setAttribute("lista", dao.listarTodos());
        // la lista de usuarios se llena solo con los que tienen rol APRENDIZ
        req.setAttribute("usuarios", new UsuarioDAOImpl().listarPorRol("APRENDIZ"));
        req.setAttribute("estadosFormativos", new String[]{"EN_FORMACION", "APLAZADO", "RETIRADO", "CERTIFICADO"});
        req.setAttribute("estados", new String[]{"ACTIVO", "INACTIVO"});
        vista(req, resp, "aprendices");
    }
}
