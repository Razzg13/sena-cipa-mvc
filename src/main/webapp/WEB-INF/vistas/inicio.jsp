<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="titulo" value="Inicio" scope="request"/>
<%@ include file="encabezado.jspf" %>
<h1>Bienvenido, <c:out value="${sessionScope.usuario.nombreCompleto}"/></h1>
<c:choose>
    <c:when test="${conectada}">
        <p class="estado-bd ok">Conectado a la base de datos <strong><c:out value="${bd}"/></strong></p>
    </c:when>
    <c:otherwise>
        <p class="estado-bd mal">No hay conexion con la base de datos <strong><c:out value="${bd}"/></strong>:
            <c:out value="${errorBd}"/></p>
    </c:otherwise>
</c:choose>
<div class="rejilla">
    <c:forEach var="m" items="${modulos}">
        <c:choose>
            <c:when test="${m.value[2] == 'si' and conectada}">
                <a class="modulo" href="${pageContext.request.contextPath}/${m.key}">
                    <strong><c:out value="${m.value[0]}"/></strong>
                    <span><c:out value="${m.value[1]}"/></span>
                </a>
            </c:when>
            <c:otherwise>
                <div class="modulo apagado" title="${m.value[2] == 'si' ? 'Sin conexion a la base' : 'En construccion para la version web'}">
                    <strong><c:out value="${m.value[0]}"/></strong>
                    <span>${m.value[2] == 'si' ? 'Sin conexion' : 'Proximamente'}</span>
                </div>
            </c:otherwise>
        </c:choose>
    </c:forEach>
</div>
<%@ include file="pie.jspf" %>
