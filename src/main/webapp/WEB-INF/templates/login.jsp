<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:if test="${model=='success'}">
	<c:redirect url="list" />
</c:if>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログインページ</title>
</head>
<body>
	<form action="login" method="POST">
		ユーザ名：<input type="text" name="name"><br> パスワード<input
			type="password" name="password">
		<button>ログイン</button>
	</form>
	<p style="color: red">${model}</p>
	<p>
		<a href="./">ホームへ戻る</a>
	</p>
</body>
</html>
