<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/include/header.jsp" %>

<main class="mypage-wrap">

        <h2 class="mypage-page-title">마이페이지</h2>
        <p class="mypage-sub">${sessionScope.loginMember.nickname}님, 환영합니다</p>

        <!-- 탭 메뉴: jQuery로 클릭한 탭만 보이게 전환 -->
        <div class="mypage-tabs">
            <button type="button" class="mypage-tab-btn active" data-tab="myPosts">내 글</button>
            <button type="button" class="mypage-tab-btn" data-tab="myReplies">내 댓글</button>
            <button type="button" class="mypage-tab-btn" data-tab="myLikes">좋아요한 글</button>
        </div>

        <!-- 탭 ① 내 글: 지금까지 만든 카드형 리스트와 같은 구조 -->
		<div id="myPosts" class="mypage-panel active">
		    <ul class="mypage-list">
		
			<c:forEach var="post" items="${myPosts}">
			    <li class="mypage-item">
			
			        <a href="${pageContext.request.contextPath}/blog/view?bno=${post.bno}"
			           class="mypage-item-link">
			
			            <c:choose>
			                <c:when test="${empty post.imgPath}">
			                    <img src="${pageContext.request.contextPath}/images/NOIMAGE.jpg"
			                         alt="기본 이미지" class="mypage-thumb">
			                </c:when>
			                <c:otherwise>
			                    <img src="/imgs/${post.imgPath}"
			                         alt="게시글 썸네일" class="mypage-thumb">
			                </c:otherwise>
			            </c:choose>
			
			            <div class="mypage-item-info">
			                <p class="mypage-item-title">
			                    <c:out value="${post.title}" />
			                </p>
			                <p class="mypage-item-meta">
			                    ${post.regDate} · 조회 ${post.viewCnt} · 좋아요 ${post.likeCnt}
			                </p>
			            </div>
			
			        </a>
			
			    </li>
			</c:forEach>
		
		        <c:if test="${empty myPosts}">
		            <li class="mypage-reply-item">작성한 게시글이 없습니다.</li>
		        </c:if>
		
		    </ul>
		</div>

		<!-- 탭 ② 내 댓글 -->
		<div id="myReplies" class="mypage-panel">
		    <ul class="mypage-list">
		
		        <c:forEach var="reply" items="${myReplies}">
		            <li class="mypage-reply-item">
		
		                <a href="${pageContext.request.contextPath}/blog/view?bno=${reply.bno}"
		                   class="mypage-reply-link">
		
		                    <p class="mypage-reply-content">
		                        <c:out value="${reply.content}" />
		                    </p>
		
		                    <p class="mypage-item-meta">
		                        원글: <c:out value="${reply.boardTitle}" />
		                        · ${reply.regDate}
		                    </p>
		
		                </a>
		
		            </li>
		        </c:forEach>
		
		        <c:if test="${empty myReplies}">
		            <li class="mypage-reply-item">작성한 댓글이 없습니다.</li>
		        </c:if>
		
		    </ul>
		</div>

		<!-- 탭 ③ 좋아요한 글 -->
		<div id="myLikes" class="mypage-panel">
		    <ul class="mypage-list">
		
		        <c:forEach var="liked" items="${myLikes}">
		            <li class="mypage-item">
		
		                <a href="${pageContext.request.contextPath}/blog/view?bno=${liked.bno}"
		                   class="mypage-item-link">
		
		                    <c:choose>
		                        <c:when test="${empty liked.imgPath}">
		                            <img src="${pageContext.request.contextPath}/images/NOIMAGE.jpg"
		                                 alt="기본 이미지" class="mypage-thumb">
		                        </c:when>
		                        <c:otherwise>
		                            <img src="/imgs/${liked.imgPath}"
		                                 alt="게시글 썸네일" class="mypage-thumb">
		                        </c:otherwise>
		                    </c:choose>
		
		                    <div class="mypage-item-info">
		                        <p class="mypage-item-title">
		                            <c:out value="${liked.title}" />
		                        </p>
		
		                        <p class="mypage-item-meta">
		                            작성자 <c:out value="${liked.writerNickname}" />
		                            · 좋아요 ${liked.likeCnt}
		                        </p>
		                    </div>
		
		                </a>
		
		            </li>
		        </c:forEach>
		
		        <c:if test="${empty myLikes}">
		            <li class="mypage-reply-item">좋아요한 게시글이 없습니다.</li>
		        </c:if>
		
		    </ul>
		</div>

    </main>

   <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/mypage.js"></script>


<%@ include file="/include/footer.jsp" %>