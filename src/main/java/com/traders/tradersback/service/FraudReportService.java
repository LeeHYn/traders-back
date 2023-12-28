package com.traders.tradersback.service;


import com.traders.tradersback.model.FraudReport;
import com.traders.tradersback.repository.FraudReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FraudReportService {

    @Autowired
    private FraudReportRepository fraudReportRepository;

    public List<FraudReport> findFraudReports(String phoneOrName) {
        return fraudReportRepository.findByReportedPhoneOrReportedName(phoneOrName, phoneOrName);
    }

    public FraudReport reportFraud(FraudReport fraudReport) {
        return fraudReportRepository.save(fraudReport);
    }
}
