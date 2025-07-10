package com.shieldx.securities.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookingResponse {
    private Integer bookingId;
    private Integer userId;
    private Integer vipId;
    private Integer securityTypeId;
    private String bouncerCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String status;
    private double totalPrice;
    private List<Integer> bouncerIds;
}
