package com.etiya.CertificateTracker.controllers;

import com.etiya.CertificateTracker.business.abstracts.CertificateService;
import com.etiya.CertificateTracker.business.requests.AddCertificateRequest;
import com.etiya.CertificateTracker.business.requests.DeleteCertificateRequest;
import com.etiya.CertificateTracker.business.requests.UpdateCertificateRequest;
import com.etiya.CertificateTracker.core.utilities.results.Result;
import jakarta.validation.Valid;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/certificates")
public class CertificateController {
    private CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @PostMapping("add")
    public Result save(@RequestBody @Valid AddCertificateRequest addCertificateRequest){
        return this.certificateService.save(addCertificateRequest);
    }
    @DeleteMapping("delete")
    public Result delete(@RequestBody @Valid DeleteCertificateRequest deleteCertificateRequest){
        return this.certificateService.delete(deleteCertificateRequest);
    }
    @PutMapping("update")
    public Result update(@RequestBody @Valid UpdateCertificateRequest updateCertificateRequest){
        return this.certificateService.update(updateCertificateRequest);
    }
    @GetMapping("getAllByIPID")
    public Result getAllByIPID(@RequestParam("ipID") int ipID){
        return this.certificateService.getAllByIPID(ipID);
    }
}
