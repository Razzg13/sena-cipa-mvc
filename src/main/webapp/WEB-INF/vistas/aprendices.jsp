<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Aprendices" scope="request"/>
<%@ include file="encabezado.jspf" %>
<div class="titulo-modulo">
    <h1>Aprendices</h1>
    <form class="buscar" method="get" action="aprendices">
        <input name="documento" placeholder="Buscar por documento" value="<c:out value='${filtro}'/>">
        <button class="boton" type="submit">Buscar</button>
    </form>
</div>

<form class="tarjeta formulario" method="post" action="aprendices">
    <h2>${empty actual or actual.id == 0 ? 'Nuevo aprendiz' : 'Modificar aprendiz'}</h2>
    <input type="hidden" name="id" value="${empty actual ? 0 : actual.id}">
    <div class="campos">
        <label>Usuario (rol APRENDIZ)
            <select name="usuarioId" required>
                <c:if test="${empty usuarios}"><option value="">No hay usuarios con rol APRENDIZ</option></c:if>
                <c:forEach var="x" items="${usuarios}">
                    <option value="${x.id}" ${actual.usuarioId == x.id ? 'selected' : ''}><c:out value="${x.documento} - ${x.nombreCompleto}"/></option>
                </c:forEach>
            </select>
        </label>
        <label>Estado formativo
            <select name="estadoFormativo">
                <c:forEach var="x" items="${estadosFormativos}">
                    <option ${actual.estadoFormativo == x ? 'selected' : ''}>${x}</option>
                </c:forEach>
            </select>
        </label>
        <label>Estado
            <select name="estado">
                <c:forEach var="x" items="${estados}">
                    <option ${actual.estado == x ? 'selected' : ''}>${x}</option>
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
                        onclick="return confirm('Seguro que quieres inactivar este aprendiz?')">Inactivar</button>
            </c:otherwise>
        </c:choose>
        <a class="boton" href="aprendices">Limpiar</a>
    </div>
</form>

<div class="tarjeta">
    <h2>Registros guardados <span class="gris">(${lista.size()})</span></h2>
    <div class="tabla-desliza">
    <table>
        <thead><tr><th>Id</th><th>Documento</th><th>Nombre</th><th>Estado formativo</th><th>Estado</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="r" items="${lista}">
            <tr class="${actual.id == r.id ? 'elegida' : ''}">
                <td>${r.id}</td>
                <td><c:out value="${r.documento}"/></td>
                <td><c:out value="${r.nombreCompleto}"/></td>
                <td>${r.estadoFormativo}</td>
                <td><span class="etiqueta ${r.estado}">${r.estado}</span></td>
                <td><a href="aprendices?id=${r.id}">Editar</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</div>
<%@ include file="pie.jspf" %>
