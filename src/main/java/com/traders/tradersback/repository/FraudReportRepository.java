package com.traders.tradersback.repository;


import com.traders.tradersback.model.FraudReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FraudReportRepository extends JpaRepository<FraudReport, Long> {
    List<FraudReport> findByReportedPhoneOrReportedName(String phone, String name);
}
