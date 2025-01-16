package com.perspectrix.market.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @PostMapping("/generate-report")
    public ResponseEntity<String> generateReport() {
        return ResponseEntity.ok("Generating report");
    }
}
