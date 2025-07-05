package com.shieldx.securities.dto;

import lombok.Data;

@Data
public class DashboardStatsResponse {
    private long totalUsers;
    private long totalBookings;
    private long totalApplications;
    private long totalPayments;

    public DashboardStatsResponse(long totalUsers, long totalBookings, long totalApplications, long totalPayments) {
        this.totalUsers = totalUsers;
        this.totalBookings = totalBookings;
        this.totalApplications = totalApplications;
        this.totalPayments = totalPayments;
    }
}