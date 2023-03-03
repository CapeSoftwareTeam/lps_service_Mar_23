package com.capeelectric.service;

import java.util.List;
import java.util.Optional;

import com.capeelectric.exception.BasicLpsException;
import com.capeelectric.model.BasicLps;


/**
 **
  * @author capeelectricsoftware
 *
 */
public interface BasicLpsService {

	public BasicLps addBasicLpsDetails(BasicLps basicLps) throws BasicLpsException;

	public List<BasicLps> retrieveBasicLpsDetails(String userName, Integer basicLpsId) throws BasicLpsException;
		
	public void updateBasicLpsDetails(BasicLps basicLps) throws BasicLpsException;
	
	public void updateBasicLpsDetailsStatus(BasicLps basicLps) throws BasicLpsException;

	public Optional<BasicLps> retrieveBasicLps(String userName);

	public BasicLps findingClientNameAndProjectName(String clientName, String projectName) throws BasicLpsException;

}
