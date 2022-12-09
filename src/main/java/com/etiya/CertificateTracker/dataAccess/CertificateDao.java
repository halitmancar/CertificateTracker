package com.etiya.CertificateTracker.dataAccess;

import com.etiya.CertificateTracker.entities.Certificate;
import com.etiya.CertificateTracker.entities.IP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateDao extends JpaRepository<Certificate, Integer> {
    Certificate getByCertificateID(int certificateID);
    List<Certificate> getAllByIp(IP ip);
}
