<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
  <title>Log in</title>
</head>
<body>

<h2 align="center">Log In</h2>
<form:form method="post" action="main">
  <table align="center">
    <tr>
      <td><form:label path="login">Login</form:label></td>
      <td><form:input path="login" /></td>
    </tr>
    <tr>
      <td><form:label path="password">Password</form:label></td>
      <td><form:input path="password" /></td>
    </tr>
    <tr>
      <td colspan="2">
        <input type="submit" value="Submit"/>
      </td>
    </tr>
  </table>
</form:form>
</body>
</html>