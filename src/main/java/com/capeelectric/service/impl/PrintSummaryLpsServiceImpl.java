package com.capeelectric.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capeelectric.exception.SummaryLpsException;
import com.capeelectric.model.BasicLps;
import com.capeelectric.model.EarthStudReport;
import com.capeelectric.model.SeperationDistanceReport;
import com.capeelectric.model.SummaryLps;
import com.capeelectric.model.SummaryLpsBuildings;
import com.capeelectric.model.SummaryLpsDeclaration;
import com.capeelectric.model.SummaryLpsObservation;
import com.capeelectric.repository.SummaryLpsRepository;
import com.capeelectric.service.PrintSummaryLpsService;
import com.capeelectric.util.ContentEvent;
import com.capeelectric.util.PageNumberEvent;
import com.capeelectric.util.PdfPTables;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PrintSummaryLpsServiceImpl implements PrintSummaryLpsService {

	private static final Logger logger = LoggerFactory.getLogger(AirTerminationLpsServiceImpl.class);

	@Autowired
	private SummaryLpsRepository summaryLpsRepository;

	@Override
	public List<SummaryLps> printLpsSummaryDetails(String userName, Integer basicLpsId, Optional<BasicLps> basicLpsDetails, Optional<SeperationDistanceReport> separateDistanceDetails, Optional<EarthStudReport> earthStudDetails, PageNumberEvent pageNum, Map<String, Map<Integer, String>> indexNumberDeatils) throws SummaryLpsException, Exception {
		logger.debug("printLpsSummaryDetails() function called");
		if (userName != null && !userName.isEmpty() && basicLpsId != null && basicLpsId != 0) {
			Document document = new Document(PageSize.A4, 68, 68, 62, 68);

			try {
				
				Map<Integer,String> summaryPgNumDetails = new HashMap<Integer,String>();

				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("LpsSummary.pdf"));

				BasicLps basicLps1 = basicLpsDetails.get();

				List<SummaryLps> lpsSum = summaryLpsRepository.findByUserNameAndBasicLpsId(userName, basicLpsId);
				SummaryLps lpsSummary = lpsSum.get(0);

				List<SummaryLpsBuildings> summaryLPsBuild = lpsSummary.getSummaryLpsBuildings();

				document.open();
				writer.setPageEvent(pageNum); //page number generating

				logger.debug("document opened");
				Font font11B = new Font(BaseFont.createFont(), 11, Font.NORMAL | Font.BOLD, BaseColor.BLACK);
				Font font10B = new Font(BaseFont.createFont(), 10, Font.NORMAL | Font.BOLD, BaseColor.BLACK);
				Font font10N = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);
				
				float[] pointColumnWidths40 = { 100F };
				PdfPTable SRtable = new PdfPTable(pointColumnWidths40);
				SRtable.setWidthPercentage(100); // Width 100%
				SRtable.setSpacingBefore(5f); // Space before table

				// printing Summary and Recommendations
				PdfPCell label2 = new PdfPCell(new Paragraph("Summary and Recommendations", font11B));
				label2.setHorizontalAlignment(Element.ALIGN_CENTER);
				label2.setGrayFill(0.92f);
				label2.setFixedHeight(20f);
				SRtable.addCell(label2);
				document.add(SRtable);

				for (SummaryLpsBuildings summaryLPsBuilding : summaryLPsBuild) {

					float[] pointColumnWidths = { 120F, 80F };

					PdfPTable table1 = new PdfPTable(pointColumnWidths);

					table1.setWidthPercentage(100); // Width 100%
					table1.setSpacingBefore(10f); // Space before table
					table1.setSpacingAfter(5f); // Space after table
					table1.getDefaultCell().setBorder(0);

					// printing building number
					PdfPCell cell1 = new PdfPCell(new Paragraph("Building number:",
							new Font(BaseFont.createFont(), 10, Font.NORMAL | Font.BOLD)));
					cell1.setBackgroundColor(new BaseColor(203, 183, 162));
					cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell1.setBorder(PdfPCell.NO_BORDER);
					table1.addCell(cell1);
					PdfPCell cell2 = new PdfPCell(new Paragraph(summaryLPsBuilding.getBuildingNumber().toString(),
							new Font(BaseFont.createFont(), 10, Font.NORMAL | Font.BOLD)));
					cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell2.setBackgroundColor(new BaseColor(203, 183, 162));
					cell2.setBorder(PdfPCell.NO_BORDER);
					table1.addCell(cell2);
					document.add(table1);
					logger.debug("BuildingCount iteration started for ->"+summaryLPsBuilding.getBuildingCount());
					PdfPTable table2 = new PdfPTable(pointColumnWidths);

					table2.setWidthPercentage(100); // Width 100%
					table2.setSpacingBefore(5f); // Space before table
					table2.setSpacingAfter(5f); // Space after table
					table2.getDefaultCell().setBorder(0);

					// printing building name
					PdfPCell cell3 = new PdfPCell(new Paragraph("Building name:",
							new Font(BaseFont.createFont(), 10, Font.NORMAL | Font.BOLD)));
					cell3.setBackgroundColor(new BaseColor(203, 183, 162));
					cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell3.setBorder(PdfPCell.NO_BORDER);
					table2.addCell(cell3);
					PdfPCell cell4 = new PdfPCell(new Paragraph(summaryLPsBuilding.getBuildingName(),
							new Font(BaseFont.createFont(), 10, Font.NORMAL | Font.BOLD)));
					cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell4.setBackgroundColor(new BaseColor(203, 183, 162));
					cell4.setBorder(PdfPCell.NO_BORDER);
					table2.addCell(cell4);
					document.add(table2);

					summaryPgNumDetails.put(ContentEvent.numberOfContents++,pageNum.getPage()+", Building number:"+summaryLPsBuilding.getBuildingNumber().toString()
							+"    Building name:"+summaryLPsBuilding.getBuildingName());

// ================================================ > AIR TERMINATION	 START HERE <=========================================================

					PdfPTable AirTerminationlabel = new PdfPTable(pointColumnWidths40);
					AirTerminationlabel.setWidthPercentage(100); // Width 100%
					AirTerminationlabel.setSpacingBefore(10f); // Space before table

					// printing airtermination
					PdfPCell label3 = new PdfPCell(new Paragraph("Air Termination", font11B));
					label3.setHorizontalAlignment(Element.ALIGN_CENTER);
					label3.setGrayFill(0.92f);
					label3.setFixedHeight(20f);
					AirTerminationlabel.addCell(label3);
					document.add(AirTerminationlabel);

					PdfPTable airbasicDetails = new PdfPTable(pointColumnWidths40);
					airbasicDetails.setWidthPercentage(100); // Width 100%
					airbasicDetails.setSpacingBefore(10f); // Space before table

					// Observation And Recommendation Heading Label
					float[] pointColumnWidths4 = { 50F, 50F };

					PdfPTable table3 = new PdfPTable(pointColumnWidths4);
					table3.setWidthPercentage(100); // Width 100%
					table3.setSpacingBefore(10f); // Space before table
					// table3.setSpacingAfter(10f);
					table3.setWidthPercentage(100);

					PdfPCell cell6 = new PdfPCell(new Paragraph("Observations", font10B));
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell6.setFixedHeight(20f);
					cell6.setGrayFill(0.92f);
					table3.addCell(cell6);

					PdfPCell cell7 = new PdfPCell(new Paragraph("Recommendation", font10B));
					cell7.setGrayFill(0.92f);
					cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
					table3.addCell(cell7);

					document.add(table3);

					PdfPTable table4 = new PdfPTable(pointColumnWidths4);
					table4.setWidthPercentage(100); // Width 100%
					// table4.setSpacingBefore(5f); // Space before table

					// AT_Basic Details Observation And Recommendation List with Iteration
					List<SummaryLpsObservation> airBasicDescription = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("airBasicDescription"))
							.collect(Collectors.toList());
					Boolean airBasicFalg = true;
					
					summaryPgNumDetails.put(ContentEvent.numberOfContents++,pageNum.getPage()+",Air Termination");
					
					logger.debug("AT_Basic Details Observation Iteration Started");
					for (SummaryLpsObservation airBasicDescriptionOb : airBasicDescription) {

						if (airBasicDescriptionOb.getObservation() != null
								&& !airBasicDescriptionOb.getObservation().isEmpty()
								&& airBasicDescriptionOb.getRecommendation() != null
								&& !airBasicDescriptionOb.getRecommendation().isEmpty()) {
							if (airBasicFalg) {
								PdfPCell airBasicCell = new PdfPCell(
										new Paragraph("1. " + "AT_Basic Details Observation", font10B));
								airBasicCell.setHorizontalAlignment(Element.ALIGN_LEFT);
								airBasicCell.setGrayFill(0.92f);
								airBasicCell.setFixedHeight(20f);
								airbasicDetails.addCell(airBasicCell);
								document.add(airbasicDetails);
								airBasicFalg = false;
							}

							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(airBasicDescriptionOb.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							table4.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(
									new Paragraph(airBasicDescriptionOb.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							table4.addCell(cell37);

						}
					}

					// noremarks cell
					if (airBasicFalg) {
						PdfPTable noRemarksPdfPTable = PdfPTables.noRemarksPdfPTable();

						PdfPCell cell105 = new PdfPCell();
						cell105.setPhrase(new Phrase("1. AT_Basic Details Observation", font10B));
						cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell105.setGrayFill(0.92f);
						cell105.setFixedHeight(20f);
						noRemarksPdfPTable.addCell(cell105);

						PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
						cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
						noRemarksPdfPTable.addCell(cell37);
						document.add(noRemarksPdfPTable);

					}
					document.add(table4);

					// Verticle Termination Main Heading Label
					PdfPTable airVerticlelabel = new PdfPTable(pointColumnWidths40);
					airVerticlelabel.setWidthPercentage(100); // Width 100%
					airVerticlelabel.setSpacingBefore(10f); // Space before table

					// Verticle Termination main Remarks Only one main filed display
					PdfPTable table5 = new PdfPTable(pointColumnWidths4);
					table5.setWidthPercentage(100); // Width 100%

					List<SummaryLpsObservation> lpsVerticalAirTermination = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("lpsVerticalAirTermination"))
							.collect(Collectors.toList());
					Boolean verticalflag = true;
					for (SummaryLpsObservation verticalObservation : lpsVerticalAirTermination) {
						if (verticalObservation.getObservation() != null
								&& !verticalObservation.getObservation().isEmpty()
								&& verticalObservation.getRecommendation() != null
								&& !verticalObservation.getRecommendation().isEmpty()) {

							if (verticalflag) {
								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph("2. " + "AT_Vertical Observation", font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_LEFT);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setFixedHeight(20f);
								airVerticlelabel.addCell(headerlabel1);
								document.add(airVerticlelabel);
								verticalflag = false;
							}
							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(verticalObservation.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							table5.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(
									new Paragraph(verticalObservation.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							table5.addCell(cell37);

						}
					}

					if (verticalflag) {
						PdfPTable noRemarksPdfPTable = PdfPTables.noRemarksPdfPTable();

						PdfPCell observationCell = new PdfPCell();
						observationCell.setPhrase(new Phrase("2. " + "AT_Vertical Observation", font10B));
						observationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						observationCell.setGrayFill(0.92f);
						observationCell.setFixedHeight(20f);
						noRemarksPdfPTable.addCell(observationCell);

						PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
						cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
						noRemarksPdfPTable.addCell(cell37);

						document.add(noRemarksPdfPTable);
					}

					document.add(table5);

					PdfPTable airVerticleListLabel = new PdfPTable(pointColumnWidths4);
					airVerticleListLabel.setWidthPercentage(100); // Width 100%

					List<SummaryLpsObservation> verticalAirTerminationList = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("verticalAirTerminationList"))
							.collect(Collectors.toList());

					// Verticle Termination List With Iteration
					for (SummaryLpsObservation summaryLpsObservation : verticalAirTerminationList) {

						if (summaryLpsObservation.getObservation() != null
								&& !summaryLpsObservation.getObservation().isEmpty()
								&& summaryLpsObservation.getRecommendation() != null
								&& !summaryLpsObservation.getRecommendation().isEmpty()) {
							if (summaryLpsObservation.getHeadingUi() != null
									&& !summaryLpsObservation.getHeadingUi().isEmpty()
									&& summaryLpsObservation.getHeadingUi().contains("AT_Vertical List")) {

								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph(summaryLpsObservation.getHeadingUi(), font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_CENTER);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setColspan(2);
								headerlabel1.setFixedHeight(20f);
								airVerticleListLabel.addCell(headerlabel1);
							}

							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(summaryLpsObservation.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							airVerticleListLabel.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(
									new Paragraph(summaryLpsObservation.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							airVerticleListLabel.addCell(cell37);

						}
					}
 
					document.add(airVerticleListLabel);

					// Mesh Observation Main Heading Label
					PdfPTable MeshObservationHeader = new PdfPTable(pointColumnWidths40);
					MeshObservationHeader.setWidthPercentage(100); // Width 100%
					MeshObservationHeader.setSpacingBefore(10f); // Space before table

					PdfPTable table7 = new PdfPTable(pointColumnWidths4);
					table7.setWidthPercentage(100); // Width 100%
					List<SummaryLpsObservation> airMeshDescription = summaryLPsBuilding.getSummaryLpsObservation()
							.stream()
							.filter(observation -> observation.getObservationComponentDetails().contains("airMesh"))
							.collect(Collectors.toList());
					Boolean airMeshDescriptionFlag = true;
					// Mesh Conductor Observation And Recommendation List with Iteration
					for (SummaryLpsObservation summaryLpsOb : airMeshDescription) {

						if (summaryLpsOb.getObservation() != null && !summaryLpsOb.getObservation().isEmpty()
								&& summaryLpsOb.getRecommendation() != null
								&& !summaryLpsOb.getRecommendation().isEmpty()) {

							if (airMeshDescriptionFlag) {
								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph("3. " + "AT_Mesh Observation", font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_LEFT);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setFixedHeight(20f);
								MeshObservationHeader.addCell(headerlabel1);
								document.add(MeshObservationHeader);

								airMeshDescriptionFlag = false;
							}
							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(summaryLpsOb.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							table7.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(new Paragraph(summaryLpsOb.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							table7.addCell(cell37);
						}
					}
					if (airMeshDescriptionFlag) {
						PdfPTable noRemarksPdfPTable = PdfPTables.noRemarksPdfPTable();

						PdfPCell observationCell = new PdfPCell();
						observationCell.setPhrase(new Phrase("3. " + "AT_Mesh Observation", font10B));
						observationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						observationCell.setGrayFill(0.92f);
						observationCell.setFixedHeight(20f);
						noRemarksPdfPTable.addCell(observationCell);

						PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
						cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
						noRemarksPdfPTable.addCell(cell37);

						document.add(noRemarksPdfPTable);
					}
					document.add(table7);

					// Holder Observation Main Heading Label
					PdfPTable HolderObservationHeader = new PdfPTable(pointColumnWidths40);
					HolderObservationHeader.setWidthPercentage(100); // Width 100%
					HolderObservationHeader.setSpacingBefore(10f); // Space before table

					// Hoders main Remarks
					PdfPTable table8 = new PdfPTable(pointColumnWidths4);
					table8.setWidthPercentage(100); // Width 100%

					List<SummaryLpsObservation> airHolderDescription = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("airHolderDescription"))
							.collect(Collectors.toList());
					Boolean airHolderDescriptionFlag = true;

					for (SummaryLpsObservation observation : airHolderDescription) {

						if (observation.getObservation() != null && !observation.getObservation().isEmpty()
								&& observation.getRecommendation() != null
								&& !observation.getRecommendation().isEmpty()) {

							if (airHolderDescriptionFlag) {
								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph("4. " + "AT_Holder Observation", font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_LEFT);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setFixedHeight(20f);
								HolderObservationHeader.addCell(headerlabel1);
								document.add(HolderObservationHeader);
								airHolderDescriptionFlag = false;
							}

							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(observation.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							table8.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(new Paragraph(observation.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							table8.addCell(cell37);

						}
					}
					if (airHolderDescriptionFlag) {
						PdfPTable noRemarksPdfPTable = PdfPTables.noRemarksPdfPTable();

						PdfPCell observationCell = new PdfPCell();
						observationCell.setPhrase(new Phrase("4. " + "AT_Holder Observation", font10B));
						observationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						observationCell.setGrayFill(0.92f);
						observationCell.setFixedHeight(20f);
						noRemarksPdfPTable.addCell(observationCell);

						PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
						cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
						noRemarksPdfPTable.addCell(cell37);

						document.add(noRemarksPdfPTable);
					}
					document.add(table8);

					PdfPTable HoldersObservListHeader = new PdfPTable(pointColumnWidths4);
					HoldersObservListHeader.setWidthPercentage(100); // Width 100%

					List<SummaryLpsObservation> airHolderList = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("airHolderList"))
							.collect(Collectors.toList());
 					// Hoders Observation List With Iteration
					for (SummaryLpsObservation observation : airHolderList) {

						if (observation.getObservation() != null && !observation.getObservation().isEmpty()
								&& observation.getRecommendation() != null
								&& !observation.getRecommendation().isEmpty()) {

							if (observation.getHeadingUi() != null && !observation.getHeadingUi().isEmpty()
									&& observation.getHeadingUi().contains("AT_Holder List")) {

								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph(observation.getHeadingUi(), font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_CENTER);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setColspan(2);
								headerlabel1.setFixedHeight(20f);
								HoldersObservListHeader.addCell(headerlabel1);

							}

							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(observation.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							HoldersObservListHeader.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(new Paragraph(observation.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							HoldersObservListHeader.addCell(cell37);
						}

					}
					document.add(HoldersObservListHeader);

					// Clamps Observation Main Heading Label
					PdfPTable ClampsObserHeader = new PdfPTable(pointColumnWidths40);
					ClampsObserHeader.setWidthPercentage(100); // Width 100%
					ClampsObserHeader.setSpacingBefore(10f); // Space before table

					PdfPTable table10 = new PdfPTable(pointColumnWidths4);
					table10.setWidthPercentage(100); // Width 100%

					List<SummaryLpsObservation> airClamps = summaryLPsBuilding.getSummaryLpsObservation().stream()
							.filter(observation -> observation.getObservationComponentDetails().contains("airClamps"))
							.collect(Collectors.toList());
					Boolean airClampsFlag = true;

					// Clamps Observation And Recommendation List with Iteration
					for (SummaryLpsObservation observation : airClamps) {
						if (observation.getObservation() != null && !observation.getObservation().isEmpty()
								&& observation.getRecommendation() != null
								&& !observation.getRecommendation().isEmpty()) {
							if (airClampsFlag) {
								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph("5. " + "AT_Clamps Observation", font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_LEFT);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setFixedHeight(20f);
								ClampsObserHeader.addCell(headerlabel1);
								document.add(ClampsObserHeader);
								airClampsFlag = false;
							}

							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(observation.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							table10.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(new Paragraph(observation.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							table10.addCell(cell37);
						}
					}
					if (airClampsFlag) {
						PdfPTable noRemarksPdfPTable = PdfPTables.noRemarksPdfPTable();

						PdfPCell observationCell = new PdfPCell();
						observationCell.setPhrase(new Phrase("5. " + "AT_Clamps Observation", font10B));
						observationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						observationCell.setGrayFill(0.92f);
						observationCell.setFixedHeight(20f);
						noRemarksPdfPTable.addCell(observationCell);

						PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
						cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
						noRemarksPdfPTable.addCell(cell37);

						document.add(noRemarksPdfPTable);
					}

					document.add(table10);

					// Expansion Observation Main Heading Label
					PdfPTable ExpansionObserHeader = new PdfPTable(pointColumnWidths40);
					ExpansionObserHeader.setWidthPercentage(100); // Width 100%
					ExpansionObserHeader.setSpacingBefore(10f); // Space before table

					PdfPTable table11 = new PdfPTable(pointColumnWidths4);
					table11.setWidthPercentage(100); // Width 100%

					List<SummaryLpsObservation> airExpansion = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("airExpansion"))
							.collect(Collectors.toList());
					Boolean airExpansionFlag = true;

					// Expansion Observation And Recommendation List with Iteration
					for (SummaryLpsObservation observation : airExpansion) {
						if (observation.getObservation() != null && !observation.getObservation().isEmpty()
								&& observation.getRecommendation() != null
								&& !observation.getRecommendation().isEmpty()) {

							if (airExpansionFlag) {

								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph("6. " + "AT_Expansion Observation", font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_LEFT);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setFixedHeight(20f);
								ExpansionObserHeader.addCell(headerlabel1);
								airExpansionFlag = false;
								document.add(ExpansionObserHeader);
							}

							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(observation.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							table11.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(new Paragraph(observation.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							table11.addCell(cell37);
						}
					}
					if (airExpansionFlag) {
						PdfPTable noRemarksPdfPTable = PdfPTables.noRemarksPdfPTable();

						PdfPCell observationCell = new PdfPCell();
						observationCell.setPhrase(new Phrase("6. " + "AT_Expansion Observation", font10B));
						observationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						observationCell.setGrayFill(0.92f);
						observationCell.setFixedHeight(20f);
						noRemarksPdfPTable.addCell(observationCell);

						PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
						cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
						noRemarksPdfPTable.addCell(cell37);

						document.add(noRemarksPdfPTable);
					}
					document.add(table11);

					// Connectors Observation Main Heading Label
					PdfPTable ConnectorsObserHeader = new PdfPTable(pointColumnWidths40);
					ConnectorsObserHeader.setWidthPercentage(100); // Width 100%
					ConnectorsObserHeader.setSpacingBefore(10f); // Space before table

					List<SummaryLpsObservation> airConnectors = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("airConnectors"))
							.collect(Collectors.toList());
					Boolean airConnectorsFlag = true;

					PdfPTable table12 = new PdfPTable(pointColumnWidths4);
					table12.setWidthPercentage(100); // Width 100%

					// Connectors Observation And Recommendation List with Iteration
					for (SummaryLpsObservation observation : airConnectors) {
						if (observation.getObservation() != null && !observation.getObservation().isEmpty()
								&& observation.getRecommendation() != null
								&& !observation.getRecommendation().isEmpty()) {

							if (airConnectorsFlag) {
								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph("7. " + "AT_Connectors Observation", font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_LEFT);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setFixedHeight(20f);
								ConnectorsObserHeader.addCell(headerlabel1);
								document.add(ConnectorsObserHeader);
								airConnectorsFlag = false;
							}

							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(observation.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							table12.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(new Paragraph(observation.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							table12.addCell(cell37);
						}
					}
					if (airConnectorsFlag) {
						PdfPTable noRemarksPdfPTable = PdfPTables.noRemarksPdfPTable();

						PdfPCell observationCell = new PdfPCell();
						observationCell.setPhrase(new Phrase("7. " + "AT_Connectors Observation", font10B));
						observationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						observationCell.setGrayFill(0.92f);
						observationCell.setFixedHeight(20f);
						noRemarksPdfPTable.addCell(observationCell);

						PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
						cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
						noRemarksPdfPTable.addCell(cell37);

						document.add(noRemarksPdfPTable);
					}
					document.add(table12);
					logger.debug("Airtermination Ended");
// ============================================== > DOWN CONDUCTOR START HERE <============================================================					
					logger.debug("DOWN CONDUCTOR START HERE");
					document.newPage();

					PdfPTable DownConductorlabel = new PdfPTable(pointColumnWidths40);
					DownConductorlabel.setWidthPercentage(100); // Width 100%
					DownConductorlabel.setSpacingBefore(10f); // Space before table

					PdfPCell label1 = new PdfPCell(new Paragraph("Down Conductors", font11B));
					label1.setHorizontalAlignment(Element.ALIGN_CENTER);
					label1.setGrayFill(0.92f);
					label1.setFixedHeight(20f);
					DownConductorlabel.addCell(label1);
					document.add(DownConductorlabel);

					PdfPTable DCBAsicDetails = new PdfPTable(pointColumnWidths40);
					DCBAsicDetails.setWidthPercentage(100); // Width 100%
					DCBAsicDetails.setSpacingBefore(10f); // Space before table

					// Observation And Recommendation Heading Label

					PdfPTable table13 = new PdfPTable(pointColumnWidths4);
					table13.setWidthPercentage(100); // Width 100%
					table13.setSpacingBefore(10f); // Space before table
					// table13.setSpacingAfter(10f);

					PdfPCell cell8 = new PdfPCell(new Paragraph("Observations", font10B));
					cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell8.setFixedHeight(20f);
					cell8.setGrayFill(0.92f);
					table13.addCell(cell8);

					PdfPCell cell9 = new PdfPCell(new Paragraph("Recommendation", font10B));
					cell9.setGrayFill(0.92f);
					cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
					table13.addCell(cell9);

					document.add(table13);

					// DC_Basic Details Observation Heading Label

					PdfPTable tableObRe = new PdfPTable(pointColumnWidths4);
					tableObRe.setWidthPercentage(100); // Width 100%
					List<SummaryLpsObservation> downConductorBasicDescription = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("downConductorBasicDescription"))
							.collect(Collectors.toList());
					Boolean downConductorBasicDescriptionFlag = true;
					summaryPgNumDetails.put(ContentEvent.numberOfContents++,pageNum.getPage()+",DownConductors");

					// DC_Basic Details Observation And Recommendation List with Iteration
					for (SummaryLpsObservation observation : downConductorBasicDescription) {

						if (observation.getObservation() != null && !observation.getObservation().isEmpty()
								&& observation.getRecommendation() != null
								&& !observation.getRecommendation().isEmpty()) {

							if (downConductorBasicDescriptionFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("1. DC_Basic Details Observation"));
								document.add(headingTable);
								downConductorBasicDescriptionFlag = false;

							}
							// add observation
							tableObRe.addCell(PdfPTables.pdfCellObservation(observation.getObservation()));
							// add recommendation
							tableObRe.addCell(PdfPTables.pdfCellObservation(observation.getRecommendation()));
						}
					}
					if (downConductorBasicDescriptionFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("1. DC_Basic Details Observation"));
					}
					document.add(tableObRe);

					List<SummaryLpsObservation> downConductorDescription = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("downConductorDescription"))
							.collect(Collectors.toList());

					Boolean downConductorDescriptionFlag = true;
					// Downconductors Observation Main Heading Label
					PdfPTable tableDCDescription = new PdfPTable(pointColumnWidths4);
					tableDCDescription.setWidthPercentage(100); // Width 100%
					// table15.setSpacingAfter(10f);

					// Downconductors Observation And Recommendation List with Iteration
					for (SummaryLpsObservation downConductorDescriptionObservation : downConductorDescription) {

						if (downConductorDescriptionObservation.getObservation() != null
								&& !downConductorDescriptionObservation.getObservation().isEmpty()
								&& downConductorDescriptionObservation.getRecommendation() != null
								&& !downConductorDescriptionObservation.getRecommendation().isEmpty()) {

							if (downConductorDescriptionFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("2. DC_Downconductors Observation"));
								document.add(headingTable);
								downConductorDescriptionFlag = false;

							}
							// add observation
							tableDCDescription.addCell(PdfPTables
									.pdfCellObservation(downConductorDescriptionObservation.getObservation()));
							// add recommendation
							tableDCDescription.addCell(PdfPTables
									.pdfCellObservation(downConductorDescriptionObservation.getRecommendation()));
						}
					}
					if (downConductorDescriptionFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("2. DC_Downconductors Observation"));
					}
					document.add(tableDCDescription);

					// Bridging Observation Main Heading Label
					List<SummaryLpsObservation> bridgingDescription = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("bridgingDescription"))
							.collect(Collectors.toList());

					Boolean bridgingDescriptionFlag = true;
					PdfPTable table16 = new PdfPTable(pointColumnWidths4);
					table16.setWidthPercentage(100); // Width 100%

					// Bridging Observation And Recommendation List with Iteration
					for (SummaryLpsObservation bridgingDescriptionObservation : bridgingDescription) {
						if (bridgingDescriptionObservation.getObservation() != null
								&& !bridgingDescriptionObservation.getObservation().isEmpty()
								&& bridgingDescriptionObservation.getRecommendation() != null
								&& !bridgingDescriptionObservation.getRecommendation().isEmpty()) {

							if (bridgingDescriptionFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("3. " + "DC_Bridging Observation"));
								document.add(headingTable);
								bridgingDescriptionFlag = false;

							}
							// add observation
							table16.addCell(
									PdfPTables.pdfCellObservation(bridgingDescriptionObservation.getObservation()));
							// add recommendation
							table16.addCell(
									PdfPTables.pdfCellObservation(bridgingDescriptionObservation.getRecommendation()));
						}
					}
					if (bridgingDescriptionFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("3. " + "DC_Bridging Observation"));
					}
					document.add(table16);

					// Holder Observation Main Heading Label

					List<SummaryLpsObservation> holder = summaryLPsBuilding.getSummaryLpsObservation().stream()
							.filter(observations -> observations.getObservationComponentDetails().contains("holder"))
							.collect(Collectors.toList());

					Boolean holderFlag = true;
					PdfPTable holderFlagTable = new PdfPTable(pointColumnWidths4);
					holderFlagTable.setWidthPercentage(100); // Width 100%

					// Bridging Observation And Recommendation List with Iteration
					for (SummaryLpsObservation holderObservation : holder) {
						if (holderObservation.getObservation() != null && !holderObservation.getObservation().isEmpty()
								&& holderObservation.getRecommendation() != null
								&& !holderObservation.getRecommendation().isEmpty()) {

							if (holderFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("4. " + "DC_Holder Observation"));
								document.add(headingTable);
								holderFlag = false;

							}
							// add observation
							holderFlagTable.addCell(PdfPTables.pdfCellObservation(holderObservation.getObservation()));
							// add recommendation
							holderFlagTable
									.addCell(PdfPTables.pdfCellObservation(holderObservation.getRecommendation()));
						}
					}
					if (holderFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("4. " + "DC_Holder Observation"));
					}
					document.add(holderFlagTable);

					// Connectors Observation Main Heading Label

					List<SummaryLpsObservation> connectors = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("connectors"))
							.collect(Collectors.toList());

					Boolean connectorFlag = true;
					PdfPTable connectorTable = new PdfPTable(pointColumnWidths4);
					connectorTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation connectorsObservation : connectors) {
						if (connectorsObservation.getObservation() != null
								&& !connectorsObservation.getObservation().isEmpty()
								&& connectorsObservation.getRecommendation() != null
								&& !connectorsObservation.getRecommendation().isEmpty()) {

							if (connectorFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("5. " + "DC_Connectors Observation"));
								document.add(headingTable);
								connectorFlag = false;

							}
							// add observation
							connectorTable
									.addCell(PdfPTables.pdfCellObservation(connectorsObservation.getObservation()));
							// add recommendation
							connectorTable
									.addCell(PdfPTables.pdfCellObservation(connectorsObservation.getRecommendation()));
						}
					}
					if (connectorFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("5. " + "DC_Connectors Observation"));
					}
					document.add(connectorTable);

					// TestingJoint Observation Main Heading Label
					List<SummaryLpsObservation> testingJoint = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("testingJoint"))
							.collect(Collectors.toList());

					Boolean testingJointFlag = true;
					PdfPTable testingJointTable = new PdfPTable(pointColumnWidths4);
					testingJointTable.setWidthPercentage(100); // Width 100%

					// Bridging Observation And Recommendation List with Iteration
					for (SummaryLpsObservation connectorsObservation : testingJoint) {
						if (connectorsObservation.getObservation() != null
								&& !connectorsObservation.getObservation().isEmpty()
								&& connectorsObservation.getRecommendation() != null
								&& !connectorsObservation.getRecommendation().isEmpty()) {

							if (testingJointFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("6. " + "DC_TestingJoint Observation"));
								document.add(headingTable);
								testingJointFlag = false;

							}
							// add observation
							testingJointTable
									.addCell(PdfPTables.pdfCellObservation(connectorsObservation.getObservation()));
							// add recommendation
							testingJointTable
									.addCell(PdfPTables.pdfCellObservation(connectorsObservation.getRecommendation()));
						}
					}
					if (testingJointFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("6. " + "DC_TestingJoint Observation"));
					}
					document.add(testingJointTable);

					// LightningCounter Observation Main Heading Label

					List<SummaryLpsObservation> lightningCounter = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("lightningCounter"))
							.collect(Collectors.toList());

					Boolean lightningCounterFlag = true;
					PdfPTable lightningCounterTable = new PdfPTable(pointColumnWidths4);
					lightningCounterTable.setWidthPercentage(100); // Width 100%

					// Bridging Observation And Recommendation List with Iteration
					for (SummaryLpsObservation lightningCounterObservation : lightningCounter) {
						if (lightningCounterObservation.getObservation() != null
								&& !lightningCounterObservation.getObservation().isEmpty()
								&& lightningCounterObservation.getRecommendation() != null
								&& !lightningCounterObservation.getRecommendation().isEmpty()) {

							if (lightningCounterFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable
										.addCell(PdfPTables.pdfCellHeading("7. " + "DC_LightningCounter Observation"));
								document.add(headingTable);
								lightningCounterFlag = false;

							}
							// add observation
							lightningCounterTable.addCell(
									PdfPTables.pdfCellObservation(lightningCounterObservation.getObservation()));
							// add recommendation
							lightningCounterTable.addCell(
									PdfPTables.pdfCellObservation(lightningCounterObservation.getRecommendation()));
						}
					}
					if (lightningCounterFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("7. " + "DC_LightningCounter Observation"));
					}
					document.add(lightningCounterTable);

					// DownConductorTesting Observation Main Heading Label
					List<SummaryLpsObservation> downConductorTesting = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("downConductorTesting"))
							.collect(Collectors.toList());

					Boolean downConductorTestingFlag = true;
					PdfPTable downConductorTestingTable = new PdfPTable(pointColumnWidths4);
					downConductorTestingTable.setWidthPercentage(100); // Width 100%

					// Bridging Observation And Recommendation List with Iteration
					for (SummaryLpsObservation downConductorTestingObservation : downConductorTesting) {
						if (downConductorTestingObservation.getObservation() != null
								&& !downConductorTestingObservation.getObservation().isEmpty()
								&& downConductorTestingObservation.getRecommendation() != null
								&& !downConductorTestingObservation.getRecommendation().isEmpty()) {

							if (downConductorTestingFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(
										PdfPTables.pdfCellHeading("8. " + "DC_DownConductorTesting Observation"));
								document.add(headingTable);
								downConductorTestingFlag = false;

							}
							// add observation
							downConductorTestingTable.addCell(
									PdfPTables.pdfCellObservation(downConductorTestingObservation.getObservation()));
							// add recommendation
							downConductorTestingTable.addCell(
									PdfPTables.pdfCellObservation(downConductorTestingObservation.getRecommendation()));
						}
					}
					if (downConductorTestingFlag) {
						document.add(
								PdfPTables.pdfCellHeadingWithNoRemarks("8. " + "DC_DownConductorTesting Observation"));
					}
					document.add(downConductorTestingTable);
					logger.debug("DOWN CONDUCTOR ended");
// ============================================== > EARTHING START HERE <================================================================
					logger.debug("EARTHING START HERE");
					document.newPage();

					PdfPTable Earthinglabel = new PdfPTable(pointColumnWidths40);
					Earthinglabel.setWidthPercentage(100); // Width 100%
					Earthinglabel.setSpacingBefore(10f); // Space before table

					PdfPCell label4 = new PdfPCell(new Paragraph("Earthing", font11B));
					label4.setHorizontalAlignment(Element.ALIGN_CENTER);
					label4.setGrayFill(0.92f);
					label4.setFixedHeight(20f);
					Earthinglabel.addCell(label4);
					document.add(Earthinglabel);

					PdfPTable ETBAsicDetails = new PdfPTable(pointColumnWidths40);
					ETBAsicDetails.setWidthPercentage(100); // Width 100%
					ETBAsicDetails.setSpacingBefore(10f); // Space before table

					// Earthing Observation And Recommendation Heading Label

					PdfPTable table22 = new PdfPTable(pointColumnWidths4);
					table22.setWidthPercentage(100); // Width 100%
					table22.setSpacingBefore(10f); // Space before table
					// table22.setSpacingAfter(10f);

					PdfPCell cell10 = new PdfPCell(new Paragraph("Observations", font10B));
					cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell10.setFixedHeight(20f);
					cell10.setGrayFill(0.92f);
					table22.addCell(cell10);

					PdfPCell cell11 = new PdfPCell(new Paragraph("Recommendation", font10B));
					cell11.setGrayFill(0.92f);
					cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
					table22.addCell(cell11);

					document.add(table22);
					
					// ET_BAsic Details Observation Heading Label
					 
					List<SummaryLpsObservation> earthingLpsDescription = summaryLPsBuilding.getSummaryLpsObservation().stream().filter(observations ->
					observations.getObservationComponentDetails().contains("earthingLpsDescription")).
					collect(Collectors.toList());
					
					Boolean earthingLpsDescriptionFlag = true;
					PdfPTable earthingLpsDescriptionTable = new PdfPTable(pointColumnWidths4);
					earthingLpsDescriptionTable.setWidthPercentage(100); // Width 100%

					summaryPgNumDetails.put(ContentEvent.numberOfContents++,pageNum.getPage()+",Earthing");

					// Bridging Observation And Recommendation List with Iteration
					for (SummaryLpsObservation earthingLpsDescriptionObservation : earthingLpsDescription) {
						if (earthingLpsDescriptionObservation.getObservation() != null
								&& !earthingLpsDescriptionObservation.getObservation().isEmpty()
								&& earthingLpsDescriptionObservation.getRecommendation() != null
								&& !earthingLpsDescriptionObservation.getRecommendation().isEmpty()) {

							if (earthingLpsDescriptionFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("1. ET_Basic Details Observation"));
								document.add(headingTable);
								earthingLpsDescriptionFlag = false;

							}
							// add observation
							earthingLpsDescriptionTable.addCell(
									PdfPTables.pdfCellObservation(earthingLpsDescriptionObservation.getObservation()));
							// add recommendation
							earthingLpsDescriptionTable.addCell(PdfPTables
									.pdfCellObservation(earthingLpsDescriptionObservation.getRecommendation()));
						}
					}
					if (earthingLpsDescriptionFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("1. ET_Basic Details Observation"));
					}
					document.add(earthingLpsDescriptionTable);
				 

					// EarthingDescription Observation Main Heading Label
					 List<SummaryLpsObservation> earthingDescriptionMain = summaryLPsBuilding.getSummaryLpsObservation().stream().filter(observations ->
						observations.getObservationComponentDetails().contains("earthingDescriptionMain")).
						collect(Collectors.toList());
						
						Boolean earthingDescriptionMainFlag = true;
						PdfPTable earthingDescriptionMainTable = new PdfPTable(pointColumnWidths4);
						earthingDescriptionMainTable.setWidthPercentage(100); // Width 100%
						 
						// Bridging Observation And Recommendation List with Iteration
						for (SummaryLpsObservation earthingDescriptionMainObservation : earthingDescriptionMain) {
							if (earthingDescriptionMainObservation.getObservation() != null
									&& !earthingDescriptionMainObservation.getObservation().isEmpty()
									&& earthingDescriptionMainObservation.getRecommendation() != null
									&& !earthingDescriptionMainObservation.getRecommendation().isEmpty()) {

								if (earthingDescriptionMainFlag) {
									PdfPTable headingTable = PdfPTables.remarksPDFTable();
									headingTable
											.addCell(PdfPTables.pdfCellHeading("2. EarthingDescription Observation"));
									document.add(headingTable);
									earthingDescriptionMainFlag = false;

								}
								// add observation
								earthingDescriptionMainTable.addCell(PdfPTables
										.pdfCellObservation(earthingDescriptionMainObservation.getObservation()));
								// add recommendation
								earthingDescriptionMainTable.addCell(PdfPTables
										.pdfCellObservation(earthingDescriptionMainObservation.getRecommendation()));
							}
						}
						if (earthingDescriptionMainFlag) {
							document.add(PdfPTables.pdfCellHeadingWithNoRemarks("2. EarthingDescription Observation"));
						}
						document.add(earthingDescriptionMainTable);

					 

					// EarthingDescription Observation List With Iteration
				 
						List<SummaryLpsObservation> earthingDescriptionList = summaryLPsBuilding.getSummaryLpsObservation().stream().filter(observations ->
						observations.getObservationComponentDetails().contains("earthingDescriptionList")).
						collect(Collectors.toList());
						
						Boolean earthingDescriptionListFlag = true;
						PdfPTable earthingDescriptionListTable = new PdfPTable(pointColumnWidths4);
						earthingDescriptionListTable.setWidthPercentage(100); // Width 100%
						 

						// Bridging Observation And Recommendation List with Iteration
						for (SummaryLpsObservation earthingDescriptionListObservation : earthingDescriptionList) {
							if (earthingDescriptionListObservation.getObservation() != null
									&& !earthingDescriptionListObservation.getObservation().isEmpty()
									&& earthingDescriptionListObservation.getRecommendation() != null
									&& !earthingDescriptionListObservation.getRecommendation().isEmpty()) {

								if (earthingDescriptionListFlag) {
									PdfPTable headingTable = PdfPTables.remarksPDFTable();
									headingTable.addCell(PdfPTables.pdfCellHeading("3. EarthingDescription List"));
									document.add(headingTable);
									earthingDescriptionListFlag = false;

								}
								// add observation
								earthingDescriptionListTable.addCell(PdfPTables
										.pdfCellObservation(earthingDescriptionListObservation.getObservation()));
								// add recommendation
								earthingDescriptionListTable.addCell(PdfPTables
										.pdfCellObservation(earthingDescriptionListObservation.getRecommendation()));
							}
						}
						if (earthingDescriptionListFlag) {
							document.add(PdfPTables.pdfCellHeadingWithNoRemarks("3. EarthingDescription List"));
						}
						document.add(earthingDescriptionListTable);

					 List<SummaryLpsObservation> earthingClamps = summaryLPsBuilding.getSummaryLpsObservation().stream().filter(observations ->
						observations.getObservationComponentDetails().contains("earthingClamps")).
						collect(Collectors.toList());

						Boolean earthingClampsObservationFlag = true;
						PdfPTable earthingClampsObservationTable = new PdfPTable(pointColumnWidths4);
						earthingClampsObservationTable.setWidthPercentage(100); // Width 100%

						// Bridging Observation And Recommendation List with Iteration
						for (SummaryLpsObservation earthingClampsObservation : earthingClamps) {
							if (earthingClampsObservation.getObservation() != null
									&& !earthingClampsObservation.getObservation().isEmpty()
									&& earthingClampsObservation.getRecommendation() != null
									&& !earthingClampsObservation.getRecommendation().isEmpty()) {

								if (earthingClampsObservationFlag) {
									PdfPTable headingTable = PdfPTables.remarksPDFTable();
									headingTable.addCell(PdfPTables.pdfCellHeading("4. EarthingClamps Observation"));
									document.add(headingTable);
									earthingClampsObservationFlag = false;

								}
								// add observation
								earthingClampsObservationTable.addCell(
										PdfPTables.pdfCellObservation(earthingClampsObservation.getObservation()));
								// add recommendation
								earthingClampsObservationTable.addCell(
										PdfPTables.pdfCellObservation(earthingClampsObservation.getRecommendation()));
							}
						}
						if (earthingClampsObservationFlag) {
							document.add(PdfPTables.pdfCellHeadingWithNoRemarks("4. EarthingClamps Observation"));
						}
						document.add(earthingClampsObservationTable);
					 

					// EarthingElectrodeChamber Observation Main Heading Label
				 
					List<SummaryLpsObservation> earthingElectrodeChamber = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("earthingElectrodeChamber"))
							.collect(Collectors.toList());

					Boolean earthingElectrodeChamberFlag = true;
					PdfPTable earthingElectrodeChamberTable = new PdfPTable(pointColumnWidths4);
					earthingElectrodeChamberTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation earthingElectrodeChamberObservation : earthingElectrodeChamber) {
						if (earthingElectrodeChamberObservation.getObservation() != null
								&& !earthingElectrodeChamberObservation.getObservation().isEmpty()
								&& earthingElectrodeChamberObservation.getRecommendation() != null
								&& !earthingElectrodeChamberObservation.getRecommendation().isEmpty()) {

							if (earthingElectrodeChamberFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable
										.addCell(PdfPTables.pdfCellHeading("5. EarthingElectrodeChamber Observation"));
								document.add(headingTable);
								earthingElectrodeChamberFlag = false;

							}
							// add observation
							earthingElectrodeChamberTable.addCell(PdfPTables
									.pdfCellObservation(earthingElectrodeChamberObservation.getObservation()));
							// add recommendation
							earthingElectrodeChamberTable.addCell(PdfPTables
									.pdfCellObservation(earthingElectrodeChamberObservation.getRecommendation()));
						}
					}
					if (earthingElectrodeChamberFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("5. EarthingElectrodeChamber Observation"));
					}
					document.add(earthingElectrodeChamberTable);

					// Earthing if Type B(ring) Display the
					List<SummaryLpsObservation> earthingSystem = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("earthingSystem"))
							.collect(Collectors.toList());

					Boolean earthingSystemFlag = true;
					PdfPTable earthingSystemTable = new PdfPTable(pointColumnWidths4);
					earthingSystemTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation earthingSystemObservation : earthingSystem) {
						if (earthingSystemObservation.getObservation() != null
								&& !earthingSystemObservation.getObservation().isEmpty()
								&& earthingSystemObservation.getRecommendation() != null
								&& !earthingSystemObservation.getRecommendation().isEmpty()) {

							if (earthingSystemFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("6. EarthingSystem Observation"));
								document.add(headingTable);
								earthingSystemFlag = false;

							}
							// add observation
							earthingSystemTable
									.addCell(PdfPTables.pdfCellObservation(earthingSystemObservation.getObservation()));
							// add recommendation
							earthingSystemTable.addCell(
									PdfPTables.pdfCellObservation(earthingSystemObservation.getRecommendation()));
						}
					}
					if (earthingSystemFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("6. EarthingSystem Observation"));
					}
					document.add(earthingSystemTable);

					// EarthElectrodeTesting Observation Main Heading Label
					List<SummaryLpsObservation> earthElectrodeTesting = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("earthElectrodeTesting"))
							.collect(Collectors.toList());

					Boolean earthElectrodeTestingFlag = true;
					PdfPTable earthElectrodeTestingTable = new PdfPTable(pointColumnWidths4);
					earthElectrodeTestingTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation earthElectrodeTestingObservation : earthElectrodeTesting) {
						if (earthElectrodeTestingObservation.getObservation() != null
								&& !earthElectrodeTestingObservation.getObservation().isEmpty()
								&& earthElectrodeTestingObservation.getRecommendation() != null
								&& !earthElectrodeTestingObservation.getRecommendation().isEmpty()) {

							if (earthElectrodeTestingFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("7. EarthElectrodeTesting Observation"));
								document.add(headingTable);
								earthElectrodeTestingFlag = false;

							}
							// add observation
							earthElectrodeTestingTable.addCell(
									PdfPTables.pdfCellObservation(earthElectrodeTestingObservation.getObservation()));
							// add recommendation
							earthElectrodeTestingTable.addCell(PdfPTables
									.pdfCellObservation(earthElectrodeTestingObservation.getRecommendation()));
						}
					}
					if (earthElectrodeTestingFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("7. EarthElectrodeTesting Observation"));
					}
					document.add(earthElectrodeTestingTable);

				 
					logger.debug("EARTHING pdf ended");
// ==================================================== > SPD START HERE <=====================================================================					

					logger.debug("SPD START HERE");
					document.newPage();

					PdfPTable SPDlabel = new PdfPTable(pointColumnWidths40);
					SPDlabel.setWidthPercentage(100); // Width 100%
					SPDlabel.setSpacingBefore(10f); // Space before table

					PdfPCell label5 = new PdfPCell(new Paragraph("SPD", font11B));
					label5.setHorizontalAlignment(Element.ALIGN_LEFT);
					label5.setGrayFill(0.92f);
					label5.setFixedHeight(20f);
					SPDlabel.addCell(label5);
					document.add(SPDlabel);

					PdfPTable SPDDetailsObserv = new PdfPTable(pointColumnWidths40);
					SPDDetailsObserv.setWidthPercentage(100); // Width 100%
					SPDDetailsObserv.setSpacingBefore(10f); // Space before table

				

					// SPD Observation And Recommendation Heading Label

					PdfPTable table29 = new PdfPTable(pointColumnWidths4);
					table29.setWidthPercentage(100); // Width 100%
					table29.setSpacingBefore(10f); // Space before table
					// table29.setSpacingAfter(10f);

					PdfPCell cell12 = new PdfPCell(new Paragraph("Observations", font10B));
					cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell12.setFixedHeight(20f);
					cell12.setGrayFill(0.92f);
					table29.addCell(cell12);

					PdfPCell cell13 = new PdfPCell(new Paragraph("Recommendation", font10B));
					cell13.setGrayFill(0.92f);
					cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
					table29.addCell(cell13);

					document.add(table29);
					
					// SPD Details Observation Heading Label
					   List<SummaryLpsObservation> spdReport = summaryLPsBuilding.getSummaryLpsObservation().stream().filter(observations ->
						observations.getObservationComponentDetails().contains("spdReport")).
						collect(Collectors.toList());
						
						Boolean spdReportFlag = true;
						PdfPTable spdReportTable = new PdfPTable(pointColumnWidths4);
						spdReportTable.setWidthPercentage(100); // Width 100%

						summaryPgNumDetails.put(ContentEvent.numberOfContents++,pageNum.getPage()+",SPD");

	 					for (SummaryLpsObservation spdReportObservation : spdReport) {
							 if (spdReportObservation.getObservation() !=null && !spdReportObservation.getObservation().isEmpty() && 
									 spdReportObservation.getRecommendation() !=null && !spdReportObservation.getRecommendation().isEmpty()) {
									
									if (spdReportFlag) {
										 PdfPTable headingTable = PdfPTables.remarksPDFTable() ;
										 headingTable.addCell(PdfPTables.pdfCellHeading("1. SPD Details Observation"));
										 document.add(headingTable);
										 spdReportFlag = false;
										 
									}
									//add observation
									spdReportTable.addCell(PdfPTables.pdfCellObservation(spdReportObservation.getObservation()));
									// add recommendation
									spdReportTable.addCell(PdfPTables.pdfCellObservation(spdReportObservation.getRecommendation()));
						}
					}
						if (spdReportFlag) {
							document.add(PdfPTables.pdfCellHeadingWithNoRemarks("1. SPD Details Observation"));
					}
					 document.add(spdReportTable);
					 

					// SPD List Observation List With Iteration
					  List<SummaryLpsObservation> spdDescription = summaryLPsBuilding.getSummaryLpsObservation().stream().filter(observations ->
						observations.getObservationComponentDetails().contains("spdDescription")).
						collect(Collectors.toList());
						
						Boolean spdDescriptionFlag = true;
						PdfPTable spdDescriptionTable = new PdfPTable(pointColumnWidths4);
						spdDescriptionTable.setWidthPercentage(100); // Width 100%
	 

	 					for (SummaryLpsObservation spdDescriptionObservation : spdDescription) {
							 if (spdDescriptionObservation.getObservation() !=null && !spdDescriptionObservation.getObservation().isEmpty() && 
									 spdDescriptionObservation.getRecommendation() !=null && !spdDescriptionObservation.getRecommendation().isEmpty()) {
									
									if (spdDescriptionFlag) {
										 PdfPTable headingTable = PdfPTables.remarksPDFTable() ;
										 headingTable.addCell(PdfPTables.pdfCellHeading("2. SPDList Observation"));
										 document.add(headingTable);
										 spdDescriptionFlag = false;
										 
									}
									//add observation
									spdDescriptionTable.addCell(PdfPTables.pdfCellObservation(spdDescriptionObservation.getObservation()));
									// add recommendation
									spdDescriptionTable.addCell(PdfPTables.pdfCellObservation(spdDescriptionObservation.getRecommendation()));
						}
					}
						if (spdDescriptionFlag) {
							document.add(PdfPTables.pdfCellHeadingWithNoRemarks("2. SPDList Observation"));
					}
					 document.add(spdDescriptionTable);
					 logger.debug("SPD PDF ended");
// ================================================== > SEPARATION DISTANCE START HERE <========================================================

					 logger.debug("SEPARATION DISTANCE START HERE");
					document.newPage();

					PdfPTable SeparationDistancelabel = new PdfPTable(pointColumnWidths40);
					SeparationDistancelabel.setWidthPercentage(100); // Width 100%
					SeparationDistancelabel.setSpacingBefore(10f); // Space before table

					PdfPCell label6 = new PdfPCell(new Paragraph("SeparationDistance", font11B));
					label6.setHorizontalAlignment(Element.ALIGN_CENTER);
					label6.setGrayFill(0.92f);
					label6.setFixedHeight(20f);
					SeparationDistancelabel.addCell(label6);
					document.add(SeparationDistancelabel);

					// SeparationDistance Observation And Recommendation Heading Label

					PdfPTable table31 = new PdfPTable(pointColumnWidths4);
					table31.setWidthPercentage(100); // Width 100%
					table31.setSpacingBefore(10f); // Space before table
					// table31.setSpacingAfter(10f);

					PdfPCell cell14 = new PdfPCell(new Paragraph("Observations", font10B));
					cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell14.setFixedHeight(20f);
					cell14.setGrayFill(0.92f);
					table31.addCell(cell14);

					PdfPCell cell15 = new PdfPCell(new Paragraph("Recommendation", font10B));
					cell15.setGrayFill(0.92f);
					cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
					table31.addCell(cell15);

					document.add(table31);

					PdfPTable table32 = new PdfPTable(pointColumnWidths4);
					table32.setWidthPercentage(100); // Width 100%
					// table32.setSpacingBefore(5f); // Space before table
					// table32.setSpacingAfter(10f);

					// SeparationDistance Observation And Recommendation List with Iteration
					 

					PdfPTable SeparationDistObserHeader = new PdfPTable(pointColumnWidths4);
					SeparationDistObserHeader.setWidthPercentage(100); // Width 100%
					SeparationDistObserHeader.setSpacingBefore(10f); // Space before table
					SeparationDistObserHeader.setSpacingAfter(5F); // Space After table

					// SeparationDistance List Observation List With Iteration

					List<SummaryLpsObservation> separateDistanceDesc = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("seperationDistanceDescription"))
							.collect(Collectors.toList());

					Boolean separateDistanceDescFlag = true;
					PdfPTable separateDistanceDescTable = new PdfPTable(pointColumnWidths4);
					separateDistanceDescTable.setWidthPercentage(100); // Width 100%
					//summaryPgNumDetails.put(ContentEvent.numberOfContents++,pageNum.getPage()+",SeparationDistance Details Observation");
					summaryPgNumDetails.put(ContentEvent.numberOfContents++,pageNum.getPage()+",SeparationDistance");


					for (SummaryLpsObservation separateDistanceDescObservation : separateDistanceDesc) {
						if (separateDistanceDescObservation.getObservation() != null
								&& !separateDistanceDescObservation.getObservation().isEmpty()
								&& separateDistanceDescObservation.getRecommendation() != null
								&& !separateDistanceDescObservation.getRecommendation().isEmpty()) {

							if (separateDistanceDescFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("1. SeparationDistance Details Observation"));
								document.add(headingTable);
								separateDistanceDescFlag = false;

							}
							// add observation
							separateDistanceDescTable.addCell(
									PdfPTables.pdfCellObservation(separateDistanceDescObservation.getObservation()));
							// add recommendation
							separateDistanceDescTable.addCell(
									PdfPTables.pdfCellObservation(separateDistanceDescObservation.getRecommendation()));
						}
					}
					if (separateDistanceDescFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("1. SeparationDistance Observation"));
					}
					document.add(separateDistanceDescTable);

					// SeparationDistance List Observation List With Iteration
					List<SummaryLpsObservation> separateDistanceDownConductors = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("separateDistanceDesc"))
							.collect(Collectors.toList());

					Boolean separateDistanceDownConductorsFlag = true;
					PdfPTable separateDDConductorsTable = new PdfPTable(pointColumnWidths4);
					separateDDConductorsTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation separateDistanceDescObservation : separateDistanceDownConductors) {
						if (separateDistanceDescObservation.getObservation() != null
								&& !separateDistanceDescObservation.getObservation().isEmpty()
								&& separateDistanceDescObservation.getRecommendation() != null
								&& !separateDistanceDescObservation.getRecommendation().isEmpty()) {

							if (separateDistanceDownConductorsFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable
										.addCell(PdfPTables.pdfCellHeading("2. SeparateDistance Observation"));
								document.add(headingTable);
								separateDistanceDownConductorsFlag = false;

							}
							// add observation
							separateDDConductorsTable.addCell(
									PdfPTables.pdfCellObservation(separateDistanceDescObservation.getObservation()));
							// add recommendation
							separateDDConductorsTable.addCell(
									PdfPTables.pdfCellObservation(separateDistanceDescObservation.getRecommendation()));
						}
					}
					if (separateDistanceDownConductorsFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("2. SeparateDistance Observation"));
					}
					document.add(separateDDConductorsTable);

					List<SummaryLpsObservation> separationDistanceDownConductors = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("separateDistanceDownConductors"))
							.collect(Collectors.toList());

					Boolean separateDDownConductorsFlag = true;
					PdfPTable separateDDownConductorsTable = new PdfPTable(pointColumnWidths4);
					separateDDownConductorsTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation separateDistanceDescObservation : separationDistanceDownConductors) {
						if (separateDistanceDescObservation.getObservation() != null
								&& !separateDistanceDescObservation.getObservation().isEmpty()
								&& separateDistanceDescObservation.getRecommendation() != null
								&& !separateDistanceDescObservation.getRecommendation().isEmpty()) {

							if (separateDDownConductorsFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable
										.addCell(PdfPTables.pdfCellHeading("3. SeparationDistanceDown Observation"));
								document.add(headingTable);
								separateDDownConductorsFlag = false;

							}
							// add observation
							separateDDownConductorsTable.addCell(
									PdfPTables.pdfCellObservation(separateDistanceDescObservation.getObservation()));
							// add recommendation
							separateDDownConductorsTable.addCell(
									PdfPTables.pdfCellObservation(separateDistanceDescObservation.getRecommendation()));
						}
					}
					if (separateDistanceDownConductorsFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("3. SeparationDistanceDown Observation"));
					}
					document.add(separateDDownConductorsTable);
					logger.debug("SEPARATION DISTANCE START HERE");
//  ================================================= > EQUIPOTENTIAL BONDING START HERE < =====================================================					
					logger.debug("EQUIPOTENTIAL BONDING START HERE");
					document.newPage();

					PdfPTable Equipotentialbondinglabel = new PdfPTable(pointColumnWidths40);
					Equipotentialbondinglabel.setWidthPercentage(100); // Width 100%
					Equipotentialbondinglabel.setSpacingBefore(10f); // Space before table

					PdfPCell label7 = new PdfPCell(new Paragraph("Equipotential Bonding", font11B));
					label7.setHorizontalAlignment(Element.ALIGN_LEFT);
					label7.setGrayFill(0.92f);
					label7.setFixedHeight(20f);
					Equipotentialbondinglabel.addCell(label7);
					document.add(Equipotentialbondinglabel);

					PdfPTable EquipotentialbondingObser = new PdfPTable(pointColumnWidths40);
					EquipotentialbondingObser.setWidthPercentage(100); // Width 100%
					EquipotentialbondingObser.setSpacingBefore(10f); // Space before table

					// Equipotential bonding Observation And Recommendation Heading Label

					PdfPTable table33 = new PdfPTable(pointColumnWidths4);
					table33.setWidthPercentage(100); // Width 100%
					table33.setSpacingBefore(10f); // Space before table
					// table33.setSpacingAfter(10f);

					PdfPCell cell16 = new PdfPCell(new Paragraph("Observations", font10B));
					cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell16.setFixedHeight(20f);
					cell16.setGrayFill(0.92f);
					table33.addCell(cell16);

					PdfPCell cell17 = new PdfPCell(new Paragraph("Recommendation", font10B));
					cell17.setGrayFill(0.92f);
					cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
					table33.addCell(cell17);

					document.add(table33);

				 
					summaryPgNumDetails.put(ContentEvent.numberOfContents++,pageNum.getPage()+",Equipotential Bonding");

					// Equipotential bonding Observation And Recommendation List with Iteration
					List<SummaryLpsObservation> earthStudDescription = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("equipotentialBonding"))
							.collect(Collectors.toList());

					Boolean earthStudDescriptionFlag = true;
					PdfPTable earthStudDescriptionTable = new PdfPTable(pointColumnWidths4);
					earthStudDescriptionTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation earthStudDescriptionObservation : earthStudDescription) {
						if (earthStudDescriptionObservation.getObservation() != null
								&& !earthStudDescriptionObservation.getObservation().isEmpty()
								&& earthStudDescriptionObservation.getRecommendation() != null
								&& !earthStudDescriptionObservation.getRecommendation().isEmpty()) {

							if (earthStudDescriptionFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("1. Equipotential Bonding Observation"));
								document.add(headingTable);
								earthStudDescriptionFlag = false;

							}
							// add observation
							earthStudDescriptionTable.addCell(
									PdfPTables.pdfCellObservation(earthStudDescriptionObservation.getObservation()));
							// add recommendation
							earthStudDescriptionTable.addCell(
									PdfPTables.pdfCellObservation(earthStudDescriptionObservation.getRecommendation()));
						}
					}
					if (earthStudDescriptionFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("1. Equipotential Bonding Observation"));
					}
					document.add(earthStudDescriptionTable);

					document.newPage();
				 }
				logger.debug("EQUIPOTENTIAL BONDING Ended");
				
//  ================================================= > RECOMMENDATIONS && DECLARATION START HERE < =====================================================
				logger.debug("RECOMMENDATIONS && DECLARATION START HERE");
				document.newPage();
				
				summaryPgNumDetails.put(ContentEvent.numberOfContents++,pageNum.getPage()+",Recommendation");

				SummaryLpsBuildings summaryLpsBuilding1 = summaryLPsBuild.get(0);
//			    this method for Adding the Main Header Fields for Every Page
				MainHeaderPropertiesLabel(document, basicLps1, summaryLpsBuilding1);

				
				PdfPTable RecommendationTable = new PdfPTable(pointColumnWidths40);
				RecommendationTable.setWidthPercentage(100); // Width 100%
				RecommendationTable.setSpacingBefore(10f); // Space before table

				PdfPCell label8 = new PdfPCell(new Paragraph("Recommendation", font11B));
				label8.setHorizontalAlignment(Element.ALIGN_LEFT);
				label8.setGrayFill(0.92f);
				label8.setFixedHeight(20f);
				RecommendationTable.addCell(label8);
				document.add(RecommendationTable);

				float[] pointColumnWidths1 = { 150F, 40F };

				PdfPTable table36 = new PdfPTable(pointColumnWidths1); // 3 columns.
				table36.setWidthPercentage(100); // Width 100%
				table36.setSpacingBefore(10f); // Space before table
				// table7.setSpacingAfter(10f); // Space after table
				table36.getDefaultCell().setBorder(0);

				PdfPTable table37 = new PdfPTable(pointColumnWidths1); // 3 columns.
				table37.setWidthPercentage(100); // Width 100%
				table37.setSpacingBefore(10f); // Space before table
				// table37.setSpacingAfter(10f); // Space after table
				table37.getDefaultCell().setBorder(0);

				for (SummaryLps summary : lpsSum) {

					PdfPCell cell132 = new PdfPCell(new Paragraph(
							"Subject to necessary remedial action being taken, I/We recommended that the LPS is further inspected and tested by : ",
							font10N));
					cell132.setBorder(PdfPCell.NO_BORDER);
					cell132.setGrayFill(0.92f);
					table36.addCell(cell132);
					PdfPCell cell29 = new PdfPCell(new Paragraph(summary.getSummaryDate(), font10B));
 					cell29.setGrayFill(0.92f);
					cell29.setBorder(PdfPCell.NO_BORDER);
					table36.addCell(cell29);

					PdfPCell cell134 = new PdfPCell(new Paragraph(
							"We also recommend that, ligthning protection system to be periodical inspected for every Years",
							font10N));
					logger.debug("protection system to be periodical inspected year added in document");

					cell134.setBorder(PdfPCell.NO_BORDER);
					cell134.setGrayFill(0.92f);
					table37.addCell(cell134);
					PdfPCell cell135 = new PdfPCell(new Paragraph(summary.getInspectedYear().toString(), font10B));
					cell135.setGrayFill(0.92f);
					cell135.setBorder(PdfPCell.NO_BORDER);
					table37.addCell(cell135);
				}
				document.add(table36);
				document.add(table37);

				PdfPTable DeclarationTable = new PdfPTable(pointColumnWidths40);
				DeclarationTable.setWidthPercentage(100); // Width 100%
				DeclarationTable.setSpacingBefore(10f); // Space before table
				DeclarationTable.setSpacingAfter(5f); // Space before table

				PdfPCell label9 = new PdfPCell(new Paragraph("Declaration", font11B));
				label9.setHorizontalAlignment(Element.ALIGN_LEFT);
				label9.setGrayFill(0.92f);
				label9.setFixedHeight(20f);
				DeclarationTable.addCell(label9);
				document.add(DeclarationTable);
				logger.debug("Declaration details added in document");
				Paragraph paragraph1 = new Paragraph(
						"I/we being the person responsible for the inspection of the lightning protection system (as indicated by my/our signatures below), particulars of which are described in this report, having exercised reasonable skill and care when carrying out the inspection, hereby declare that information in this report including the observations provides an accurate assessment of condition of lightning protection system taking into account :",
						font10N);
				document.add(paragraph1);

				PdfPTable table38 = new PdfPTable(2);
				table38.setWidthPercentage(100); // Width 100%
				table38.setSpacingBefore(10f); // Space before table
				table38.getDefaultCell().setBorder(0);

				addRow(table38, "Inspected and Tested  By ", "Authorized By");

				List<SummaryLpsDeclaration> summaryLPsDec = lpsSummary.getSummaryLpsDeclaration();
				SummaryLpsDeclaration declaration = summaryLPsDec.get(0);
				SummaryLpsDeclaration declaration11 = summaryLPsDec.get(1);

				float[] pointColumnWidthsSec5 = { 40F, 90F, 40F, 90F };

				PdfPTable table = new PdfPTable(pointColumnWidthsSec5); // 3 columns.
				table.setWidthPercentage(100); // Width 100%
				
				PdfPTable table1 = new PdfPTable(pointColumnWidthsSec5); // 3 columns.
				table1.setWidthPercentage(100); // Width 100%

				PdfPTable table2 = new PdfPTable(pointColumnWidthsSec5); // 3 columns.
				table2.setWidthPercentage(100); // Width 100%

				// conversin code for signature in Inspeted
				String signature = declaration.getSignature();
				Base64 decoder = new Base64();
				byte[] imageByte = decoder.decode(signature);
				String s = new String(imageByte);
				
				String inspectedSignature_list[] = s.split(",");
				String inspectedSignature1 = inspectedSignature_list[1];
				byte[] inspetedImageByte = decoder.decode(inspectedSignature1);
				// conversion code for signature in Autherized
				String autherizedsignature = declaration11.getSignature();
				Base64 autherizeddecoder = new Base64();
				byte[] autherizedimageByte = autherizeddecoder.decode(autherizedsignature);
				String autherizedString = new String(autherizedimageByte);
				String autherizedsignature_list[] = autherizedString.split(",");
				String autherizedSignature1 = autherizedsignature_list[1];
				byte[] autherizedImageByte = decoder.decode(autherizedSignature1);
				
				addRow(table, "Name", declaration.getName(), "Name", declaration11.getName());
				addRow(table, "Company", declaration.getCompany(), "Company", declaration11.getCompany());
				document.add(table38);
				document.add(table);
				addRow1(table1, "Signature	", inspetedImageByte, "Signature	", autherizedImageByte);
				document.add(table1);
				addRow(table2, "Position", declaration.getPosition(), "Position", declaration11.getPosition());
				addRow(table2, "Address", declaration.getAddress(), "Address", declaration11.getAddress());
				addRow(table2, "Date", declaration.getDate(), "Date", declaration11.getDate());
				document.add(table2);
				logger.debug("document closed");

				indexNumberDeatils.put("Summary", summaryPgNumDetails);

				document.close();
				writer.close();
			} catch (Exception e) {
				logger.debug("Summary PDF creation Failed");
				throw new Exception("Summary PDF creation Failed"+e.getMessage());
			}

		} else {
			logger.debug("Summary PDF Invalid Inputs");
			throw new SummaryLpsException("Invalid Inputs");
		}
		return null;
	}
	
	
	private void MainHeaderPropertiesLabel(Document document, BasicLps basicLps1,
			SummaryLpsBuildings summaryLPsBuilding) throws DocumentException, IOException {
		float[] pointColumnWidths200 = { 100F };

		PdfPTable table1111 = new PdfPTable(pointColumnWidths200);
		table1111.setWidthPercentage(100); // Width 100%
//						    table1111.setSpacingBefore(5f); // Space before table
//						    table1111.setSpacingAfter(f); // Space after table
		table1111.getDefaultCell().setBorder(0);

		PdfPCell arrangements1001 = new PdfPCell(new Paragraph(
				basicLps1.getProjectName() + " / " + summaryLPsBuilding.getBuildingName() + " / "
						+ summaryLPsBuilding.getBuildingNumber().toString(),
				new Font(BaseFont.createFont(), 8, Font.NORMAL | Font.NORMAL)));
//							arrangements1001.setBackgroundColor(new BaseColor(203, 183, 162));
		arrangements1001.setHorizontalAlignment(Element.ALIGN_RIGHT);
		arrangements1001.setBorder(PdfPCell.NO_BORDER);
		table1111.addCell(arrangements1001);
		document.add(table1111);
	}
	

	private void addRow(PdfPTable table9, String string, String string2) throws DocumentException, IOException {
		Font font8 = new Font(BaseFont.createFont(), 10, Font.BOLD, BaseColor.BLACK);
		PdfPCell nameCell = new PdfPCell(new Paragraph(string, font8));
		PdfPCell nameCell1 = new PdfPCell(new Paragraph(string2, font8));
		nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		nameCell.setGrayFill(0.92f);
		nameCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		nameCell1.setGrayFill(0.92f);
		table9.addCell(nameCell);
		table9.addCell(nameCell1);
	}

	private void addRow(PdfPTable table6, String string, String string2, String string3, String string4)
			throws DocumentException, IOException {
		Font font = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);
		PdfPCell nameCell = new PdfPCell(new Paragraph(string, font));
		PdfPCell valueCell1 = new PdfPCell(new Paragraph(string2, new Font(BaseFont.createFont(), 10, Font.NORMAL)));
		PdfPCell valueCell2 = new PdfPCell(new Paragraph(string3, font));
		PdfPCell valueCell3 = new PdfPCell(new Paragraph(string4, new Font(BaseFont.createFont(), 10, Font.NORMAL)));
		nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		valueCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		valueCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		valueCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		nameCell.setGrayFill(0.92f);
		valueCell2.setGrayFill(0.92f);
		table6.addCell(nameCell);
		table6.addCell(valueCell1);
		table6.addCell(valueCell2);
		table6.addCell(valueCell3);

	}


	@Override
	public List<SummaryLps> printLpsSummaryDetails(String userName, Integer basicLpsId)
			throws SummaryLpsException, Exception {

		if (userName != null && !userName.isEmpty() && basicLpsId != null && basicLpsId != 0) {
			Document document = new Document(PageSize.A4, 68, 68, 62, 68);

			try {

				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("LpsSummary.pdf"));

				List<SummaryLps> lpsSum = summaryLpsRepository.findByUserNameAndBasicLpsId(userName, basicLpsId);
				SummaryLps lpsSummary = lpsSum.get(0);

				List<SummaryLpsBuildings> summaryLPsBuild = lpsSummary.getSummaryLpsBuildings();
//				SummaryLpsBuildings summaryLpsBuilding1 = summaryLPsBuild.get(0);

//				List<SummaryLpsObservation> SummaryLpsOb = summaryLpsBuilding1.getSummaryLpsObservation();
//				SummaryLpsObservation summaryLpsObse = SummaryLpsOb.get(0);

//				List<SummaryLpsInnerObservation> summaryLpsInnerObs = summaryLpsObse.getSummaryLpsInnerObservation();
//				SummaryLpsInnerObservation summaryLpsInnerObser = summaryLpsInnerObs.get(0);

				document.open();

				Font font12B = new Font(BaseFont.createFont(), 12, Font.NORMAL | Font.BOLD, BaseColor.BLACK);
				Font font11B = new Font(BaseFont.createFont(), 11, Font.NORMAL | Font.BOLD, BaseColor.BLACK);
				Font font10B = new Font(BaseFont.createFont(), 10, Font.NORMAL | Font.BOLD, BaseColor.BLACK);
				Font font10N = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);
				Font font9 = new Font(BaseFont.createFont(), 9, Font.NORMAL, BaseColor.BLACK);

				float[] pointColumnWidths40 = { 100F };

				PdfPTable headertable = new PdfPTable(pointColumnWidths40);
				headertable.setWidthPercentage(100); // Width 100%
				headertable.setSpacingBefore(10f); // Space before table

				PdfPCell label = new PdfPCell(new Paragraph("Summary", font12B));
				label.setHorizontalAlignment(Element.ALIGN_CENTER);
				label.setGrayFill(0.92f);
				label.setFixedHeight(20f);
				headertable.addCell(label);

				document.add(headertable);
				

				PdfPTable SRtable = new PdfPTable(pointColumnWidths40);
				SRtable.setWidthPercentage(100); // Width 100%
				SRtable.setSpacingBefore(5f); // Space before table

				// printing Summary and Recommendations
				PdfPCell label2 = new PdfPCell(new Paragraph("Summary and Recommendations", font11B));
				label2.setHorizontalAlignment(Element.ALIGN_CENTER);
				label2.setGrayFill(0.92f);
				label2.setFixedHeight(20f);
				SRtable.addCell(label2);
				document.add(SRtable);

				// iterating based on building
				for (SummaryLpsBuildings summaryLPsBuilding : summaryLPsBuild) {

					float[] pointColumnWidths = { 120F, 80F };

					PdfPTable table1 = new PdfPTable(pointColumnWidths);

					table1.setWidthPercentage(100); // Width 100%
					table1.setSpacingBefore(10f); // Space before table
					table1.setSpacingAfter(5f); // Space after table
					table1.getDefaultCell().setBorder(0);

					// printing building number
					PdfPCell cell1 = new PdfPCell(new Paragraph("Building number:",
							new Font(BaseFont.createFont(), 10, Font.NORMAL | Font.BOLD)));
					cell1.setBackgroundColor(new BaseColor(203, 183, 162));
					cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell1.setBorder(PdfPCell.NO_BORDER);
					table1.addCell(cell1);
					PdfPCell cell2 = new PdfPCell(new Paragraph(summaryLPsBuilding.getBuildingNumber().toString(),
							new Font(BaseFont.createFont(), 10, Font.NORMAL | Font.BOLD)));
					cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell2.setBackgroundColor(new BaseColor(203, 183, 162));
					cell2.setBorder(PdfPCell.NO_BORDER);
					table1.addCell(cell2);
					document.add(table1);

					PdfPTable table2 = new PdfPTable(pointColumnWidths);

					table2.setWidthPercentage(100); // Width 100%
					table2.setSpacingBefore(5f); // Space before table
					table2.setSpacingAfter(5f); // Space after table
					table2.getDefaultCell().setBorder(0);

					// printing building name
					PdfPCell cell3 = new PdfPCell(new Paragraph("Building name:",
							new Font(BaseFont.createFont(), 10, Font.NORMAL | Font.BOLD)));
					cell3.setBackgroundColor(new BaseColor(203, 183, 162));
					cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell3.setBorder(PdfPCell.NO_BORDER);
					table2.addCell(cell3);
					PdfPCell cell4 = new PdfPCell(new Paragraph(summaryLPsBuilding.getBuildingName(),
							new Font(BaseFont.createFont(), 10, Font.NORMAL | Font.BOLD)));
					cell4.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell4.setBackgroundColor(new BaseColor(203, 183, 162));
					cell4.setBorder(PdfPCell.NO_BORDER);
					table2.addCell(cell4);
					document.add(table2);

// ================================================ > AIR TERMINATION	 START HERE <=========================================================

					PdfPTable AirTerminationlabel = new PdfPTable(pointColumnWidths40);
					AirTerminationlabel.setWidthPercentage(100); // Width 100%
					AirTerminationlabel.setSpacingBefore(10f); // Space before table

					// printing airtermination
					PdfPCell label3 = new PdfPCell(new Paragraph("Air Termination", font11B));
					label3.setHorizontalAlignment(Element.ALIGN_LEFT);
					label3.setGrayFill(0.92f);
					label3.setFixedHeight(20f);
					AirTerminationlabel.addCell(label3);
					document.add(AirTerminationlabel);

					PdfPTable airbasicDetails = new PdfPTable(pointColumnWidths40);
					airbasicDetails.setWidthPercentage(100); // Width 100%
					airbasicDetails.setSpacingBefore(10f); // Space before table

					// Observation And Recommendation Heading Label
					float[] pointColumnWidths4 = { 50F, 50F };

					PdfPTable table3 = new PdfPTable(pointColumnWidths4);
					table3.setWidthPercentage(100); // Width 100%
					table3.setSpacingBefore(10f); // Space before table
					// table3.setSpacingAfter(10f);
					table3.setWidthPercentage(100);

					PdfPCell cell6 = new PdfPCell(new Paragraph("Observations", font10B));
					cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell6.setFixedHeight(20f);
					cell6.setGrayFill(0.92f);
					table3.addCell(cell6);

					PdfPCell cell7 = new PdfPCell(new Paragraph("Recommendation", font10B));
					cell7.setGrayFill(0.92f);
					cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
					table3.addCell(cell7);

					document.add(table3);

					PdfPTable table4 = new PdfPTable(pointColumnWidths4);
					table4.setWidthPercentage(100); // Width 100%
					// table4.setSpacingBefore(5f); // Space before table
					// table4.setSpacingAfter(10f);

					// AT_Basic Details Observation Heading Label

					// AT_Basic Details Observation And Recommendation List with Iteration
					List<SummaryLpsObservation> airBasicDescription = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("airBasicDescription"))
							.collect(Collectors.toList());
					Boolean airBasicFalg = true;
					for (SummaryLpsObservation airBasicDescriptionOb : airBasicDescription) {

						if (airBasicDescriptionOb.getObservation() != null
								&& !airBasicDescriptionOb.getObservation().isEmpty()
								&& airBasicDescriptionOb.getRecommendation() != null
								&& !airBasicDescriptionOb.getRecommendation().isEmpty()) {
							if (airBasicFalg) {
								PdfPCell airBasicCell = new PdfPCell(
										new Paragraph("1. " + "AT_Basic Details Observation", font10B));
								airBasicCell.setHorizontalAlignment(Element.ALIGN_LEFT);
								airBasicCell.setGrayFill(0.92f);
								airBasicCell.setFixedHeight(20f);
								airbasicDetails.addCell(airBasicCell);
								document.add(airbasicDetails);
								airBasicFalg = false;
							}

							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(airBasicDescriptionOb.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							table4.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(
									new Paragraph(airBasicDescriptionOb.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							table4.addCell(cell37);

						}
					}

					// noremarks cell
					if (airBasicFalg) {
						PdfPTable noRemarksPdfPTable = PdfPTables.noRemarksPdfPTable();

						PdfPCell cell105 = new PdfPCell();
						cell105.setPhrase(new Phrase("1. AT_Basic Details Observation", font10B));
						cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell105.setGrayFill(0.92f);
						cell105.setFixedHeight(20f);
						noRemarksPdfPTable.addCell(cell105);

						PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
						cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
						noRemarksPdfPTable.addCell(cell37);
						document.add(noRemarksPdfPTable);

					}
					document.add(table4);

					// Verticle Termination Main Heading Label
					PdfPTable airVerticlelabel = new PdfPTable(pointColumnWidths40);
					airVerticlelabel.setWidthPercentage(100); // Width 100%
					airVerticlelabel.setSpacingBefore(10f); // Space before table

					// Verticle Termination main Remarks Only one main filed display
					PdfPTable table5 = new PdfPTable(pointColumnWidths4);
					table5.setWidthPercentage(100); // Width 100%
					// table5.setSpacingBefore(10f); // Space before table
					// table5.setSpacingAfter(5f);

					List<SummaryLpsObservation> lpsVerticalAirTermination = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("lpsVerticalAirTermination"))
							.collect(Collectors.toList());
					Boolean verticalflag = true;
					for (SummaryLpsObservation verticalObservation : lpsVerticalAirTermination) {
						if (verticalObservation.getObservation() != null
								&& !verticalObservation.getObservation().isEmpty()
								&& verticalObservation.getRecommendation() != null
								&& !verticalObservation.getRecommendation().isEmpty()) {

							if (verticalflag) {
								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph("2. " + "AT_Vertical Observation", font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_LEFT);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setFixedHeight(20f);
								airVerticlelabel.addCell(headerlabel1);
								document.add(airVerticlelabel);
								verticalflag = false;
							}
							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(verticalObservation.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							table5.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(
									new Paragraph(verticalObservation.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							table5.addCell(cell37);

						}
					}

					if (verticalflag) {
						PdfPTable noRemarksPdfPTable = PdfPTables.noRemarksPdfPTable();

						PdfPCell observationCell = new PdfPCell();
						observationCell.setPhrase(new Phrase("2. " + "AT_Vertical Observation", font10B));
						observationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						observationCell.setGrayFill(0.92f);
						observationCell.setFixedHeight(20f);
						noRemarksPdfPTable.addCell(observationCell);

						PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
						cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
						noRemarksPdfPTable.addCell(cell37);

						document.add(noRemarksPdfPTable);
					}

					document.add(table5);

					PdfPTable airVerticleListLabel = new PdfPTable(pointColumnWidths4);
					airVerticleListLabel.setWidthPercentage(100); // Width 100%
					// airVerticleListLabel.setSpacingBefore(5f); // Space before table
					// airVerticleListLabel.setSpacingAfter(5F); // Space After table

					List<SummaryLpsObservation> verticalAirTerminationList = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("verticalAirTerminationList"))
							.collect(Collectors.toList());

					// Verticle Termination List With Iteration
					for (SummaryLpsObservation summaryLpsObservation : verticalAirTerminationList) {

						if (summaryLpsObservation.getObservation() != null
								&& !summaryLpsObservation.getObservation().isEmpty()
								&& summaryLpsObservation.getRecommendation() != null
								&& !summaryLpsObservation.getRecommendation().isEmpty()) {
							if (summaryLpsObservation.getHeadingUi() != null
									&& !summaryLpsObservation.getHeadingUi().isEmpty()
									&& summaryLpsObservation.getHeadingUi().contains("AT_Vertical List")) {

								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph(summaryLpsObservation.getHeadingUi(), font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_CENTER);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setColspan(2);
								headerlabel1.setFixedHeight(20f);
								airVerticleListLabel.addCell(headerlabel1);
							}

							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(summaryLpsObservation.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							airVerticleListLabel.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(
									new Paragraph(summaryLpsObservation.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							airVerticleListLabel.addCell(cell37);

						}
					}
 
					document.add(airVerticleListLabel);

					// Mesh Observation Main Heading Label
					PdfPTable MeshObservationHeader = new PdfPTable(pointColumnWidths40);
					MeshObservationHeader.setWidthPercentage(100); // Width 100%
					MeshObservationHeader.setSpacingBefore(10f); // Space before table

					PdfPTable table7 = new PdfPTable(pointColumnWidths4);
					table7.setWidthPercentage(100); // Width 100%
					// table7.setSpacingBefore(10f); // Space before table
					// table7.setSpacingAfter(10f);
					List<SummaryLpsObservation> airMeshDescription = summaryLPsBuilding.getSummaryLpsObservation()
							.stream()
							.filter(observation -> observation.getObservationComponentDetails().contains("airMesh"))
							.collect(Collectors.toList());
					Boolean airMeshDescriptionFlag = true;
					// Mesh Conductor Observation And Recommendation List with Iteration
					for (SummaryLpsObservation summaryLpsOb : airMeshDescription) {

						if (summaryLpsOb.getObservation() != null && !summaryLpsOb.getObservation().isEmpty()
								&& summaryLpsOb.getRecommendation() != null
								&& !summaryLpsOb.getRecommendation().isEmpty()) {

							if (airMeshDescriptionFlag) {
								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph("3. " + "AT_Mesh Observation", font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_LEFT);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setFixedHeight(20f);
								MeshObservationHeader.addCell(headerlabel1);
								document.add(MeshObservationHeader);

								airMeshDescriptionFlag = false;
							}
							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(summaryLpsOb.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							table7.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(new Paragraph(summaryLpsOb.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							table7.addCell(cell37);
						}
					}
					if (airMeshDescriptionFlag) {
						PdfPTable noRemarksPdfPTable = PdfPTables.noRemarksPdfPTable();

						PdfPCell observationCell = new PdfPCell();
						observationCell.setPhrase(new Phrase("3. " + "AT_Mesh Observation", font10B));
						observationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						observationCell.setGrayFill(0.92f);
						observationCell.setFixedHeight(20f);
						noRemarksPdfPTable.addCell(observationCell);

						PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
						cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
						noRemarksPdfPTable.addCell(cell37);

						document.add(noRemarksPdfPTable);
					}
					document.add(table7);

					// Holder Observation Main Heading Label
					PdfPTable HolderObservationHeader = new PdfPTable(pointColumnWidths40);
					HolderObservationHeader.setWidthPercentage(100); // Width 100%
					HolderObservationHeader.setSpacingBefore(10f); // Space before table

					// Hoders main Remarks
					PdfPTable table8 = new PdfPTable(pointColumnWidths4);
					table8.setWidthPercentage(100); // Width 100%
					// table8.setSpacingBefore(10f); // Space before table
					// table8.setSpacingAfter(5f);

					List<SummaryLpsObservation> airHolderDescription = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("airHolderDescription"))
							.collect(Collectors.toList());
					Boolean airHolderDescriptionFlag = true;

					for (SummaryLpsObservation observation : airHolderDescription) {

						if (observation.getObservation() != null && !observation.getObservation().isEmpty()
								&& observation.getRecommendation() != null
								&& !observation.getRecommendation().isEmpty()) {

							if (airHolderDescriptionFlag) {
								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph("4. " + "AT_Holder Observation", font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_LEFT);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setFixedHeight(20f);
								HolderObservationHeader.addCell(headerlabel1);
								document.add(HolderObservationHeader);
								airHolderDescriptionFlag = false;
							}

							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(observation.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							table8.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(new Paragraph(observation.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							table8.addCell(cell37);

						}
					}
					if (airHolderDescriptionFlag) {
						PdfPTable noRemarksPdfPTable = PdfPTables.noRemarksPdfPTable();

						PdfPCell observationCell = new PdfPCell();
						observationCell.setPhrase(new Phrase("4. " + "AT_Holder Observation", font10B));
						observationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						observationCell.setGrayFill(0.92f);
						observationCell.setFixedHeight(20f);
						noRemarksPdfPTable.addCell(observationCell);

						PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
						cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
						noRemarksPdfPTable.addCell(cell37);

						document.add(noRemarksPdfPTable);
					}
					document.add(table8);

					PdfPTable HoldersObservListHeader = new PdfPTable(pointColumnWidths4);
					HoldersObservListHeader.setWidthPercentage(100); // Width 100%
					// HoldersObservListHeader.setSpacingBefore(5f); // Space before table
					// HoldersObservListHeader.setSpacingAfter(5F); // Space After table

					List<SummaryLpsObservation> airHolderList = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("airHolderList"))
							.collect(Collectors.toList());
 					// Hoders Observation List With Iteration
					for (SummaryLpsObservation observation : airHolderList) {

						if (observation.getObservation() != null && !observation.getObservation().isEmpty()
								&& observation.getRecommendation() != null
								&& !observation.getRecommendation().isEmpty()) {

							if (observation.getHeadingUi() != null && !observation.getHeadingUi().isEmpty()
									&& observation.getHeadingUi().contains("AT_Holder List")) {

								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph(observation.getHeadingUi(), font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_CENTER);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setColspan(2);
								headerlabel1.setFixedHeight(20f);
								HoldersObservListHeader.addCell(headerlabel1);
 
							}

							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(observation.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							HoldersObservListHeader.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(new Paragraph(observation.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							HoldersObservListHeader.addCell(cell37);
						}

					}
					document.add(HoldersObservListHeader);

					// Clamps Observation Main Heading Label
					PdfPTable ClampsObserHeader = new PdfPTable(pointColumnWidths40);
					ClampsObserHeader.setWidthPercentage(100); // Width 100%
					ClampsObserHeader.setSpacingBefore(10f); // Space before table

					PdfPTable table10 = new PdfPTable(pointColumnWidths4);
					table10.setWidthPercentage(100); // Width 100%
					// table10.setSpacingBefore(10f); // Space before table
					// table10.setSpacingAfter(10f);

					List<SummaryLpsObservation> airClamps = summaryLPsBuilding.getSummaryLpsObservation().stream()
							.filter(observation -> observation.getObservationComponentDetails().contains("airClamps"))
							.collect(Collectors.toList());
					Boolean airClampsFlag = true;

					// Clamps Observation And Recommendation List with Iteration
					for (SummaryLpsObservation observation : airClamps) {
						if (observation.getObservation() != null && !observation.getObservation().isEmpty()
								&& observation.getRecommendation() != null
								&& !observation.getRecommendation().isEmpty()) {
							if (airClampsFlag) {
								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph("5. " + "AT_Clamps Observation", font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_LEFT);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setFixedHeight(20f);
								ClampsObserHeader.addCell(headerlabel1);
								document.add(ClampsObserHeader);
								airClampsFlag = false;
							}

							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(observation.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							table10.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(new Paragraph(observation.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							table10.addCell(cell37);
						}
					}
					if (airClampsFlag) {
						PdfPTable noRemarksPdfPTable = PdfPTables.noRemarksPdfPTable();

						PdfPCell observationCell = new PdfPCell();
						observationCell.setPhrase(new Phrase("5. " + "AT_Clamps Observation", font10B));
						observationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						observationCell.setGrayFill(0.92f);
						observationCell.setFixedHeight(20f);
						noRemarksPdfPTable.addCell(observationCell);

						PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
						cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
						noRemarksPdfPTable.addCell(cell37);

						document.add(noRemarksPdfPTable);
					}

					document.add(table10);

					// Expansion Observation Main Heading Label
					PdfPTable ExpansionObserHeader = new PdfPTable(pointColumnWidths40);
					ExpansionObserHeader.setWidthPercentage(100); // Width 100%
					ExpansionObserHeader.setSpacingBefore(10f); // Space before table

					PdfPTable table11 = new PdfPTable(pointColumnWidths4);
					table11.setWidthPercentage(100); // Width 100%
					// table11.setSpacingBefore(10f); // Space before table
					// table11.setSpacingAfter(10f);

					List<SummaryLpsObservation> airExpansion = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("airExpansion"))
							.collect(Collectors.toList());
					Boolean airExpansionFlag = true;

					// Expansion Observation And Recommendation List with Iteration
					for (SummaryLpsObservation observation : airExpansion) {
						if (observation.getObservation() != null && !observation.getObservation().isEmpty()
								&& observation.getRecommendation() != null
								&& !observation.getRecommendation().isEmpty()) {

							if (airExpansionFlag) {

								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph("6. " + "AT_Expansion Observation", font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_LEFT);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setFixedHeight(20f);
								ExpansionObserHeader.addCell(headerlabel1);
								airExpansionFlag = false;
								document.add(ExpansionObserHeader);
							}

							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(observation.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							table11.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(new Paragraph(observation.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							table11.addCell(cell37);
						}
					}
					if (airExpansionFlag) {
						PdfPTable noRemarksPdfPTable = PdfPTables.noRemarksPdfPTable();

						PdfPCell observationCell = new PdfPCell();
						observationCell.setPhrase(new Phrase("6. " + "AT_Expansion Observation", font10B));
						observationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						observationCell.setGrayFill(0.92f);
						observationCell.setFixedHeight(20f);
						noRemarksPdfPTable.addCell(observationCell);

						PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
						cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
						noRemarksPdfPTable.addCell(cell37);

						document.add(noRemarksPdfPTable);
					}
					document.add(table11);

					// Connectors Observation Main Heading Label
					PdfPTable ConnectorsObserHeader = new PdfPTable(pointColumnWidths40);
					ConnectorsObserHeader.setWidthPercentage(100); // Width 100%
					ConnectorsObserHeader.setSpacingBefore(10f); // Space before table

					List<SummaryLpsObservation> airConnectors = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("airConnectors"))
							.collect(Collectors.toList());
					Boolean airConnectorsFlag = true;

					PdfPTable table12 = new PdfPTable(pointColumnWidths4);
					table12.setWidthPercentage(100); // Width 100%
					// table12.setSpacingBefore(10f); // Space before table
					// table12.setSpacingAfter(10f);

					// Connectors Observation And Recommendation List with Iteration
					for (SummaryLpsObservation observation : airConnectors) {
						if (observation.getObservation() != null && !observation.getObservation().isEmpty()
								&& observation.getRecommendation() != null
								&& !observation.getRecommendation().isEmpty()) {

							if (airConnectorsFlag) {
								PdfPCell headerlabel1 = new PdfPCell(
										new Paragraph("7. " + "AT_Connectors Observation", font10B));
								headerlabel1.setHorizontalAlignment(Element.ALIGN_LEFT);
								headerlabel1.setGrayFill(0.92f);
								headerlabel1.setFixedHeight(20f);
								ConnectorsObserHeader.addCell(headerlabel1);
								document.add(ConnectorsObserHeader);
								airConnectorsFlag = false;
							}

							PdfPCell cell105 = new PdfPCell();
							cell105.setPhrase(new Phrase(observation.getObservation(), font10N));
							cell105.setHorizontalAlignment(Element.ALIGN_LEFT);
							cell105.setBackgroundColor(new GrayColor(0.93f));
							table12.addCell(cell105);

							PdfPCell cell37 = new PdfPCell(new Paragraph(observation.getRecommendation(), font10N));
							cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
							table12.addCell(cell37);
						}
					}
					if (airConnectorsFlag) {
						PdfPTable noRemarksPdfPTable = PdfPTables.noRemarksPdfPTable();

						PdfPCell observationCell = new PdfPCell();
						observationCell.setPhrase(new Phrase("7. " + "AT_Connectors Observation", font10B));
						observationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
						observationCell.setGrayFill(0.92f);
						observationCell.setFixedHeight(20f);
						noRemarksPdfPTable.addCell(observationCell);

						PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
						cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
						noRemarksPdfPTable.addCell(cell37);

						document.add(noRemarksPdfPTable);
					}
					document.add(table12);

// ============================================== > DOWN CONDUCTOR START HERE <============================================================					

					document.newPage();

					PdfPTable DownConductorlabel = new PdfPTable(pointColumnWidths40);
					DownConductorlabel.setWidthPercentage(100); // Width 100%
					DownConductorlabel.setSpacingBefore(10f); // Space before table

					PdfPCell label1 = new PdfPCell(new Paragraph("Down Conductors", font11B));
					label1.setHorizontalAlignment(Element.ALIGN_CENTER);
					label1.setGrayFill(0.92f);
					label1.setFixedHeight(20f);
					DownConductorlabel.addCell(label1);
					document.add(DownConductorlabel);

					PdfPTable DCBAsicDetails = new PdfPTable(pointColumnWidths40);
					DCBAsicDetails.setWidthPercentage(100); // Width 100%
					DCBAsicDetails.setSpacingBefore(10f); // Space before table

					// Observation And Recommendation Heading Label

					PdfPTable table13 = new PdfPTable(pointColumnWidths4);
					table13.setWidthPercentage(100); // Width 100%
					table13.setSpacingBefore(10f); // Space before table
					// table13.setSpacingAfter(10f);

					PdfPCell cell8 = new PdfPCell(new Paragraph("Observations", font10B));
					cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell8.setFixedHeight(20f);
					cell8.setGrayFill(0.92f);
					table13.addCell(cell8);

					PdfPCell cell9 = new PdfPCell(new Paragraph("Recommendation", font10B));
					cell9.setGrayFill(0.92f);
					cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
					table13.addCell(cell9);

					document.add(table13);

					// DC_Basic Details Observation Heading Label

					PdfPTable tableObRe = new PdfPTable(pointColumnWidths4);
					tableObRe.setWidthPercentage(100); // Width 100%
					// table14.setSpacingBefore(5f); // Space before table
					// table14.setSpacingAfter(10f);
					List<SummaryLpsObservation> downConductorBasicDescription = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("downConductorBasicDescription"))
							.collect(Collectors.toList());
					Boolean downConductorBasicDescriptionFlag = true;
					// DC_Basic Details Observation And Recommendation List with Iteration
					for (SummaryLpsObservation observation : downConductorBasicDescription) {

						if (observation.getObservation() != null && !observation.getObservation().isEmpty()
								&& observation.getRecommendation() != null
								&& !observation.getRecommendation().isEmpty()) {

							if (downConductorBasicDescriptionFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("1. DC_Basic Details Observation"));
								document.add(headingTable);
								downConductorBasicDescriptionFlag = false;

							}
							// add observation
							tableObRe.addCell(PdfPTables.pdfCellObservation(observation.getObservation()));
							// add recommendation
							tableObRe.addCell(PdfPTables.pdfCellObservation(observation.getRecommendation()));
						}
					}
					if (downConductorBasicDescriptionFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("1. DC_Basic Details Observation"));
					}
					document.add(tableObRe);

					List<SummaryLpsObservation> downConductorDescription = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observation -> observation
									.getObservationComponentDetails().contains("downConductorDescription"))
							.collect(Collectors.toList());

					Boolean downConductorDescriptionFlag = true;
					// Downconductors Observation Main Heading Label
					PdfPTable tableDCDescription = new PdfPTable(pointColumnWidths4);
					tableDCDescription.setWidthPercentage(100); // Width 100%
					// table15.setSpacingAfter(10f);

					// Downconductors Observation And Recommendation List with Iteration
					for (SummaryLpsObservation downConductorDescriptionObservation : downConductorDescription) {

						if (downConductorDescriptionObservation.getObservation() != null
								&& !downConductorDescriptionObservation.getObservation().isEmpty()
								&& downConductorDescriptionObservation.getRecommendation() != null
								&& !downConductorDescriptionObservation.getRecommendation().isEmpty()) {

							if (downConductorDescriptionFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("2. DC_Downconductors Observation"));
								document.add(headingTable);
								downConductorDescriptionFlag = false;

							}
							// add observation
							tableDCDescription.addCell(PdfPTables
									.pdfCellObservation(downConductorDescriptionObservation.getObservation()));
							// add recommendation
							tableDCDescription.addCell(PdfPTables
									.pdfCellObservation(downConductorDescriptionObservation.getRecommendation()));
						}
					}
					if (downConductorDescriptionFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("2. DC_Downconductors Observation"));
					}
					document.add(tableDCDescription);

					// Bridging Observation Main Heading Label
					List<SummaryLpsObservation> bridgingDescription = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("bridgingDescription"))
							.collect(Collectors.toList());

					Boolean bridgingDescriptionFlag = true;
					PdfPTable table16 = new PdfPTable(pointColumnWidths4);
					table16.setWidthPercentage(100); // Width 100%

					// Bridging Observation And Recommendation List with Iteration
					for (SummaryLpsObservation bridgingDescriptionObservation : bridgingDescription) {
						if (bridgingDescriptionObservation.getObservation() != null
								&& !bridgingDescriptionObservation.getObservation().isEmpty()
								&& bridgingDescriptionObservation.getRecommendation() != null
								&& !bridgingDescriptionObservation.getRecommendation().isEmpty()) {

							if (bridgingDescriptionFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("3. " + "DC_Bridging Observation"));
								document.add(headingTable);
								bridgingDescriptionFlag = false;

							}
							// add observation
							table16.addCell(
									PdfPTables.pdfCellObservation(bridgingDescriptionObservation.getObservation()));
							// add recommendation
							table16.addCell(
									PdfPTables.pdfCellObservation(bridgingDescriptionObservation.getRecommendation()));
						}
					}
					if (bridgingDescriptionFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("3. " + "DC_Bridging Observation"));
					}
					document.add(table16);

					// Holder Observation Main Heading Label

					List<SummaryLpsObservation> holder = summaryLPsBuilding.getSummaryLpsObservation().stream()
							.filter(observations -> observations.getObservationComponentDetails().contains("holder"))
							.collect(Collectors.toList());

					Boolean holderFlag = true;
					PdfPTable holderFlagTable = new PdfPTable(pointColumnWidths4);
					holderFlagTable.setWidthPercentage(100); // Width 100%

					// Bridging Observation And Recommendation List with Iteration
					for (SummaryLpsObservation holderObservation : holder) {
						if (holderObservation.getObservation() != null && !holderObservation.getObservation().isEmpty()
								&& holderObservation.getRecommendation() != null
								&& !holderObservation.getRecommendation().isEmpty()) {

							if (holderFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("4. " + "DC_Holder Observation"));
								document.add(headingTable);
								holderFlag = false;

							}
							// add observation
							holderFlagTable.addCell(PdfPTables.pdfCellObservation(holderObservation.getObservation()));
							// add recommendation
							holderFlagTable
									.addCell(PdfPTables.pdfCellObservation(holderObservation.getRecommendation()));
						}
					}
					if (holderFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("4. " + "DC_Holder Observation"));
					}
					document.add(holderFlagTable);

					// Connectors Observation Main Heading Label

					List<SummaryLpsObservation> connectors = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("connectors"))
							.collect(Collectors.toList());

					Boolean connectorFlag = true;
					PdfPTable connectorTable = new PdfPTable(pointColumnWidths4);
					connectorTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation connectorsObservation : connectors) {
						if (connectorsObservation.getObservation() != null
								&& !connectorsObservation.getObservation().isEmpty()
								&& connectorsObservation.getRecommendation() != null
								&& !connectorsObservation.getRecommendation().isEmpty()) {

							if (connectorFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("5. " + "DC_Connectors Observation"));
								document.add(headingTable);
								connectorFlag = false;

							}
							// add observation
							connectorTable
									.addCell(PdfPTables.pdfCellObservation(connectorsObservation.getObservation()));
							// add recommendation
							connectorTable
									.addCell(PdfPTables.pdfCellObservation(connectorsObservation.getRecommendation()));
						}
					}
					if (connectorFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("5. " + "DC_Connectors Observation"));
					}
					document.add(connectorTable);

					// TestingJoint Observation Main Heading Label
					List<SummaryLpsObservation> testingJoint = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("testingJoint"))
							.collect(Collectors.toList());

					Boolean testingJointFlag = true;
					PdfPTable testingJointTable = new PdfPTable(pointColumnWidths4);
					testingJointTable.setWidthPercentage(100); // Width 100%

					// Bridging Observation And Recommendation List with Iteration
					for (SummaryLpsObservation connectorsObservation : testingJoint) {
						if (connectorsObservation.getObservation() != null
								&& !connectorsObservation.getObservation().isEmpty()
								&& connectorsObservation.getRecommendation() != null
								&& !connectorsObservation.getRecommendation().isEmpty()) {

							if (testingJointFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("6. " + "DC_TestingJoint Observation"));
								document.add(headingTable);
								testingJointFlag = false;

							}
							// add observation
							testingJointTable
									.addCell(PdfPTables.pdfCellObservation(connectorsObservation.getObservation()));
							// add recommendation
							testingJointTable
									.addCell(PdfPTables.pdfCellObservation(connectorsObservation.getRecommendation()));
						}
					}
					if (testingJointFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("6. " + "DC_TestingJoint Observation"));
					}
					document.add(testingJointTable);

					// LightningCounter Observation Main Heading Label

					List<SummaryLpsObservation> lightningCounter = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("lightningCounter"))
							.collect(Collectors.toList());

					Boolean lightningCounterFlag = true;
					PdfPTable lightningCounterTable = new PdfPTable(pointColumnWidths4);
					lightningCounterTable.setWidthPercentage(100); // Width 100%

					// Bridging Observation And Recommendation List with Iteration
					for (SummaryLpsObservation lightningCounterObservation : lightningCounter) {
						if (lightningCounterObservation.getObservation() != null
								&& !lightningCounterObservation.getObservation().isEmpty()
								&& lightningCounterObservation.getRecommendation() != null
								&& !lightningCounterObservation.getRecommendation().isEmpty()) {

							if (lightningCounterFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable
										.addCell(PdfPTables.pdfCellHeading("7. " + "DC_LightningCounter Observation"));
								document.add(headingTable);
								lightningCounterFlag = false;

							}
							// add observation
							lightningCounterTable.addCell(
									PdfPTables.pdfCellObservation(lightningCounterObservation.getObservation()));
							// add recommendation
							lightningCounterTable.addCell(
									PdfPTables.pdfCellObservation(lightningCounterObservation.getRecommendation()));
						}
					}
					if (lightningCounterFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("7. " + "DC_LightningCounter Observation"));
					}
					document.add(lightningCounterTable);

					// DownConductorTesting Observation Main Heading Label
					List<SummaryLpsObservation> downConductorTesting = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("downConductorTesting"))
							.collect(Collectors.toList());

					Boolean downConductorTestingFlag = true;
					PdfPTable downConductorTestingTable = new PdfPTable(pointColumnWidths4);
					downConductorTestingTable.setWidthPercentage(100); // Width 100%

					// Bridging Observation And Recommendation List with Iteration
					for (SummaryLpsObservation downConductorTestingObservation : downConductorTesting) {
						if (downConductorTestingObservation.getObservation() != null
								&& !downConductorTestingObservation.getObservation().isEmpty()
								&& downConductorTestingObservation.getRecommendation() != null
								&& !downConductorTestingObservation.getRecommendation().isEmpty()) {

							if (downConductorTestingFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(
										PdfPTables.pdfCellHeading("8. " + "DC_DownConductorTesting Observation"));
								document.add(headingTable);
								downConductorTestingFlag = false;

							}
							// add observation
							downConductorTestingTable.addCell(
									PdfPTables.pdfCellObservation(downConductorTestingObservation.getObservation()));
							// add recommendation
							downConductorTestingTable.addCell(
									PdfPTables.pdfCellObservation(downConductorTestingObservation.getRecommendation()));
						}
					}
					if (downConductorTestingFlag) {
						document.add(
								PdfPTables.pdfCellHeadingWithNoRemarks("8. " + "DC_DownConductorTesting Observation"));
					}
					document.add(downConductorTestingTable);

// ============================================== > EARTHING START HERE <================================================================

					document.newPage();

					PdfPTable Earthinglabel = new PdfPTable(pointColumnWidths40);
					Earthinglabel.setWidthPercentage(100); // Width 100%
					Earthinglabel.setSpacingBefore(10f); // Space before table

					PdfPCell label4 = new PdfPCell(new Paragraph("Earthing", font11B));
					label4.setHorizontalAlignment(Element.ALIGN_CENTER);
					label4.setGrayFill(0.92f);
					label4.setFixedHeight(20f);
					Earthinglabel.addCell(label4);
					document.add(Earthinglabel);

					PdfPTable ETBAsicDetails = new PdfPTable(pointColumnWidths40);
					ETBAsicDetails.setWidthPercentage(100); // Width 100%
					ETBAsicDetails.setSpacingBefore(10f); // Space before table

					// Earthing Observation And Recommendation Heading Label

					PdfPTable table22 = new PdfPTable(pointColumnWidths4);
					table22.setWidthPercentage(100); // Width 100%
					table22.setSpacingBefore(10f); // Space before table
					// table22.setSpacingAfter(10f);

					PdfPCell cell10 = new PdfPCell(new Paragraph("Observations", font10B));
					cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell10.setFixedHeight(20f);
					cell10.setGrayFill(0.92f);
					table22.addCell(cell10);

					PdfPCell cell11 = new PdfPCell(new Paragraph("Recommendation", font10B));
					cell11.setGrayFill(0.92f);
					cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
					table22.addCell(cell11);

					document.add(table22);
					
					// ET_BAsic Details Observation Heading Label
					 
					List<SummaryLpsObservation> earthingLpsDescription = summaryLPsBuilding.getSummaryLpsObservation().stream().filter(observations ->
					observations.getObservationComponentDetails().contains("earthingLpsDescription")).
					collect(Collectors.toList());
					
					Boolean earthingLpsDescriptionFlag = true;
					PdfPTable earthingLpsDescriptionTable = new PdfPTable(pointColumnWidths4);
					earthingLpsDescriptionTable.setWidthPercentage(100); // Width 100%

					// Bridging Observation And Recommendation List with Iteration
					for (SummaryLpsObservation earthingLpsDescriptionObservation : earthingLpsDescription) {
						if (earthingLpsDescriptionObservation.getObservation() != null
								&& !earthingLpsDescriptionObservation.getObservation().isEmpty()
								&& earthingLpsDescriptionObservation.getRecommendation() != null
								&& !earthingLpsDescriptionObservation.getRecommendation().isEmpty()) {

							if (earthingLpsDescriptionFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("1. ET_Basic Details Observation"));
								document.add(headingTable);
								earthingLpsDescriptionFlag = false;

							}
							// add observation
							earthingLpsDescriptionTable.addCell(
									PdfPTables.pdfCellObservation(earthingLpsDescriptionObservation.getObservation()));
							// add recommendation
							earthingLpsDescriptionTable.addCell(PdfPTables
									.pdfCellObservation(earthingLpsDescriptionObservation.getRecommendation()));
						}
					}
					if (earthingLpsDescriptionFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("1. ET_Basic Details Observation"));
					}
					document.add(earthingLpsDescriptionTable);
				 

					// EarthingDescription Observation Main Heading Label
					 List<SummaryLpsObservation> earthingDescriptionMain = summaryLPsBuilding.getSummaryLpsObservation().stream().filter(observations ->
						observations.getObservationComponentDetails().contains("earthingDescriptionMain")).
						collect(Collectors.toList());
						
						Boolean earthingDescriptionMainFlag = true;
						PdfPTable earthingDescriptionMainTable = new PdfPTable(pointColumnWidths4);
						earthingDescriptionMainTable.setWidthPercentage(100); // Width 100%
						 
						// Bridging Observation And Recommendation List with Iteration
						for (SummaryLpsObservation earthingDescriptionMainObservation : earthingDescriptionMain) {
							if (earthingDescriptionMainObservation.getObservation() != null
									&& !earthingDescriptionMainObservation.getObservation().isEmpty()
									&& earthingDescriptionMainObservation.getRecommendation() != null
									&& !earthingDescriptionMainObservation.getRecommendation().isEmpty()) {

								if (earthingDescriptionMainFlag) {
									PdfPTable headingTable = PdfPTables.remarksPDFTable();
									headingTable
											.addCell(PdfPTables.pdfCellHeading("2. EarthingDescription Observation"));
									document.add(headingTable);
									earthingDescriptionMainFlag = false;

								}
								// add observation
								earthingDescriptionMainTable.addCell(PdfPTables
										.pdfCellObservation(earthingDescriptionMainObservation.getObservation()));
								// add recommendation
								earthingDescriptionMainTable.addCell(PdfPTables
										.pdfCellObservation(earthingDescriptionMainObservation.getRecommendation()));
							}
						}
						if (earthingDescriptionMainFlag) {
							document.add(PdfPTables.pdfCellHeadingWithNoRemarks("2. EarthingDescription Observation"));
						}
						document.add(earthingDescriptionMainTable);

					 

					// EarthingDescription Observation List With Iteration
				 
						List<SummaryLpsObservation> earthingDescriptionList = summaryLPsBuilding.getSummaryLpsObservation().stream().filter(observations ->
						observations.getObservationComponentDetails().contains("earthingDescriptionList")).
						collect(Collectors.toList());
						
						Boolean earthingDescriptionListFlag = true;
						PdfPTable earthingDescriptionListTable = new PdfPTable(pointColumnWidths4);
						earthingDescriptionListTable.setWidthPercentage(100); // Width 100%
						 

						// Bridging Observation And Recommendation List with Iteration
						for (SummaryLpsObservation earthingDescriptionListObservation : earthingDescriptionList) {
							if (earthingDescriptionListObservation.getObservation() != null
									&& !earthingDescriptionListObservation.getObservation().isEmpty()
									&& earthingDescriptionListObservation.getRecommendation() != null
									&& !earthingDescriptionListObservation.getRecommendation().isEmpty()) {

								if (earthingDescriptionListFlag) {
									PdfPTable headingTable = PdfPTables.remarksPDFTable();
									headingTable.addCell(PdfPTables.pdfCellHeading("3. EarthingDescription List"));
									document.add(headingTable);
									earthingDescriptionListFlag = false;

								}
								// add observation
								earthingDescriptionListTable.addCell(PdfPTables
										.pdfCellObservation(earthingDescriptionListObservation.getObservation()));
								// add recommendation
								earthingDescriptionListTable.addCell(PdfPTables
										.pdfCellObservation(earthingDescriptionListObservation.getRecommendation()));
							}
						}
						if (earthingDescriptionListFlag) {
							document.add(PdfPTables.pdfCellHeadingWithNoRemarks("3. EarthingDescription List"));
						}
						document.add(earthingDescriptionListTable);

					 List<SummaryLpsObservation> earthingClamps = summaryLPsBuilding.getSummaryLpsObservation().stream().filter(observations ->
						observations.getObservationComponentDetails().contains("earthingClamps")).
						collect(Collectors.toList());

						Boolean earthingClampsObservationFlag = true;
						PdfPTable earthingClampsObservationTable = new PdfPTable(pointColumnWidths4);
						earthingClampsObservationTable.setWidthPercentage(100); // Width 100%

						// Bridging Observation And Recommendation List with Iteration
						for (SummaryLpsObservation earthingClampsObservation : earthingClamps) {
							if (earthingClampsObservation.getObservation() != null
									&& !earthingClampsObservation.getObservation().isEmpty()
									&& earthingClampsObservation.getRecommendation() != null
									&& !earthingClampsObservation.getRecommendation().isEmpty()) {

								if (earthingClampsObservationFlag) {
									PdfPTable headingTable = PdfPTables.remarksPDFTable();
									headingTable.addCell(PdfPTables.pdfCellHeading("4. EarthingClamps Observation"));
									document.add(headingTable);
									earthingClampsObservationFlag = false;

								}
								// add observation
								earthingClampsObservationTable.addCell(
										PdfPTables.pdfCellObservation(earthingClampsObservation.getObservation()));
								// add recommendation
								earthingClampsObservationTable.addCell(
										PdfPTables.pdfCellObservation(earthingClampsObservation.getRecommendation()));
							}
						}
						if (earthingClampsObservationFlag) {
							document.add(PdfPTables.pdfCellHeadingWithNoRemarks("4. EarthingClamps Observation"));
						}
						document.add(earthingClampsObservationTable);
					 

					// EarthingElectrodeChamber Observation Main Heading Label
				 
					List<SummaryLpsObservation> earthingElectrodeChamber = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("earthingElectrodeChamber"))
							.collect(Collectors.toList());

					Boolean earthingElectrodeChamberFlag = true;
					PdfPTable earthingElectrodeChamberTable = new PdfPTable(pointColumnWidths4);
					earthingElectrodeChamberTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation earthingElectrodeChamberObservation : earthingElectrodeChamber) {
						if (earthingElectrodeChamberObservation.getObservation() != null
								&& !earthingElectrodeChamberObservation.getObservation().isEmpty()
								&& earthingElectrodeChamberObservation.getRecommendation() != null
								&& !earthingElectrodeChamberObservation.getRecommendation().isEmpty()) {

							if (earthingElectrodeChamberFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable
										.addCell(PdfPTables.pdfCellHeading("5. EarthingElectrodeChamber Observation"));
								document.add(headingTable);
								earthingElectrodeChamberFlag = false;

							}
							// add observation
							earthingElectrodeChamberTable.addCell(PdfPTables
									.pdfCellObservation(earthingElectrodeChamberObservation.getObservation()));
							// add recommendation
							earthingElectrodeChamberTable.addCell(PdfPTables
									.pdfCellObservation(earthingElectrodeChamberObservation.getRecommendation()));
						}
					}
					if (earthingElectrodeChamberFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("5. EarthingElectrodeChamber Observation"));
					}
					document.add(earthingElectrodeChamberTable);

					// Earthing if Type B(ring) Display the
					List<SummaryLpsObservation> earthingSystem = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("earthingSystem"))
							.collect(Collectors.toList());

					Boolean earthingSystemFlag = true;
					PdfPTable earthingSystemTable = new PdfPTable(pointColumnWidths4);
					earthingSystemTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation earthingSystemObservation : earthingSystem) {
						if (earthingSystemObservation.getObservation() != null
								&& !earthingSystemObservation.getObservation().isEmpty()
								&& earthingSystemObservation.getRecommendation() != null
								&& !earthingSystemObservation.getRecommendation().isEmpty()) {

							if (earthingSystemFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("6. EarthingSystem Observation"));
								document.add(headingTable);
								earthingSystemFlag = false;

							}
							// add observation
							earthingSystemTable
									.addCell(PdfPTables.pdfCellObservation(earthingSystemObservation.getObservation()));
							// add recommendation
							earthingSystemTable.addCell(
									PdfPTables.pdfCellObservation(earthingSystemObservation.getRecommendation()));
						}
					}
					if (earthingSystemFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("6. EarthingSystem Observation"));
					}
					document.add(earthingSystemTable);

					// EarthElectrodeTesting Observation Main Heading Label
					List<SummaryLpsObservation> earthElectrodeTesting = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("earthElectrodeTesting"))
							.collect(Collectors.toList());

					Boolean earthElectrodeTestingFlag = true;
					PdfPTable earthElectrodeTestingTable = new PdfPTable(pointColumnWidths4);
					earthElectrodeTestingTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation earthElectrodeTestingObservation : earthElectrodeTesting) {
						if (earthElectrodeTestingObservation.getObservation() != null
								&& !earthElectrodeTestingObservation.getObservation().isEmpty()
								&& earthElectrodeTestingObservation.getRecommendation() != null
								&& !earthElectrodeTestingObservation.getRecommendation().isEmpty()) {

							if (earthElectrodeTestingFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("7. EarthElectrodeTesting Observation"));
								document.add(headingTable);
								earthElectrodeTestingFlag = false;

							}
							// add observation
							earthElectrodeTestingTable.addCell(
									PdfPTables.pdfCellObservation(earthElectrodeTestingObservation.getObservation()));
							// add recommendation
							earthElectrodeTestingTable.addCell(PdfPTables
									.pdfCellObservation(earthElectrodeTestingObservation.getRecommendation()));
						}
					}
					if (earthElectrodeTestingFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("7. EarthElectrodeTesting Observation"));
					}
					document.add(earthElectrodeTestingTable);

				 

// ==================================================== > SPD START HERE <=====================================================================					

					document.newPage();

					PdfPTable SPDlabel = new PdfPTable(pointColumnWidths40);
					SPDlabel.setWidthPercentage(100); // Width 100%
					SPDlabel.setSpacingBefore(10f); // Space before table

					PdfPCell label5 = new PdfPCell(new Paragraph("SPD", font11B));
					label5.setHorizontalAlignment(Element.ALIGN_LEFT);
					label5.setGrayFill(0.92f);
					label5.setFixedHeight(20f);
					SPDlabel.addCell(label5);
					document.add(SPDlabel);

					PdfPTable SPDDetailsObserv = new PdfPTable(pointColumnWidths40);
					SPDDetailsObserv.setWidthPercentage(100); // Width 100%
					SPDDetailsObserv.setSpacingBefore(10f); // Space before table

				

					// SPD Observation And Recommendation Heading Label

					PdfPTable table29 = new PdfPTable(pointColumnWidths4);
					table29.setWidthPercentage(100); // Width 100%
					table29.setSpacingBefore(10f); // Space before table
					// table29.setSpacingAfter(10f);

					PdfPCell cell12 = new PdfPCell(new Paragraph("Observations", font10B));
					cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell12.setFixedHeight(20f);
					cell12.setGrayFill(0.92f);
					table29.addCell(cell12);

					PdfPCell cell13 = new PdfPCell(new Paragraph("Recommendation", font10B));
					cell13.setGrayFill(0.92f);
					cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
					table29.addCell(cell13);

					document.add(table29);
					
					// SPD Details Observation Heading Label
					   List<SummaryLpsObservation> spdReport = summaryLPsBuilding.getSummaryLpsObservation().stream().filter(observations ->
						observations.getObservationComponentDetails().contains("spdReport")).
						collect(Collectors.toList());
						
						Boolean spdReportFlag = true;
						PdfPTable spdReportTable = new PdfPTable(pointColumnWidths4);
						spdReportTable.setWidthPercentage(100); // Width 100%

	 					for (SummaryLpsObservation spdReportObservation : spdReport) {
							 if (spdReportObservation.getObservation() !=null && !spdReportObservation.getObservation().isEmpty() && 
									 spdReportObservation.getRecommendation() !=null && !spdReportObservation.getRecommendation().isEmpty()) {
									
									if (spdReportFlag) {
										 PdfPTable headingTable = PdfPTables.remarksPDFTable() ;
										 headingTable.addCell(PdfPTables.pdfCellHeading("1. SPD Details Observation"));
										 document.add(headingTable);
										 spdReportFlag = false;
										 
									}
									//add observation
									spdReportTable.addCell(PdfPTables.pdfCellObservation(spdReportObservation.getObservation()));
									// add recommendation
									spdReportTable.addCell(PdfPTables.pdfCellObservation(spdReportObservation.getRecommendation()));
						}
					}
						if (spdReportFlag) {
							document.add(PdfPTables.pdfCellHeadingWithNoRemarks("1. SPD Details Observation"));
					}
					 document.add(spdReportTable);
					 

					// SPD List Observation List With Iteration
					  List<SummaryLpsObservation> spdDescription = summaryLPsBuilding.getSummaryLpsObservation().stream().filter(observations ->
						observations.getObservationComponentDetails().contains("spdDescription")).
						collect(Collectors.toList());
						
						Boolean spdDescriptionFlag = true;
						PdfPTable spdDescriptionTable = new PdfPTable(pointColumnWidths4);
						spdDescriptionTable.setWidthPercentage(100); // Width 100%
	 

	 					for (SummaryLpsObservation spdDescriptionObservation : spdDescription) {
							 if (spdDescriptionObservation.getObservation() !=null && !spdDescriptionObservation.getObservation().isEmpty() && 
									 spdDescriptionObservation.getRecommendation() !=null && !spdDescriptionObservation.getRecommendation().isEmpty()) {
									
									if (spdDescriptionFlag) {
										 PdfPTable headingTable = PdfPTables.remarksPDFTable() ;
										 headingTable.addCell(PdfPTables.pdfCellHeading("2. SPDList Observation"));
										 document.add(headingTable);
										 spdDescriptionFlag = false;
										 
									}
									//add observation
									spdDescriptionTable.addCell(PdfPTables.pdfCellObservation(spdDescriptionObservation.getObservation()));
									// add recommendation
									spdDescriptionTable.addCell(PdfPTables.pdfCellObservation(spdDescriptionObservation.getRecommendation()));
						}
					}
						if (spdDescriptionFlag) {
							document.add(PdfPTables.pdfCellHeadingWithNoRemarks("2. SPDList Observation"));
					}
					 document.add(spdDescriptionTable);

// ================================================== > SEPARATION DISTANCE START HERE <========================================================

					document.newPage();

					PdfPTable SeparationDistancelabel = new PdfPTable(pointColumnWidths40);
					SeparationDistancelabel.setWidthPercentage(100); // Width 100%
					SeparationDistancelabel.setSpacingBefore(10f); // Space before table

					PdfPCell label6 = new PdfPCell(new Paragraph("SeparationDistance", font11B));
					label6.setHorizontalAlignment(Element.ALIGN_LEFT);
					label6.setGrayFill(0.92f);
					label6.setFixedHeight(20f);
					SeparationDistancelabel.addCell(label6);
					document.add(SeparationDistancelabel);

					// SeparationDistance Observation And Recommendation Heading Label

					PdfPTable table31 = new PdfPTable(pointColumnWidths4);
					table31.setWidthPercentage(100); // Width 100%
					table31.setSpacingBefore(10f); // Space before table
					// table31.setSpacingAfter(10f);

					PdfPCell cell14 = new PdfPCell(new Paragraph("Observations", font10B));
					cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell14.setFixedHeight(20f);
					cell14.setGrayFill(0.92f);
					table31.addCell(cell14);

					PdfPCell cell15 = new PdfPCell(new Paragraph("Recommendation", font10B));
					cell15.setGrayFill(0.92f);
					cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
					table31.addCell(cell15);

					document.add(table31);

					PdfPTable table32 = new PdfPTable(pointColumnWidths4);
					table32.setWidthPercentage(100); // Width 100%
					// table32.setSpacingBefore(5f); // Space before table
					// table32.setSpacingAfter(10f);

					// SeparationDistance Observation And Recommendation List with Iteration
					 

					PdfPTable SeparationDistObserHeader = new PdfPTable(pointColumnWidths4);
					SeparationDistObserHeader.setWidthPercentage(100); // Width 100%
					SeparationDistObserHeader.setSpacingBefore(10f); // Space before table
					SeparationDistObserHeader.setSpacingAfter(5F); // Space After table

					// SeparationDistance List Observation List With Iteration

					List<SummaryLpsObservation> separateDistanceDesc = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("seperationDistanceDescription"))
							.collect(Collectors.toList());

					Boolean separateDistanceDescFlag = true;
					PdfPTable separateDistanceDescTable = new PdfPTable(pointColumnWidths4);
					separateDistanceDescTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation separateDistanceDescObservation : separateDistanceDesc) {
						if (separateDistanceDescObservation.getObservation() != null
								&& !separateDistanceDescObservation.getObservation().isEmpty()
								&& separateDistanceDescObservation.getRecommendation() != null
								&& !separateDistanceDescObservation.getRecommendation().isEmpty()) {

							if (separateDistanceDescFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("1. SeparationDistance Observation"));
								document.add(headingTable);
								separateDistanceDescFlag = false;

							}
							// add observation
							separateDistanceDescTable.addCell(
									PdfPTables.pdfCellObservation(separateDistanceDescObservation.getObservation()));
							// add recommendation
							separateDistanceDescTable.addCell(
									PdfPTables.pdfCellObservation(separateDistanceDescObservation.getRecommendation()));
						}
					}
					if (separateDistanceDescFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("1. SeparationDistance Observation"));
					}
					document.add(separateDistanceDescTable);

					// SeparationDistance List Observation List With Iteration
					List<SummaryLpsObservation> separateDistanceDownConductors = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("separateDistanceDesc"))
							.collect(Collectors.toList());

					Boolean separateDistanceDownConductorsFlag = true;
					PdfPTable separateDDConductorsTable = new PdfPTable(pointColumnWidths4);
					separateDDConductorsTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation separateDistanceDescObservation : separateDistanceDownConductors) {
						if (separateDistanceDescObservation.getObservation() != null
								&& !separateDistanceDescObservation.getObservation().isEmpty()
								&& separateDistanceDescObservation.getRecommendation() != null
								&& !separateDistanceDescObservation.getRecommendation().isEmpty()) {

							if (separateDistanceDownConductorsFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable
										.addCell(PdfPTables.pdfCellHeading("2. SeparateDistance Observation"));
								document.add(headingTable);
								separateDistanceDownConductorsFlag = false;

							}
							// add observation
							separateDDConductorsTable.addCell(
									PdfPTables.pdfCellObservation(separateDistanceDescObservation.getObservation()));
							// add recommendation
							separateDDConductorsTable.addCell(
									PdfPTables.pdfCellObservation(separateDistanceDescObservation.getRecommendation()));
						}
					}
					if (separateDistanceDownConductorsFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("2. SeparateDistance Observation"));
					}
					document.add(separateDDConductorsTable);

					List<SummaryLpsObservation> separationDistanceDownConductors = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("separateDistanceDownConductors"))
							.collect(Collectors.toList());

					Boolean separateDDownConductorsFlag = true;
					PdfPTable separateDDownConductorsTable = new PdfPTable(pointColumnWidths4);
					separateDDownConductorsTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation separateDistanceDescObservation : separationDistanceDownConductors) {
						if (separateDistanceDescObservation.getObservation() != null
								&& !separateDistanceDescObservation.getObservation().isEmpty()
								&& separateDistanceDescObservation.getRecommendation() != null
								&& !separateDistanceDescObservation.getRecommendation().isEmpty()) {

							if (separateDDownConductorsFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable
										.addCell(PdfPTables.pdfCellHeading("3. SeparationDistanceDown Observation"));
								document.add(headingTable);
								separateDDownConductorsFlag = false;

							}
							// add observation
							separateDDownConductorsTable.addCell(
									PdfPTables.pdfCellObservation(separateDistanceDescObservation.getObservation()));
							// add recommendation
							separateDDownConductorsTable.addCell(
									PdfPTables.pdfCellObservation(separateDistanceDescObservation.getRecommendation()));
						}
					}
					if (separateDistanceDownConductorsFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("3. SeparationDistanceDown Observation"));
					}
					document.add(separateDDownConductorsTable);

//  ================================================= > EQUIPOTENTIAL BONDING START HERE < =====================================================					

					document.newPage();

					PdfPTable Equipotentialbondinglabel = new PdfPTable(pointColumnWidths40);
					Equipotentialbondinglabel.setWidthPercentage(100); // Width 100%
					Equipotentialbondinglabel.setSpacingBefore(10f); // Space before table

					PdfPCell label7 = new PdfPCell(new Paragraph("Equipotential Bonding", font11B));
					label7.setHorizontalAlignment(Element.ALIGN_LEFT);
					label7.setGrayFill(0.92f);
					label7.setFixedHeight(20f);
					Equipotentialbondinglabel.addCell(label7);
					document.add(Equipotentialbondinglabel);

					PdfPTable EquipotentialbondingObser = new PdfPTable(pointColumnWidths40);
					EquipotentialbondingObser.setWidthPercentage(100); // Width 100%
					EquipotentialbondingObser.setSpacingBefore(10f); // Space before table

					// Equipotential bonding Observation And Recommendation Heading Label

					PdfPTable table33 = new PdfPTable(pointColumnWidths4);
					table33.setWidthPercentage(100); // Width 100%
					table33.setSpacingBefore(10f); // Space before table
					// table33.setSpacingAfter(10f);

					PdfPCell cell16 = new PdfPCell(new Paragraph("Observations", font10B));
					cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell16.setFixedHeight(20f);
					cell16.setGrayFill(0.92f);
					table33.addCell(cell16);

					PdfPCell cell17 = new PdfPCell(new Paragraph("Recommendation", font10B));
					cell17.setGrayFill(0.92f);
					cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
					table33.addCell(cell17);

					document.add(table33);

				 

					// Equipotential bonding Observation And Recommendation List with Iteration
					List<SummaryLpsObservation> earthStudDescription = summaryLPsBuilding
							.getSummaryLpsObservation().stream().filter(observations -> observations
									.getObservationComponentDetails().contains("equipotentialBonding"))
							.collect(Collectors.toList());

					Boolean earthStudDescriptionFlag = true;
					PdfPTable earthStudDescriptionTable = new PdfPTable(pointColumnWidths4);
					earthStudDescriptionTable.setWidthPercentage(100); // Width 100%

					for (SummaryLpsObservation earthStudDescriptionObservation : earthStudDescription) {
						if (earthStudDescriptionObservation.getObservation() != null
								&& !earthStudDescriptionObservation.getObservation().isEmpty()
								&& earthStudDescriptionObservation.getRecommendation() != null
								&& !earthStudDescriptionObservation.getRecommendation().isEmpty()) {

							if (earthStudDescriptionFlag) {
								PdfPTable headingTable = PdfPTables.remarksPDFTable();
								headingTable.addCell(PdfPTables.pdfCellHeading("1. Equipotential Bonding Observation"));
								document.add(headingTable);
								earthStudDescriptionFlag = false;

							}
							// add observation
							earthStudDescriptionTable.addCell(
									PdfPTables.pdfCellObservation(earthStudDescriptionObservation.getObservation()));
							// add recommendation
							earthStudDescriptionTable.addCell(
									PdfPTables.pdfCellObservation(earthStudDescriptionObservation.getRecommendation()));
						}
					}
					if (earthStudDescriptionFlag) {
						document.add(PdfPTables.pdfCellHeadingWithNoRemarks("1. Equipotential Bonding Observation"));
					}
					document.add(earthStudDescriptionTable);

					document.newPage();
				}

				document.newPage();

				PdfPTable RecommendationTable = new PdfPTable(pointColumnWidths40);
				RecommendationTable.setWidthPercentage(100); // Width 100%
				RecommendationTable.setSpacingBefore(10f); // Space before table

				PdfPCell label8 = new PdfPCell(new Paragraph("Recommendation", font11B));
				label8.setHorizontalAlignment(Element.ALIGN_LEFT);
				label8.setGrayFill(0.92f);
				label8.setFixedHeight(20f);
				RecommendationTable.addCell(label8);
				document.add(RecommendationTable);

				float[] pointColumnWidths1 = { 150F, 40F };

				PdfPTable table36 = new PdfPTable(pointColumnWidths1); // 3 columns.
				table36.setWidthPercentage(100); // Width 100%
				table36.setSpacingBefore(10f); // Space before table
				// table7.setSpacingAfter(10f); // Space after table
				table36.getDefaultCell().setBorder(0);

				PdfPTable table37 = new PdfPTable(pointColumnWidths1); // 3 columns.
				table37.setWidthPercentage(100); // Width 100%
				table37.setSpacingBefore(10f); // Space before table
				// table37.setSpacingAfter(10f); // Space after table
				table37.getDefaultCell().setBorder(0);

				for (SummaryLps summary : lpsSum) {

					PdfPCell cell132 = new PdfPCell(new Paragraph(
							"Subject to necessary remedial action being taken, I/We recommended that the LPS is further inspected and tested by : ",
							font10N));
					cell132.setBorder(PdfPCell.NO_BORDER);
					cell132.setGrayFill(0.92f);
					table36.addCell(cell132);
					PdfPCell cell29 = new PdfPCell(new Paragraph(summary.getSummaryDate(), font10B));
					cell29.setGrayFill(0.92f);
					cell29.setBorder(PdfPCell.NO_BORDER);
					table36.addCell(cell29);

					PdfPCell cell134 = new PdfPCell(new Paragraph(
							"We also recommend that, ligthning protection system to be periodical inspected for every Years",
							font10N));
					cell134.setBorder(PdfPCell.NO_BORDER);
					cell134.setGrayFill(0.92f);
					table37.addCell(cell134);
					PdfPCell cell135 = new PdfPCell(new Paragraph(summary.getInspectedYear().toString(), font10B));
					cell135.setGrayFill(0.92f);
					cell135.setBorder(PdfPCell.NO_BORDER);
					table37.addCell(cell135);
				}
				document.add(table36);
				document.add(table37);

				PdfPTable DeclarationTable = new PdfPTable(pointColumnWidths40);
				DeclarationTable.setWidthPercentage(100); // Width 100%
				DeclarationTable.setSpacingBefore(10f); // Space before table
				DeclarationTable.setSpacingAfter(5f); // Space before table

				PdfPCell label9 = new PdfPCell(new Paragraph("Declaration", font11B));
				label9.setHorizontalAlignment(Element.ALIGN_LEFT);
				label9.setGrayFill(0.92f);
				label9.setFixedHeight(20f);
				DeclarationTable.addCell(label9);
				document.add(DeclarationTable);

				Paragraph paragraph1 = new Paragraph(
						"I/we being the person responsible for the inspection of the lightning protection system (as indicated by my/our signatures below), particulars of which are described in this report, having exercised reasonable skill and care when carrying out the inspection, hereby declare that information in this report including the observations provides an accurate assessment of condition of lightning protection system taking into account :",
						font10N);
				document.add(paragraph1);

				PdfPTable table38 = new PdfPTable(2);
				table38.setWidthPercentage(100); // Width 100%
				table38.setSpacingBefore(10f); // Space before table
				table38.getDefaultCell().setBorder(0);

				addRow(table38, "Inspected and Tested  By ", "Authorized By");

				List<SummaryLpsDeclaration> summaryLPsDec = lpsSummary.getSummaryLpsDeclaration();
				SummaryLpsDeclaration declaration = summaryLPsDec.get(0);
				SummaryLpsDeclaration declaration11 = summaryLPsDec.get(1);

				float[] pointColumnWidthsSec5 = { 40F, 90F, 40F, 90F };

				PdfPTable table = new PdfPTable(pointColumnWidthsSec5); // 3 columns.
				table.setWidthPercentage(100); // Width 100%

				 PdfPTable table1 = new PdfPTable(pointColumnWidthsSec5); // 3 columns.
				 table1.setWidthPercentage(100); // Width 100%

				 PdfPTable table2 = new PdfPTable(pointColumnWidthsSec5); // 3 columns.
				 table2.setWidthPercentage(100); // Width 100%

				// conversin code for signature in Inspeted
				String signature = declaration.getSignature();
				Base64 decoder = new Base64();
				byte[] imageByte = decoder.decode(signature);
				String s = new String(imageByte);
				String inspectedSignature_list[] = s.split(",");

				// conversion code for signature in Autherized
				String autherizedsignature = declaration11.getSignature();

				Base64 autherizeddecoder = new Base64();
				byte[] autherizedimageByte = autherizeddecoder.decode(autherizedsignature);
				String autherizedString = new String(autherizedimageByte);
				String autherizedsignature_list[] = autherizedString.split(",");

				if (inspectedSignature_list.length == 2 && autherizedsignature_list.length == 2) {
					String inspectedSignature1 = inspectedSignature_list[1];
					byte[] inspetedImageByte = decoder.decode(inspectedSignature1);

					String autherizedSignature1 = autherizedsignature_list[1];
					byte[] autherizedImageByte = decoder.decode(autherizedSignature1);
					addRow(table, "Name", declaration.getName(), "Name", declaration11.getName());
					addRow(table, "Company", declaration.getCompany(), "Company", declaration11.getCompany());
					document.add(table38);
					document.add(table);
					addRow1(table1, "Signature	", inspetedImageByte, "Signature	", autherizedImageByte);
					document.add(table1);
				}

				addRow(table2, "Position", declaration.getPosition(), "Position", declaration11.getPosition());
				addRow(table2, "Address", declaration.getAddress(), "Address", declaration11.getAddress());
				addRow(table2, "Date", declaration.getDate(), "Date", declaration11.getDate());
				document.add(table2);

				document.close();
				writer.close();
			
			} catch (Exception e) {
				throw new SummaryLpsException("PDF creation Exception"+e);
			}
		} else {
			throw new SummaryLpsException("Invalid Inputs");
		}
		return null;
	}


	private void addRow1(PdfPTable table1, String string, byte[] inspetedImageByte, String string2,
			byte[] autherizedImageByte) throws DocumentException, IOException {
		Font font = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);
		PdfPCell nameCell = new PdfPCell(new Paragraph(string, font));

		Image image = Image.getInstance(inspetedImageByte);
		image.setAbsolutePosition(0, 0);
		image.scaleToFit(30, 50);

		Image Autherizedimage = Image.getInstance(autherizedImageByte);
		Autherizedimage.setAbsolutePosition(0, 0);
		Autherizedimage.scaleToFit(30, 50);
		PdfPCell valueCell1 = new PdfPCell(image);
		PdfPCell valueCell2 = new PdfPCell(new Paragraph(string2, font));
		PdfPCell valueCell3 = new PdfPCell(Autherizedimage);
		nameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		valueCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		valueCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		valueCell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		nameCell.setGrayFill(0.92f);
		valueCell2.setGrayFill(0.92f);
		table1.addCell(nameCell);
		table1.addCell(valueCell1);
		table1.addCell(valueCell2);
		table1.addCell(valueCell3);

		
	}

}
