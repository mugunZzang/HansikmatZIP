package com.spring.app.springscheduler.jungyj.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.spring.app.user.qna.jungyj.model.UserQnaDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaFileCleanupScheduler {

	private final UserQnaDAO userQnaDao;
    private final FileCleanupHelper fileCleanupHelper;

    @Value("${file.qnaimage-dir}")
    private String qnaimageDir;

    // 이벤트(03:00)와 겹치지 않게 새벽 3시 10분 실행
//  @Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "0 10 3 * * *")
    public void cleanupOrphanQnaImages() {
        Set<String> usedFilenames = new HashSet<>(userQnaDao.findAllUsedQnaImageFilenames());
        fileCleanupHelper.deleteOrphanFiles(qnaimageDir, usedFilenames, "QnA");
    }
}
