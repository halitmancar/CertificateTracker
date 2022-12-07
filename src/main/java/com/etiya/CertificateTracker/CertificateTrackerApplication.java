package com.etiya.CertificateTracker;

import com.etiya.CertificateTracker.business.abstracts.CertificateService;
import com.etiya.CertificateTracker.business.concretes.CertificateManager;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Date;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class CertificateTrackerApplication {

	private static CertificateManager certificateManager;

	public CertificateTrackerApplication(@Lazy CertificateManager certificateManager) {
		this.certificateManager = certificateManager;
	}

	public static void main(String[] args) {
		SpringApplication.run(CertificateTrackerApplication.class, args);
		Timer timer = new Timer();
		Date date = new Date();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("asdasd"+new Date());
				certificateManager.checkExpirationDates();
			}
		}, date, 30000);
	}

	@Bean
	public ModelMapper getModelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper;
	}

	@Bean
	public JavaMailSender getJavaMailSender(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("certificatetrackeretiya@gmail.com");
		mailSender.setPassword("xrrtjbylzvydjiei");

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");
		return mailSender;
	}
}
