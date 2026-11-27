package com.spring.app.springscheduler.jungyj.service;

import java.io.File;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FileCleanupHelper {

	public void deleteOrphanFiles(String dirPath, Set<String> usedFilenames, String targetName) {
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        long oneHourAgo = System.currentTimeMillis() - (60 * 60 * 1000L);
        int deletedCount = 0;

        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }

            boolean isOldEnough = file.lastModified() < oneHourAgo;
//            System.out.println("이건 뭐야 usedFilenames에는 "+usedFilenames.toString());
            
//            System.out.println("file 이름 알아봐" + file.getName());
            
            boolean isOrphan = !usedFilenames.contains(file.getName());

            if (isOldEnough && isOrphan) {
                if (file.delete()) {
                    deletedCount++;
                    log.info("[{}] 고아 파일 삭제 완료: {}", targetName, file.getName());
                } else {
                    log.warn("[{}] 고아 파일 삭제 실패: {}", targetName, file.getName());
                }
            }
        }

        log.info("[{}] 고아 파일 정리 종료 - 총 {}건 삭제", targetName, deletedCount);
    }
	
}
