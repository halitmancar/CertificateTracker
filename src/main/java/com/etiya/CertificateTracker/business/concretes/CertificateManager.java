package com.etiya.CertificateTracker.business.concretes;

import com.etiya.CertificateTracker.business.abstracts.CertificateService;
import com.etiya.CertificateTracker.business.abstracts.IPService;
import com.etiya.CertificateTracker.business.requests.AddCertificateRequest;
import com.etiya.CertificateTracker.core.utilities.business.BusinessRules;
import com.etiya.CertificateTracker.core.utilities.email.EmailService;
import com.etiya.CertificateTracker.core.utilities.mapping.ModelMapperService;
import com.etiya.CertificateTracker.core.utilities.results.Result;
import com.etiya.CertificateTracker.core.utilities.results.SuccessResult;
import com.etiya.CertificateTracker.dataAccess.CertificateDao;
import com.etiya.CertificateTracker.entities.Certificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CertificateManager implements CertificateService {

    private CertificateDao certificateDao;
    private ModelMapperService modelMapperService;
    private IPService ipService;
    private EmailService emailService;

    @Autowired
    public CertificateManager(CertificateDao certificateDao, ModelMapperService modelMapperService, @Lazy IPService ipService, EmailService emailService) {
        this.certificateDao = certificateDao;
        this.modelMapperService = modelMapperService;
        this.ipService = ipService;
        this.emailService = emailService;
    }

    @Override
    public Result save(AddCertificateRequest addCertificateRequest) {
        Result result = BusinessRules.run(ipService.checkIfIPExists(addCertificateRequest.getIpID()));
        if (result != null){
            return result;
        }
        Certificate certificate = modelMapperService.forRequest().map(addCertificateRequest, Certificate.class);
        this.certificateDao.save(certificate);
        remainingDayCalculator(certificate.getExpirationDate());
        return new SuccessResult("Sertifika başarıyla eklendi.");
    }

    public void checkExpirationDates() {
        List<Certificate> certificates;
        certificates = this.certificateDao.findAll();
        for (Certificate c:certificates) {
            if (remainingDayCalculator(c.getExpirationDate())>10){
                long remainingDays = remainingDayCalculator(c.getExpirationDate());
                System.out.println("User notified. Remaining days: " + remainingDays);
                emailService.sendSimpleMessage("halit.mancar@etiya.com","About Certificate Expiration on Host: "
                        + c.getIp().getIpAddress() ,
                        "Certificate with ID " + c.getCertificateID() + " is going to expire in " + remainingDays +" days!");
            }
        }
    }

    public long remainingDayCalculator(Date date){
        LocalDate present = LocalDate.now();
        long days = ChronoUnit.DAYS.between(present, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        return days;
    }

}
