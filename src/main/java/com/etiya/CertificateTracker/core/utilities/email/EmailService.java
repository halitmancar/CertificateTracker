package com.etiya.CertificateTracker.core.utilities.email;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
