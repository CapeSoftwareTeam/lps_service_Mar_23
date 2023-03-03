package com.capeelectric.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capeelectric.controller.BasicLpsController;
import com.capeelectric.exception.BasicLpsException;
import com.capeelectric.model.BasicLps;
import com.capeelectric.model.License;
import com.capeelectric.repository.BasicLpsRepository;
import com.capeelectric.repository.FileDBRepository;
import com.capeelectric.repository.LicenseRepository;
import com.capeelectric.service.BasicLpsService;
import com.capeelectric.util.UserFullName;

/**
 * This BasicLpsServiceImpl service class doing save and retrieve operation
 * related to BasicLpsDetails
 * 
 * @author capeelectricsoftware
 *
 */

@Service
public class BasicLpsServiceImpl implements BasicLpsService {

	private static final Logger logger = LoggerFactory.getLogger(BasicLpsController.class);

	@Autowired
	private BasicLpsRepository basicLpsRepository;

	@Autowired
	private UserFullName userFullName;

	private BasicLps basicLpsData;
	
	@Autowired
	private FileDBRepository fileDBRepository;
	
	@Autowired
	private LicenseRepository licenseRepository;

	@Transactional
	@Override
	public BasicLps addBasicLpsDetails(BasicLps basicLps) throws BasicLpsException {
		logger.info("Called addBasicLpsDetails function");

		if (basicLps != null && basicLps.getClientName() != null) {
			Optional<BasicLps> basicLpsDetailsRepo = basicLpsRepository
					.findByClientNameAndStatusAndProjectName(basicLps.getClientName(), "Active",basicLps.getProjectName());
			
			logger.debug("Basic Client Repo data available");

			if (!basicLpsDetailsRepo.isPresent()) {
				if(updateFileData(basicLps.getAvailabilityOfPreviousReport(),basicLps.getFileId())) {
					basicLps.setFileId(null);
				}
				
				basicLps.setStatus("Active");
				basicLps.setCreatedDate(LocalDateTime.now());
				basicLps.setUpdatedDate(LocalDateTime.now());
				basicLps.setCreatedBy(userFullName.findByUserName(basicLps.getUserName()));
				basicLps.setUpdatedBy(userFullName.findByUserName(basicLps.getUserName()));
				basicLps.setAllStepsCompleted("step-1 completed");
 
				logger.info("Ended addBasicLpsDetails function");
				
				return basicLpsRepository.save(basicLps);
			} else {
				logger.error("Project name " + basicLps.getProjectName() + " is already exists for this Client "+ basicLps.getClientName());
				throw new BasicLpsException("Project name " + basicLps.getProjectName() + " is already exists for this Client "+ basicLps.getClientName());
			}

		} else {
			logger.error("Invalid Inputs");
			throw new BasicLpsException("Invalid Inputs");
		}

	}

	private Boolean updateFileData(String availabilityOfPreviousReport, Integer fileId) {
		if (fileId !=null && availabilityOfPreviousReport.equalsIgnoreCase("No")) {
			fileDBRepository.deleteById(fileId);
			return true;
		}
		return false;
	}

	@Override
	public List<BasicLps> retrieveBasicLpsDetails(String userName, Integer basicLpsId) throws BasicLpsException {
		logger.info("Called retrieveBasicLpsDetails function");

		if (userName != null) {
			Optional<BasicLps> basicLpsRepo = basicLpsRepository.findByBasicLpsId(basicLpsId);
			if (basicLpsRepo.isPresent()) {
				logger.debug("Basic Client Repo data available");
				logger.info("Ended retrieveBasicLpsDetails function");
				List<BasicLps> basicList = new ArrayList<BasicLps>();
				basicList.add(basicLpsRepo.get());
				return basicList;
			} else {
				logger.error("Given UserName & Id doesn't exist in Basic Lps Details");
				return new ArrayList<BasicLps>();
			}
		} else {
			logger.error("Invalid Inputs");
			throw new BasicLpsException("Invalid Inputs");
		}
	}

	@Transactional
	@Override
	public void updateBasicLpsDetails(BasicLps basicLps) throws BasicLpsException {
		logger.info("Called updateBasicLpsDetails function");

		if (basicLps != null && basicLps.getBasicLpsId() != null && basicLps.getBasicLpsId() != 0) {

			Optional<BasicLps> basicLpsRepo1 = basicLpsRepository.findByBasicLpsId(basicLps.getBasicLpsId());
			//Optional<BasicLps> basicLpsRepo = basicLpsRepository.findByBasicLpsId(basicLps.getBasicLpsId());

			if (basicLpsRepo1.isPresent()) {
				if(updateFileData(basicLps.getAvailabilityOfPreviousReport(),basicLps.getFileId())) {
					basicLps.setFileId(null);
				}
				//if (basicLpsRepo.isPresent() && basicLpsRepo.get().getBasicLpsId().equals(basicLps.getBasicLpsId())) {
				    basicLps.setStatus("Active");
					basicLps.setUpdatedDate(LocalDateTime.now());
					basicLps.setUpdatedBy(userFullName.findByUserName(basicLps.getUserName()));
					basicLpsRepository.save(basicLps);
					logger.debug("Basic Lps successfully Updated in DB");

				//} 
			//else {
			//		logger.error("Given Basic LPS Id is Invalid");
			//		throw new BasicLpsException("Given Basic LPS Id is Invalid");
			//	}
			} else {
				logger.error("Project name " + basicLps.getProjectName() + " is already exists");
				throw new BasicLpsException("Project name " + basicLps.getProjectName() + " is already exists");
			}

		} else {
			logger.error("Invalid Inputs");
			throw new BasicLpsException("Invalid inputs");
		}
		logger.info("Ended updateBasicLpsDetails function");

	}

	@Transactional
	@Override
	public void updateBasicLpsDetailsStatus(BasicLps basicLps) throws BasicLpsException {
		logger.info("Called updateBasicLpsDetailsStatus function");

		if (basicLps != null && basicLps.getBasicLpsId() != null && basicLps.getBasicLpsId() != 0) {
			Optional<BasicLps> basicLpsRepo = basicLpsRepository.findByBasicLpsId(basicLps.getBasicLpsId());
			if (basicLpsRepo.isPresent() && basicLpsRepo.get().getBasicLpsId().equals(basicLps.getBasicLpsId())) {
				basicLpsData = basicLpsRepo.get();
				Optional<License> license = licenseRepository.findByUserName(basicLpsRepo.get().getUserName());
				if ((null == basicLpsData.getAllStepsCompleted() || (null != basicLpsData.getAllStepsCompleted()
						&& !basicLpsData.getAllStepsCompleted().equalsIgnoreCase("AllStepCompleted")))
						&& license.isPresent()) {
					license.get()
							.setLpsNoOfLicence(String.valueOf(Integer.parseInt(license.get().getLpsNoOfLicence()) + 1));
					licenseRepository.save(license.get());
					logger.debug("License successfully updated for " + license.get().getUserName());
				}

				basicLpsData.setStatus("InActive");
				basicLpsData.setUpdatedDate(LocalDateTime.now());
				basicLpsData.setUpdatedBy(userFullName.findByUserName(basicLps.getUserName()));
				basicLpsRepository.save(basicLpsData);
				logger.debug("Basic Lps successfully Updated in DB with InActive Status");

			} else {
				logger.error("Given Basic LPS Id is Invalid");
				throw new BasicLpsException("Given Basic LPS Id is Invalid");
			}

		} else {
			logger.error("Invalid Inputs");
			throw new BasicLpsException("Invalid inputs");
		}
		logger.info("Ended updateBasicLpsDetailsStatus function");

	}
	
	@Override
	public BasicLps findingClientNameAndProjectName(String clientName, String projectName) throws BasicLpsException {
		logger.info("Called findingClientNameAndProjectName function");

			Optional<BasicLps> basicLpsRepo = basicLpsRepository.findByClientNameAndStatusAndProjectName(clientName, "Active",projectName);
			
			if (basicLpsRepo.isPresent()) {
				
				List<BasicLps> basicList = new ArrayList<BasicLps>();
				basicList.add(basicLpsRepo.get());
				
				return basicLpsRepo.get();
			}  	
			return null;
			
		}  

	/**
	 * @param viewer mailId
	 * this method finding given MailId active or not
    */	
	@Override
	public Optional<BasicLps> retrieveBasicLps(String userName) {
		return basicLpsRepository.findByMailIdAndStatus(userName,"Active");
	}

}
