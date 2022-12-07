package com.etiya.CertificateTracker.controllers;

import com.etiya.CertificateTracker.business.abstracts.IPService;
import com.etiya.CertificateTracker.business.requests.AddIPRequest;
import com.etiya.CertificateTracker.core.utilities.results.Result;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/ips")
public class IPController {
    private IPService iPService;

    @Autowired
    public IPController(IPService iPService) {
        this.iPService = iPService;
    }

    @PostMapping("add")
    public Result add(@RequestBody @Valid AddIPRequest addIPRequest) throws IOException {
        return this.iPService.save(addIPRequest);
    }
}
