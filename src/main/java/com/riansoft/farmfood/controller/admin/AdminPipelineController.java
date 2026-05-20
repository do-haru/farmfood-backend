package com.riansoft.farmfood.controller.admin;

import com.riansoft.farmfood.service.pipeline.PipelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/pipeline")
@RequiredArgsConstructor
public class AdminPipelineController {

    private final PipelineService pipelineService;

    @PostMapping("/run-all")
    public ResponseEntity<String> runAll() {
        pipelineService.runAll();
        return ResponseEntity.accepted().body("파이프라인 시작 (수집 → 추출 → 지표 → 순위), 진행 상황은 서버 로그를 확인하세요.");
    }
}
