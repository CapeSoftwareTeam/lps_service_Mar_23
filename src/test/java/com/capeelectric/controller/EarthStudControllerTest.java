package com.capeelectric.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.capeelectric.exception.AirTerminationException;
import com.capeelectric.exception.BasicLpsException;
import com.capeelectric.exception.DownConductorException;
import com.capeelectric.exception.EarthStudException;
import com.capeelectric.exception.EarthingLpsException;
import com.capeelectric.exception.SPDException;
import com.capeelectric.model.EarthStudReport;
import com.capeelectric.service.impl.EarthStudServiceImpl;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class EarthStudControllerTest {

	private static final Logger logger = LoggerFactory.getLogger(EarthStudControllerTest.class);

	@InjectMocks
	private EarthStudController earthStudController;

	@MockBean
	private EarthStudServiceImpl earthStudServiceImpl;

	private EarthStudReport earthStudReport;

	{
		earthStudReport = new EarthStudReport();
		earthStudReport.setBasicLpsId(1);
		earthStudReport.setUserName("LVsystem@gmail.com");
		earthStudReport.setUserName("Inspector@gmail.com");
		earthStudReport.setBasicLpsId(1);
	}

	@Test
	public void testAddEarthStudDetails() throws EarthStudException, BasicLpsException, AirTerminationException, DownConductorException, EarthingLpsException, SPDException, Exception {
		logger.info("testAddEarthStudDetails Function Started");

		doNothing().when(earthStudServiceImpl).addEarthStudDetails(earthStudReport);
		ResponseEntity<String> addEarthStudDetails = earthStudController.addEarthStud(earthStudReport);
		equals(addEarthStudDetails.getBody());
		logger.info("testAddEarthStudDetails Function Ended");
	}

	@Test
	public void testRetrieveEarthStudDetails() throws EarthStudException {
		List<EarthStudReport> arrayList = new ArrayList<>();
		arrayList.add(earthStudReport);

		logger.info("testRetrieveEarthStudDetails Function Started");

		when(earthStudServiceImpl.retrieveEarthStudDetails("LVsystem@gmail.com", 12)).thenReturn(arrayList);
		ResponseEntity<List<EarthStudReport>> retrieveEarthStudDetails = earthStudController
				.retrieveEarthStudDetails("LVsystem@gmail.com", 12);
		assertEquals(HttpStatus.OK, retrieveEarthStudDetails.getStatusCode());

		logger.info("testRetrieveEarthStudDetails Function Ended");

	}

	@Test
	public void testUpdateEarthStud() throws EarthStudException, AirTerminationException {

		logger.info("testUpdateEarthStud Function Started");
		ResponseEntity<String> expectedResponseEntity = new ResponseEntity<String>(HttpStatus.OK);
		ResponseEntity<String> actualResponseEntity = earthStudController.updateEarthStud(earthStudReport);
		assertEquals(actualResponseEntity.getStatusCode(), expectedResponseEntity.getStatusCode());
		logger.info("testUpdateEarthStud Function Ended");
	}
}
