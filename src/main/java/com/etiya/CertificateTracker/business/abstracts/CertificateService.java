package com.etiya.CertificateTracker.business.abstracts;

import com.etiya.CertificateTracker.business.requests.AddCertificateRequest;
import com.etiya.CertificateTracker.core.utilities.results.Result;

public interface CertificateService {
    Result save(AddCertificateRequest addCertificateRequest);
}
