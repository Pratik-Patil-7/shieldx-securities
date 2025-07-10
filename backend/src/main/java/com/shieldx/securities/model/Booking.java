package com.shieldx.securities.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vip_id")
    private VipPerson vip;

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

    @ManyToMany
    @JoinTable(
        name = "booking_bouncers",
        joinColumns = @JoinColumn(name = "booking_id"),
        inverseJoinColumns = @JoinColumn(name = "bouncer_id")
    )
    private List<Bouncer> bouncers = new ArrayList<>();
}
