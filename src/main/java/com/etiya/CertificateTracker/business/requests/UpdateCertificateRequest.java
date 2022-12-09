package com.etiya.CertificateTracker.business.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCertificateRequest {
    @NotNull
    private int certificateID;
    @NotNull
    private Date expirationDate;
}
