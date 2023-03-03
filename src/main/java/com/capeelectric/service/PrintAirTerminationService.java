package com.capeelectric.service;

import java.util.Map;
import java.util.Optional;

import com.capeelectric.exception.AirTerminationException;
import com.capeelectric.model.AirTermination;
import com.capeelectric.model.BasicLps;
import com.capeelectric.util.PageNumberEvent;

public interface PrintAirTerminationService {

	public void printAirTermination(String userName, Integer basicLpsId, Optional<BasicLps> basicLpsDetails,
			Optional<AirTermination> lpsAirTermination, PageNumberEvent pageNumObject,  Map<String, Map<Integer, String>> indexNumberDeatils)
			throws AirTerminationException, Exception;

//	public void printAirTermination(String userName, Integer lpsId) throws AirTerminationException;

	
}
