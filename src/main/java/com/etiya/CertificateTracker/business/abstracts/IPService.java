package com.etiya.CertificateTracker.business.abstracts;

import com.etiya.CertificateTracker.business.requests.AddIPRequest;
import com.etiya.CertificateTracker.business.requests.DeleteIPRequest;
import com.etiya.CertificateTracker.core.utilities.results.Result;
import com.etiya.CertificateTracker.entities.IP;

import java.io.IOException;

public interface IPService {
    Result save(AddIPRequest addIPRequest) throws IOException;
    Result delete(DeleteIPRequest deleteIPRequest);
    Result checkIfIPExists(int id);
    IP getByIpID(int ipID);
}
