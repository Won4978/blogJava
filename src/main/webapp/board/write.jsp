<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/include/header.jsp" %>
<main class="write-wrap">

	<h2 class="write-page-title">글쓰기</h2>					<!-- enctype : 첨부파일 -->
	<form id="writeForm" class="write-form" method="post" enctype="multipart/form-data"			
		action="${pageContext.request.contextPath}/blog/write" >
		
	<input type="hidden" name="writer" value="${sessionScope.loginMember.nickname }">	
		<div class="form-row">
			<label>작성자</label>
			<p class="write-author">
			<input type="text" id="writer" class="form-input" value="${sessionScope.loginMember.nickname }" readonly>	
			</p>
		</div>
		
		<div class="form-row">
			<label for="title">제목</label>	<!--label : for과 같은 이름인 id로 커서 이동  -->
			<input type="text" id="title" name="title" placeholder="제목 입력" class="form-input">		
		</div>	
		<div class="form-row">
			<label>대표 이미지 <span class="form-option">선택</span></label>
			<div class="image-upload-box">
				<img id="imgPreview" src="${pageContext.request.contextPath}/images/NOIMAGE.jpg"
				alt="이미지 미리보기" class="img-preview">
				<div class="image-upload-btns">
					<label for="imgFile" class="btn btn-outline file-label">
						이미지선택
					</label>
					<input type="file" id="imgFile" name="imgFile" accept="image/*" class="file-input">
					<!-- input type="file" : 파일 업로드 accept="image/*": 이미지만 업로드 가능 -->
					<button type="button" id="imgRemoveBtn" class="btn btn-outline">이미지 제거</button>					
				</div>
			</div>
		</div>
		<div class="form-row">
			<label for="content">내용</label>
			<textarea rows="12" id="content" name="content" placeholder="내용 입력" class="form-textarea"></textarea>
			<!-- textarea태그는 시작과 끝을 붙여야 한다. 공백금지 -->
		</div>
		<div class="write-btns">
		<a href="${pageContext.request.contextPath}/board/list" class="btn btn-outline">취소</a>
		<button type="submit" class="btn btn-primary">등록</button>
		</div>
				
	</form>
</main>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script src="${pageContext.request.contextPath}/js/write.js"></script>
  



<%@ include file="/include/footer.jsp" %>
