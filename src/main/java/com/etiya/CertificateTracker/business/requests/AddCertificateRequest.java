package com.etiya.CertificateTracker.business.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCertificateRequest {
    @JsonIgnore
    private int certificateID;
    @NotNull
    private Date expirationDate;
    @NotNull
    private int ipID;
}
