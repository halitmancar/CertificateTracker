package com.etiya.CertificateTracker.dataAccess;

import com.etiya.CertificateTracker.entities.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CertificateDao extends JpaRepository<Certificate, Integer> {
}
