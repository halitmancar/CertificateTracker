package com.etiya.CertificateTracker.dataAccess;

import com.etiya.CertificateTracker.entities.IP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPDao extends JpaRepository<IP, Integer> {
    IP getByIpAddress(String ip);
    IP getByIpID(int ipID);
}
