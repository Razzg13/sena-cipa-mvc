<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Sesiones" scope="request"/>
<%@ include file="encabezado.jspf" %>
<div class="titulo-modulo">
    <h1>Sesiones de formacion</h1>
    <form class="buscar" method="get" action="sesiones">
        <input name="tema" placeholder="Buscar por tema" value="<c:out value='${filtro}'/>">
        <button class="boton" type="submit">Buscar</button>
    </form>
</div>

<form class="tarjeta formulario" method="post" action="sesiones">
    <h2>${empty actual or actual.id == 0 ? 'Nueva sesion' : 'Modificar sesion'}</h2>
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
        <label>Fecha <input name="fecha" type="date" value="<c:out value='${actual.fecha}'/>" required></label>
        <label>Hora de inicio <input name="horaInicio" type="time" value="<c:out value='${actual.horaInicio}'/>" required></label>
        <label>Hora de fin <input name="horaFin" type="time" value="<c:out value='${actual.horaFin}'/>" required></label>
        <label>Tema <input name="tema" type="text" value="<c:out value='${actual.tema}'/>" required></label>
    </div>
    <div class="botones">
        <c:choose>
            <c:when test="${empty actual or actual.id == 0}">
                <button class="boton principal" name="accion" value="registrar">Registrar</button>
            </c:when>
            <c:otherwise>
                <button class="boton principal" name="accion" value="modificar">Guardar cambios</button>
                <button class="boton peligro" name="accion" value="eliminar" formnovalidate
                        onclick="return confirm('Seguro que quieres eliminar esta sesion?')">Eliminar</button>
            </c:otherwise>
        </c:choose>
        <a class="boton" href="sesiones">Limpiar</a>
    </div>
</form>

<div class="tarjeta">
    <h2>Registros guardados <span class="gris">(${lista.size()})</span></h2>
    <div class="tabla-desliza">
    <table>
        <thead><tr><th>Id</th><th>Ficha</th><th>Fecha</th><th>Inicio</th><th>Fin</th><th>Tema</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="r" items="${lista}">
            <tr class="${actual.id == r.id ? 'elegida' : ''}">
                <td>${r.id}</td>
                <td><c:out value="${r.numeroFicha}"/></td>
                <td>${r.fecha}</td>
                <td>${r.horaInicio}</td>
                <td>${r.horaFin}</td>
                <td><c:out value="${r.tema}"/></td>
                <td><a href="sesiones?id=${r.id}">Editar</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</div>
<%@ include file="pie.jspf" %>
