package com.capeelectric.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.capeelectric.exception.SummaryLpsException;
import com.capeelectric.model.BasicLps;
import com.capeelectric.model.EarthStudReport;
import com.capeelectric.model.SeperationDistanceReport;
import com.capeelectric.model.SummaryLps;
import com.capeelectric.util.PageNumberEvent;

public interface PrintSummaryLpsService {

	List<SummaryLps> printLpsSummaryDetails(String userName, Integer basicLpsId, Optional<BasicLps> basicLpsDetails, Optional<SeperationDistanceReport> separateDistanceDetails, Optional<EarthStudReport> earthStudDetails, PageNumberEvent pageNum, Map<String, Map<Integer, String>> indexNumberDeatils)
			throws SummaryLpsException, Exception;


	List<SummaryLps> printLpsSummaryDetails(String userName, Integer basicLpsId)throws SummaryLpsException, Exception;
}
