<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Usuarios" scope="request"/>
<%@ include file="encabezado.jspf" %>
<div class="titulo-modulo">
    <h1>Registro de usuarios</h1>
    <form class="buscar" method="get" action="usuarios">
        <input name="documento" placeholder="Buscar por documento">
        <button class="boton" type="submit">Buscar</button>
    </form>
</div>

<form class="tarjeta formulario" method="post" action="usuarios" autocomplete="off">
    <h2>${empty actual or actual.id == 0 ? 'Nuevo usuario' : 'Modificar usuario'}</h2>
    <input type="hidden" name="id" value="${empty actual ? 0 : actual.id}">
    <div class="campos">
        <label>Documento <input name="documento" value="<c:out value='${actual.documento}'/>" required></label>
        <label>Nombre completo <input name="nombre" value="<c:out value='${actual.nombreCompleto}'/>" required></label>
        <label>Correo <input name="correo" type="email" value="<c:out value='${actual.correo}'/>" required></label>
        <label>Telefono (opcional) <input name="telefono" value="<c:out value='${actual.telefono}'/>"></label>
        <label>Clave ${empty actual or actual.id == 0 ? '' : '(vacia = no cambiarla)'}
            <input name="clave" type="password" minlength="6" autocomplete="new-password">
        </label>
        <label>Confirmar clave <input name="confirmar" type="password" autocomplete="new-password"></label>
        <label>Rol
            <select name="rol">
                <c:forEach var="r" items="${roles}">
                    <option ${actual.rol == r ? 'selected' : ''}>${r}</option>
                </c:forEach>
            </select>
        </label>
        <label>Estado
            <select name="estado">
                <c:forEach var="e" items="${estados}">
                    <option ${actual.estado == e ? 'selected' : ''}>${e}</option>
                </c:forEach>
            </select>
        </label>
    </div>
    <div class="botones">
        <c:choose>
            <c:when test="${empty actual or actual.id == 0}">
                <button class="boton principal" name="accion" value="registrar">Registrar</button>
            </c:when>
            <c:otherwise>
                <button class="boton principal" name="accion" value="modificar">Guardar cambios</button>
                <button class="boton peligro" name="accion" value="inactivar" formnovalidate
                        onclick="return confirm('Seguro que quieres inactivar este usuario?')">Inactivar</button>
            </c:otherwise>
        </c:choose>
        <a class="boton" href="usuarios">Limpiar</a>
    </div>
</form>

<div class="tarjeta">
    <h2>Registros guardados <span class="gris">(${lista.size()})</span></h2>
    <div class="tabla-desliza">
    <table>
        <thead><tr><th>Id</th><th>Documento</th><th>Nombre completo</th><th>Correo</th><th>Rol</th><th>Estado</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="u" items="${lista}">
            <tr class="${actual.id == u.id ? 'elegida' : ''}">
                <td>${u.id}</td>
                <td><c:out value="${u.documento}"/></td>
                <td><c:out value="${u.nombreCompleto}"/></td>
                <td><c:out value="${u.correo}"/></td>
                <td>${u.rol}</td>
                <td><span class="etiqueta ${u.estado}">${u.estado}</span></td>
                <td><a href="usuarios?id=${u.id}">Editar</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</div>
<%@ include file="pie.jspf" %>
