package com.traders.tradersback.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_report")
public class FraudReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fraudReportId;

    @Column
    private Long reporterMemberNum;

    @Column
    private Long reportedMemberNum;

    @Column
    private String reportedPhone;

    @Column
    private String reportedName;

    @Column
    private String fraudDetails;

    @Column
    private LocalDateTime reportDate;

    // Getters and Setters

    public Long getFraudReportId() {
        return fraudReportId;
    }

    public void setFraudReportId(Long fraudReportId) {
        this.fraudReportId = fraudReportId;
    }

    public Long getReporterMemberNum() {
        return reporterMemberNum;
    }

    public void setReporterMemberNum(Long reporterMemberNum) {
        this.reporterMemberNum = reporterMemberNum;
    }

    public Long getReportedMemberNum() {
        return reportedMemberNum;
    }

    public void setReportedMemberNum(Long reportedMemberNum) {
        this.reportedMemberNum = reportedMemberNum;
    }

    public String getReportedPhone() {
        return reportedPhone;
    }

    public void setReportedPhone(String reportedPhone) {
        this.reportedPhone = reportedPhone;
    }

    public String getReportedName() {
        return reportedName;
    }

    public void setReportedName(String reportedName) {
        this.reportedName = reportedName;
    }

    public String getFraudDetails() {
        return fraudDetails;
    }

    public void setFraudDetails(String fraudDetails) {
        this.fraudDetails = fraudDetails;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }
}
