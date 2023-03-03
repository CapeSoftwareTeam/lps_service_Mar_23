package com.capeelectric.service;

import java.util.Map;
import java.util.Optional;

import com.capeelectric.exception.SPDException;
import com.capeelectric.model.BasicLps;
import com.capeelectric.model.SpdReport;
import com.capeelectric.util.PageNumberEvent;

public interface PrintSPDService {

	public void printSPD(String userName, Integer lpsId, Optional<BasicLps> basicLpsDetails,
			Optional<SpdReport> spdDetails, PageNumberEvent pageNumObject, Map<String, Map<Integer, String>> indexNumberDeatils) throws SPDException, Exception;

//	public void printSPD(String userName, Integer lpsId)throws SPDException;
}
