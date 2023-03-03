package com.capeelectric.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.capeelectric.config.AWSLPSConfig;

/**
 * 
 * @author capeelectricsoftware
 *
 */
@Service
public class AWSEmailService {

    private static final Logger log = LoggerFactory.getLogger(AWSEmailService.class);
    
    @Autowired
	private RestTemplate restTemplate;
    
    @Autowired
    private AWSLPSConfig awsConfiguration;
	
    public void sendLPSEmailPDF(String userName, Integer id, int count, String keyname) {
		String type = "LPS";
		restTemplate.exchange(awsConfiguration.getSendEmailWithPDF() + userName + "/"+type+"/"+ id +"/"+ keyname,
				HttpMethod.GET, null, String.class);

		log.debug("Cape-Electric-AWS-Email service Response was successful");
		
	}
    
    public void sendRiskAssessmentEmailPDF(String userName, Integer id, int count, String keyname) {
		String type = "RISK";
		restTemplate.exchange(awsConfiguration.getSendEmailWithPDF() + userName + "/"+type+"/"+ id +"/"+ keyname,
				HttpMethod.GET, null, String.class);

		log.debug("Cape-Electric-AWS-Email service Response was successful");
		
	}
}