<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Instructores" scope="request"/>
<%@ include file="encabezado.jspf" %>
<div class="titulo-modulo">
    <h1>Instructores</h1>
    <form class="buscar" method="get" action="instructores">
        <input name="documento" placeholder="Buscar por documento" value="<c:out value='${filtro}'/>">
        <button class="boton" type="submit">Buscar</button>
    </form>
</div>

<form class="tarjeta formulario" method="post" action="instructores">
    <h2>${empty actual or actual.id == 0 ? 'Nuevo instructor' : 'Modificar instructor'}</h2>
    <input type="hidden" name="id" value="${empty actual ? 0 : actual.id}">
    <div class="campos">
        <label>Usuario (rol INSTRUCTOR)
            <select name="usuarioId" required>
                <c:if test="${empty usuarios}"><option value="">No hay usuarios con rol INSTRUCTOR</option></c:if>
                <c:forEach var="x" items="${usuarios}">
                    <option value="${x.id}" ${actual.usuarioId == x.id ? 'selected' : ''}><c:out value="${x.documento} - ${x.nombreCompleto}"/></option>
                </c:forEach>
            </select>
        </label>
        <label>Especialidad <input name="especialidad" type="text" value="<c:out value='${actual.especialidad}'/>" required></label>
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
                        onclick="return confirm('Seguro que quieres inactivar este instructor?')">Inactivar</button>
            </c:otherwise>
        </c:choose>
        <a class="boton" href="instructores">Limpiar</a>
    </div>
</form>

<div class="tarjeta">
    <h2>Registros guardados <span class="gris">(${lista.size()})</span></h2>
    <div class="tabla-desliza">
    <table>
        <thead><tr><th>Id</th><th>Documento</th><th>Nombre</th><th>Especialidad</th><th>Estado</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="r" items="${lista}">
            <tr class="${actual.id == r.id ? 'elegida' : ''}">
                <td>${r.id}</td>
                <td><c:out value="${r.documento}"/></td>
                <td><c:out value="${r.nombreCompleto}"/></td>
                <td><c:out value="${r.especialidad}"/></td>
                <td><span class="etiqueta ${r.estado}">${r.estado}</span></td>
                <td><a href="instructores?id=${r.id}">Editar</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</div>
<%@ include file="pie.jspf" %>
