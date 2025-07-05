package com.shieldx.securities.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class BookingResponse {
    private int bookingId;
    private int userId;
    private int vipId;
    private int securityTypeId;
    private String bouncerCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String status;
    private double totalPrice;

    public BookingResponse(int bookingId, int userId, int vipId, int securityTypeId, String bouncerCount,
                          LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime,
                          String location, String status, double totalPrice) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.vipId = vipId;
        this.securityTypeId = securityTypeId;
        this.bouncerCount = bouncerCount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.status = status;
        this.totalPrice = totalPrice;
    }
}