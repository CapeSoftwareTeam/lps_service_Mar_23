package com.capeelectric.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capeelectric.exception.AirTerminationException;
import com.capeelectric.exception.BasicLpsException;
import com.capeelectric.exception.DownConductorException;
import com.capeelectric.exception.EarthStudException;
import com.capeelectric.exception.EarthingLpsException;
import com.capeelectric.exception.SPDException;
import com.capeelectric.service.impl.AWSEmailService;

@RestController
@RequestMapping("/api/v1")
public class MailPDFController {

	private static final Logger logger = LoggerFactory.getLogger(MailPDFController.class);
	@Autowired
	private AWSEmailService awsEmailService;

	@GetMapping("/lps/sendPDFinMail/{userName}/{lpsId}/{projectName}")
	public ResponseEntity<byte[]> sendLPSFinalPDF(@PathVariable String userName, @PathVariable Integer lpsId, @PathVariable String projectName)
			throws BasicLpsException, AirTerminationException, DownConductorException, SPDException,
			EarthStudException, EarthingLpsException, Exception {

		awsEmailService.sendLPSEmailPDF(userName,lpsId, lpsId, projectName);
		
		return new ResponseEntity<byte[]>(HttpStatus.OK);

	}
	
	@GetMapping("/risk/sendPDFinMail/{userName}/{riskId}/{projectName}")
	public ResponseEntity<byte[]> sendRiskAssessmentFinalPDF(@PathVariable String userName, @PathVariable Integer riskId,
			@PathVariable String projectName) throws Exception {
		logger.info("called sendFinalPDF function userName: {},riskId : {}, projecttName : {}", userName, riskId,
				projectName);

		awsEmailService.sendRiskAssessmentEmailPDF(userName, riskId, riskId, projectName);

		return new ResponseEntity<byte[]>(HttpStatus.OK);
	}
}