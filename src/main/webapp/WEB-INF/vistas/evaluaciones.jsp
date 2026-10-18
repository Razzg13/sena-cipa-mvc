<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Evaluaciones" scope="request"/>
<%@ include file="encabezado.jspf" %>
<div class="titulo-modulo">
    <h1>Evaluaciones</h1>
    <form class="buscar" method="get" action="evaluaciones">
        <input name="documento" placeholder="Documento del aprendiz" value="<c:out value='${filtro}'/>">
        <button class="boton" type="submit">Buscar</button>
    </form>
</div>

<c:if test="${not empty filtro}">
    <p class="gris">Mostrando solo los registros de <strong><c:out value="${filtro}"/></strong> &middot; <a href="evaluaciones">ver todos</a></p>
</c:if>
<form class="tarjeta formulario" method="post" action="evaluaciones">
    <h2>${empty actual or actual.id == 0 ? 'Nueva evaluacion' : 'Modificar evaluacion'}</h2>
    <input type="hidden" name="id" value="${empty actual ? 0 : actual.id}">
    <div class="campos">
        <label>Actividad
            <select name="actividadId" required>
                <c:if test="${empty actividades}"><option value="">No hay actividades</option></c:if>
                <c:forEach var="x" items="${actividades}">
                    <option value="${x.id}" ${actual.actividadId == x.id ? 'selected' : ''}><c:out value="${x.nombre} (${x.numeroFicha})"/></option>
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
        <label>Calificacion de 0.00 a 5.00 (opcional) <input name="calificacion" type="text" value="<c:out value='${actual.calificacion}'/>"></label>
        <label>Juicio
            <select name="juicio">
                <c:forEach var="x" items="${juicios}">
                    <option ${actual.juicio == x ? 'selected' : ''}>${x}</option>
                </c:forEach>
            </select>
        </label>
        <label>Retroalimentacion (opcional) <input name="retroalimentacion" type="text" value="<c:out value='${actual.retroalimentacion}'/>"></label>
        <label>Fecha de evaluacion <input name="fechaEvaluacion" type="date" value="<c:out value='${actual.fechaEvaluacion}'/>" required></label>
    </div>
    <div class="botones">
        <c:choose>
            <c:when test="${empty actual or actual.id == 0}">
                <button class="boton principal" name="accion" value="registrar">Registrar</button>
            </c:when>
            <c:otherwise>
                <button class="boton principal" name="accion" value="modificar">Guardar cambios</button>
                <button class="boton peligro" name="accion" value="eliminar" formnovalidate
                        onclick="return confirm('Seguro que quieres eliminar esta evaluacion?')">Eliminar</button>
            </c:otherwise>
        </c:choose>
        <a class="boton" href="evaluaciones">Limpiar</a>
    </div>
</form>

<div class="tarjeta">
    <h2>Registros guardados <span class="gris">(${lista.size()})</span></h2>
    <div class="tabla-desliza">
    <table>
        <thead><tr><th>Id</th><th>Actividad</th><th>Aprendiz</th><th>Ficha</th><th>Nota</th><th>Juicio</th><th>Fecha</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="r" items="${lista}">
            <tr class="${actual.id == r.id ? 'elegida' : ''}">
                <td>${r.id}</td>
                <td><c:out value="${r.nombreActividad}"/></td>
                <td><c:out value="${r.nombreAprendiz}"/></td>
                <td><c:out value="${r.numeroFicha}"/></td>
                <td><c:out value="${r.calificacion}"/></td>
                <td>${r.juicio}</td>
                <td>${r.fechaEvaluacion}</td>
                <td><a href="evaluaciones?id=${r.id}">Editar</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</div>
<%@ include file="pie.jspf" %>
