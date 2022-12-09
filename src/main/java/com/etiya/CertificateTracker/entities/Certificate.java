package com.etiya.CertificateTracker.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CERTIFICATES")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private int certificateID;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @JsonIgnore
    @ManyToOne (cascade = CascadeType.DETACH)
    @JoinColumn(name = "ip_id")
    private IP ip;
}
