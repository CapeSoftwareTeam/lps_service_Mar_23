package com.capeelectric.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.capeelectric.service.PrintFinalPDFService;
import com.capeelectric.util.HeaderFooterPageEvent;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PrintFinalPDFServiceImpl implements PrintFinalPDFService {

	@Autowired
	private AWSS3ServiceImpl awsS3ServiceImpl;

	@Value("${s3.bucket.name}")
	private String s3BucketName;

	@Value("${s3.lps.summaryfile.bucket.name}")
	private String summaryBucketName;

	@Value("${access.key.id}")
	private String accessKeyId;

	@Value("${access.key.secret}")
	private String accessKeySecret;

	private static final Logger logger = LoggerFactory.getLogger(PrintFinalPDFServiceImpl.class);

	@Override
	public void printFinalPDF(String userName, Integer lpsId, String projectName) throws Exception {

		if (userName != null && !userName.isEmpty() && lpsId != null && lpsId != 0) {
			try {

				List<InputStream> inputPdfList = new ArrayList<InputStream>();

				inputPdfList.add(new FileInputStream("BasicLps.pdf"));
				inputPdfList.add(new FileInputStream("AirTermination.pdf"));
				inputPdfList.add(new FileInputStream("DownConductorLps.pdf"));
				inputPdfList.add(new FileInputStream("EarthingLps.pdf"));
				inputPdfList.add(new FileInputStream("SPD.pdf"));
				inputPdfList.add(new FileInputStream("SDandEarthStud.pdf"));
				inputPdfList.add(new FileInputStream("LpsSummary.pdf"));

				logger.debug("All component PDF merge Started");
				OutputStream outputStream = new FileOutputStream("finalPDF" + ".pdf");
				mergePdfFiles(inputPdfList, outputStream, awsS3ServiceImpl);
				addIndexPageWithFinalReportPDF(projectName);
				logger.debug("All component PDF merge Ended");
				try {

//					Create a S3 client with in-program credential
					BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, accessKeySecret);
					AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
							.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();

//					Uploading the PDF File in AWS S3 Bucket with folderName + fileNameInS3
					logger.debug("Uploading file Started in AWS s3 ");
					if (projectName.trim().length() > 0) {
						PutObjectRequest request = new PutObjectRequest(s3BucketName,
								"LPS_Project Name_".concat(projectName.trim())+"/"+(projectName.trim()+".pdf"),
								new File(projectName+".pdf"));
						s3Client.putObject(request);

						logger.info("Uploading file done in AWS s3 ");
					} else {
						logger.error("There is no basic details available");
						throw new Exception("There is no basic details available");
					}

				} catch (AmazonS3Exception e) {
					logger.error("Uploading file Falied in AWS s3 ");
					throw new Exception("Uploading file Falied in AWS s3 ");
				}

			} catch (Exception e) {
				logger.error("Uploading & Mergeing PDF Falied");
				throw new Exception("Uploading & Mergeing PDF Falied");
			}
		} else {
			logger.error("Invalid Inputs");
			throw new Exception("Invalid Inputs");
		}
	}
	
	public void printRiskFinalPDF(String userName, Integer riskId, String projectName) throws Exception {
		if (userName != null && !userName.isEmpty() && riskId != null && riskId != 0) {
			Document document = new Document(PageSize.A4, 68, 68, 62, 68);
			try {

				BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, accessKeySecret);
				AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
						.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();

				S3Object fullObject01;
				fullObject01 = s3Client.getObject(new GetObjectRequest(s3BucketName, "01 page static.pdf"));

				S3ObjectInputStream is01 = fullObject01.getObjectContent();
				FileOutputStream a01 = new FileOutputStream("01 page static.pdf");
				int len01;
				byte[] buffer = new byte[4096];
				while ((len01 = is01.read(buffer, 0, buffer.length)) != -1) {
					a01.write(buffer, 0, len01);
				}

				S3Object fullObject03;
				fullObject03 = s3Client.getObject(new GetObjectRequest(s3BucketName, "03 page static.pdf"));

				S3ObjectInputStream is03 = fullObject03.getObjectContent();
				FileOutputStream a03 = new FileOutputStream("03 page static.pdf");
				int len03;
				byte[] buffer03 = new byte[4096];
				while ((len03 = is03.read(buffer03, 0, buffer03.length)) != -1) {
					a03.write(buffer03, 0, len03);
				}

				//S3Object fullObject05;
				//fullObject05 = s3Client.getObject(new GetObjectRequest(s3BucketName, "05 page static.pdf"));

				//S3ObjectInputStream is05 = fullObject05.getObjectContent();
//				FileOutputStream a05 = new FileOutputStream("05 page static.pdf");
//				int len05;
//				byte[] buffer05 = new byte[4096];
////				while ((len05 = is05.read(buffer05, 0, buffer05.length)) != -1) {
////					a05.write(buffer05, 0, len05);
////				}

				List<InputStream> inputPdfList = new ArrayList<InputStream>();
				inputPdfList.add(new FileInputStream("01 page static.pdf"));
				inputPdfList.add(new FileInputStream("02 Customer Details.pdf"));
				inputPdfList.add(new FileInputStream("03 page static.pdf"));
				inputPdfList.add(new FileInputStream("04 RiskAssessment Details.pdf"));
				//inputPdfList.add(new FileInputStream("05 page static.pdf"));

				OutputStream outputStream = new FileOutputStream(projectName + ".pdf");
				mergePdfFiles(inputPdfList, outputStream, awsS3ServiceImpl);

				try {
					// Create a S3 client with in-program credential

					// Uploading the PDF File in AWS S3 Bucket with folderName + fileNameInS3

					if (projectName.length() > 0) {
						PutObjectRequest request = new PutObjectRequest(s3BucketName,
								"RiskAssessment_Project Name_".concat(projectName) + "/" + (projectName + ".pdf"),
								new File(projectName + ".pdf"));
						s3Client.putObject(request);
						logger.info("Uploading file done in AWS s3");

					} else {
						logger.error("There is project name available");
						throw new Exception("There is no project name available");

					}
				} catch (AmazonS3Exception e) {
					logger.error("Uploading file in AWS got failed please try again with proper data");
					throw new Exception("Uploading file in AWS got failed please try again with proper data");
				}

			} catch (Exception e) {
				logger.error("Please provide proper AWS credentials");
				throw new Exception("Please provide proper AWS credentials");
			}
			document.close();
		} else {
			throw new Exception("Invalid Inputs");
		}
	}

	private static void mergePdfFiles(List<InputStream> inputPdfList, OutputStream outputStream,
			AWSS3ServiceImpl awsS3ServiceImpl) throws Exception {
		try {
		Document document = new Document();
		List<PdfReader> readers = new ArrayList<PdfReader>();
		int totalPages = 0;
		Iterator<InputStream> pdfIterator = inputPdfList.iterator();
		while (pdfIterator.hasNext()) {
			InputStream pdf = pdfIterator.next();
			PdfReader pdfReader = new PdfReader(pdf);
			readers.add(pdfReader);
			totalPages = totalPages + pdfReader.getNumberOfPages();
		}
		PdfWriter writer = PdfWriter.getInstance(document, outputStream);
		Image image = Image.getInstance(awsS3ServiceImpl.findByName("Original1.png"));

		//image.scaleToFit(145, 155);
		//image.setAbsolutePosition(15, -41);
		image.scaleToFit(125, 155);
		image.setAbsolutePosition(30, -32);
		
		HeaderFooterPageEvent event = new HeaderFooterPageEvent();
		writer.setPageEvent((PdfPageEvent) event);
		document.open();
		PdfContentByte pageContentByte = writer.getDirectContent();
		PdfImportedPage pdfImportedPage;
		int currentPdfReaderPage = 1;
		Iterator<PdfReader> iteratorPDFReader = readers.iterator();
		while (iteratorPDFReader.hasNext()) {
			PdfReader pdfReader = iteratorPDFReader.next();
			while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
				document.newPage();
				document.add(image);
				pdfImportedPage = writer.getImportedPage(pdfReader, currentPdfReaderPage);
				pageContentByte.addTemplate(pdfImportedPage, 0, 0);
				currentPdfReaderPage++;
			}
			currentPdfReaderPage = 1;
		}
		outputStream.flush();
		document.close();
		outputStream.close();
		} catch (Exception message) {
			logger.error("Uploading & Mergeing PDF Falied"+message.getMessage());
			throw new Exception("Uploading & Mergeing PDF Falied");
		}

	}
		private static void addIndexPageWithFinalReportPDF(String projectName) throws Exception {
			try {
				PDFMergerUtility ut = new PDFMergerUtility();
				
	 			ut.addSource("index.pdf");
				ut.addSource("finalPDF.pdf");
				ut.setDestinationFileName(projectName + ".pdf");
				ut.mergeDocuments(null);

			} catch (Exception message) {
				logger.error("IndexPage PDF Mergeing Falied"+message.getMessage());
				throw new Exception("IndexPage PDF Mergeing Falied");
			}
			
		}
	@Override
	public void printSummaryPDF(String userName, Integer lpsId, String projectName) throws Exception {

		if (userName != null && !userName.isEmpty() && lpsId != null && lpsId != 0) {
			try {

				// Create a S3 client with in-program credential
				BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, accessKeySecret);
				AmazonS3 s3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTH_1)
						.withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();

				// Uploading the PDF File in AWS S3 Bucket with folderName + fileNameInS3

				if (projectName.length() > 0) {
					PutObjectRequest request = new PutObjectRequest(summaryBucketName,
							"LPS_Project In SummaryFile Name_".concat(projectName) + "/" + (projectName + ".pdf"),
							new File(projectName + ".pdf"));
					s3Client.putObject(request);

					logger.info("Uploading file done in AWS s3 ");
				} else {
					logger.error("There is no summary details available");
					throw new Exception("There is no summary details available");
				}

			} catch (AmazonS3Exception e) {
				logger.error("SummaryPDF creation failed");
				throw new Exception("SummaryPDF creation failed");
			}

		} else {
			logger.error("Invalid Inputs");
			throw new Exception("Invalid Inputs");
		}
	}
}