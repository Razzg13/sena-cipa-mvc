<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Actividades" scope="request"/>
<%@ include file="encabezado.jspf" %>
<div class="titulo-modulo">
    <h1>Actividades evaluativas</h1>
    <form class="buscar" method="get" action="actividades">
        <input name="nombre" placeholder="Buscar por nombre" value="<c:out value='${filtro}'/>">
        <button class="boton" type="submit">Buscar</button>
    </form>
</div>

<form class="tarjeta formulario" method="post" action="actividades">
    <h2>${empty actual or actual.id == 0 ? 'Nueva actividad' : 'Modificar actividad'}</h2>
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
        <label>Nombre <input name="nombre" type="text" value="<c:out value='${actual.nombre}'/>" required></label>
        <label>Descripcion (opcional) <input name="descripcion" type="text" value="<c:out value='${actual.descripcion}'/>"></label>
        <label>Fecha limite <input name="fechaLimite" type="date" value="<c:out value='${actual.fechaLimite}'/>" required></label>
    </div>
    <div class="botones">
        <c:choose>
            <c:when test="${empty actual or actual.id == 0}">
                <button class="boton principal" name="accion" value="registrar">Registrar</button>
            </c:when>
            <c:otherwise>
                <button class="boton principal" name="accion" value="modificar">Guardar cambios</button>
                <button class="boton peligro" name="accion" value="eliminar" formnovalidate
                        onclick="return confirm('Seguro que quieres eliminar esta actividad?')">Eliminar</button>
            </c:otherwise>
        </c:choose>
        <a class="boton" href="actividades">Limpiar</a>
    </div>
</form>

<div class="tarjeta">
    <h2>Registros guardados <span class="gris">(${lista.size()})</span></h2>
    <div class="tabla-desliza">
    <table>
        <thead><tr><th>Id</th><th>Ficha</th><th>Nombre</th><th>Descripcion</th><th>Fecha limite</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="r" items="${lista}">
            <tr class="${actual.id == r.id ? 'elegida' : ''}">
                <td>${r.id}</td>
                <td><c:out value="${r.numeroFicha}"/></td>
                <td><c:out value="${r.nombre}"/></td>
                <td><c:out value="${r.descripcion}"/></td>
                <td>${r.fechaLimite}</td>
                <td><a href="actividades?id=${r.id}">Editar</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</div>
<%@ include file="pie.jspf" %>
