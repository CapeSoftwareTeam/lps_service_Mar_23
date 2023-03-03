package com.capeelectric.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capeelectric.exception.AirTerminationException;
import com.capeelectric.exception.BasicLpsException;

@RestController()
@RequestMapping("/api/lps/v1")
public class PrintBasicLpsController {

	
	private static final Logger logger = LoggerFactory.getLogger(PrintBasicLpsController.class);

//	@Autowired
//	private PrintBasicLpsService printBasicLpsService;

	@GetMapping("/printBasicLps/{userName}/{lpsId}")
	public ResponseEntity<String> printBasicLps(@PathVariable String userName, @PathVariable Integer lpsId)
			throws AirTerminationException, BasicLpsException {
		logger.info("called printBasicLps UserName: {},BasicLpsId : {}", userName, lpsId);
//		printBasicLpsService.printBasicLps(userName, lpsId);
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
