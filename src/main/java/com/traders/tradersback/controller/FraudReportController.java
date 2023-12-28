package com.traders.tradersback.controller;


import com.traders.tradersback.model.FraudReport;
import com.traders.tradersback.service.FraudReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fraud")
public class FraudReportController {

    @Autowired
    private FraudReportService fraudReportService;

    @GetMapping("/search")
    public List<FraudReport> searchFraudReports(@RequestParam String search) {
        return fraudReportService.findFraudReports(search);
    }

    @PostMapping("/report")
    public FraudReport reportFraud(@RequestBody FraudReport fraudReport) {
        return fraudReportService.reportFraud(fraudReport);
    }
}
