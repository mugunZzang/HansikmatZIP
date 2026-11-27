package com.spring.app.user.qna.jungyj.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.spring.app.common.FileManager;
import com.spring.app.qna.jungyj.domain.QnaDTO;
import com.spring.app.qna.jungyj.domain.QnaImagesDTO;
import com.spring.app.user.qna.jungyj.model.UserQnaDAO;

import lombok.RequiredArgsConstructor;

@Service    // userQnaService_imple     이게 Bean 으로 등록되면서 메모리에 올라감
@RequiredArgsConstructor
public class UserQnaService_imple implements UserQnaService {
	
	private final UserQnaDAO userQnaDao;
	
	// === 파일업로드 및 파일다운로드를 해주는 FileManager 클래스 의존객체 주입하기(DI : Dependency Injection) === 
	private final FileManager fileManager;
	
	@Value("${file.qnaimage-dir}")
	private String qnaimageDir; // 문의사항 이미지 등록시 파일 경로 잡아주는 것
	
	@Override
	@Transactional
	public void qnaRegister(QnaDTO qnaDto, List<MultipartFile> files) throws Exception{
		
		// 1. QnA 본문 먼저 INSERT (<selectKey>로 qnaDto.qnaseq에 시퀀스 값이 자동 세팅됨)
        userQnaDao.insertQna(qnaDto);
        
        // 2. 첨부파일이 있으면 서버 업로드 후 이미지 테이블 INSERT (최대 2장)
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) {
                    continue;
                }

                
                
                String savedFileName = doFileUpload(file.getBytes(), file.getOriginalFilename(), qnaimageDir);

                QnaImagesDTO imgDto = new QnaImagesDTO();
                imgDto.setFk_qnaseq(qnaDto.getQnaseq()); // 1번에서 채워진 PK
                imgDto.setQna_orgfilename(file.getOriginalFilename()); // qnaImage DTO에 파일명 삽입
                imgDto.setQna_sysfilename(savedFileName);
                

                userQnaDao.insertQnaImage(imgDto);
            }
        }
		
        
		
		
		
	}
	
	
	// 첨부파일 업로드 해주는 함수 
	private String doFileUpload(byte[] bytes, String originalFilename, String path) {
		
		String fileName = "";
		try {
			fileName = fileManager.doFileUpload(bytes, originalFilename, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return fileName;
	} // end of private String updateFile(byte[] bytes, String originalFilename, String path)---------------


}
