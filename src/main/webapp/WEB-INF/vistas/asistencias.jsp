<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Asistencias" scope="request"/>
<%@ include file="encabezado.jspf" %>
<div class="titulo-modulo">
    <h1>Asistencias</h1>
    <form class="buscar" method="get" action="asistencias">
        <input name="documento" placeholder="Documento del aprendiz" value="<c:out value='${filtro}'/>">
        <button class="boton" type="submit">Buscar</button>
    </form>
</div>

<c:if test="${not empty filtro}">
    <p class="gris">Mostrando solo los registros de <strong><c:out value="${filtro}"/></strong> &middot; <a href="asistencias">ver todos</a></p>
</c:if>
<form class="tarjeta formulario" method="post" action="asistencias">
    <h2>${empty actual or actual.id == 0 ? 'Nueva asistencia' : 'Modificar asistencia'}</h2>
    <input type="hidden" name="id" value="${empty actual ? 0 : actual.id}">
    <div class="campos">
        <label>Sesion
            <select name="sesionId" required>
                <c:if test="${empty sesiones}"><option value="">No hay sesiones</option></c:if>
                <c:forEach var="x" items="${sesiones}">
                    <option value="${x.id}" ${actual.sesionId == x.id ? 'selected' : ''}><c:out value="${x.numeroFicha} - ${x.fecha} ${x.tema}"/></option>
                </c:forEach>
            </select>
        </label>
        <label>Matricula (aprendiz y ficha)
            <select name="matriculaId" required>
                <c:if test="${empty matriculas}"><option value="">No hay matriculas activas</option></c:if>
                <c:forEach var="x" items="${matriculas}">
                    <option value="${x.id}" ${actual.matriculaId == x.id ? 'selected' : ''}><c:out value="${x.nombreAprendiz} (${x.numeroFicha})"/></option>
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
        <label>Observacion (opcional) <input name="observacion" type="text" value="<c:out value='${actual.observacion}'/>"></label>
    </div>
    <div class="botones">
        <c:choose>
            <c:when test="${empty actual or actual.id == 0}">
                <button class="boton principal" name="accion" value="registrar">Registrar</button>
            </c:when>
            <c:otherwise>
                <button class="boton principal" name="accion" value="modificar">Guardar cambios</button>
                <button class="boton peligro" name="accion" value="eliminar" formnovalidate
                        onclick="return confirm('Seguro que quieres eliminar esta asistencia?')">Eliminar</button>
            </c:otherwise>
        </c:choose>
        <a class="boton" href="asistencias">Limpiar</a>
    </div>
</form>

<div class="tarjeta">
    <h2>Registros guardados <span class="gris">(${lista.size()})</span></h2>
    <div class="tabla-desliza">
    <table>
        <thead><tr><th>Id</th><th>Sesion</th><th>Aprendiz</th><th>Ficha</th><th>Estado</th><th>Observacion</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="r" items="${lista}">
            <tr class="${actual.id == r.id ? 'elegida' : ''}">
                <td>${r.id}</td>
                <td><c:out value="${r.fechaSesion} ${r.temaSesion}"/></td>
                <td><c:out value="${r.nombreAprendiz}"/></td>
                <td><c:out value="${r.numeroFicha}"/></td>
                <td>${r.estado}</td>
                <td><c:out value="${r.observacion}"/></td>
                <td><a href="asistencias?id=${r.id}">Editar</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</div>
<%@ include file="pie.jspf" %>
