package com.etiya.CertificateTracker.business.concretes;

import com.etiya.CertificateTracker.business.abstracts.CertificateService;
import com.etiya.CertificateTracker.business.abstracts.IPService;
import com.etiya.CertificateTracker.business.requests.AddCertificateRequest;
import com.etiya.CertificateTracker.business.requests.AddIPRequest;
import com.etiya.CertificateTracker.core.utilities.business.BusinessRules;
import com.etiya.CertificateTracker.core.utilities.mapping.ModelMapperService;
import com.etiya.CertificateTracker.core.utilities.results.ErrorResult;
import com.etiya.CertificateTracker.core.utilities.results.Result;
import com.etiya.CertificateTracker.core.utilities.results.SuccessResult;
import com.etiya.CertificateTracker.dataAccess.IPDao;
import com.etiya.CertificateTracker.entities.IP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Locale;

@Service
public class IPManager implements IPService {

    private IPDao IPDao;
    private ModelMapperService modelMapperService;
    private CertificateService certificateService;

    @Autowired
    public IPManager(IPDao IPDao, ModelMapperService modelMapperService, CertificateService certificateService) {
        this.IPDao = IPDao;
        this.modelMapperService = modelMapperService;
        this.certificateService = certificateService;
    }

    @Override
    public Result save(AddIPRequest addIPRequest) throws IOException {
        Result result = BusinessRules.run(checkExistingIPAddress(addIPRequest.getIpAddress()),checkIfIPisValid(addIPRequest.getIpAddress()));
        if (result!=null){
            return result;
        }
        String httpsString = "https://";
        IP iP =modelMapperService.forRequest().map(addIPRequest, IP.class);
        iP.setIpAddress(httpsString + iP.getIpAddress().toLowerCase(Locale.ROOT));
        this.IPDao.saveAndFlush(iP);
        certificateGetter(iP.getIpAddress());
        return new SuccessResult("IP başarıyla eklendi.");
    }

    @Override
    public Result checkIfIPExists(int id) {
        if(this.IPDao.existsById(id)){
            return new SuccessResult();
        } else {
            return new ErrorResult("İlgili ID'ye kayıtlı IP bulunamadı.");
        }
    }

    public Result checkExistingIPAddress(String ip){
        for (IP ips: this.IPDao.findAll()) {
            if (ips.getIpAddress().equals("https://"+ip.toLowerCase(Locale.ROOT))){
                return new ErrorResult("IP zaten kayıtlı!");
            }
        }
        return new SuccessResult();
    }

    private void certificateGetter(String ip) throws IOException {
        URL url = new URL(ip);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        int responseCode = connection.getResponseCode();
        if (responseCode==200){
            System.out.println("Connection established!");
        }

        Certificate[] certificate = connection.getServerCertificates();

        int certificateCount = certificate.length;
        System.out.println(certificateCount);

        for (Certificate certs: certificate) {
            X509Certificate serverCertificate = (X509Certificate) certs;
            Date expirationDate = serverCertificate.getNotAfter();
            AddCertificateRequest addCertificateRequest = new AddCertificateRequest();
            addCertificateRequest.setExpirationDate(expirationDate);
            addCertificateRequest.setIpID(this.IPDao.getByIpAddress(url.toString()).getIpID());
            certificateService.save(addCertificateRequest);
        }
        connection.disconnect();
    }

    private Result checkIfIPisValid(String ip) throws MalformedURLException {
        String httpsIP= "https://"+ip;
        URL url = new URL(httpsIP);
        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.getResponseCode();
            return new SuccessResult();
        } catch (IOException e) {
            return new ErrorResult("IP is not valid!");
        }
    }
}
