package com.sena.cipa.web;

import com.sena.cipa.modelo.Bd;
import com.sena.cipa.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.mindrot.jbcrypt.BCrypt;

// CONTROLADOR del inicio de sesion: compara la clave escrita con la cifrada (BCrypt) de la base.
@WebServlet("/login")
public class LoginServlet extends ServletBase {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        vista(req, resp, "login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String documento = campo(req, "documento");
        String clave = req.getParameter("clave") == null ? "" : req.getParameter("clave");

        // se trae la clave cifrada solo aqui; UsuarioDAOImpl nunca la saca de la base
        String cifrada = Bd.uno("SELECT clave FROM usuarios WHERE documento = ? AND estado = 'ACTIVO'",
                rs -> rs.getString("clave"), documento);

        if (cifrada == null || clave.isEmpty() || !BCrypt.checkpw(clave, cifrada)) {
            req.setAttribute("error", "Documento o clave incorrectos");
            req.setAttribute("documento", documento);
            vista(req, resp, "login");
            return;
        }
        Usuario u = Bd.uno("SELECT id, nombre_completo, rol FROM usuarios WHERE documento = ?", rs -> {
            Usuario x = new Usuario();
            x.setId(rs.getInt("id"));
            x.setDocumento(documento);
            x.setNombreCompleto(rs.getString("nombre_completo"));
            x.setRol(rs.getString("rol"));
            return x;
        }, documento);

        req.getSession().invalidate();   // sesion nueva al entrar
        req.getSession(true).setAttribute("usuario", u);
        ir(req, resp, "/inicio");
    }
}
