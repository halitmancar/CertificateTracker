package com.etiya.CertificateTracker.business.abstracts;

import com.etiya.CertificateTracker.business.requests.AddIPRequest;
import com.etiya.CertificateTracker.core.utilities.results.Result;

import java.io.IOException;

public interface IPService {
    Result save(AddIPRequest addIPRequest) throws IOException;
    Result checkIfIPExists(int id);
}
