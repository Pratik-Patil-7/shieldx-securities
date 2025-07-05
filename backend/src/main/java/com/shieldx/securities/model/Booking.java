package com.shieldx.securities.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vip_id")
    private VipPerson vip;

    @ManyToOne
    @JoinColumn(name = "bouncer_id")
    private Bouncer bouncer;

    @ManyToOne
    @JoinColumn(name = "st_id")
    private SecurityType securityType;

    private String bouncerCount;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String location;
    private String status = "pending";
    private double totalPrice;
}

