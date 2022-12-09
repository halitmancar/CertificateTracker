package com.etiya.CertificateTracker.business.abstracts;

import com.etiya.CertificateTracker.business.requests.AddCertificateRequest;
import com.etiya.CertificateTracker.business.requests.DeleteCertificateRequest;
import com.etiya.CertificateTracker.business.requests.UpdateCertificateRequest;
import com.etiya.CertificateTracker.core.utilities.results.Result;
import com.etiya.CertificateTracker.core.utilities.results.SuccessDataResult;
import com.etiya.CertificateTracker.entities.Certificate;
import com.etiya.CertificateTracker.entities.IP;

import java.util.List;

public interface CertificateService {
    SuccessDataResult<Certificate> save(AddCertificateRequest addCertificateRequest);
    Result delete(DeleteCertificateRequest deleteCertificateRequest);
    Result update(UpdateCertificateRequest updateCertificateRequest);
    void deleteAllByIP(IP ip);
    SuccessDataResult<List<Certificate>> getAllByIPID(int ipID);
}
