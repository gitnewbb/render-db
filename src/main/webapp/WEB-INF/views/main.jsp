<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>

<html>
<head>
  <title>천하제일 삼행시대회</title>
  <style>
    @font-face {
      font-family: 'ChosunGu';
      src: url('https://fastly.jsdelivr.net/gh/projectnoonnu/noonfonts_20-04@1.0/ChosunGu.woff') format('woff');
      font-weight: normal;
      font-style: normal;
    }
    body {
      font-family: 'ChosunGu', sans-serif;
      background: #f9f9f9;
      margin: 0;
      padding: 20px;
    }
    h1, h2 {
      text-align: center;
    }
    .post {
      border: 1px solid #ccc;
      margin: 10px auto;
      padding: 10px;
      background: white;
      width: 80%;
    }
    .top3 {
      border: 2px solid gold;
      background: #fffbe6;
      margin: 10px auto;
      padding: 10px;
      width: 80%;
      cursor: pointer;
    }
    .top3-content {
      display: none;
      margin-top: 10px;
      padding: 10px;
      background: #fff;
      border: 1px dashed #aaa;
      font-size: 1.2em;
    }
    .post-content {
      display: none;
      margin-top: 10px;
      padding: 10px;
      background: #f0f0f0;
      font-size: 1.2em;
    }
    .pagination {
      text-align: center;
      margin-top: 20px;
    }
    .pagination a {
      margin: 0 5px;
      text-decoration: none;
    }
    #writeModal, #modalOverlay {
      display: none;
      position: fixed;
      z-index: 999;
    }
    #writeModal {
      top: 15%;
      left: 50%;
      transform: translateX(-50%);
      background: white;
      border: 1px solid #ccc;
      padding: 30px;
      width: 500px;
      box-shadow: 0 0 15px rgba(0,0,0,0.4);
    }
    #modalOverlay {
      top: 0; left: 0;
      width: 100%; height: 100%;
      background: rgba(0, 0, 0, 0.5);
      z-index: 998;
    }
  </style>
</head>
<body>

<h1>🏆 선하제일 삼행시대회</h1>
<hr/>

<h2>🔥 인기 TOP 3 삼행시</h2>
<%
  List<org.andh.stocknotrecommend.model.dto.Account> top3 =
          (List<org.andh.stocknotrecommend.model.dto.Account>) request.getAttribute("top3");
  if (top3 != null) {
    for (org.andh.stocknotrecommend.model.dto.Account post : top3) {
%>
<div class="top3" onclick="toggleTop3('<%= post.account_id() %>')">
  <strong><%= post.title() %></strong> by <%= post.nickname() %><br/>
  <em>👍 <%= post.recommended() %> / 👎 <%= post.notrecommended() %></em>
  <div id="top3-content-<%= post.account_id() %>" class="top3-content">
    <pre><%= post.body_data() %></pre>
  </div>
</div>
<% } } %>

<div style="text-align:center; margin:20px 0;">
  <button onclick="openModal()">✍ 글쓰기 도전</button>
</div>

<!-- 글쓰기 모델 -->
<div id="modalOverlay" onclick="closeModal()"></div>
<div id="writeModal">
  <h3>게시 구성</h3>
  <form method="POST" action="write" onsubmit="return closeModalOnSubmit()">
    <label>닉네임: <input type="text" name="nickname" required></label><br/><br/>
    <label>비밀번호: <input type="password" name="password" required></label><br/><br/>
    <label>제목: <input type="text" name="title" required></label><br/><br/>
    <label>내용:<br/>
      <textarea name="body_data" rows="7" cols="50" required></textarea>
    </label><br/><br/>
    <button type="submit">게시</button>
    <button type="button" onclick="closeModal()">취소</button>
  </form>
</div>

<hr/>
<h2>📋 전체 삼행시 목록</h2>
<%
  List<org.andh.stocknotrecommend.model.dto.Account> accounts =
          (List<org.andh.stocknotrecommend.model.dto.Account>) request.getAttribute("accounts");
  if (accounts != null) {
    for (org.andh.stocknotrecommend.model.dto.Account post : accounts) {
%>
<div class="post">
  <h3 onclick="toggleContent('<%= post.account_id() %>')" style="cursor:pointer;">
    제목: <%= post.title() %> | 닉네임: <%= post.nickname() %>
  </h3>
  <div id="content-<%= post.account_id() %>" style="display:none; margin-top:10px;">
    <pre><%= post.body_data() %></pre>
  </div>
  <p style="margin-top:5px;">
    👍 추천수: <%= post.recommended() %> | 👎 비추천수: <%= post.notrecommended() %>
  </p>
  <form action="recommend" method="POST" style="display:inline;">
    <input type="hidden" name="account_id" value="<%= post.account_id() %>">
    <button type="submit">👍 추천</button>
  </form>
  <form action="notrecommend" method="POST" style="display:inline;">
    <input type="hidden" name="account_id" value="<%= post.account_id() %>">
    <button type="submit">👎 비추천</button>
  </form>
  <button onclick="showDeleteForm('<%= post.account_id() %>')">삭제</button>
  <form id="delete-form-<%= post.account_id() %>" action="unsubscribe" method="POST" style="display:none; margin-top:5px;">
    <input type="hidden" name="account_id" value="<%= post.account_id() %>">
    비밀번호: <input type="password" name="password" required>
    <button type="submit">확인</button>
    <button type="button" onclick="hideDeleteForm('<%= post.account_id() %>')">취소</button>
  </form>
</div>
<%
    }
  }
%>

<!-- 페이지네이션 -->
<div class="pagination">
  <%
    Integer currentPage = (Integer) request.getAttribute("currentPage");
    Integer totalPages = (Integer) request.getAttribute("totalPages");
    if (currentPage != null && totalPages != null) {
      for (int i = 1; i <= totalPages; i++) {
        if (i == currentPage) {
  %>
  <strong>[<%= i %>]</strong>
  <%
  } else {
  %>
  <a href="?page=<%= i %>">[<%= i %>]</a>
  <%
        }
      }
    }
  %>
</div>

<!-- JavaScript -->
<script>
  function openModal() {
    document.getElementById('writeModal').style.display = 'block';
    document.getElementById('modalOverlay').style.display = 'block';
  }
  function closeModal() {
    document.getElementById('writeModal').style.display = 'none';
    document.getElementById('modalOverlay').style.display = 'none';
  }
  function closeModalOnSubmit() {
    closeModal(); return true;
  }
  function toggleContent(id) {
    const content = document.getElementById('content-' + id);
    content.style.display = (content.style.display === 'none') ? 'block' : 'none';
  }
  function showDeleteForm(id) {
    document.getElementById('delete-form-' + id).style.display = 'block';
  }
  function hideDeleteForm(id) {
    document.getElementById('delete-form-' + id).style.display = 'none';
  }
  function toggleTop3(id) {
    const content = document.getElementById('top3-content-' + id);
    content.style.display = (content.style.display === 'none') ? 'block' : 'none';
  }
</script>

</body>
</html>
