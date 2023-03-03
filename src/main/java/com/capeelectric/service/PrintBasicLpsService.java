package com.capeelectric.service;

import java.util.Map;
import java.util.Optional;

import com.capeelectric.exception.BasicLpsException;
import com.capeelectric.model.BasicLps;
import com.capeelectric.util.PageNumberEvent;

public interface PrintBasicLpsService {

	public void printBasicLps(String userName, Integer lpsId, Optional<BasicLps> basicLpsDetails,
			PageNumberEvent pageNum, Map<String, Map<Integer, String>> indexNumberDeatils)
			throws BasicLpsException, Exception;

//	public void printBasicLps(String userName, Integer lpsId)throws BasicLpsException;;

}
