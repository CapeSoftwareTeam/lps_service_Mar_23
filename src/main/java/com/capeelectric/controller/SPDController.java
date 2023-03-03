/**
 * 
 */
package com.capeelectric.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capeelectric.exception.AirTerminationException;
import com.capeelectric.exception.SPDException;
import com.capeelectric.model.SpdReport;
import com.capeelectric.service.SPDService;



/**
 * @author CAPE-SOFTWARE
 *
 */

@RestController
@RequestMapping("/api/v1")
public class SPDController {
	
	private static final Logger logger = LoggerFactory.getLogger(SPDController.class);

	@Autowired
	private SPDService SPDService;
	
	@PostMapping("/lps/addSPDDetails")
	public ResponseEntity<String> addSPDDetails(@RequestBody  SpdReport spdReport)
			throws SPDException, AirTerminationException {
		logger.info("called addSPDDetails function UserName : {}, BasicLpsId : {}",
				spdReport.getUserName(), spdReport.getBasicLpsId());
		SPDService.addSPDDetails(spdReport);
		logger.info("Ended addSPDDetails function");
		return new ResponseEntity<String>("SPD Details Sucessfully Saved",
				HttpStatus.CREATED);
	}

	@GetMapping("/lps/retrieveSPD/{userName}/{basicLpsId}")
	public ResponseEntity<List<SpdReport>> retrieveSPDDetails(@PathVariable String userName,
			@PathVariable Integer basicLpsId) throws SPDException {
		logger.info("started retrieveSPDDetails function UserName : {}, BasicLpsId : {}", userName, basicLpsId);
		return new ResponseEntity<List<SpdReport>>(
				SPDService.retrieveSPDDetails(userName, basicLpsId), HttpStatus.OK);
	}
	
	@PutMapping("/lps/updateSpdDetails")
	public ResponseEntity<String> updateSpdDetails(@RequestBody SpdReport spdReport)
			throws SPDException, AirTerminationException {
		logger.info("called updateSpdDetails function UserName : {},BasicLpsId : {},SpdReportId : {}",
				spdReport.getUserName(), spdReport.getBasicLpsId(),
				spdReport.getSpdReportId());
		SPDService.updateSpdDetails(spdReport);
		logger.info("Ended updateSpdDetails function");
	   return new ResponseEntity<String>("SPD Details successfully Updated", HttpStatus.OK);
	}
}
