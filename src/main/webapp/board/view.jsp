<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/include/header.jsp"%>


<main class="view-wrap">
	<div class="view-header">
		<h2 class="view-title">${view.title}</h2>
		<p class="view-meta">${view.writerNickname}| ${view.regDate} | 조회
			${view.viewCnt}</p>
	</div>
		<c:if test="${not empty view.imgPath}">
		    <div class="view-image">
		        <img src="/imgs/${view.imgPath}" alt="" class="view-thum">
		    </div>
		</c:if>
	<div class="view-content">${view.content}</div>
	<div class="view-actions">
		<button type="button" id="likeBtn" class="btn btn-outline like-btn ${liked ? 'liked':'' }">
			♥ 좋아요 <span id="likeCount">${view.likeCnt}</span>
		</button>
		<div class="view-actions-right">
			<a
				href="${pageContext.request.contextPath}/blog/update?bno=${view.bno}"
				class="btn btn-outline">수정</a> <a
				href="${pageContext.request.contextPath}/blog/delete?bno=${view.bno}"
				class="btn btn-outline" onclick="return confirm('삭제할래요?')"> 삭제</a> <a
				href="${pageContext.request.contextPath}/blog/list"
				class="btn btn-primary">목록</a>
		</div>
	</div>


	<!-- 댓글 영역 -->
	<div class="reply-section">
		<h3 class="reply-title">
			댓글 <span id="replyCount">${fn:length(replyList)}</span>
		</h3>
		<ul class="reply-list" id="replylist">
			<c:forEach var="r" items="${replyList}">
				<li class="reply-item" data-rno="${r.rno}">
					<div class="reply-info">
						<span class="reply-writer">${r.nickName}</span> <span
							class="reply-data">${r.regDate}</span>
					</div>
					<p class="reply-content">${r.content}</p>
					<div class="reply-btns">
						<button type="button" class="reply-edit-btn">수정</button>
						<button type="button" class="reply-delete-btn">삭제</button>
					</div>
				</li>
			</c:forEach>
		</ul>

		<div class="reply-form">
			<input type="text" id="replycontent" placeholder="댓글 입력"
				class="reply-input">
			<button type="button" id="replySubmitBtn" class="btn btn-primary">등록</button>
		</div>
	</div>

</main>

<!-- Modal -->
<div class="modal fade" id="replyModal" tabindex="-1"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<h1 class="modal-title fs-5" id="exampleModalLabel">댓글 수정</h1>
				<button type="button" class="btn-close" data-bs-dismiss="modal"
					aria-label="Close"></button>
			</div>
			<div class="modal-body">
				<input type="hidden" id="modalRno">
				<textarea id="modalContent" class="form-control" row="4"></textarea>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-secondary"
					data-bs-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary" id="modalSaveBtn">Save changes</button>
			</div>
		</div>
	</div>
</div>




<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>


<script>
   		// 좋아요 제어
   		$("#likeBtn").on("click",function(){
   			const bno = ${view.bno};
			$.ajax({
				url:"${pageContext.request.contextPath}/blog/like",
				type:'post',
				data:{bno: bno},
				dataType:'json',
				success:function(result){
					if(!result.success){
						alert(result.message);
						return;
					}
					$("#likeCount").text(result.likeCount);
					$("#likeBtn").toggleClass("liked",result.liked);
				},error:function(){
					alert("통신오류!")
				}
			})
   	})
 // 댓글 등록 제어
$("#replySubmitBtn").on("click", function() {

    const content = $("#replycontent").val().trim();

    if(content == '') {
        alert("내용을 입력하세요");
        return;
    }

    $.ajax({
        url: "${pageContext.request.contextPath}/Reply/write",
        type: 'post',
        data: {bno: ${view.bno}, content: content},
        dataType: 'json',

        success: function(result) {

            if(!result.success) {
                alert(result.message || '댓글등록 실패');
                return;
            }
            
            alert("댓글이 등록되었습니다.");

            // DB 등록 성공 후 화면에 댓글 추가
            const newItem = `
                <li class="reply-item" data-rno="\${result.rno}">
                    <div class="reply-info">
                        <span class="reply-writer">\${result.writerNickname}</span>
                        <span class="reply-date">\${result.regDate}</span>
                    </div>

                    <p class="reply-content">\${result.content}</p>

                    <div class="reply-btns">
                        <button type="button" class="reply-edit-btn">수정</button>
                        <button type="button" class="reply-delete-btn">삭제</button>
                    </div>
                </li>
            `;

            $('#replylist').append(newItem);
            $('#replycontent').val('');
        },error:function(){
        	alert("댓글 등록중 오류 발생")
        }
    });

});
   	
   	//댓글 삭제
   	   	$("#replylist").on("click",'.reply-delete-btn',function(){
   	   		if(confirm('댓글을 삭제하십니까?')){
   	   			
   	   			const $item = $(this).closest('.reply-item');
   	   			const rno = $item.data('rno');
   	   			
   	   			$.ajax({
   	   				url:"${pageContext.request.contextPath}/Reply/delete",
   	   				type:'post',
   	   				data:{rno:rno},
   	   				dataType:'json',
   	   				success:function(result){
   	   					if(result.success){
   	   						alert(result.message);
   	   						$item.remove();
   	   					}else{
   	   						alert(result.message);
   	   					}
   	   				},error:function(){
   	   					alert("통신오류!");
   	   				}
   	   			})
   	   		}
   	   	})
   	
   	
   	// 댓글 수정
   	const replyModal = new bootstrap.Modal(document.getElementById('replyModal'));
   	document.getElementById('replyModal').addEventListener('show.bs.modal',function(){
   		$('#modalContent').focus();	
   	}); 
   	$("#replylist").on("click",'.reply-edit-btn',function(){
   		const $item=$(this).closest('.reply-item');
   		$('#modalRno').val($item.data('rno'));
   		$('#modalContent').val($item.find('.reply-content').text());
   		replyModal.show();
   	})
   	
   	$('#modalSaveBtn').on('click',function(){
   		const rno = $('#modalRno').val();
   		const newContent = $('#modalContent').val().trim();
   		if(newContent ==''){
   			alert('댓글 내용을 입력하세요');
   			return;
   		}
   		$.ajax({
   			url:'${pageContext.request.contextPath}/Reply/modify',
   			type :'post',
   			data:{rno:rno,content:newContent},
   			dataType:'json',
   			success:function(result){
   				if(result.success){
   					$('#replylist .reply-item[data-rno="'+rno+'"]')
   					.find(".reply-content").text(result.content);
   					replyModal.hide();
   					alert(result.message);
   				}else{
   					alert(result.message);
   				}
   			},error:function(){
  				alert("통신오류!");
   			}
   			
   			
   		})
   	})
   

   	
   	
   	//자바스크립트
 	/*		document.getElementById("likeBtn").addEventListener("click",function(){
// 전체 문서에서(document) 설정해둔 아이디를 가지고와서(getElementById("likeBtn")) 마우스 이벤트를 할때(addEventListener) 함수기능(,function(){})을 실행
 			let likeBtn = this;
 			likeBtn.classList.toggle("liked");
 			let count = parseInt(document.getElementById("likeCount").textContent);
 			// likeCount안에 있는 문자열 숫자(textCount)를 가져와서 숫자로 변경(parseInt)
 			if(likeBtn.classList.contains('liked')){ // 클래스안에 liked를 포함하고 있다면(contains)
 				document.getElementById('likeCount').textContent = count +1;
 			}else{
 				document.getElementById('likeCount').textContent = count -1;
 			}
 		})*/
   	</script>

<%@ include file="/include/footer.jsp"%>

