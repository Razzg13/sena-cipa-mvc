<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Fichas" scope="request"/>
<%@ include file="encabezado.jspf" %>
<div class="titulo-modulo">
    <h1>Fichas de formacion</h1>
    <form class="buscar" method="get" action="fichas">
        <input name="numero" placeholder="Buscar por numero">
        <button class="boton" type="submit">Buscar</button>
    </form>
</div>

<form class="tarjeta formulario" method="post" action="fichas">
    <h2>${empty actual or actual.id == 0 ? 'Nueva ficha' : 'Modificar ficha'}</h2>
    <input type="hidden" name="id" value="${empty actual ? 0 : actual.id}">
    <div class="campos">
        <label>Numero de ficha <input name="numero" value="<c:out value='${actual.numero}'/>" required></label>
        <label>Programa
            <select name="programaId" required>
                <c:if test="${empty programas}"><option value="">No hay programas activos</option></c:if>
                <c:forEach var="p" items="${programas}">
                    <option value="${p.id}" ${actual.programaId == p.id ? 'selected' : ''}><c:out value="${p.codigo} - ${p.nombre}"/></option>
                </c:forEach>
            </select>
        </label>
        <label>Jornada
            <select name="jornada">
                <c:forEach var="j" items="${jornadas}">
                    <option ${actual.jornada == j ? 'selected' : ''}>${j}</option>
                </c:forEach>
            </select>
        </label>
        <label>Fecha de inicio <input name="fechaInicio" type="date" value="<c:out value='${actual.fechaInicio}'/>" required></label>
        <label>Fecha de fin <input name="fechaFin" type="date" value="<c:out value='${actual.fechaFin}'/>" required></label>
        <label>Ambiente <input name="ambiente" value="<c:out value='${actual.ambiente}'/>" required></label>
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
                <button class="boton peligro" name="accion" value="cancelar" formnovalidate
                        onclick="return confirm('Seguro que quieres cancelar esta ficha?')">Cancelar ficha</button>
            </c:otherwise>
        </c:choose>
        <a class="boton" href="fichas">Limpiar</a>
    </div>
</form>

<div class="tarjeta">
    <h2>Registros guardados <span class="gris">(${lista.size()})</span></h2>
    <div class="tabla-desliza">
    <table>
        <thead><tr><th>Id</th><th>Numero</th><th>Programa</th><th>Jornada</th><th>Inicio</th><th>Fin</th><th>Ambiente</th><th>Estado</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="f" items="${lista}">
            <tr class="${actual.id == f.id ? 'elegida' : ''}">
                <td>${f.id}</td>
                <td><c:out value="${f.numero}"/></td>
                <td><c:out value="${f.nombrePrograma}"/></td>
                <td>${f.jornada}</td>
                <td>${f.fechaInicio}</td>
                <td>${f.fechaFin}</td>
                <td><c:out value="${f.ambiente}"/></td>
                <td><span class="etiqueta ${f.estado}">${f.estado}</span></td>
                <td><a href="fichas?id=${f.id}">Editar</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</div>
<%@ include file="pie.jspf" %>
