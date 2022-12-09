package com.etiya.CertificateTracker.business.concretes;

import com.etiya.CertificateTracker.business.abstracts.CertificateService;
import com.etiya.CertificateTracker.business.abstracts.IPService;
import com.etiya.CertificateTracker.business.requests.AddCertificateRequest;
import com.etiya.CertificateTracker.business.requests.DeleteCertificateRequest;
import com.etiya.CertificateTracker.business.requests.UpdateCertificateRequest;
import com.etiya.CertificateTracker.core.utilities.businessRules.BusinessRules;
import com.etiya.CertificateTracker.core.utilities.email.EmailService;
import com.etiya.CertificateTracker.core.utilities.mapping.ModelMapperService;
import com.etiya.CertificateTracker.core.utilities.results.ErrorResult;
import com.etiya.CertificateTracker.core.utilities.results.Result;
import com.etiya.CertificateTracker.core.utilities.results.SuccessDataResult;
import com.etiya.CertificateTracker.core.utilities.results.SuccessResult;
import com.etiya.CertificateTracker.dataAccess.CertificateDao;
import com.etiya.CertificateTracker.entities.Certificate;
import com.etiya.CertificateTracker.entities.IP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
    public SuccessDataResult<Certificate> save(AddCertificateRequest addCertificateRequest) {
        Result result = BusinessRules.run(ipService.checkIfIPExists(addCertificateRequest.getIpID()),
                checkIfDateIsFuture(addCertificateRequest.getExpirationDate()));
        if (result != null){
            return new SuccessDataResult<Certificate>(null, result.getMessage());
        }
        Certificate certificate = modelMapperService.forRequest().map(addCertificateRequest, Certificate.class);
        this.certificateDao.save(certificate);
        return new SuccessDataResult<Certificate>(certificate, "Sertifika başarıyla eklendi.");
    } //Check date before saving

    @Override
    public Result delete(DeleteCertificateRequest deleteCertificateRequest) {
        Result result = BusinessRules.run(checkIfCertificateExists(deleteCertificateRequest.getCertificateID()));
        if (result!=null){
            return result;
        }
        Certificate certificate = modelMapperService.forRequest().map(deleteCertificateRequest, Certificate.class);
        this.certificateDao.delete(certificate);
        return new SuccessResult("Sertifika kaydı başarıyla silindi.");
    }

    @Override
    public Result update(UpdateCertificateRequest updateCertificateRequest) {
        Result result = BusinessRules.run(checkIfCertificateExists(updateCertificateRequest.getCertificateID()),
                checkIfDateIsFuture(updateCertificateRequest.getExpirationDate()));
        if (result!=null){
            return result;
        }
        Certificate certificate = modelMapperService.forRequest().map(updateCertificateRequest, Certificate.class);
        Certificate certificate1 = this.certificateDao.getByCertificateID(updateCertificateRequest.getCertificateID());
        int ipID = certificate1.getIp().getIpID();
        this.certificateDao.delete(certificate);
        AddCertificateRequest addCertificateRequest = new AddCertificateRequest();
        addCertificateRequest.setExpirationDate(certificate.getExpirationDate());
        addCertificateRequest.setIpID(ipID);
        Certificate savedCertificate = save(addCertificateRequest).getData();
        return new SuccessResult("Sertifika kaydı başarıyla güncellendi!" +
                " Yeni sertifika ID: " + savedCertificate.getCertificateID());
    }

    @Override
    public void deleteAllByIP(IP ip) {
        List<Certificate> certificates = this.certificateDao.getAllByIp(ip);
        for (Certificate c:certificates) {
            this.certificateDao.delete(c);
        }
    }

    @Override
    public SuccessDataResult<List<Certificate>> getAllByIPID(int ipID) {
        IP ip = this.ipService.getByIpID(ipID);
        List<Certificate> certificates = this.certificateDao.getAllByIp(ip);
        return new SuccessDataResult<List<Certificate>>(certificates, certificates.size() + " adet kayıt bulundu!");
    }

    public void checkExpirationDates() {
        List<Certificate> certificates;
        certificates = this.certificateDao.findAll();
        for (Certificate c:certificates) {
            if (remainingDayCalculator(c.getExpirationDate())>200){
                long remainingDays = remainingDayCalculator(c.getExpirationDate());
                System.out.println("User notified. Remaining days: " + remainingDays);
                emailService.sendSimpleMessage("halit.mancar@etiya.com","About Certificate Expiration on Host: "
                        + c.getIp().getIpAddress() ,
                        "Certificate with ID " + c.getCertificateID() + " on host: " + c.getIp().getIpAddress() +
                                " is going to expire in " + remainingDays +" days!");
            }
        }
    }

    public long remainingDayCalculator(Date date){
        LocalDate present = LocalDate.now();
        long days = ChronoUnit.DAYS.between(present, date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        return days;
    }

    private Result checkIfCertificateExists(int certificateID){
        if (this.certificateDao.existsById(certificateID)){
            return new SuccessResult();
        } else {
            return new ErrorResult("İlgili ID'ye kayıtlı sertifika bulunamadı!");
        }
    }

    private Result checkIfDateIsFuture(Date date){
        if (date.after(new Date())){
            return new SuccessResult();
        } else{
            return new ErrorResult("Expiration Date alanı geçmiş bir tarih olamaz!");
        }
    }

}
