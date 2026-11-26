


$(function(){
    // ==== 스마트 에디터 구현 시작 ==== //
	//전역변수
	var obj = [];
	
	
	// 스마트에디터 프레임생성		

	 nhn.husky.EZCreator.createInIFrame({
	     oAppRef: obj,
	     elPlaceHolder: "smartEditorContent",
	     
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
		// id가 smartEditorContent인 textarea에 에디터에서 대입
		obj.getById["smartEditorContent"].exec("UPDATE_CONTENTS_FIELD", []);
		// === 스마트 에디터 구현 끝 === //
		$(".error").hide(); // 기존 에러 메시지 초기화
		let is_infoData_OK = true;

		$(".infoData").each(function (index, elmt) {
		    const $el = $(elmt);
		    const val = $el.val().trim();

		    // 유효성 검사
		    if (val === "" || val === "<p>&nbsp;</p>" || val === "<br>") {
				$el.closest(".col-md-4, .col, .col-sm-4, .mb-4, div:not(.input-group)").find(".error").show();
		        $el.focus();
		        is_infoData_OK = false;
		        return false; // each 중단
		    }
		});
		
		// === 글내용 유효성 검사(스마트 에디터를 사용할 경우) ===
		let content_val = $("textarea[name='event_contents']").val().trim();
		  
	  //  alert(content_val);  // content 에 공백만 여러개를 입력하여 쓰기할 경우 알아보는 것.
	  //  <p>&nbsp; &nbsp; &nbsp; &nbsp;</p> 이라고 나온다.
	  
	    content_val = content_val.replace(/&nbsp;/gi, "");  // 공백(&nbsp;)을 "" 으로 변환
	      /*    
		         대상문자열.replace(/찾을 문자열/gi, "변경할 문자열");
			   ==> 여기서 꼭 알아야 될 점은 나누기(/)표시안에 넣는 찾을 문자열의 따옴표는 없어야 한다는 점입니다. 
			       그리고 뒤의 gi는 다음을 의미합니다.
			
			   g : 전체 모든 문자열을 변경 global
			   i : 영문 대소문자를 무시, 모두 일치하는 패턴 검색 ignore
	      */
	   // alert(content_val);
	   // <p>                                      </p>
	   
	      content_val = content_val.substring(content_val.indexOf("<p>")+3);
//	      alert(content_val);
		  //                                       </p>
		  
	      content_val = content_val.substring(0, content_val.indexOf("</p>"));
	   // alert(content_val);
	     
	      if(content_val.trim().length == 0) {
	    	  alert("글내용을 입력하세요!!");
	    	  return; // 종료
	      }

		

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