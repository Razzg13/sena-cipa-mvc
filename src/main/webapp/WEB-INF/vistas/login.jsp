<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>SENA CIPA - Iniciar sesion</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilo.css">
    <link rel="icon" href="${pageContext.request.contextPath}/img/logo_sena.png">
</head>
<body class="fondo-login">
<form class="tarjeta login" method="post" action="${pageContext.request.contextPath}/login">
    <img src="${pageContext.request.contextPath}/img/logo_sena.png" alt="SENA" class="logo-login">
    <h1>Sistema Academico CIPA</h1>
    <p class="gris">Ingresa con tu documento y tu clave</p>
    <c:if test="${not empty error}">
        <div class="aviso mal"><c:out value="${error}"/></div>
    </c:if>
    <label>Documento
        <input name="documento" value="<c:out value='${documento}'/>" required autofocus>
    </label>
    <label>Clave
        <input name="clave" type="password" required>
    </label>
    <button class="boton principal" type="submit">Entrar</button>
</form>
</body>
</html>
