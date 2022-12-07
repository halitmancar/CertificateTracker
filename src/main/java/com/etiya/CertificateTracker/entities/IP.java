package com.etiya.CertificateTracker.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "IP_ADDRESSES")
public class IP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ip_id")
    private int ipID;

    @Column(name = "ip_address")
    private String ipAddress;

    @OneToMany(mappedBy = "ip")
    private List<Certificate> certificates;
}
