package com.capeelectric.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.capeelectric.exception.AirTerminationException;
import com.capeelectric.model.AirBasicDescription;
import com.capeelectric.model.AirClamps;
import com.capeelectric.model.AirConnectors;
import com.capeelectric.model.AirExpansion;
import com.capeelectric.model.AirHolderDescription;
import com.capeelectric.model.AirHolderList;
import com.capeelectric.model.AirMeshDescription;
import com.capeelectric.model.AirTermination;
import com.capeelectric.model.BasicLps;
import com.capeelectric.model.LpsAirDiscription;
import com.capeelectric.model.LpsVerticalAirTermination;
import com.capeelectric.model.VerticalAirTerminationList;
import com.capeelectric.repository.AirTerminationLpsRepository;
import com.capeelectric.repository.BasicLpsRepository;
import com.capeelectric.service.impl.AirTerminationLpsServiceImpl;
import com.capeelectric.util.AddRemovedStatus;
import com.capeelectric.util.FindNonRemovedObjects;
import com.capeelectric.util.UpdateBuildingCountToFile;
import com.capeelectric.util.UserFullName;

@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class AirTerminationLpsServiceTest {

	private static final Logger logger = LoggerFactory.getLogger(AirTerminationLpsServiceTest.class);

	@MockBean
	private UserFullName userFullName;

	@MockBean
	private AirTerminationLpsRepository airTerminationLpsRepository;

	@MockBean
	private BasicLpsRepository basicLpsRepository;
	
	@MockBean
	private AirTerminationException airTerminationException;
	
	@MockBean
	private FindNonRemovedObjects findNonRemovedObjects;
	
	@MockBean
	private AddRemovedStatus addRemovedStatus;
	
	@MockBean
	private UpdateBuildingCountToFile updateBuildingCountToFile;
	
	@InjectMocks
	private AirTerminationLpsServiceImpl airTerminationLpsServiceImpl;

	private AirTermination airTermination;

	{
		airTermination = new AirTermination();
		airTermination.setBasicLpsId(1);
		airTermination.setUserName("Inspector@gmail.com");
		airTermination.setAirTerminationId(2);
		LpsAirDiscription lpsAirDiscription = new LpsAirDiscription();
		lpsAirDiscription.setFlag("A");
		List<LpsAirDiscription> arrayList = new ArrayList<LpsAirDiscription>();
		arrayList.add(lpsAirDiscription);
		airTermination.setLpsAirDescription(arrayList);
		
	}
	private BasicLps basicLps;

	{
		basicLps = new BasicLps();
		basicLps.setBasicLpsId(1);
		basicLps.setClientName("Inspector@gmail.com");
		
	}

	@Test
	public void testAddAirTerminationLpsDetails() throws AirTerminationException {
		List<LpsAirDiscription> listofLpsAirDiscription= new ArrayList<LpsAirDiscription>();
		listofLpsAirDiscription.add(new LpsAirDiscription());
		
		ArrayList<LpsVerticalAirTermination> lpsVerticalAirTermination = new ArrayList<LpsVerticalAirTermination>();
		LpsVerticalAirTermination lps = new LpsVerticalAirTermination();
		lps.setFlag("A");
		
		ArrayList<VerticalAirTerminationList> verticalAirTerminationList = new ArrayList<VerticalAirTerminationList>();
		VerticalAirTerminationList verticalLps = new VerticalAirTerminationList();
		verticalLps.setFlag("A");
		verticalAirTerminationList.add(verticalLps);		
		lps.setVerticalAirTerminationList(verticalAirTerminationList);
		lpsVerticalAirTermination.add(lps);
	
		ArrayList<AirClamps> airClampsList = new ArrayList<AirClamps>();
		AirClamps airClamps = new AirClamps();
		airClamps.setFlag("A");		
		airClampsList.add(airClamps); 
		
		ArrayList<AirConnectors> airConnectorsList = new ArrayList<AirConnectors>();
		AirConnectors airConnectors = new AirConnectors();
		airConnectors.setFlag("A");		
		airConnectorsList.add(airConnectors);
		
		ArrayList<AirExpansion> airExpansionList = new ArrayList<AirExpansion>();
		AirExpansion airExpansion = new AirExpansion();
		airExpansion.setFlag("A");		
		airExpansionList.add(airExpansion);
		
		ArrayList<AirHolderDescription> airHolderDescriptionList = new ArrayList<AirHolderDescription>();
		AirHolderDescription airHolderDescription = new AirHolderDescription();
		airHolderDescription.setFlag("A");	
		
		ArrayList<AirHolderList> listOfairHolderList = new ArrayList<AirHolderList>();
		AirHolderList airHolderList = new AirHolderList();
		airHolderList.setFlag("A");
		listOfairHolderList.add(airHolderList);
		airHolderDescription.setAirHolderList(listOfairHolderList);
		airHolderDescriptionList.add(airHolderDescription);
		
		ArrayList<AirMeshDescription> airMeshDescriptionList = new ArrayList<AirMeshDescription>();
		AirMeshDescription airMeshDescription = new AirMeshDescription();
		airMeshDescription.setFlag("A");		
		airMeshDescriptionList.add(airMeshDescription);
		
		ArrayList<AirBasicDescription> airBasicDescriptionList = new ArrayList<AirBasicDescription>();
		AirBasicDescription airBasicDescription = new AirBasicDescription();
		airBasicDescription.setFlag("A");		
		airBasicDescriptionList.add(airBasicDescription);
		
		LpsAirDiscription lpsAirDiscription = listofLpsAirDiscription.get(0);
		lpsAirDiscription.setLpsVerticalAirTermination(lpsVerticalAirTermination);
		lpsAirDiscription.setAirClamps(airClampsList);
		lpsAirDiscription.setAirConnectors(airConnectorsList);
		lpsAirDiscription.setAirExpansion(airExpansionList);
		lpsAirDiscription.setAirHolderDescription(airHolderDescriptionList);
		lpsAirDiscription.setAirMeshDescription(airMeshDescriptionList);
		lpsAirDiscription.setAirBasicDescription(airBasicDescriptionList);
		lpsAirDiscription.setBuildingCount(123);
		
		airTermination.setLpsAirDescription(listofLpsAirDiscription);

		
		when(basicLpsRepository.findByBasicLpsId(1)).thenReturn(Optional.of(basicLps));
		when(airTerminationLpsRepository.findByBasicLpsId(2)).thenReturn(Optional.of(airTermination));
		when(airTerminationLpsRepository.save(airTermination)).thenReturn(airTermination);
		airTerminationLpsServiceImpl.addAirTerminationLpsDetails(airTermination);
		
		when(airTerminationLpsRepository.findByBasicLpsId(1)).thenReturn(Optional.of(airTermination));
		AirTerminationException basicLpsException_3 = Assertions.assertThrows(AirTerminationException.class,
				() -> airTerminationLpsServiceImpl.addAirTerminationLpsDetails(airTermination));
		assertEquals(basicLpsException_3.getMessage(), "Given Basic LPS Id is already Available in Air Termination");

		
		basicLps.setBasicLpsId(10);
		AirTerminationException basicLpsException_4 = Assertions.assertThrows(AirTerminationException.class,
				() -> airTerminationLpsServiceImpl.addAirTerminationLpsDetails(airTermination));
		assertEquals(basicLpsException_4.getMessage(), "Given Basic LPS Id is Not Registered in Basic LPS");

		airTermination.setUserName(null);
		AirTerminationException basicLpsException_2 = Assertions.assertThrows(AirTerminationException.class,
				() -> airTerminationLpsServiceImpl.addAirTerminationLpsDetails(airTermination));
		assertEquals(basicLpsException_2.getMessage(), "Invalid Inputs");
		
		
	}

	@Test
	public void testRetrieveAirTerminationLps() throws AirTerminationException {

		List<AirTermination> arrayList = new ArrayList<AirTermination>();
		arrayList.add(airTermination);
		when(airTerminationLpsRepository.findByBasicLpsId(airTermination.getBasicLpsId())).thenReturn(Optional.of(airTermination));

		logger.info("SuccessFlow of Retrieve  AirTerminationLps Obeject");
		airTerminationLpsServiceImpl.retrieveAirTerminationLps(1);
		
		logger.info("Invalid Input flow");
		
		AirTerminationException basicLpsException = Assertions.assertThrows(AirTerminationException.class,
				() -> airTerminationLpsServiceImpl.retrieveAirTerminationLps(null));
		assertEquals(basicLpsException.getMessage(), "Invalid Inputs");
		
		logger.info("Given UserName & Id doesn't exist in Air Termination LPS Details");
	 
		when(airTerminationLpsRepository.findByBasicLpsId(airTermination.getBasicLpsId())).thenReturn(Optional.of(airTermination));
 		 assertNotNull( airTerminationLpsServiceImpl.retrieveAirTerminationLps(12));
 
	}

	@Test
	public void testUpdateAirTerminationLps() throws AirTerminationException {

		airTermination.setUserName("LVsystem@gmail.com");
		airTermination.setAirTerminationId(1);
		when(airTerminationLpsRepository.findById(1)).thenReturn(Optional.of(airTermination));
		airTerminationLpsServiceImpl.updateAirTerminationLps(airTermination);

		AirTermination airTermination_1 = new AirTermination();
		airTermination_1.setBasicLpsId(2);
		airTermination_1.setAirTerminationId(50);
		airTermination_1.setUserName("cape");
		
		when(airTerminationLpsRepository.findById(20)).thenReturn(Optional.of(airTermination));
		AirTerminationException basicLpsException_2 = Assertions.assertThrows(AirTerminationException.class,
				() -> airTerminationLpsServiceImpl.updateAirTerminationLps(airTermination_1));
		assertEquals(basicLpsException_2.getMessage(), "Given Basic LPS Id and LPS Air Description Id is Invalid");
		
		airTermination.setBasicLpsId(null);
		AirTerminationException assertThrows_1 = Assertions.assertThrows(AirTerminationException.class,
				() -> airTerminationLpsServiceImpl.updateAirTerminationLps(airTermination));
		assertEquals(assertThrows_1.getMessage(), "Invalid inputs");
		
	}

}
