<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Matriculas" scope="request"/>
<%@ include file="encabezado.jspf" %>
<div class="titulo-modulo">
    <h1>Matriculas</h1>
    <form class="buscar" method="get" action="matriculas">
        <input name="documento" placeholder="Documento del aprendiz" value="<c:out value='${filtro}'/>">
        <button class="boton" type="submit">Buscar</button>
    </form>
</div>

<c:if test="${not empty filtro}">
    <p class="gris">Mostrando solo los registros de <strong><c:out value="${filtro}"/></strong> &middot; <a href="matriculas">ver todos</a></p>
</c:if>
<form class="tarjeta formulario" method="post" action="matriculas">
    <h2>${empty actual or actual.id == 0 ? 'Nueva matricula' : 'Modificar matricula'}</h2>
    <input type="hidden" name="id" value="${empty actual ? 0 : actual.id}">
    <div class="campos">
        <label>Aprendiz
            <select name="aprendizId" required>
                <c:if test="${empty aprendices}"><option value="">No hay aprendices activos</option></c:if>
                <c:forEach var="x" items="${aprendices}">
                    <option value="${x.id}" ${actual.aprendizId == x.id ? 'selected' : ''}><c:out value="${x.documento} - ${x.nombreCompleto}"/></option>
                </c:forEach>
            </select>
        </label>
        <label>Ficha
            <select name="fichaId" required>
                <c:if test="${empty fichas}"><option value="">No hay fichas en ejecucion</option></c:if>
                <c:forEach var="x" items="${fichas}">
                    <option value="${x.id}" ${actual.fichaId == x.id ? 'selected' : ''}><c:out value="${x.numero} - ${x.nombrePrograma}"/></option>
                </c:forEach>
            </select>
        </label>
        <label>Fecha de matricula <input name="fechaMatricula" type="date" value="<c:out value='${actual.fechaMatricula}'/>" required></label>
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
                <button class="boton peligro" name="accion" value="cancelar" formnovalidate
                        onclick="return confirm('Seguro que quieres cancelar esta matricula?')">Cancelar matricula</button>
            </c:otherwise>
        </c:choose>
        <a class="boton" href="matriculas">Limpiar</a>
    </div>
</form>

<div class="tarjeta">
    <h2>Registros guardados <span class="gris">(${lista.size()})</span></h2>
    <div class="tabla-desliza">
    <table>
        <thead><tr><th>Id</th><th>Aprendiz</th><th>Documento</th><th>Ficha</th><th>Fecha</th><th>Estado</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="r" items="${lista}">
            <tr class="${actual.id == r.id ? 'elegida' : ''}">
                <td>${r.id}</td>
                <td><c:out value="${r.nombreAprendiz}"/></td>
                <td><c:out value="${r.documentoAprendiz}"/></td>
                <td><c:out value="${r.numeroFicha}"/></td>
                <td>${r.fechaMatricula}</td>
                <td><span class="etiqueta ${r.estado}">${r.estado}</span></td>
                <td><a href="matriculas?id=${r.id}">Editar</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</div>
<%@ include file="pie.jspf" %>
