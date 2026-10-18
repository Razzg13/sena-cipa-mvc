package com.sena.cipa.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// Toda pagina pide haber iniciado sesion, menos el login y los archivos de estilo/imagenes.
// Tambien deja todo en UTF-8 para que las tildes y la n con virgulilla lleguen bien.
@WebFilter("/*")
public class FiltroSesion implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String ruta = req.getRequestURI().substring(req.getContextPath().length());
        boolean libre = ruta.equals("/login") || ruta.startsWith("/css/") || ruta.startsWith("/img/");
        HttpSession sesion = req.getSession(false);
        boolean conectado = sesion != null && sesion.getAttribute("usuario") != null;

        if (libre || conectado) {
            chain.doFilter(request, response);
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
