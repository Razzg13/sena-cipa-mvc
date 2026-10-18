<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Asignaciones" scope="request"/>
<%@ include file="encabezado.jspf" %>
<div class="titulo-modulo">
    <h1>Asignaciones de instructor</h1>
    <form class="buscar" method="get" action="asignaciones">
        <input name="ficha" placeholder="Numero de ficha" value="<c:out value='${filtro}'/>">
        <button class="boton" type="submit">Buscar</button>
    </form>
</div>

<c:if test="${not empty filtro}">
    <p class="gris">Mostrando solo los registros de <strong><c:out value="${filtro}"/></strong> &middot; <a href="asignaciones">ver todos</a></p>
</c:if>
<form class="tarjeta formulario" method="post" action="asignaciones">
    <h2>${empty actual or actual.id == 0 ? 'Nueva asignacion' : 'Modificar asignacion'}</h2>
    <input type="hidden" name="id" value="${empty actual ? 0 : actual.id}">
    <div class="campos">
        <label>Ficha
            <select name="fichaId" required>
                <c:if test="${empty fichas}"><option value="">No hay fichas</option></c:if>
                <c:forEach var="x" items="${fichas}">
                    <option value="${x.id}" ${actual.fichaId == x.id ? 'selected' : ''}><c:out value="${x.numero} - ${x.nombrePrograma}"/></option>
                </c:forEach>
            </select>
        </label>
        <label>Instructor
            <select name="instructorId" required>
                <c:if test="${empty instructores}"><option value="">No hay instructores activos</option></c:if>
                <c:forEach var="x" items="${instructores}">
                    <option value="${x.id}" ${actual.instructorId == x.id ? 'selected' : ''}><c:out value="${x.documento} - ${x.nombreCompleto}"/></option>
                </c:forEach>
            </select>
        </label>
        <label>Fecha de inicio <input name="fechaInicio" type="date" value="<c:out value='${actual.fechaInicio}'/>" required></label>
        <label>Fecha de fin <input name="fechaFin" type="date" value="<c:out value='${actual.fechaFin}'/>" required></label>
    </div>
    <div class="botones">
        <c:choose>
            <c:when test="${empty actual or actual.id == 0}">
                <button class="boton principal" name="accion" value="registrar">Registrar</button>
            </c:when>
            <c:otherwise>
                <button class="boton principal" name="accion" value="modificar">Guardar cambios</button>
                <button class="boton peligro" name="accion" value="eliminar" formnovalidate
                        onclick="return confirm('Seguro que quieres eliminar esta asignacion?')">Eliminar</button>
            </c:otherwise>
        </c:choose>
        <a class="boton" href="asignaciones">Limpiar</a>
    </div>
</form>

<div class="tarjeta">
    <h2>Registros guardados <span class="gris">(${lista.size()})</span></h2>
    <div class="tabla-desliza">
    <table>
        <thead><tr><th>Id</th><th>Ficha</th><th>Instructor</th><th>Desde</th><th>Hasta</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="r" items="${lista}">
            <tr class="${actual.id == r.id ? 'elegida' : ''}">
                <td>${r.id}</td>
                <td><c:out value="${r.numeroFicha}"/></td>
                <td><c:out value="${r.nombreInstructor}"/></td>
                <td>${r.fechaInicio}</td>
                <td>${r.fechaFin}</td>
                <td><a href="asignaciones?id=${r.id}">Editar</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</div>
<%@ include file="pie.jspf" %>
