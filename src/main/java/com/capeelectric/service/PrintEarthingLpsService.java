package com.capeelectric.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.capeelectric.exception.EarthingLpsException;
import com.capeelectric.model.BasicLps;
import com.capeelectric.model.EarthingLpsDescription;
import com.capeelectric.model.EarthingReport;
import com.capeelectric.util.PageNumberEvent;

public interface PrintEarthingLpsService {

	List<EarthingLpsDescription> printEarthingLpsDetails(String userName, Integer basicLpsId,
			Optional<BasicLps> basicLpsDetails, Optional<EarthingReport> earthingLpsDetails, PageNumberEvent pageNumObject, Map<String, Map<Integer, String>> indexNumberDeatils) throws EarthingLpsException, Exception;

//	List<EarthingLpsDescription> printEarthingLpsDetails(String userName, Integer basicLpsId) throws EarthingLpsException;

}
