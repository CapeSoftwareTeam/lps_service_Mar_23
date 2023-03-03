package com.capeelectric.service.impl;

import java.io.File;
import java.io.FileOutputStream;

import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.capeelectric.exception.BasicLpsException;
import com.capeelectric.model.BasicLps;
import com.capeelectric.model.Register;
import com.capeelectric.model.ResponseFile;
import com.capeelectric.repository.BasicLpsRepository;
import com.capeelectric.repository.FileDBRepository;
import com.capeelectric.service.PrintBasicLpsService;
import com.capeelectric.util.AWSS3Configuration;
import com.capeelectric.util.Constants;
import com.capeelectric.util.ContentEvent;
import com.capeelectric.util.PageNumberEvent;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PrintBasicLpsServiceImpl implements PrintBasicLpsService {

	@Autowired
	private BasicLpsRepository basicLpsRepository;
	
	@Autowired
	private FileDBRepository fileDBRepository;
	
	@Autowired
	private AWSS3Configuration AWSS3configuration;
	
	@Autowired
	private com.capeelectric.repository.RegistrationRepository registrationRepository;
	
	@Value("${s3.lps.file.upload.bucket.name}")
	private String s3LpsFileUploadBucketName;
	
	private static final Logger logger = LoggerFactory.getLogger(PrintBasicLpsServiceImpl.class);


//	@Override
//	public void printBasicLps(String userName, Integer lpsId ) throws BasicLpsException, Exception {

	@Override
	public void printBasicLps(String userName, Integer lpsId, Optional<BasicLps> basicLpsDetails,
			PageNumberEvent pageNum, Map<String, Map<Integer, String>> indexNumberDeatils)
			throws BasicLpsException, Exception {

		if (userName != null && !userName.isEmpty() && lpsId != null && lpsId != 0) {
			Document document = new Document(PageSize.A4, 68, 68, 62, 68);
			// Optional<BasicLps> basicLpsDetails
			try {
				
				//adding index details
				Map<Integer,String> basicDetailsContents = new HashMap<Integer,String>();
				ContentEvent.numberOfContents = 1;
				 
				float[] pointColumnWidths = { 25F, 150F, 55F, 50F };
				Font font1A = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);

				PdfPTable fileDataTable = new PdfPTable(pointColumnWidths);
				fileDataTable.setWidthPercentage(100); // Width 100%
				fileDataTable.setSpacingBefore(5f); // Space before table
				fileDataTable.setSpacingAfter(10f);
				fileDataTable.setWidthPercentage(100);

				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("BasicLps.pdf"));

				Optional<BasicLps> basicLps = basicLpsRepository.findByBasicLpsId(lpsId);
				BasicLps basicLps1 = basicLps.get();

				writer.setPageEvent(pageNum); //page number generating
				document.open();

				float[] pointColumnWidths4 = { 100F };
				
				PdfPTable table = new PdfPTable(pointColumnWidths4);
				table.setWidthPercentage(100); // Width 100%
				table.setSpacingBefore(10f); // Space before table
				table.setWidthPercentage(100);

				Font font11 = new Font(BaseFont.createFont(), 11, Font.NORMAL | Font.BOLD, BaseColor.BLACK);

				PdfPCell cell = new PdfPCell(new Paragraph("Basic details ", font11));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setGrayFill(0.92f);
				cell.setFixedHeight(20f);
				table.addCell(cell);
				pageNum.setPage(1);
				basicDetailsContents.put(ContentEvent.numberOfContents++, pageNum.getPage()+ ",Basic details"); //indexpage

				document.add(table);
				float[] pointColumnWidths1 = { 30F, 70F };
				Font font2 = new Font(BaseFont.createFont(), 10, Font.NORMAL | Font.BOLD, BaseColor.BLACK);
				Font font3 = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);
				
				PdfPTable table1 = new PdfPTable(pointColumnWidths1);
				table1.setWidthPercentage(100); // Width 100%
				// table1.setSpacingBefore(10f); // Space before table
				table1.setWidthPercentage(100);

				PdfPCell cell11 = new PdfPCell(new Paragraph("Client Name", font2));
				cell11.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell11.setFixedHeight(20f);
				cell11.setGrayFill(0.92f);
				table1.addCell(cell11);

				PdfPCell cell2 = new PdfPCell(new Paragraph(basicLps1.getClientName(), font3));
				cell2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table1.addCell(cell2);

				PdfPCell cell31 = new PdfPCell(new Paragraph("Project Name", font2));
				cell31.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell31.setFixedHeight(20f);
				cell31.setGrayFill(0.92f);
				table1.addCell(cell31);

				PdfPCell cell41 = new PdfPCell(new Paragraph(basicLps1.getProjectName(), font3));
				cell41.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table1.addCell(cell41);

				PdfPCell cell51 = new PdfPCell(new Paragraph("PMC Name", font2));
				cell51.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell51.setFixedHeight(20f);
				cell51.setGrayFill(0.92f);
				table1.addCell(cell51);

				PdfPCell cell61 = new PdfPCell(new Paragraph(basicLps1.getPmcName(), font3));
				cell61.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table1.addCell(cell61);

				PdfPCell cell71 = new PdfPCell(new Paragraph("Consultant Name", font2));
				cell71.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell71.setFixedHeight(20f);
				cell71.setGrayFill(0.92f);
				table1.addCell(cell71);

				PdfPCell cell81 = new PdfPCell(new Paragraph(basicLps1.getConsultantName(), font3));
				cell81.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table1.addCell(cell81);

				PdfPCell cell91 = new PdfPCell(new Paragraph("Contractor Name", font2));
				cell91.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell91.setFixedHeight(20f);
				cell91.setGrayFill(0.92f);
				table1.addCell(cell91);

				PdfPCell cell10 = new PdfPCell(new Paragraph(basicLps1.getContractorName(), font3));
				cell10.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table1.addCell(cell10);

				PdfPCell cell111 = new PdfPCell(new Paragraph("Dealer/Sub-contractor", font2));
				cell111.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell111.setFixedHeight(20f);
				cell111.setGrayFill(0.92f);
				table1.addCell(cell111);

				PdfPCell cell12 = new PdfPCell(new Paragraph(basicLps1.getDealerContractorName(), font3));
				cell12.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table1.addCell(cell12);

				PdfPCell cell13 = new PdfPCell(new Paragraph("Address", font2));
				cell13.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell13.setFixedHeight(20f);
				cell13.setGrayFill(0.92f);
				table1.addCell(cell13);

				PdfPCell cell14 = new PdfPCell(new Paragraph(basicLps1.getAddress(), font3));
				cell14.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table1.addCell(cell14);

				PdfPCell cell15 = new PdfPCell(new Paragraph("Location/Region/Branch", font2));
				cell15.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell15.setFixedHeight(20f);
				cell15.setGrayFill(0.92f);
				table1.addCell(cell15);

				PdfPCell cell16 = new PdfPCell(new Paragraph(basicLps1.getLocation(), font3));
				cell16.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table1.addCell(cell16);

				PdfPCell cell151 = new PdfPCell(new Paragraph("Type of Industry", font2));
				cell151.setHorizontalAlignment(Element.ALIGN_LEFT);
//				cell151.setFixedHeight(25f);
				cell151.setGrayFill(0.92f);
				table1.addCell(cell151);

				PdfPCell cell201 = new PdfPCell(new Paragraph(basicLps1.getIndustryType(), font3));
				cell201.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table1.addCell(cell201);

				document.add(table1);

				PdfPTable table4 = new PdfPTable(pointColumnWidths1);
				table4.setWidthPercentage(100); // Width 100%
				// table4.setSpacingBefore(10f); // Space before table
				table4.setWidthPercentage(100);

				PdfPCell cell27 = new PdfPCell(new Paragraph("Soil Resistivity (ohms)", font2));
				cell27.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell27.setGrayFill(0.92f);
				cell27.setFixedHeight(20f);
				table4.addCell(cell27);

				PdfPCell cell28 = new PdfPCell(new Paragraph(basicLps1.getSoilResistivity(), font3));
				cell28.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table4.addCell(cell28);

				document.add(table4);

				float[] pointColumnWidths30 = { 5F };

				PdfPTable table21 = new PdfPTable(pointColumnWidths30);
				table21.setWidthPercentage(100); // Width 100%
				table21.setSpacingBefore(20f); // Space before table
				table21.setWidthPercentage(100);

				//inspector details
				PdfPCell cell30 = new PdfPCell(new Paragraph("Person who prepared the report", font11));
				cell30.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell30.setGrayFill(0.92f);
				cell30.setFixedHeight(20f);
				table21.addCell(cell30);
				document.add(table21);

				PdfPTable table5 = new PdfPTable(pointColumnWidths1);
				table5.setWidthPercentage(100); // Width 100%
				// table1.setSpacingBefore(10f); // Space before table
				table5.setWidthPercentage(100);

				PdfPCell cell40 = new PdfPCell(new Paragraph("Email", font2));
				cell40.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				cell40.setFixedHeight(20f);
				cell40.setGrayFill(0.92f);
				table5.addCell(cell40);

				PdfPCell cell43 = new PdfPCell(new Paragraph(basicLps1.getUserName(), font3));
				cell43.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				table5.addCell(cell43);

				Optional<Register> registerRepo = registrationRepository.findByUsername(basicLps1.getUserName());
					if (registerRepo.isPresent()) {
						PdfPCell nameCell = new PdfPCell(new Paragraph("Name", font2));
						nameCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
						nameCell.setFixedHeight(20f);
						nameCell.setGrayFill(0.92f);
						table5.addCell(nameCell);
						
						PdfPCell cell42 = new PdfPCell(new Paragraph(registerRepo.get().getName(), font3));
						cell42.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
						cell42.setFixedHeight(20f);
						cell42.setGrayFill(0.92f);
						table5.addCell(cell42);
					}
			
				document.add(table5);
				
				float[] orderingPointColumnWidths = { 25F };

				PdfPTable orderingTable = new PdfPTable(orderingPointColumnWidths);
				orderingTable.setWidthPercentage(100); // Width 100%
				orderingTable.setSpacingBefore(20f); // Space before table
				orderingTable.setWidthPercentage(100);
				
				//client details
				PdfPCell orderingTablecell = new PdfPCell(new Paragraph("Person who ordered the report", font11));
				orderingTablecell.setHorizontalAlignment(Element.ALIGN_LEFT);
				orderingTablecell.setGrayFill(0.92f);
				orderingTablecell.setFixedHeight(20f);
				orderingTable.addCell(orderingTablecell);
				document.add(orderingTable);

				PdfPTable orderingTable1 = new PdfPTable(pointColumnWidths1);
				orderingTable1.setWidthPercentage(100); // Width 100%
				// table1.setSpacingBefore(10f); // Space before table
				orderingTable1.setWidthPercentage(100);

				PdfPCell orderingTable1Cell = new PdfPCell(new Paragraph("Email", font2));
				orderingTable1Cell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				orderingTable1Cell.setFixedHeight(20f);
				orderingTable1Cell.setGrayFill(0.92f);
				orderingTable1.addCell(orderingTable1Cell);

				PdfPCell orderingTable1Cell1 = new PdfPCell(new Paragraph(basicLps1.getMailId(), font3));
				orderingTable1Cell1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				orderingTable1.addCell(orderingTable1Cell1);

				PdfPCell orderingTable1cell2 = new PdfPCell(new Paragraph("Name", font2));
				orderingTable1cell2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
				orderingTable1cell2.setFixedHeight(20f);
				orderingTable1cell2.setGrayFill(0.92f);
				orderingTable1.addCell(orderingTable1cell2);
				
				registerRepo = registrationRepository.findByUsername(basicLps1.getMailId());

				if (registerRepo.isPresent()) {
					PdfPCell cell44 = new PdfPCell(new Paragraph(registerRepo.get().getName(), font3));
					cell44.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					orderingTable1.addCell(cell44);
					
					PdfPCell companyCell = new PdfPCell(new Paragraph("Company", font2));
					companyCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					companyCell.setFixedHeight(20f);
					companyCell.setGrayFill(0.92f);
					orderingTable1.addCell(companyCell);

					PdfPCell companyCell1 = new PdfPCell(new Paragraph(registerRepo.get().getCompanyName(), font3));
					companyCell1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					orderingTable1.addCell(companyCell1);
					
					PdfPCell departmentCell = new PdfPCell(new Paragraph("Department", font2));
					departmentCell.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					departmentCell.setFixedHeight(20f);
					departmentCell.setGrayFill(0.92f);
					orderingTable1.addCell(departmentCell);

					PdfPCell departmentCell1 = new PdfPCell(new Paragraph(registerRepo.get().getDepartment(), font3));
					departmentCell1.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					orderingTable1.addCell(departmentCell1);

					PdfPCell cell45 = new PdfPCell(new Paragraph("Designation", font2));
					cell45.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					cell45.setFixedHeight(20f);
					cell45.setGrayFill(0.92f);
					orderingTable1.addCell(cell45);

					PdfPCell cell46 = new PdfPCell(new Paragraph(registerRepo.get().getDesignation(), font3));
					cell46.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					orderingTable1.addCell(cell46);

					PdfPCell cell47 = new PdfPCell(new Paragraph("Contact Number", font2));
					cell47.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					cell47.setFixedHeight(20f);
					cell47.setGrayFill(0.92f);
					orderingTable1.addCell(cell47);

					PdfPCell cell48 = new PdfPCell(new Paragraph(registerRepo.get().getContactNumber(), font3));
					cell48.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					orderingTable1.addCell(cell48);

					PdfPCell cell52 = new PdfPCell(new Paragraph("Availability of previous report", font2));
					cell52.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					cell52.setFixedHeight(20f);
					cell52.setGrayFill(0.92f);
					orderingTable1.addCell(cell52);

					PdfPCell cell53 = new PdfPCell(new Paragraph(basicLps1.getAvailabilityOfPreviousReport(), font3));
					cell53.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
					orderingTable1.addCell(cell53);

					document.add(orderingTable1);
				}
				
				if (basicLps1.getAvailabilityOfPreviousReport().equalsIgnoreCase("Yes")) {
					try {
//						// Create a S3 client with in-program credential
//						BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId,
//								accessKeySecret);
//						AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
//								.withRegion(Regions.AP_SOUTH_1)
//								.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
						AmazonS3 s3Client = AWSS3configuration.getAmazonS3Client();
						// Uploading the PDF File in AWS S3 Bucket with folderName + fileNameInS3
						Optional<ResponseFile> fileData = fileDBRepository.findById(basicLps1.getFileId());
						
						if (fileData.isPresent() && fileData.get() !=null &&  fileData.get().getFileName().length() > 0) {
							
							 Optional<ResponseFile> fileDataRepo = fileDBRepository.findById(basicLpsDetails.get().getFileId());

								Blob blob = fileDataRepo.get().getData();
								byte[] bytes = blob.getBytes(1l, (int) blob.length());
								FileOutputStream fileout = new FileOutputStream(fileDataRepo.get().getFileName());
								fileout.write(bytes);
						 
							PutObjectRequest request = new PutObjectRequest(s3LpsFileUploadBucketName,
									"LPS_BasicUploadedFile Name_"
											+ fileData.get().getFileId().toString()
													.concat(fileData.get().getFileName()),
									new File(fileData.get().getFileName()));
							 
								s3Client.putObject(request);
							 
							logger.debug(
									"Basic file uploded AWS s3");

							PdfPCell cell7322 = new PdfPCell(new Paragraph(
									"Copy and Paste below URL in a new window or tab and download/view the uploaded file:",
									font1A));
							// cell73.setGrayFill(0.92f);
							// cell7322.setBorder(PdfPCell.NO_BORDER);
							cell7322.setColspan(4);
							fileDataTable.addCell(cell7322);

							PdfPCell cell732 = new PdfPCell(new Paragraph(
									Constants.LPS_FILE_UPLOAD_DOMAIN + "/"
											+ "LPS_BasicUploadedFile Name_"
											+ fileData.get().getFileId().toString()
											.concat(fileData.get().getFileName()),
									FontFactory.getFont(FontFactory.HELVETICA, 6, Font.UNDERLINE,
											BaseColor.BLUE)));
							cell732.setGrayFill(0.92f);
							// cell732.setBorder(PdfPCell.NO_BORDER);
							cell732.setColspan(4);
							cell732.setFixedHeight(13f);
							fileDataTable.addCell(cell732);
							document.add(fileDataTable);
						} else {
							logger.error("LPS_BasicUploadedFile no file available");
							throw new Exception(
									"LPS_BasicUploadedFile no file available");
						}

					} catch (AmazonS3Exception e) {
						logger.error("Finding LPS_BasicUploadedFile is falied in AWS s3");
						throw new Exception("Finding LPS_BasicUploadedFile is falied in AWS s3"+e.getMessage());
					}
				
				}

//				for (AirBasicDescription basicDesc2 : basicDesc1) {
//					basicDescription(basicDesc2, table21);
//					document.add(table21);
//				}

				document.close();
				writer.close();
				indexNumberDeatils.put("BasicDetails", basicDetailsContents);

			} catch (Exception exception) {
				logger.error("BasicPDF creation failed" +exception);
				throw new Exception("BasicPDF creation failed");
			}

		} else {
			logger.error("Invalid Inputs");
			throw new BasicLpsException("Invalid Inputs");
		}
	}

//	private void basicDescription(AirBasicDescription basicDesc2, PdfPTable table2)
//			throws DocumentException, IOException {
//
//		Font font = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);
//
//		PdfPCell cell34 = new PdfPCell(new Paragraph("1", font));
//		cell34.setHorizontalAlignment(Element.ALIGN_CENTER);
////		cell34.setFixedHeight(20f);
//		cell34.setGrayFill(0.92f);
//		table2.addCell(cell34);
//
//		PdfPCell cell35 = new PdfPCell(new Paragraph("Collect approved copy of drawing from the site", font));
//		cell35.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
////		cell35.setFixedHeight(20f);
//		cell35.setGrayFill(0.92f);
//		table2.addCell(cell35);
//
//		PdfPCell cell36 = new PdfPCell(new Paragraph(basicDesc2.getApprovedDrawingObserv(), font));
//		cell36.setHorizontalAlignment(Element.ALIGN_LEFT);
////		cell36.setFixedHeight(20f);
//		table2.addCell(cell36);
//
//		PdfPCell cell37 = new PdfPCell(new Paragraph(basicDesc2.getApprovedDrawingRemarks(), font));
////		cell37.setFixedHeight(20f);
//		cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
//		table2.addCell(cell37);
//
//		PdfPCell cell40 = new PdfPCell(new Paragraph("1a", font));
//		cell40.setHorizontalAlignment(Element.ALIGN_RIGHT);
////		cell40.setFixedHeight(20f);
//		cell40.setGrayFill(0.92f);
//		table2.addCell(cell40);
//
//		PdfPCell cell41 = new PdfPCell(new Paragraph("Architect Name", font));
//		cell41.setHorizontalAlignment(Element.ALIGN_RIGHT);
////		cell41.setFixedHeight(20f);
//		cell41.setGrayFill(0.92f);
//		table2.addCell(cell41);
//
//		PdfPCell cell42 = new PdfPCell(new Paragraph(basicDesc2.getArchitectNameObserv(), font));
//		cell42.setHorizontalAlignment(Element.ALIGN_LEFT);
////		cell42.setFixedHeight(20f);
//		table2.addCell(cell42);
//
//		PdfPCell cell43 = new PdfPCell(new Paragraph(basicDesc2.getArchitectNameRemarks(), font));
////		cell43.setFixedHeight(20f);
//		cell43.setHorizontalAlignment(Element.ALIGN_LEFT);
//		table2.addCell(cell43);
//
//		PdfPCell cell44 = new PdfPCell(new Paragraph("1b", font));
//		cell44.setHorizontalAlignment(Element.ALIGN_RIGHT);
////		cell43.setFixedHeight(20f);
//		cell44.setGrayFill(0.92f);
//		table2.addCell(cell44);
//
//		PdfPCell cell45 = new PdfPCell(new Paragraph("Date of Design", font));
//		cell45.setHorizontalAlignment(Element.ALIGN_RIGHT);
////		cell45.setFixedHeight(20f);
//		cell45.setGrayFill(0.92f);
//		table2.addCell(cell45);
//
//		PdfPCell cell46 = new PdfPCell(new Paragraph(basicDesc2.getDesignDateObserv(), font));
//		cell46.setHorizontalAlignment(Element.ALIGN_LEFT);
////		cell46.setFixedHeight(20f);
//		table2.addCell(cell46);
//
//		PdfPCell cell47 = new PdfPCell(new Paragraph(basicDesc2.getDesignDateRemarks(), font));
////		cell47.setFixedHeight(20f);
//		cell47.setHorizontalAlignment(Element.ALIGN_LEFT);
//		table2.addCell(cell47);
//
//		PdfPCell cell48 = new PdfPCell(new Paragraph("1c", font));
//		cell48.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		cell48.setGrayFill(0.92f);
////		cell48.setFixedHeight(20f);
//		table2.addCell(cell48);
//
//		PdfPCell cell49 = new PdfPCell(new Paragraph("Approved by", font));
//		cell49.setHorizontalAlignment(Element.ALIGN_RIGHT);
////		cell49.setFixedHeight(20f);
//		cell49.setGrayFill(0.92f);
//		table2.addCell(cell49);
//
//		PdfPCell cell50 = new PdfPCell(new Paragraph(basicDesc2.getApprovedByObserv(), font));
//		cell50.setHorizontalAlignment(Element.ALIGN_LEFT);
////		cell50.setFixedHeight(20f);
//		table2.addCell(cell50);
//
//		PdfPCell cell51 = new PdfPCell(new Paragraph(basicDesc2.getApprovedByRemarks(), font));
////		cell51.setFixedHeight(20f);
//		cell51.setHorizontalAlignment(Element.ALIGN_LEFT);
//		table2.addCell(cell51);
//
//		PdfPCell cell52 = new PdfPCell(new Paragraph("1d", font));
//		cell52.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		cell52.setGrayFill(0.92f);
////		cell52.setFixedHeight(20f);
//		table2.addCell(cell52);
//
//		PdfPCell cell53 = new PdfPCell(new Paragraph("Date of approval", font));
//		cell53.setHorizontalAlignment(Element.ALIGN_RIGHT);
////		cell53.setFixedHeight(20f);
//		cell53.setGrayFill(0.92f);
//		table2.addCell(cell53);
//
//		PdfPCell cell54 = new PdfPCell(new Paragraph(basicDesc2.getDateOfApprovalOb(), font));
//		cell54.setHorizontalAlignment(Element.ALIGN_LEFT);
////		cell54.setFixedHeight(20f);
//		table2.addCell(cell54);
//
//		PdfPCell cell55 = new PdfPCell(new Paragraph(basicDesc2.getDateOfApprovalRem(), font));
//		cell55.setHorizontalAlignment(Element.ALIGN_LEFT);
//		table2.addCell(cell55);
//
//		PdfPCell cell56 = new PdfPCell(new Paragraph("1e", font));
//		cell56.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		cell56.setGrayFill(0.92f);
//		table2.addCell(cell56);
//
//		PdfPCell cell57 = new PdfPCell(new Paragraph("Drawing number", font));
//		cell57.setHorizontalAlignment(Element.ALIGN_RIGHT);
////		cell57.setFixedHeight(20f);
//		cell57.setGrayFill(0.92f);
//		table2.addCell(cell57);
//
//		PdfPCell cell58 = new PdfPCell(new Paragraph(basicDesc2.getDrawingObserv(), font));
//		cell58.setHorizontalAlignment(Element.ALIGN_LEFT);
////		cell58.setFixedHeight(20f);
//		table2.addCell(cell58);
//
//		PdfPCell cell59 = new PdfPCell(new Paragraph(basicDesc2.getDrawingRemarks(), font));
//		cell59.setHorizontalAlignment(Element.ALIGN_LEFT);
//		table2.addCell(cell59);
//
//		PdfPCell cell60 = new PdfPCell(new Paragraph("1f", font));
//		cell60.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		cell60.setGrayFill(0.92f);
//		table2.addCell(cell60);
//
//		PdfPCell cell61 = new PdfPCell(new Paragraph("Revision number", font));
//		cell61.setHorizontalAlignment(Element.ALIGN_RIGHT);
////		cell61.setFixedHeight(20f);
//		cell61.setGrayFill(0.92f);
//		table2.addCell(cell61);
//
//		PdfPCell cell62 = new PdfPCell(new Paragraph(basicDesc2.getRevisionNoObserv(), font));
//		cell62.setHorizontalAlignment(Element.ALIGN_LEFT);
////		cell62.setFixedHeight(20f);
//		table2.addCell(cell62);
//
//		PdfPCell cell63 = new PdfPCell(new Paragraph(basicDesc2.getRevisionNoRemarks(), font));
//		cell63.setHorizontalAlignment(Element.ALIGN_LEFT);
//		table2.addCell(cell63);
//
//		PdfPCell cell64 = new PdfPCell(new Paragraph("2", font));
//		cell64.setHorizontalAlignment(Element.ALIGN_CENTER);
//		cell64.setGrayFill(0.92f);
//		table2.addCell(cell64);
//
//		PdfPCell cell65 = new PdfPCell(new Paragraph("Check for any deviation of design with standard", font));
//		cell65.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
////		cell65.setFixedHeight(20f);
//		cell65.setGrayFill(0.92f);
//		table2.addCell(cell65);
//
//		PdfPCell cell66 = new PdfPCell(new Paragraph(basicDesc2.getDeviationObserv(), font));
//		cell66.setHorizontalAlignment(Element.ALIGN_LEFT);
////		cell66.setFixedHeight(20f);
//		table2.addCell(cell66);
//
//		PdfPCell cell67 = new PdfPCell(new Paragraph(basicDesc2.getDeviationRemarks(), font));
//		cell67.setHorizontalAlignment(Element.ALIGN_LEFT);
//		table2.addCell(cell67);
//
//		PdfPCell cell68 = new PdfPCell(new Paragraph("3", font));
//		cell68.setHorizontalAlignment(Element.ALIGN_CENTER);
//		cell68.setGrayFill(0.92f);
//		table2.addCell(cell68);
//
//		PdfPCell cell69 = new PdfPCell(new Paragraph("Quality of installation", font));
//		cell69.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
////		cell69.setFixedHeight(20f);
//		cell69.setGrayFill(0.92f);
//		table2.addCell(cell69);
//
//		PdfPCell cell70 = new PdfPCell(new Paragraph(basicDesc2.getInstallationQualityObserv(), font));
//		cell70.setHorizontalAlignment(Element.ALIGN_LEFT);
////		cell70.setFixedHeight(20f);
//		table2.addCell(cell70);
//
//		PdfPCell cell71 = new PdfPCell(new Paragraph(basicDesc2.getInstallationQualityRemarks(), font));
//		cell71.setHorizontalAlignment(Element.ALIGN_LEFT);
//		table2.addCell(cell71);
//
//		PdfPCell cell72 = new PdfPCell(new Paragraph("", font));
//		cell72.setHorizontalAlignment(Element.ALIGN_CENTER);
//		cell72.setGrayFill(0.92f);
//		table2.addCell(cell72);
//
//		PdfPCell cell73 = new PdfPCell(new Paragraph("", font));
//		cell73.setHorizontalAlignment(Element.ALIGN_CENTER);
//		cell73.setFixedHeight(20f);
//		cell73.setGrayFill(0.92f);
//		table2.addCell(cell73);
//
//		PdfPCell cell74 = new PdfPCell(new Paragraph("", font));
//		cell74.setHorizontalAlignment(Element.ALIGN_LEFT);
//		cell74.setFixedHeight(20f);
//		table2.addCell(cell74);
//
//		PdfPCell cell75 = new PdfPCell(new Paragraph("", font));
//		cell75.setHorizontalAlignment(Element.ALIGN_LEFT);
//		table2.addCell(cell75);
//
//		PdfPCell cell76 = new PdfPCell(new Paragraph("", font));
//		cell76.setHorizontalAlignment(Element.ALIGN_CENTER);
//		cell76.setGrayFill(0.92f);
//		table2.addCell(cell76);
//
//		PdfPCell cell77 = new PdfPCell(new Paragraph("", font));
//		cell77.setHorizontalAlignment(Element.ALIGN_CENTER);
//		cell77.setFixedHeight(20f);
//		cell77.setGrayFill(0.92f);
//		table2.addCell(cell77);
//
//		PdfPCell cell78 = new PdfPCell(new Paragraph("", font));
//		cell78.setHorizontalAlignment(Element.ALIGN_CENTER);
//		cell78.setFixedHeight(20f);
//		table2.addCell(cell78);
//
//		PdfPCell cell79 = new PdfPCell(new Paragraph("", font));
//		cell79.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table2.addCell(cell79);
//
//		// Extra Empty Columns and rows
//
////		PdfPCell cell80 = new PdfPCell(new Paragraph("", font));
////		cell80.setHorizontalAlignment(Element.ALIGN_CENTER);
////		cell80.setGrayFill(0.92f);
////		table2.addCell(cell80);
////
////		PdfPCell cell81 = new PdfPCell(new Paragraph("", font));
////		cell81.setHorizontalAlignment(Element.ALIGN_CENTER);
////		cell81.setFixedHeight(20f);
////		cell81.setGrayFill(0.92f);
////		table2.addCell(cell81);
////
////		PdfPCell cell82 = new PdfPCell(new Paragraph("", font));
////		cell82.setHorizontalAlignment(Element.ALIGN_CENTER);
////		cell82.setFixedHeight(20f);
////		table2.addCell(cell82);
////
////		PdfPCell cell83 = new PdfPCell(new Paragraph("", font));
////		cell83.setHorizontalAlignment(Element.ALIGN_CENTER);
////		table2.addCell(cell83);
////
////		PdfPCell cell84 = new PdfPCell(new Paragraph("", font));
////		cell84.setHorizontalAlignment(Element.ALIGN_CENTER);
////		cell84.setGrayFill(0.92f);
////		table2.addCell(cell84);
////
////		PdfPCell cell85 = new PdfPCell(new Paragraph("", font));
////		cell85.setHorizontalAlignment(Element.ALIGN_CENTER);
////		cell85.setFixedHeight(20f);
////		cell85.setGrayFill(0.92f);
////		table2.addCell(cell85);
////
////		PdfPCell cell86 = new PdfPCell(new Paragraph("", font));
////		cell86.setHorizontalAlignment(Element.ALIGN_CENTER);
////		cell86.setFixedHeight(20f);
////		table2.addCell(cell86);
////
////		PdfPCell cell87 = new PdfPCell(new Paragraph("", font));
////		cell87.setHorizontalAlignment(Element.ALIGN_CENTER);
////		table2.addCell(cell87);
////
////		PdfPCell cell88 = new PdfPCell(new Paragraph("", font));
////		cell88.setHorizontalAlignment(Element.ALIGN_CENTER);
////		cell88.setGrayFill(0.92f);
////		table2.addCell(cell88);
////
////		PdfPCell cell89 = new PdfPCell(new Paragraph("", font));
////		cell89.setHorizontalAlignment(Element.ALIGN_CENTER);
////		cell89.setFixedHeight(20f);
////		cell89.setGrayFill(0.92f);
////		table2.addCell(cell89);
////
////		PdfPCell cell90 = new PdfPCell(new Paragraph("", font));
////		cell90.setHorizontalAlignment(Element.ALIGN_CENTER);
////		cell90.setFixedHeight(20f);
////		table2.addCell(cell90);
////
////		PdfPCell cell91 = new PdfPCell(new Paragraph("", font));
////		cell91.setHorizontalAlignment(Element.ALIGN_CENTER);
////		table2.addCell(cell91);
//
//	}

}