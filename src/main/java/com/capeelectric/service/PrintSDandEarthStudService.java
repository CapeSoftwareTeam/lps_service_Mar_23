package com.capeelectric.service;

import java.util.Map;
import java.util.Optional;

import com.capeelectric.exception.EarthStudException;
import com.capeelectric.model.BasicLps;
import com.capeelectric.model.EarthStudReport;
import com.capeelectric.model.SeperationDistanceReport;
import com.capeelectric.util.PageNumberEvent;

public interface PrintSDandEarthStudService {

	public void printSDandEarthStud(String userName, Integer lpsId,
			Optional<BasicLps> basicLpsDetails, Optional<SeperationDistanceReport> separateDistanceReport, Optional<EarthStudReport> earthStudDetails, PageNumberEvent pageNumObject, Map<String, Map<Integer, String>> indexNumberDeatils)
			throws EarthStudException, Exception;

//	public void printSDandEarthStud(String userName, Integer lpsId) throws EarthStudException;

}
