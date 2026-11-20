


$(function(){
    // ==== 스마트 에디터 구현 시작 ==== //
	//전역변수
	var obj = [];
	
	
	// 스마트에디터 프레임생성		
/*  
	 nhn.husky.EZCreator.createInIFrame({
	     oAppRef: obj,
	     elPlaceHolder: "content",
	     
	//   sSkinURI: contextPath+"smarteditor/SmartEditor2Skin.html",
	     
	//   또는

	//   Thymeleaf @{} 문법을 사용하면 여기에는 Context Path 가 자동으로 포함된다.
	     sSkinURI: contextPath + "smarteditor/SmartEditor2Skin.html",
	     
	     htParams : {
	         // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
	         bUseToolbar : true,            
	         
	         // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
	         
	         bUseVerticalResizer : true,    
	         // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
	         bUseModeChanger : true,
	     }
	 }); 
	// ==== 스마트 에디터 구현 끝 ==== //
*/	


	// 제품이미지 파일선택을 선택하면 화면에 이미지를 미리 보여주기 시작 
	const $previewImg = $("#previewImg");

	$("#eventImage").on("change", function (e) {
	    const file = e.target.files[0];

	    if (!file) {
	        return;
	    }

	    const maxSize = 5 * 1024 * 1024; // 5MB
	    const allowedTypes = ["image/jpeg", "image/png"];

	    if (!allowedTypes.includes(file.type)) {
	        alert("JPG, PNG 형식의 이미지 파일만 업로드할 수 있습니다.");
	        $(this).val("");
	        $previewImg.addClass("d-none").attr("src", ""); // 다시 기본 화면으로
	        return;
	    }

	    if (file.size > maxSize) {
	        alert("이미지 용량은 최대 5MB까지 가능합니다.");
	        $(this).val("");
	        $previewImg.addClass("d-none").attr("src", ""); // 다시 기본 화면으로
	        return;
	    }

	    const fileReader = new FileReader();
	    fileReader.onload = function () {
	        $previewImg.attr("src", fileReader.result).removeClass("d-none");
	    };
	    fileReader.readAsDataURL(file);
	});
	
	// 등록하기 버튼 클릭했을 때
	$("button#eventRegister").on("click", function(){
		
		
		// === 스마트 에디터 구현 시작 === //
		// id가 content인 textarea에 에디터에서 대입
//		obj.getById["content"].exec("UPDATE_CONTENTS_FIELD", []);
		// === 스마트 에디터 구현 끝 === //
		$(".error").hide(); // 기존 에러 메시지 초기화
		let is_infoData_OK = true;

		$(".infoData").each(function (index, elmt) {
		    const $el = $(elmt);
		    const val = $el.val().trim();

		    // 스마트에디터 빈 값("<p>&nbsp;</p>", "<br>" 등)까지 체크하려면 아래처럼 보완 가능
		    if (val === "" || val === "<p>&nbsp;</p>" || val === "<br>") {
		        // input-group이나 스마트에디터 iframe이 있어도 안전하게 .error 찾기
		        $el.closest(".col-md-4, .col, .col-sm-4, .mb-4, div").find(".error").show();
		        $el.focus();
		        is_infoData_OK = false;
		        return false; // each 중단
		    }
		});

		if (!is_infoData_OK) {
		    return;
		}

		// (선택) 시작일/종료일 순서 검사
		if ($("#startDate").val() > $("#endDate").val()) {
		    alert("종료일은 시작일보다 빠를 수 없습니다.");
		    $("#endDate").focus();
		    return;
		}

		// 모든 검사 통과 시 폼 전송
		$("form").submit();
		
		
	});
});