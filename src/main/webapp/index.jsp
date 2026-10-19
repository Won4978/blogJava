<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    if (request.getAttribute("reclist") == null) {
        response.sendRedirect(request.getContextPath() + "/home/main");
        return;
    }
%>
<%@ include file="/include/header.jsp" %>

   <section class="visual">	<!-- 하나의 주제나 기능을 가진 영역을 만들 때 -->
		<div id="carouselExample" class="carousel slide">
		<div class="carousel-inner">
		  <div class="carousel-item active">
		    <img src="${pageContext.request.contextPath}/images/FCB.jpg" class="d-block w-100" alt="...">
		  </div>
		  <div class="carousel-item">
		    <img src="${pageContext.request.contextPath}/images/FCB.jpg" class="d-block w-100" alt="...">
		  </div>
		  <div class="carousel-item">
		    <img src="${pageContext.request.contextPath}/images/FCB2.jpg" class="d-block w-100" alt="...">
		  </div>
		</div>
		<button class="carousel-control-prev" type="button" data-bs-target="#carouselExample" data-bs-slide="prev">
		  <span class="carousel-control-prev-icon" aria-hidden="true"></span>
		  <span class="visually-hidden">Previous</span>
		</button>
		<button class="carousel-control-next" type="button" data-bs-target="#carouselExample" data-bs-slide="next">
		  <span class="carousel-control-next-icon" aria-hidden="true"></span>
		  <span class="visually-hidden">Next</span>
		</button>
		</div> 
   </section>
   <!-- 컨텐츠 영역 : 최신글 인기글 들어갈 자리 -->
   <main class="content">
   		<section class="content-box">
   			<h2 class="section-title">최신글</h2>
   			<ul class="post-card-list">
			<c:forEach var="b" items="${reclist}">
			    <li class="post-card">
					<c:choose>
					    <c:when test="${not empty b.imgPath}">
					        <a href="${pageContext.request.contextPath}/blog/view?bno=${b.bno}">
							    <img src="/imgs/${b.imgPath}"
							         alt="게시글 이미지" class="post-thumb">
							</a>
					    </c:when>
					    <c:otherwise>
							<a href="${pageContext.request.contextPath}/blog/view?bno=${b.bno}">
							    <img src="${pageContext.request.contextPath}/images/NOIMAGE.jpg"
							         alt="이미지 없음" class="post-thumb">
							</a>
					    </c:otherwise>
					</c:choose>
			        <div class="post-text">
					    <div class="post-top">
					        <span class="post-title">
							    <a href="${pageContext.request.contextPath}/blog/view?bno=${b.bno}">
							        ${b.title}
							    </a>
							</span>
					        <span class="post-date">${b.regDate}</span>
					    </div>
					
					    <div class="post-info">
					        작성자 ${b.writerNickname} · 조회수 ${b.viewCnt} · 좋아요 ${b.likeCnt}
					    </div>
					</div>
			    </li>
			</c:forEach>   				 				
   			</u1>
   		</section>
   		<section class="content-box">
   			<h2 class="section-title">인기글</h2>
   			<ul class="post-card-list">
			<c:forEach var="b" items="${poplist}">
			    <li class="post-card">
					<c:choose>
					    <c:when test="${not empty b.imgPath}">
							<a href="${pageContext.request.contextPath}/blog/view?bno=${b.bno}">
							    <img src="/imgs/${b.imgPath}"
							         alt="게시글 이미지" class="post-thumb">
							</a>					 		
					    </c:when>
					    <c:otherwise>
							<a href="${pageContext.request.contextPath}/blog/view?bno=${b.bno}">
							    <img src="${pageContext.request.contextPath}/images/NOIMAGE.jpg"
							         alt="이미지 없음" class="post-thumb">
							</a>
					    </c:otherwise>
					</c:choose>
					<div class="post-text">
					    <div class="post-top">
					        <span class="post-title">
							    <a href="${pageContext.request.contextPath}/blog/view?bno=${b.bno}">
							        ${b.title}
							    </a>
							</span>					        
					        <span class="post-date">${b.regDate}</span>
					    </div>
					
					    <div class="post-info">
					        작성자 ${b.writerNickname} · 조회수 ${b.viewCnt} · 좋아요 ${b.likeCnt}
					    </div>
					</div>
			    </li>
			</c:forEach>
   			</ul>
   		</section> 
   </main>
   <script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="/include/footer.jsp" %>
