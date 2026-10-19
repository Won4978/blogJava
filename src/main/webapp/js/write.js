$(function() {
	// 이미지 미리보기
	const defaultImg= $ ("#imgPreview").attr('src');
									//.attr('1') : 1에 있는 값을 불러오기
	$("#imgFile").on("change",function(){
		const file = this.files[0];
		if(!file) return;
		// 브라우저 안 임시 URL생성 후 미리보기에 반영
		const previewUrl = URL.createObjectURL(file);
		$("#imgPreview").attr("src",previewUrl);
		$("removeImg").val("false");
						// .attr('1','2') : 1을 2로 대체
});
	// 이미지 제거
	$("#imgRemoveBtn").on("click",function(){
		$("#imgFile").val('');
		$("#imgPreview").attr("src",defaultImg);
		$("removeImg").val('true');
	});
	
	
	$('#writeForm').on('submit',function(e){
		const title = $("#title").val().trim();
		const content = $("#content").val().trim();
		if(title===''){
			alert("제목 입력");
			e.preventDefault();				// 화면 고정
			$("#title").focus();
			return;
		}
		if(content==''){
			alert("내용 입력");
			e.preventDefault();
			$("#content").focus();
			return;
		}
	})
	
	
	
	
});

