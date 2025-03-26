<%--
  Created by IntelliJ IDEA.
  User: tal63
  Date: 25. 3. 26.
  Time: 오후 2:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Title</title>
  </head>
  <body>
    <p>Hello World!</p>
    <p><%=request.getAttribute("accounts")%></p>
    <form method="POST" >
      <input hidden name="account_id" value=0>
      <label>
        NickName:
        <input type="text" name="nickname">
      </label>
      <button>submit</button>
    </form>
  <form action="unsubscribe">
    <label>
      <label>
        want delete what? (id):
        <input type="text" name="id">
      </label>
      <button>delete</button>
    </label>
  </form>
  
  </body>
</html>
