<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
  <title>Settings</title>
</head>
<body>

<h2 align="center">Settings</h2>
<form:form method="post" action="save">
<table align="center">
 <tr>
  <td>
    <INPUT TYPE="radio" name="login" value="EKB">EKB
  </td>
  <td>
    <INPUT TYPE="radio" name="login" value="MSK">MSK
  </td>
  <td>
    <INPUT TYPE="radio" name="login" value="SPB">SPB
  </td>
  </tr>
  <tr>
  </tr>
  <tr>
      <td>
        <INPUT TYPE="radio" name="password" value="src1">OpenWeather
      </td>
      <td>
        <INPUT TYPE="radio" name="password" value="src2">WeatherBit
      </td>
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