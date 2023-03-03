/**
 * 
 */
package com.capeelectric.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capeelectric.exception.AirTerminationException;
import com.capeelectric.exception.BasicLpsException;
import com.capeelectric.exception.DownConductorException;
import com.capeelectric.exception.EarthStudException;
import com.capeelectric.exception.EarthingLpsException;
import com.capeelectric.exception.SPDException;
import com.capeelectric.exception.SummaryLpsException;
import com.capeelectric.model.AirTermination;
import com.capeelectric.model.BasicLps;
import com.capeelectric.model.DownConductorReport;
import com.capeelectric.model.EarthStudReport;
import com.capeelectric.model.EarthingReport;
import com.capeelectric.model.SeperationDistanceReport;
import com.capeelectric.model.SpdReport;
import com.capeelectric.model.SummaryLps;
import com.capeelectric.model.SummaryLpsBuildings;
import com.capeelectric.repository.AirTerminationLpsRepository;
import com.capeelectric.repository.BasicLpsRepository;
import com.capeelectric.repository.DownConductorRepository;
import com.capeelectric.repository.EarthStudRepository;
import com.capeelectric.repository.EarthingLpsRepository;
import com.capeelectric.repository.SPDRepository;
import com.capeelectric.repository.SeperationDistanceRepository;
import com.capeelectric.repository.SummaryBuildingRepository;
import com.capeelectric.repository.SummaryLpsRepository;
import com.capeelectric.service.PrintAirTerminationService;
import com.capeelectric.service.PrintBasicLpsService;
import com.capeelectric.service.PrintDownConductorService;
import com.capeelectric.service.PrintEarthingLpsService;
import com.capeelectric.service.PrintFinalPDFService;
import com.capeelectric.service.PrintSDandEarthStudService;
import com.capeelectric.service.PrintSPDService;
import com.capeelectric.service.PrintSummaryLpsService;
import com.capeelectric.service.SummaryLpsService;
import com.capeelectric.util.FindNonRemovedObjects;
import com.capeelectric.util.PageNumberEvent;
import com.capeelectric.util.UserFullName;

/**
 * @author CAPE-SOFTWARE
 *
 */
@Service
public class SummaryLpsServiceImpl implements SummaryLpsService {
	private static final Logger logger = LoggerFactory.getLogger(SummaryLpsServiceImpl.class);

	@Autowired
	private SummaryLpsRepository summaryLpsRepository;

	@Autowired
	private BasicLpsRepository basicLpsRepository;

	@Autowired
	private UserFullName userFullName;

	@Autowired
	private DownConductorRepository downConductorRepository;

	@Autowired
	private EarthingLpsRepository earthingLpsRepository;

	@Autowired
	private SPDRepository spdRepository;

	@Autowired
	private AirTerminationLpsRepository airTerminationLpsRepository;

	@Autowired
	private SeperationDistanceRepository seperationDistanceRepository;

	@Autowired
	private EarthStudRepository earthStudRepository;

	@Autowired
	private FindNonRemovedObjects findNonRemovedObjects;

	@Autowired
	private PrintBasicLpsService printBasicLpsService;

	@Autowired
	private PrintAirTerminationService printAirTerminationService;

	@Autowired
	private PrintDownConductorService printDownConductorService;

	@Autowired
	private PrintEarthingLpsService printEarthingLpsService;

	@Autowired
	private PrintSPDService printSPDService;

	@Autowired
	private PrintSDandEarthStudService printSDandEarthStudService;

	@Autowired
	private PrintSummaryLpsService printSummaryLpsService;

	@Autowired
	private PrintFinalPDFService printFinalPDFService;
	
	@Autowired
	private SummaryBuildingRepository summaryBuildingRepository;
															 
	@Transactional
	@Override
	public String addSummaryLpsDetails(SummaryLps summaryLps,Boolean isSubmitted)
			throws SummaryLpsException, BasicLpsException, AirTerminationException, DownConductorException,
			EarthingLpsException, SPDException, EarthStudException, Exception {
		logger.info("Called addSummaryLpsDetails function");

		if (summaryLps != null && summaryLps.getUserName() != null && !summaryLps.getUserName().isEmpty()
				&& summaryLps.getBasicLpsId() != null && summaryLps.getBasicLpsId() != 0) {
			// For Basic Lps data
			Optional<BasicLps> basicLpsDetails = basicLpsRepository.findByBasicLpsId(summaryLps.getBasicLpsId());

			// For Air Termination data
			Optional<AirTermination> lpsAirDisc = airTerminationLpsRepository
					.findByBasicLpsId(summaryLps.getBasicLpsId());

			// For Down Conductor data
			Optional<DownConductorReport> downConductorDetails = downConductorRepository
					.findByBasicLpsId(summaryLps.getBasicLpsId());

			// For Earthing Lps data
			Optional<EarthingReport> earthingLpsDetails = earthingLpsRepository
					.findByBasicLpsId(summaryLps.getBasicLpsId());

			// For SPD data
			Optional<SpdReport> spdDetails = spdRepository.findByBasicLpsId(summaryLps.getBasicLpsId());

			// For Seperation Distance data
			Optional<SeperationDistanceReport> separateDistanceDetails = seperationDistanceRepository
					.findByBasicLpsId(summaryLps.getBasicLpsId());

			// For Earth Stud data
			Optional<EarthStudReport> earthStudDetails = earthStudRepository
					.findByBasicLpsId(summaryLps.getBasicLpsId());

			if (!basicLpsDetails.isPresent() && !lpsAirDisc.isPresent() && !downConductorDetails.isPresent()
					&& !earthingLpsDetails.isPresent() && !separateDistanceDetails.isPresent()
					&& !spdDetails.isPresent() && !earthStudDetails.isPresent()) {
				logger.error("Please enter details for all previous steps to proceed further");
				throw new SummaryLpsException("Please enter details for all previous steps to proceed further");
			} else if (!basicLpsDetails.isPresent()) {
				logger.error("Please enter Basic Information step to proceed further");
				throw new SummaryLpsException("Please enter Basic Information step to proceed further");
			} else if (!lpsAirDisc.isPresent()) {
				logger.error("Please enter Air Termination step to proceed further");
				throw new SummaryLpsException("Please enter Air Termination step to proceed further");
			} else if (!downConductorDetails.isPresent()) {
				logger.error("Please enter Down Conductors step to proceed further");
				throw new SummaryLpsException("Please enter Down Conductors step to proceed further");
			} else if (!earthingLpsDetails.isPresent()) {
				logger.error("Please enter Earthing step to proceed further");
				throw new SummaryLpsException("Please enter Earthing step to proceed further");
			} else if (!spdDetails.isPresent()) {
				logger.error("Please enter SPD step to proceed further");
				throw new SummaryLpsException("Please enter SPD step to proceed further");
			} else if (!separateDistanceDetails.isPresent()) {
				logger.error("Please enter Seperation Distance step to proceed further");
				throw new SummaryLpsException("Please enter Seperation Distance step to proceed further");
			} else if (!earthStudDetails.isPresent()) {
				logger.error("Please enter Equipotential Distance step to proceed further");
				throw new SummaryLpsException("Please enter Equipotential Distance step to proceed further");
			}

			if (basicLpsDetails.isPresent()
					&& basicLpsDetails.get().getBasicLpsId().equals(summaryLps.getBasicLpsId())) {

				//Optional<SummaryLps> summaryLpsRepo = summaryLpsRepository.findByBasicLpsIdAndFlag(summaryLps.getBasicLpsId(),"A");
				
//				if (!summaryLpsRepo.isPresent() || !summaryLpsRepo.get().getBasicLpsId().equals(summaryLps.getBasicLpsId())) {

				List<SummaryLpsBuildings> summaryLpsBuildings = summaryLps.getSummaryLpsBuildings();
				if (summaryLpsBuildings != null && summaryLpsBuildings.size() > 0 && summaryLps.getSummaryLpsDeclaration()!=null && summaryLps.getSummaryLpsDeclaration().size()==2) {
					checkBuildingCount(summaryLpsBuildings);// here finding Duplicates buildingCount number																
					summaryLps.setCreatedDate(LocalDateTime.now());
					summaryLps.setUpdatedDate(LocalDateTime.now());
					summaryLps.setCreatedBy(userFullName.findByUserName(summaryLps.getUserName()));
					summaryLps.setUpdatedBy(userFullName.findByUserName(summaryLps.getUserName()));
					summaryLps.setFlag("A");
 					summaryLpsRepository.save(summaryLps);
					logger.debug("Summary Lps Report Details Successfully Saved in DB");
					userFullName.addUpdatedByandDate(summaryLps.getBasicLpsId(),userFullName.findByUserName(summaryLps.getUserName()),"step-8 completed");
					logger.debug("Basic Lps UpdatedBy and UpdatedDate by Summary");

					if (!isSubmitted) {
						uploadPdfDetails(summaryLps.getUserName(), summaryLps.getBasicLpsId(), lpsAirDisc,
								downConductorDetails, earthingLpsDetails, spdDetails, separateDistanceDetails,
								earthStudDetails);
						logger.debug("Summary Lps Details Sucessfully Submitted");
						return "Summary Lps Details Successfully Submitted";
					} else {
						logger.debug("Summary Lps Details Sucessfully Saved");
						return "Summary Lps Details Successfully Saved";
					}
				} else {
					logger.error("Please fill all the fields before clicking next button");
					throw new SummaryLpsException("Please fill all the fields before clicking next button");
				}
//				} 
//				else {
//					logger.error("Basic LPS Id Already Available.Create New Basic Id");
//					throw new SummaryLpsException("Basic LPS Id Already Available.Create New Basic Id");
//				}

			} else {
				logger.error("Given Basic LPS Id is Not Registered in Basic LPS");
				throw new SummaryLpsException("Given Basic LPS Id is Not Registered in Basic LPS");
			}
		} else {
			logger.error("Invalid Inputs");
			throw new SummaryLpsException("Invalid Inputs");
		}
	}
	

	private void checkBuildingCount(List<SummaryLpsBuildings> summaryLpsBuildings) throws SummaryLpsException {
		
		for (SummaryLpsBuildings summaryLpsBuilding : summaryLpsBuildings) {
			Optional<SummaryLpsBuildings> building = summaryBuildingRepository.findByBuildingCount(summaryLpsBuilding.getBuildingCount());
			if (building.isPresent()) {
				logger.error(building.get().getBuildingCount() + " Already this BuildingCount Available");
				throw new SummaryLpsException("Given buildings are already available in our system, Please create new building");
			}  
		}
		
	}

	@Transactional
	@Override
	public String updateSummaryLpsDetails(SummaryLps summaryLps,Boolean isSubmitted)
			throws SummaryLpsException, BasicLpsException, AirTerminationException, DownConductorException,
			EarthingLpsException, SPDException, EarthStudException, Exception {
		logger.info("Called updateSummaryLpsDetails function");

		if (summaryLps != null && summaryLps.getUserName() != null && !summaryLps.getUserName().isEmpty()
				&& summaryLps.getBasicLpsId() != null && summaryLps.getBasicLpsId() != 0) {

			// For Basic Lps data
			Optional<BasicLps> basicLpsDetails = basicLpsRepository.findByBasicLpsId(summaryLps.getBasicLpsId());

			// For Air Termination data
			Optional<AirTermination> lpsAirDisc = airTerminationLpsRepository
					.findByBasicLpsId(summaryLps.getBasicLpsId());

			// For Down Conductor data
			Optional<DownConductorReport> downConductorDetails = downConductorRepository
					.findByBasicLpsId(summaryLps.getBasicLpsId());

			// For Earthing Lps data
			Optional<EarthingReport> earthingLpsDetails = earthingLpsRepository
					.findByBasicLpsId(summaryLps.getBasicLpsId());

			// For SPD data
			Optional<SpdReport> spdDetails = spdRepository.findByBasicLpsId(summaryLps.getBasicLpsId());

			// For Seperation Distance data
			Optional<SeperationDistanceReport> separateDistanceDetails = seperationDistanceRepository
					.findByBasicLpsId(summaryLps.getBasicLpsId());


			// For Earth Stud data
			Optional<EarthStudReport> earthStudDetails = earthStudRepository
					.findByBasicLpsId(summaryLps.getBasicLpsId());

			if (!basicLpsDetails.isPresent() && !lpsAirDisc.isPresent() && !downConductorDetails.isPresent()
					&& !earthingLpsDetails.isPresent() && !separateDistanceDetails.isPresent()
					&& !spdDetails.isPresent() && !earthStudDetails.isPresent()) {
				logger.error("Please enter details for all previous steps to proceed further");
				throw new SummaryLpsException("Please enter details for all previous steps to proceed further");
			} else if (!basicLpsDetails.isPresent()) {
				logger.error("Please enter Basic Information step to proceed further");
				throw new SummaryLpsException("Please enter Basic Information step to proceed further");
			} else if (!lpsAirDisc.isPresent()) {
				logger.error("Please enter Air Termination step to proceed further");
				throw new SummaryLpsException("Please enter Air Termination step to proceed further");
			} else if (!downConductorDetails.isPresent()) {
				logger.error("Please enter Down Conductors step to proceed further");
				throw new SummaryLpsException("Please enter Down Conductors step to proceed further");
			} else if (!earthingLpsDetails.isPresent()) {
				logger.error("Please enter Earthing step to proceed further");
				throw new SummaryLpsException("Please enter Earthing step to proceed further");
			} else if (!spdDetails.isPresent()) {
				logger.error("Please enter SPD step to proceed further");
				throw new SummaryLpsException("Please enter SPD step to proceed further");
			} else if (!separateDistanceDetails.isPresent()) {
				logger.error("Please enter Seperation Distance step to proceed further");
				throw new SummaryLpsException("Please enter Seperation Distance step to proceed further");
			} else if (!earthStudDetails.isPresent()) {
				logger.error("Please enter Equipotential Distance step to proceed further");
				throw new SummaryLpsException("Please enter Equipotential Distance step to proceed further");
			}

			if (basicLpsDetails.isPresent()
					&& basicLpsDetails.get().getBasicLpsId().equals(summaryLps.getBasicLpsId())) {

				Optional<SummaryLps> summaryRepo = summaryLpsRepository.findById(summaryLps.getSummaryLpsId());
				if (summaryRepo.isPresent()
						&& summaryRepo.get().getSummaryLpsId().equals(summaryLps.getSummaryLpsId()) 
						&& summaryLps.getSummaryLpsBuildings() != null 
						&& summaryLps.getSummaryLpsBuildings().size() > 0
						&& summaryLps.getSummaryLpsDeclaration()!=null 
						&& summaryLps.getSummaryLpsDeclaration().size()==2) {
					checkBuildingCountWithId(summaryLps.getSummaryLpsBuildings());									   
 					summaryLps.setUpdatedDate(LocalDateTime.now());
					summaryLps.setUpdatedBy(userFullName.findByUserName(summaryLps.getUserName()));
					summaryLpsRepository.save(summaryLps);
					logger.debug("Summary Lps Report Details Successfully Updated in DB");
					userFullName.addUpdatedByandDate(summaryLps.getBasicLpsId(),userFullName.findByUserName(summaryLps.getUserName()),"");
					logger.debug("Basic Lps UpdatedBy and UpdatedDate by Summary");

					if (!isSubmitted) {
						uploadPdfDetails(summaryLps.getUserName(), summaryLps.getBasicLpsId(), lpsAirDisc,
								downConductorDetails, earthingLpsDetails, spdDetails, separateDistanceDetails,
								earthStudDetails);
						logger.debug("Summary Lps Details Sucessfully Submitted");
						return "Summary Lps Details Sucessfully Submitted";
					} else {
						logger.debug("Summary Lps Details Sucessfully Updated");
						return "Summary Lps Details Sucessfully Updated";
					}

				} else {
					logger.error("Please fill all the fields before clicking next button");
					throw new SummaryLpsException("Please fill all the fields before clicking next button");
				}

			} else {
				logger.error("Given Basic LPS Id is Not Registered in Basic LPS");
				throw new SummaryLpsException("Given Basic LPS Id is Not Registered in Basic LPS");
			}
		} else {
			logger.error("Invalid Inputs");
			throw new SummaryLpsException("Invalid Inputs");
		}
	}


	private void checkBuildingCountWithId(List<SummaryLpsBuildings> summaryLpsBuildings) throws SummaryLpsException {
 
		for (SummaryLpsBuildings summaryLpsBuilding : summaryLpsBuildings) {
			Optional<SummaryLpsBuildings> buildingRepo = summaryBuildingRepository
					.findByBuildingCount(summaryLpsBuilding.getBuildingCount());

			//new building
			if (null == summaryLpsBuilding.getSummaryLpsBuildingsId() && !buildingRepo.isPresent() && null != buildingRepo.get()) {
				logger.error(buildingRepo.get().getBuildingCount() +"this building already Present");
				throw new SummaryLpsException(buildingRepo.get().getBuildingCount() +"this building already Present");
				
			}
			//existing record
			if (buildingRepo.isPresent() && null != buildingRepo.get()
					&& null != buildingRepo.get().getSummaryLpsBuildingsId() 
					&&  !buildingRepo.get().getSummaryLpsBuildingsId().equals(summaryLpsBuilding.getSummaryLpsBuildingsId())) {
				logger.error(buildingRepo.get().getBuildingCount() +" Already this BuildingCount Available");
				throw new SummaryLpsException("Given buildings are already available in our system, Please create new building");
			}
		}

	}
																												   


	@Override
	public List<SummaryLps> retrieveSummaryLpsDetails(String userName, Integer basicLpsId) throws SummaryLpsException {
		if (userName != null) {
			logger.info("Called retrieveSummaryLpsDetails function");
			List<SummaryLps> summaryList = new ArrayList<SummaryLps>();
			List<SummaryLps> summaryLpsRepo = summaryLpsRepository.findByBasicLpsId(basicLpsId);
			if (summaryLpsRepo != null && !summaryLpsRepo.isEmpty()) {
				for (SummaryLps summaryLpsItr : summaryLpsRepo) {
					if (!summaryLpsItr.getFlag().equalsIgnoreCase("R")) {
						summaryLpsItr.setSummaryLpsBuildings(
								findNonRemovedObjects.findNonRemovedSummaryBuildings(summaryLpsItr));
						logger.debug("Successfully done findNonRemovedSummaryBuildings() call");
						summaryList.add(summaryLpsItr);
					}
				}
				logger.info("Ended retrieveSummaryLpsDetails function");
				return summaryList;
			} else {
				logger.error("Given UserName & Id doesn't exist in Summary Lps Report Details");
				return new ArrayList<SummaryLps>();
			}
		} else {
			logger.error("Invalid Inputs");
			throw new SummaryLpsException("Invalid Inputs");
		}
	}
	
	@Transactional
	private void uploadPdfDetails(String userName,Integer basicLpsId, Optional<AirTermination> lpsAirDisc, Optional<DownConductorReport> downConductorDetails,
			Optional<EarthingReport> earthingLpsDetails, Optional<SpdReport> spdDetails, Optional<SeperationDistanceReport> separateDistanceDetails, Optional<EarthStudReport> earthStudDetails) 
			throws BasicLpsException, AirTerminationException, DownConductorException, EarthingLpsException,
			SPDException, EarthStudException, SummaryLpsException, Exception {
		 
		//singleton object for finding pageNo
		PageNumberEvent pageNumObject = PageNumberEvent.getPageNumObject();
		Map<String, Map<Integer, String>> indexNumberDeatils = new HashMap<String, Map<Integer, String>>();
 		Optional<BasicLps> basicLpsDetails = basicLpsRepository.findByBasicLpsId(basicLpsId);
		if (basicLpsDetails.isPresent() && basicLpsDetails.get().getBasicLpsId().equals(basicLpsId)) {
			logger.debug("Basic Lps Report Details Successfully Updated as All Steps Completed in DB");

			try {
	
				// LPS PDF upload OnSubmit time
				printBasicLpsService.printBasicLps(userName, basicLpsId, basicLpsDetails,pageNumObject,indexNumberDeatils);
				logger.debug("PDF printBasicLps() function called successfully");

				printAirTerminationService.printAirTermination(userName, basicLpsId, basicLpsDetails, lpsAirDisc,pageNumObject,indexNumberDeatils);
				logger.debug("PDF printAirTermination() function called successfully");

				printDownConductorService.printDownConductor(userName, basicLpsId, basicLpsDetails,
						downConductorDetails,pageNumObject,indexNumberDeatils);
				logger.debug("PDF printDownConductor() function called successfully");

				printEarthingLpsService.printEarthingLpsDetails(userName, basicLpsId, basicLpsDetails,
						earthingLpsDetails,pageNumObject,indexNumberDeatils);
				logger.debug("PDF printTesting() function called successfully");

				printSPDService.printSPD(userName, basicLpsId, basicLpsDetails, spdDetails, pageNumObject, indexNumberDeatils);
				logger.debug("PDF printSPD() function called successfully");

				printSDandEarthStudService.printSDandEarthStud(userName, basicLpsId, basicLpsDetails,
						separateDistanceDetails, earthStudDetails, pageNumObject, indexNumberDeatils);
				logger.debug("PDF printSDandEarthStud() function called successfully");
				printSummaryLpsService.printLpsSummaryDetails(userName, basicLpsId, basicLpsDetails,
						separateDistanceDetails, earthStudDetails, pageNumObject, indexNumberDeatils);
				logger.debug("PDF printLpsSummaryDetails() function called successfully");
				new PrintIndexServiceImpl().printIndexPage(indexNumberDeatils);
				
				printFinalPDFService.printFinalPDF(userName, basicLpsId, basicLpsDetails.get().getProjectName());
				logger.debug("PDF printFinalPDF() function called successfully");

 				basicLpsDetails.get().setAllStepsCompleted("AllStepCompleted");
				basicLpsRepository.save(basicLpsDetails.get());
			}
			catch (Exception e) {
				logger.error("PDF creation failed"+e.getMessage());
				throw new Exception("PDF creation failed");
			}

		} else {
			logger.error("Basic LPS Id Information not Available in Basic LPS Id Details");
			throw new SummaryLpsException("Basic LPS Id Information not Available in Basic LPS Id Details");
		}
	}
}
