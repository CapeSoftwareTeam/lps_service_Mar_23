package com.capeelectric.service;

import java.util.Map;
import java.util.Optional;

import com.capeelectric.exception.DownConductorException;
import com.capeelectric.model.BasicLps;
import com.capeelectric.model.DownConductorReport;
import com.capeelectric.util.PageNumberEvent;

public interface PrintDownConductorService {

	public void printDownConductor(String userName, Integer lpsId, Optional<BasicLps> basicLpsDetails,
			Optional<DownConductorReport> downConductorDetails, PageNumberEvent pageNumObject, Map<String, Map<Integer, String>> indexNumberDeatils) throws DownConductorException, Exception;

//	public void printDownConductor(String userName, Integer lpsId)throws DownConductorException;

}
