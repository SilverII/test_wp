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
    <INPUT TYPE="radio" name="login" value="EKB" ${cty == "1486209" ? 'checked' : ''}>EKB
  </td>
  <td>
    <INPUT TYPE="radio" name="login" value="MSK" ${cty == "524894" ? 'checked' : ''} ${cty == "524901" ? 'checked' : ''}>MSK
  </td>
  <td>
    <INPUT TYPE="radio" name="login" value="SPB" ${cty == "498817" ? 'checked' : ''}>SPB
  </td>
  </tr>
  <tr>
  </tr>
  <tr>
      <td>
        <INPUT TYPE="radio" name="password" value="src1" ${src == "src1" ? 'checked' : ''}>OpenWeather
      </td>
      <td>
        <INPUT TYPE="radio" name="password" value="src2" ${src == "src2" ? 'checked' : ''}>WeatherBit
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