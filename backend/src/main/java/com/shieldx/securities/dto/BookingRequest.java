package com.shieldx.securities.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BookingRequest {

    @NotNull(message = "User ID is required")
    private Integer userId;
    
    @NotNull(message = "VIP ID is required")
    private int vipId;

    @NotNull(message = "Security Type ID is required")
    private int securityTypeId;

    @NotBlank(message = "Bouncer count is required")
    @Pattern(regexp = "\\d+", message = "Bouncer count must be a number")
    private String bouncerCount;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be today or in the future")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Bouncer IDs are required")
    private List<Integer> bouncerIds;
}
