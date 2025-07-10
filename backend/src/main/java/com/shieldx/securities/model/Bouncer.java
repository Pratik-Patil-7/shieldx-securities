package com.shieldx.securities.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "bouncer")
@Data
public class Bouncer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bouncerId;

    private String name;
    private String gender;
    private String age;
    private String email;
    private String mobile;
    private String address;
    private String qualification;
    private String experience;
    private String resumeUrl;
    private String photoUrl;
    private String isArmed;
    private String status = "pending";

    @ManyToMany(mappedBy = "bouncers")
    private List<Booking> bookings;
}
