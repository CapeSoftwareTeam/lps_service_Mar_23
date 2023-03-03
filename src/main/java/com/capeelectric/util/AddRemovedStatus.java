/**
 * 
 */
package com.capeelectric.util;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.capeelectric.exception.AirTerminationException;
import com.capeelectric.model.DownConductorDescription;
import com.capeelectric.model.EarthElectrodeTesting;
import com.capeelectric.model.EarthStudDescription;
import com.capeelectric.model.EarthingDescription;
import com.capeelectric.model.EarthingLpsDescription;
import com.capeelectric.model.EarthingReport;
import com.capeelectric.model.LpsAirDiscription;
import com.capeelectric.model.ResponseFile;
import com.capeelectric.model.SPD;
import com.capeelectric.model.SeparateDistance;
import com.capeelectric.model.SeparateDistanceDownConductors;
import com.capeelectric.model.SeperationDistanceDescription;
import com.capeelectric.model.SeperationDistanceReport;
import com.capeelectric.model.SpdDescription;
import com.capeelectric.model.SpdReport;
import com.capeelectric.model.SummaryLps;
import com.capeelectric.model.SummaryLpsBuildings;
import com.capeelectric.repository.DownConductorListRepository;
import com.capeelectric.repository.EarthStudListRepository;
import com.capeelectric.repository.EarthingLpsListRepository;
import com.capeelectric.repository.FileDBRepository;
import com.capeelectric.repository.SPDListRepository;
import com.capeelectric.repository.SeperationDistanceListRepository;
import com.capeelectric.repository.SummaryLpsListRepository;
import com.capeelectric.repository.SummaryLpsRepository;

/**
 * @author CAPE-SOFTWARE
 *
 */

@Configuration
public class AddRemovedStatus {
	private static final Logger logger = LoggerFactory.getLogger(AddRemovedStatus.class);

	@Autowired
	private DownConductorListRepository downConductorListRepository;

	@Autowired
	private EarthingLpsListRepository earthingLpsListRepository;

	@Autowired
	private SPDListRepository spdListRepository;

	@Autowired
	private SeperationDistanceListRepository seperationDistanceListRepository;

	@Autowired
	private EarthStudListRepository earthStudListRepository;

	@Autowired
	private SummaryLpsListRepository summaryLpsListRepository;

	@Autowired
	private SummaryLpsRepository summaryLpsRepository;
	
	@Autowired
	private FileDBRepository fileDBRepository;
	
	@Autowired
	private UpdateBuildingCountToFile updateFile;

	// Method for adding R status in Down Conductors
	public void addRemoveStatusInDownConductors(List<LpsAirDiscription> lpsAirDiscription)
			throws AirTerminationException {
		logger.info("Called addRemoveStatusInDownConductors function");

		for (LpsAirDiscription lpsAirDiscriptionItr : lpsAirDiscription) {
			if (lpsAirDiscriptionItr != null && lpsAirDiscriptionItr.getBuildingCount() != null) {
				try {
					DownConductorDescription downConductorDescriptionRepo = downConductorListRepository
							.findByBuildingCount(lpsAirDiscriptionItr.getBuildingCount());
					if (downConductorDescriptionRepo != null && downConductorDescriptionRepo.getBuildingCount()
							.equals(lpsAirDiscriptionItr.getBuildingCount())) {

						logger.debug("downConductorDescriptionRepo available for building count:"
								+ lpsAirDiscriptionItr.getBuildingCount());
						Boolean flagSave = false;
						if (lpsAirDiscriptionItr.getFlag().equalsIgnoreCase("R")) {
							removedFileBaseOnBuildingCount(lpsAirDiscriptionItr.getBuildingCount());
							downConductorDescriptionRepo.setFlag("R");
							flagSave = true;
						} else if (!downConductorDescriptionRepo.getBuildingName()
								.equalsIgnoreCase(lpsAirDiscriptionItr.getBuildingName())
								|| !downConductorDescriptionRepo.getBuildingNumber()
										.equals(lpsAirDiscriptionItr.getBuildingNumber())) {
							downConductorDescriptionRepo.setBuildingNumber(lpsAirDiscriptionItr.getBuildingNumber());
							downConductorDescriptionRepo.setBuildingName(lpsAirDiscriptionItr.getBuildingName());
							flagSave = true;
						}

						if (flagSave) {
							logger.debug("Building count:" + lpsAirDiscriptionItr.getBuildingCount()
									+ "for Downconductor updated with status R");
							downConductorListRepository.save(downConductorDescriptionRepo);
						}

					}
				} catch (Exception e) {
					logger.error("Please check removed Air Termination Building data not available in Down Conductor"
							+ e.getMessage());
					throw new AirTerminationException(
							"Please check removed Air Termination Building data not available in Down Conductor"
									+ e.getMessage());
				}
			}
		}
		logger.info("Ended addRemoveStatusInDownConductors function");
	}

	// Method for adding R status in Earthing Lps
	public void addRemoveStatusInEarthingLps(List<LpsAirDiscription> lpsAirDiscription) throws AirTerminationException {
		logger.info("Called addRemoveStatusInEarthingLps function");

		for (LpsAirDiscription lpsAirDiscriptionItr : lpsAirDiscription) {
			if (lpsAirDiscriptionItr != null && lpsAirDiscriptionItr.getBuildingCount() != null) {
				try {
					EarthingLpsDescription earthingLpsDescriptionRepo = earthingLpsListRepository
							.findByBuildingCount(lpsAirDiscriptionItr.getBuildingCount());
					if (earthingLpsDescriptionRepo != null && earthingLpsDescriptionRepo.getBuildingCount()
							.equals(lpsAirDiscriptionItr.getBuildingCount())) {

						Boolean flagSave = false;

						if (lpsAirDiscriptionItr.getFlag().equalsIgnoreCase("R")) {
							earthingLpsDescriptionRepo.setFlag("R");
							flagSave = true;
						}

						else if (!earthingLpsDescriptionRepo.getBuildingName()
								.equalsIgnoreCase(lpsAirDiscriptionItr.getBuildingName())
								|| !earthingLpsDescriptionRepo.getBuildingNumber()
										.equals(lpsAirDiscriptionItr.getBuildingNumber())) {
							earthingLpsDescriptionRepo.setBuildingNumber(lpsAirDiscriptionItr.getBuildingNumber());
							earthingLpsDescriptionRepo.setBuildingName(lpsAirDiscriptionItr.getBuildingName());
							flagSave = true;
						}

						if (flagSave) {
							logger.debug("Building count:" + lpsAirDiscriptionItr.getBuildingCount()
									+ "for Earthing Lps updated with status R");
							earthingLpsListRepository.save(earthingLpsDescriptionRepo);
						}

					}
				} catch (Exception e) {
					logger.debug("Please check removed Air Termination Building data not available in Earthing Lps"
							+ e.getMessage());
					throw new AirTerminationException(
							"Please check removed Air Termination Building data not available in Earthing Lps"
									+ e.getMessage());
				}
			}
		}
		logger.info("Ended addRemoveStatusInEarthingLps function");
	}

	// Method for adding R status in SPD
	public void addRemoveStatusInSpd(List<LpsAirDiscription> lpsAirDiscription) throws AirTerminationException {
		logger.info("Called addRemoveStatusInSpd function");

		for (LpsAirDiscription lpsAirDiscriptionItr : lpsAirDiscription) {
			if (lpsAirDiscriptionItr != null && lpsAirDiscriptionItr.getBuildingCount() != null) {
				try {
					SPD spdRepo = spdListRepository.findByBuildingCount(lpsAirDiscriptionItr.getBuildingCount());
					if (spdRepo != null && spdRepo.getBuildingCount().equals(lpsAirDiscriptionItr.getBuildingCount())) {
						Boolean flagSave = false;

						if (lpsAirDiscriptionItr.getFlag().equalsIgnoreCase("R")) {
							spdRepo.setFlag("R");
							flagSave = true;
						}

						else if (!spdRepo.getBuildingName().equalsIgnoreCase(lpsAirDiscriptionItr.getBuildingName())
								|| !spdRepo.getBuildingNumber().equals(lpsAirDiscriptionItr.getBuildingNumber())) {
							spdRepo.setBuildingNumber(lpsAirDiscriptionItr.getBuildingNumber());
							spdRepo.setBuildingName(lpsAirDiscriptionItr.getBuildingName());
							flagSave = true;
						}

						if (flagSave) {
							logger.debug("Building count:" + lpsAirDiscriptionItr.getBuildingCount()
									+ "for Earthing Lps updated with status R");
							spdListRepository.save(spdRepo);
						}
					}
				} catch (Exception e) {
					logger.debug(
							"Please check removed Air Termination Building data not available in SPD" + e.getMessage());
					throw new AirTerminationException(
							"Please check removed Air Termination Building data not available in SPD" + e.getMessage());
				}
			}
		}
		logger.info("Ended addRemoveStatusInSpd function");
	}

	// Method for adding R status in Seperation Distance
	public void addRemoveStatusInSeperationDistance(List<LpsAirDiscription> lpsAirDiscription)
			throws AirTerminationException {
		logger.info("Called addRemoveStatusInSeperationDistance function");

		for (LpsAirDiscription lpsAirDiscriptionItr : lpsAirDiscription) {
			if (lpsAirDiscriptionItr != null && lpsAirDiscriptionItr.getBuildingCount() != null) {
				try {
					SeperationDistanceDescription seperationDistanceDescriptionRepo = seperationDistanceListRepository
							.findByBuildingCount(lpsAirDiscriptionItr.getBuildingCount());
					if (seperationDistanceDescriptionRepo != null && seperationDistanceDescriptionRepo
							.getBuildingCount().equals(lpsAirDiscriptionItr.getBuildingCount())) {
						logger.debug("seperationDistanceDescriptionRepo available for building count:"
								+ lpsAirDiscriptionItr.getBuildingCount());
						Boolean flagSave = false;

						if (lpsAirDiscriptionItr.getFlag().equalsIgnoreCase("R")) {
							seperationDistanceDescriptionRepo.setFlag("R");
							flagSave = true;
						} else if (!seperationDistanceDescriptionRepo.getBuildingName()
								.equalsIgnoreCase(lpsAirDiscriptionItr.getBuildingName())
								|| !seperationDistanceDescriptionRepo.getBuildingNumber()
										.equals(lpsAirDiscriptionItr.getBuildingNumber())) {
							seperationDistanceDescriptionRepo
									.setBuildingNumber(lpsAirDiscriptionItr.getBuildingNumber());
							seperationDistanceDescriptionRepo.setBuildingName(lpsAirDiscriptionItr.getBuildingName());
							flagSave = true;
						}

						if (flagSave) {
							logger.debug("Building count:" + lpsAirDiscriptionItr.getBuildingCount()
									+ "for Earthing Lps updated with status R");
							seperationDistanceListRepository.save(seperationDistanceDescriptionRepo);
						}
					}
				} catch (Exception e) {
					logger.debug(
							"Please check removed Air Termination Building data not available in Seperation Distance"
									+ e.getMessage());
					throw new AirTerminationException(
							"Please check removed Air Termination Building data not available in Seperation Distance"
									+ e.getMessage());
				}
			}
		}
		logger.info("Ended addRemoveStatusInSeperationDistance function");

	}

	// Method for adding R status in Earth Stud
	public void addRemoveStatusInEarthStud(List<LpsAirDiscription> lpsAirDiscription) throws AirTerminationException {
		logger.info("Called addRemoveStatusInEarthStud function");

		for (LpsAirDiscription lpsAirDiscriptionItr : lpsAirDiscription) {
			if (lpsAirDiscriptionItr != null && lpsAirDiscriptionItr.getBuildingCount() != null) {
				try {
					EarthStudDescription earthStudDescriptionRepo = earthStudListRepository
							.findByBuildingCount(lpsAirDiscriptionItr.getBuildingCount());
					if (earthStudDescriptionRepo != null && earthStudDescriptionRepo.getBuildingCount()
							.equals(lpsAirDiscriptionItr.getBuildingCount())) {
						logger.debug("earthStudDescriptionRepo available for building count:"
								+ lpsAirDiscriptionItr.getBuildingCount());
						Boolean flagSave = false;

						if (lpsAirDiscriptionItr.getFlag().equalsIgnoreCase("R")) {
							earthStudDescriptionRepo.setFlag("R");
							flagSave = true;
						}

						else if (!earthStudDescriptionRepo.getBuildingName()
								.equalsIgnoreCase(lpsAirDiscriptionItr.getBuildingName())
								|| !earthStudDescriptionRepo.getBuildingNumber()
										.equals(lpsAirDiscriptionItr.getBuildingNumber())) {
							earthStudDescriptionRepo.setBuildingNumber(lpsAirDiscriptionItr.getBuildingNumber());
							earthStudDescriptionRepo.setBuildingName(lpsAirDiscriptionItr.getBuildingName());
							flagSave = true;
						}

						if (flagSave) {
							logger.debug("Building count:" + lpsAirDiscriptionItr.getBuildingCount()
									+ "for Earthing Lps updated with status R");
							earthStudListRepository.save(earthStudDescriptionRepo);
						}
					}
				} catch (Exception e) {
					logger.debug("Please check removed Air Termination Building data not available in Earth Stud"
							+ e.getMessage());
					throw new AirTerminationException(
							"Please check removed Air Termination Building data not available in Earth Stud"
									+ e.getMessage());
				}
			}
		}
		logger.info("Ended addRemoveStatusInEarthStud function");

	}

	// Method for adding R status in Earth Stud
	public void addRemoveStatusInSummaryLps(List<LpsAirDiscription> lpsAirDiscription, String userName,
			Integer basiclpsId) throws AirTerminationException {
		logger.info("Called addRemoveStatusInSummaryLps function");

		for (LpsAirDiscription lpsAirDiscriptionItr : lpsAirDiscription) {
			if (lpsAirDiscriptionItr != null && lpsAirDiscriptionItr.getBuildingCount() != null ) {
				try {
					 SummaryLpsBuildings summaryLpsRepo = summaryLpsListRepository
					 .findByBuildingCount(lpsAirDiscriptionItr.getBuildingCount());
//					List<SummaryLps> summaryLpsRepo = summaryLpsRepository.findByUserNameAndBasicLpsId(userName,
//							basiclpsId);

				//	for (SummaryLps summaryLps : summaryLpsRepo) {
						//for (SummaryLpsBuildings summaryLpsBuilding : summaryLpsRepo.getSummaryLpsBuildings()) {

							if (null != summaryLpsRepo && summaryLpsRepo.getBuildingCount().equals(summaryLpsRepo.getBuildingCount())) {
								logger.debug("summaryLpsBuildingsRepo available for building count:"
										+ lpsAirDiscriptionItr.getBuildingCount());
								Boolean flagSave = false;

								if (lpsAirDiscriptionItr.getFlag().equalsIgnoreCase("R")) {
									summaryLpsRepo.setFlag("R");
									flagSave = true;
								}

								else if (!summaryLpsRepo.getBuildingName()
										.equalsIgnoreCase(lpsAirDiscriptionItr.getBuildingName())
										|| !summaryLpsRepo.getBuildingNumber()
												.equals(lpsAirDiscriptionItr.getBuildingNumber())) {
									summaryLpsRepo.setBuildingNumber(lpsAirDiscriptionItr.getBuildingNumber());
									summaryLpsRepo.setBuildingName(lpsAirDiscriptionItr.getBuildingName());
									flagSave = true;
								}

								if (flagSave) {
									logger.debug("Building count:" + lpsAirDiscriptionItr.getBuildingCount()
											+ "for Earthing Lps updated with status R");
									summaryLpsListRepository.save(summaryLpsRepo);
								}
							//}
						//}

					}
				} catch (Exception e) {
					logger.debug("Please check removed Air Termination Building data not available in Summary"
							+ e.getMessage());
					throw new AirTerminationException(
							"Please check removed Air Termination Building data not available in Summary"
									+ e.getMessage());
				}
			}
		}
		logger.info("Ended addRemoveStatusInSummaryLps function");

	}
	private void removedFileBaseOnBuildingCount(Integer buildingCount) {
		List<ResponseFile> removeAllBuildings = fileDBRepository.findByBuildingCount(buildingCount);
		if (!removeAllBuildings.isEmpty() && removeAllBuildings.size() > 0) {
			fileDBRepository.deleteAll(removeAllBuildings);
		}
	}
	
	// Remove earthing api related remarks in summaryLPS
		public void removeEarthingSummaryLpsObservations(EarthingReport earthingReportRepo) {
			for (EarthingLpsDescription earthingLpsDescription : earthingReportRepo.getEarthingLpsDescription()) {
				List<EarthingDescription> earthingDescriptionRepo = earthingLpsDescription.getEarthingDescription();
				for (EarthingDescription earthingDescription : earthingDescriptionRepo) {
					if (earthingDescription.getFlag().equalsIgnoreCase("R")) {
						updateFile.remoeSummaryObservationsData(earthingDescription.getEarthDescriptionId());
					}
				}
				List<EarthElectrodeTesting> earthElectrodeTesting = earthingLpsDescription.getEarthElectrodeTesting();
				for (EarthElectrodeTesting earthElectrodeTestingItr : earthElectrodeTesting) {
					if (earthElectrodeTestingItr.getFlag().equalsIgnoreCase("R")) {
						updateFile.remoeSummaryObservationsData(earthElectrodeTestingItr.getEarthingElectrodeTestingId());
					}
				}
			}
		}
		
		// remove spd api remarks in summaryObservation
		public void removeSpdSummaryLpsObservation(SpdReport spdReport) {
			List<SPD> spdList = spdReport.getSpd();
			for(SPD spd:spdList) {
				if(spd !=null && spd.getSpdDescription().size() !=0) {
					List<SpdDescription> spdDescription = spd.getSpdDescription();
					for (SpdDescription spdDescriptionItr : spdDescription) {
						if (spdDescriptionItr.getFlag().equalsIgnoreCase("R")) {
							updateFile.remoeSummaryObservationsData(spdDescriptionItr.getSpdDescriptionId());
						}
					}
				}
				
			}
		}
		
		//remove seperation API remarks in summaryObservation
		public void removeSeperationSummaryObservation(SeperationDistanceReport seperationDistanceReport) {

			List<SeperationDistanceDescription> seperationDistanceDescription = seperationDistanceReport
					.getSeperationDistanceDescription();
			for (SeperationDistanceDescription seperationDistanceDescriptionItr : seperationDistanceDescription) {

				if (seperationDistanceDescriptionItr.getFlag().equalsIgnoreCase("R")) {
					List<SeparateDistance> separateDistance = seperationDistanceDescriptionItr.getSeparateDistance();
					List<SeparateDistanceDownConductors> separateDistanceDownConductors = seperationDistanceDescriptionItr
							.getSeparateDistanceDownConductors();

					for (SeparateDistance separateDistanceItr : separateDistance) {
						if (separateDistanceItr.getFlag().equalsIgnoreCase("R")) {
							updateFile.remoeSummaryObservationsData(separateDistanceItr.getSeperationDistanceDescId());

						}
					}

					for (SeparateDistanceDownConductors separateDistanceDownConductorItr : separateDistanceDownConductors) {
						if (separateDistanceDownConductorItr.getFlag().equalsIgnoreCase("R")) {
							updateFile.remoeSummaryObservationsData(separateDistanceDownConductorItr.getSeperationDistanceDownConductorId());

						}
					}
				}

			}
		}
	}