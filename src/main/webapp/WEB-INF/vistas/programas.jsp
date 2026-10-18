<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Programas de formacion" scope="request"/>
<%@ include file="encabezado.jspf" %>
<div class="titulo-modulo">
    <h1>Programas de formacion</h1>
    <form class="buscar" method="get" action="programas">
        <input name="codigo" placeholder="Buscar por codigo">
        <button class="boton" type="submit">Buscar</button>
    </form>
</div>

<form class="tarjeta formulario" method="post" action="programas">
    <h2>${empty actual or actual.id == 0 ? 'Nuevo programa' : 'Modificar programa'}</h2>
    <input type="hidden" name="id" value="${empty actual ? 0 : actual.id}">
    <div class="campos">
        <label>Codigo <input name="codigo" value="<c:out value='${actual.codigo}'/>" required></label>
        <label>Nombre <input name="nombre" value="<c:out value='${actual.nombre}'/>" required></label>
        <label>Nivel
            <select name="nivel">
                <c:forEach var="n" items="${niveles}">
                    <option ${actual.nivel == n ? 'selected' : ''}>${n}</option>
                </c:forEach>
            </select>
        </label>
        <label>Version <input name="version" value="<c:out value='${actual.version}'/>" required></label>
        <label>Duracion (horas)
            <input name="duracion" type="number" min="1" value="${empty actual or actual.duracionHoras == 0 ? '' : actual.duracionHoras}" required>
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
                        onclick="return confirm('Seguro que quieres inactivar este programa?')">Inactivar</button>
            </c:otherwise>
        </c:choose>
        <a class="boton" href="programas">Limpiar</a>
    </div>
</form>

<div class="tarjeta">
    <h2>Registros guardados <span class="gris">(${lista.size()})</span></h2>
    <div class="tabla-desliza">
    <table>
        <thead><tr><th>Id</th><th>Codigo</th><th>Nombre</th><th>Nivel</th><th>Version</th><th>Horas</th><th>Estado</th><th></th></tr></thead>
        <tbody>
        <c:forEach var="p" items="${lista}">
            <tr class="${actual.id == p.id ? 'elegida' : ''}">
                <td>${p.id}</td>
                <td><c:out value="${p.codigo}"/></td>
                <td><c:out value="${p.nombre}"/></td>
                <td>${p.nivel}</td>
                <td><c:out value="${p.version}"/></td>
                <td>${p.duracionHoras}</td>
                <td><span class="etiqueta ${p.estado}">${p.estado}</span></td>
                <td><a href="programas?id=${p.id}">Editar</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    </div>
</div>
<%@ include file="pie.jspf" %>
