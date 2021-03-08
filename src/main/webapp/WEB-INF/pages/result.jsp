<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
  <title>Weather Info</title>
</head>
<body>
<%response.setHeader("Refresh", "180;URL=/WP/auth");%>
<h2 align="center">Weather Information</h2>
<table align="center">
  <tr>
    <td>Temperature</td>
    <td>${temp}</td>
  </tr>
  <tr>
  <tr>
    <td>Wind</td>
    <td>${wind}</td>
  </tr>
  <tr>
     <td>Clouds</td>
     <td>${clouds}</td>
  </tr>
</table>
<h3 align="center"><a href="settings">Settings</a></h3>
</body>
</html>