package com.sena.cipa.web;

import com.sena.cipa.modelo.Usuario;
import com.sena.cipa.modelo.UsuarioDAO;
import com.sena.cipa.modelo.UsuarioDAOImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

// CONTROLADOR web de usuarios (mismas reglas que UsuarioControlador en Swing).
// Solo el ADMINISTRADOR puede entrar, porque aqui se crean cuentas y se cambian claves.
@WebServlet("/usuarios")
public class UsuarioServlet extends ServletBase {

    private final UsuarioDAO dao = new UsuarioDAOImpl();

    private boolean esAdministrador(HttpServletRequest req) {
        Usuario u = (Usuario) req.getSession().getAttribute("usuario");
        return u != null && "ADMINISTRADOR".equals(u.getRol());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!esAdministrador(req)) {
            aviso(req, "Solo un ADMINISTRADOR puede gestionar usuarios", false);
            ir(req, resp, "/inicio");
            return;
        }
        Usuario actual = null;
        if (!campo(req, "id").isEmpty()) {
            actual = dao.leerPorId(entero(req, "id"));
        } else if (!campo(req, "documento").isEmpty()) {
            actual = dao.buscarPorDocumento(campo(req, "documento"));
            if (actual == null) {
                req.setAttribute("error", "No existe un usuario con el documento " + campo(req, "documento"));
            }
        }
        mostrar(req, resp, actual);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!esAdministrador(req)) {
            aviso(req, "Solo un ADMINISTRADOR puede gestionar usuarios", false);
            ir(req, resp, "/inicio");
            return;
        }
        String accion = campo(req, "accion");
        int id = entero(req, "id");

        if ("inactivar".equals(accion)) {
            if (dao.eliminar(id)) {
                aviso(req, "Usuario inactivado", true);
            } else {
                aviso(req, "No se pudo inactivar el usuario", false);
            }
            ir(req, resp, "/usuarios");
            return;
        }

        boolean nuevo = !"modificar".equals(accion) || id == 0;
        Usuario u = new Usuario();
        u.setId(id);
        u.setDocumento(campo(req, "documento"));
        u.setNombreCompleto(campo(req, "nombre"));
        u.setCorreo(campo(req, "correo"));
        String telefono = campo(req, "telefono");
        u.setTelefono(telefono.isEmpty() ? null : telefono);
        u.setRol(campo(req, "rol"));
        u.setEstado(campo(req, "estado"));
        // las claves no se recortan: un espacio puede ser parte de la clave
        String clave = req.getParameter("clave") == null ? "" : req.getParameter("clave");
        String confirmar = req.getParameter("confirmar") == null ? "" : req.getParameter("confirmar");
        u.setClave(clave);

        String error = validar(u, nuevo, clave, confirmar);
        if (error != null) {
            req.setAttribute("error", error);
            mostrar(req, resp, u);
            return;
        }
        boolean ok = nuevo ? dao.registrar(u) : dao.actualizar(u);
        if (!ok) {
            req.setAttribute("error", "No se pudo guardar. Revisa que el documento y el correo no esten repetidos");
            mostrar(req, resp, u);
            return;
        }
        aviso(req, nuevo ? "Usuario registrado" : "Usuario modificado", true);
        ir(req, resp, "/usuarios");
    }

    private String validar(Usuario u, boolean nuevo, String clave, String confirmar) {
        if (u.getDocumento().isEmpty() || u.getNombreCompleto().isEmpty() || u.getCorreo().isEmpty()) {
            return "Completa el documento, el nombre y el correo";
        }
        if (!u.getCorreo().contains("@") || !u.getCorreo().contains(".")) {
            return "El correo no es valido";
        }
        if (nuevo && clave.isEmpty()) {
            return "Escribe una clave para el usuario";
        }
        if (!clave.isEmpty() && clave.length() < 6) {
            return "La clave debe tener minimo 6 caracteres";
        }
        if (!clave.equals(confirmar)) {
            return "Las claves no coinciden";
        }
        return null;
    }

    private void mostrar(HttpServletRequest req, HttpServletResponse resp, Usuario actual)
            throws ServletException, IOException {
        if (actual != null) {
            actual.setClave(null);   // la clave nunca se devuelve a la pagina
        }
        req.setAttribute("actual", actual);
        req.setAttribute("lista", dao.listarTodos());
        req.setAttribute("roles", new String[]{
            "ADMINISTRADOR", "COORDINACION_ACADEMICA", "INSTRUCTOR", "APRENDIZ", "CONSULTA_AUDITOR"});
        req.setAttribute("estados", new String[]{"ACTIVO", "INACTIVO"});
        vista(req, resp, "usuarios");
    }
}
