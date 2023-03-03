package com.capeelectric.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TreeMap;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author CAPE-SOFTWARE
 *
 */
public class PrintIndexServiceImpl {

	Document document;
	PdfWriter writer;
	Font font;
	Font font3;
	Font font11C;
	Map<String, Map<Integer, String>> contentsAll;
	Integer countNumber;
	Integer buildingCountNumber;

	private static final Logger logger = LoggerFactory.getLogger(PrintIndexServiceImpl.class);

	public void printIndexPage(Map<String, Map<Integer, String>> indexNumberDeatils)
			throws Exception {
		document = new Document();

		writer = PdfWriter.getInstance(document, new FileOutputStream("index.pdf"));

		font = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);
		font3 = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);
		font11C = new Font(BaseFont.createFont(), 10, Font.NORMAL | Font.BOLD, BaseColor.BLACK);
		document.open();

		float[] pointColumnWidths = { 200F };

		PdfPTable headertable = new PdfPTable(pointColumnWidths);
		headertable.setWidthPercentage(100);// Width 100%
		headertable.setSpacingBefore(5f); // Space before table

		PdfPCell label = new PdfPCell(
				new Paragraph("Index", new Font(BaseFont.createFont(), 11, Font.NORMAL | Font.BOLD, BaseColor.BLACK)));
		label.setHorizontalAlignment(Element.ALIGN_CENTER);
		label.setGrayFill(0.92f);
		label.setFixedHeight(20f);
		headertable.addCell(label);
		addHomePage();
		document.newPage();
		document.add(headertable);

		if (indexNumberDeatils.get("BasicDetails") != null) {

			addHeader("Basic Details");
			countNumber = 1;
			TreeMap<Integer, String> sortedPageNum = new TreeMap<Integer, String>();
			sortedPageNum.putAll(indexNumberDeatils.get("BasicDetails"));
			buildingCountNumber = 1;
			for (Entry<Integer, String> values : sortedPageNum.entrySet()) {
				addDocumentDetailsWithBuildingName(document, values, true);

			}

		}
		if (indexNumberDeatils.get("Airtermination") != null) {

			TreeMap<Integer, String> sortedPageNum = new TreeMap<Integer, String>();
			sortedPageNum.putAll(indexNumberDeatils.get("Airtermination"));

			addHeader("Air Termination");
			buildingCountNumber = 1;
			for (Entry<Integer, String> values : sortedPageNum.entrySet()) {
				if (values.getValue().contains("Building number")) {
					addDocumentDetailsWithBuildingName(document, values, false);
					countNumber = 1;
				} else {
					addDocumentDetails(document, values);
				}

			}

		}
		if (indexNumberDeatils.get("DownConductor") != null) {

			TreeMap<Integer, String> sortedPageNum = new TreeMap<Integer, String>();
			sortedPageNum.putAll(indexNumberDeatils.get("DownConductor"));

			addHeader("DownConductors");
			buildingCountNumber = 1;
			for (Entry<Integer, String> values : sortedPageNum.entrySet()) {
				if (values.getValue().contains("Building number")) {
					addDocumentDetailsWithBuildingName(document, values, false);
					// buildingCountNumber++;
					countNumber = 1;
				} else {
					addDocumentDetails(document, values);
				}

			}

		}
		if (indexNumberDeatils.get("Earthing") != null) {
			TreeMap<Integer, String> sortedPageNum = new TreeMap<Integer, String>();
			sortedPageNum.putAll(indexNumberDeatils.get("Earthing"));

			addHeader("Earthing");
			buildingCountNumber = 1;
			for (Entry<Integer, String> values : sortedPageNum.entrySet()) {
				if (values.getValue().contains("Building number")) {
					addDocumentDetailsWithBuildingName(document, values, false);
					countNumber = 1;
				} else {
					addDocumentDetails(document, values);
				}

			}

		}
		if (indexNumberDeatils.get("SPD") != null) {
			TreeMap<Integer, String> sortedPageNum = new TreeMap<Integer, String>();
			sortedPageNum.putAll(indexNumberDeatils.get("SPD"));

			addHeader("SPD");
			buildingCountNumber = 1;
			for (Entry<Integer, String> values : sortedPageNum.entrySet()) {
				if (values.getValue().contains("Building number")) {
					addDocumentDetailsWithBuildingName(document, values, false);
					countNumber = 1;
				} else {
					addDocumentDetails(document, values);
				}

			}

		}
		if (indexNumberDeatils.get("SeperationDistance") != null) {
			TreeMap<Integer, String> sortedPageNum = new TreeMap<Integer, String>();
			sortedPageNum.putAll(indexNumberDeatils.get("SeperationDistance"));

			addHeader("Seperation Distance");
			buildingCountNumber = 1;
			for (Entry<Integer, String> values : sortedPageNum.entrySet()) {
				if (values.getValue().contains("Building number")) {
					addDocumentDetailsWithBuildingName(document, values, false);
					countNumber = 1;
				} else {
					addDocumentDetails(document, values);
				}
			}

		}
		if (indexNumberDeatils.get("EarthStud") != null) {
			TreeMap<Integer, String> sortedPageNum = new TreeMap<Integer, String>();
			sortedPageNum.putAll(indexNumberDeatils.get("EarthStud"));

			addHeader("Equipotential Bonding System");
			buildingCountNumber = 1;
			for (Entry<Integer, String> values : sortedPageNum.entrySet()) {
				if (values.getValue().contains("Building number")) {
					addDocumentDetailsWithBuildingName(document, values, false);
					countNumber = 1;
				} else {
					addDocumentDetails(document, values);
				}

			}

		}

		if (indexNumberDeatils.get("Summary") != null) {
			TreeMap<Integer, String> sortedPageNum = new TreeMap<Integer, String>();
			sortedPageNum.putAll(indexNumberDeatils.get("Summary"));

			addHeader("Summary");
			buildingCountNumber = 1;
			for (Entry<Integer, String> values : sortedPageNum.entrySet()) {
				if (values.getValue().contains("Building number")) {
					addDocumentDetailsWithBuildingName(document, values, false);
					// buildingCountNumber++;
					countNumber = 1;
				} else {
					addDocumentDetails(document, values);
				}
			}

		}

		document.close();
	}

	private void addHeader(String headerName) throws DocumentException, IOException {

		float[] pointColumnWidths = { 120F, 80F };

		PdfPTable headertable = new PdfPTable(pointColumnWidths);
		headertable.setWidthPercentage(100);// Width 100%
		headertable.setSpacingBefore(5f); // Space before table

		PdfPCell label = new PdfPCell(new Paragraph(headerName,
				new Font(BaseFont.createFont(), 11, Font.NORMAL | Font.BOLD, BaseColor.BLACK)));
		label.setHorizontalAlignment(Element.ALIGN_LEFT);
		label.setGrayFill(0.92f);
		label.setFixedHeight(20f);
		headertable.addCell(label);

		PdfPCell label1 = new PdfPCell(new Paragraph("  Page No",
				new Font(BaseFont.createFont(), 11, Font.NORMAL | Font.BOLD, BaseColor.BLACK)));
		label1.setHorizontalAlignment(Element.ALIGN_CENTER);
		label1.setGrayFill(0.92f);
		label1.setFixedHeight(20f);
		headertable.addCell(label1);

		document.add(headertable);
	}

	private void addDocumentDetails(Document document, Entry<Integer, String> values)
			throws Exception {
		
		try {
			float[] pointColumnWidths = { 120F, 50F };
			PdfPTable table = new PdfPTable(pointColumnWidths);

			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(5f); // Space before table
			table.setSpacingAfter(5f); // Space after table
			table.setWidthPercentage(100);
			table.getDefaultCell().setBorder(0);
			PdfPCell cell = new PdfPCell(
					new Paragraph("                    " + countNumber++ + ".   " + values.getValue().split(",")[1],
							new Font(BaseFont.createFont(), 11, Font.NORMAL)));
			cell.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell);
			PdfPCell cell2 = new PdfPCell(
					new Paragraph(values.getValue().split(",")[0], new Font(BaseFont.createFont(), 10, Font.NORMAL)));
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell2.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell2);

			document.add(table);
		} catch (Exception e) {
			logger.error("Exception occurred while generating the index" + e.getMessage());
			throw new Exception("Exception occurred while generating the index");
		}

	}

	private void addDocumentDetailsWithBuildingName(Document document, Entry<Integer, String> values,
			Boolean isBasicComponent) throws Exception {

		try {
			float[] pointColumnWidths = { 120F, 50F };
			PdfPTable table = new PdfPTable(pointColumnWidths);

			table.setWidthPercentage(100); // Width 100%
			table.setSpacingBefore(5f); // Space before table
			table.setSpacingAfter(5f); // Space after table
			table.setWidthPercentage(100);
			table.getDefaultCell().setBorder(0);

			PdfPCell cell = null;
			if (isBasicComponent) {
				cell = new PdfPCell(
						new Paragraph("      " + buildingCountNumber++ + ".   " + values.getValue().split(",")[1],
								new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK)));
			} else {
				cell = new PdfPCell(
						new Paragraph("      " + buildingCountNumber++ + ".   " + values.getValue().split(",")[1],
								new Font(BaseFont.createFont(), 10, Font.BOLD, BaseColor.BLACK)));
			}
			cell.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell);
			PdfPCell cell2 = new PdfPCell(new Paragraph(values.getValue().split(",")[0],
					new Font(BaseFont.createFont(), 10, Font.BOLD, BaseColor.BLACK)));
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			// cell2.setBackgroundColor(new BaseColor(203, 183, 162));
			cell2.setBorder(PdfPCell.NO_BORDER);
			table.addCell(cell2);

			document.add(table);
		} catch (Exception e) {
			logger.error("Exception occurred while generating the index" + e.getMessage());
			throw new Exception("Exception occurred while generating the index");
		}

	}

	private void addHomePage() throws DocumentException, IOException {
		Font font12B = new Font(BaseFont.createFont(), 12, Font.NORMAL | Font.BOLD, BaseColor.BLACK);
		Font font10N = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);

		float[] pointColumnWidths0 = { 100F };

		PdfPTable titlebox = new PdfPTable(pointColumnWidths0);
		titlebox.setWidthPercentage(100); // Width 100%
		titlebox.setSpacingBefore(10f); // Space before table
		titlebox.getDefaultCell().setBorder(15);

		PdfPCell cell20 = new PdfPCell(
				new Paragraph("\r\n" + "LIGHTNING PROTECTION SURVEY REPORT AS PER IS/IEC 62305", font12B));
		cell20.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell20.setFixedHeight(40);
		titlebox.addCell(cell20);
		document.add(titlebox);

		// basicDetailsContents.put(ContentEvent.numberOfContents++, pageNum.getPage()+
		// ",Lightning protection survey report");

		PdfPTable projectHeader = new PdfPTable(pointColumnWidths0);
		projectHeader.setSpacingBefore(15f); // Space before table
//		projectHeader.setSpacingAfter(7f); // Space after table
		projectHeader.setWidthPercentage(100);
		projectHeader.getDefaultCell().setBorder(0);

		PdfPCell title = new PdfPCell(new Paragraph("Project", new Font(BaseFont.createFont(), 11, Font.NORMAL)));
		title.setBackgroundColor(new GrayColor(0.82f));
		title.setHorizontalAlignment(Element.ALIGN_LEFT);
		title.setBorder(PdfPCell.NO_BORDER);
		projectHeader.addCell(title);

		document.add(projectHeader);

		PdfPTable titlebox1 = new PdfPTable(pointColumnWidths0);
		titlebox1.setWidthPercentage(100); // Width 100%
		titlebox1.setSpacingBefore(10f); // Space before table
		titlebox1.getDefaultCell().setBorder(15);

		PdfPCell cell25 = new PdfPCell(new Paragraph("", font10N));
		cell25.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell25.setFixedHeight(75);
		titlebox1.addCell(cell25);

		document.add(titlebox1);

		float[] pointColumnWidths11 = { 30F, 30F };
		PdfPTable table11 = new PdfPTable(pointColumnWidths11);

		table11.setWidthPercentage(60); // Width 100%
		table11.setSpacingBefore(25f); // Space before table
		table11.setSpacingAfter(15f); // Space after table
		table11.getDefaultCell().setBorder(15);

		PdfPCell certificate12 = new PdfPCell(new Paragraph("", new Font(BaseFont.createFont(), 10, Font.NORMAL)));
		table11.addCell(new Phrase("Starting date of inspection", new Font(BaseFont.createFont(), 10, Font.NORMAL)));
		certificate12.setHorizontalAlignment(Element.ALIGN_LEFT);
		certificate12.setBorder(15);
		certificate12.setFixedHeight(25f);
		table11.addCell(certificate12);

		PdfPCell certificate13 = new PdfPCell(new Paragraph("", new Font(BaseFont.createFont(), 10, Font.NORMAL)));
		table11.addCell(new Phrase("Ending date of inspection", new Font(BaseFont.createFont(), 10, Font.NORMAL)));
		certificate13.setHorizontalAlignment(Element.ALIGN_LEFT);
		certificate13.setFixedHeight(25f);
		certificate13.setBorder(15);
		table11.addCell(certificate13);
		document.add(table11);

		float[] pointColumnWidths2 = { 90F, 90F, 90F };

		PdfPTable table2 = new PdfPTable(pointColumnWidths2);
		table2.setWidthPercentage(100); // Width 100%
		table2.setSpacingBefore(10f); // Space before table
		table2.getDefaultCell().setBorder(15);

		PdfPCell cell1 = new PdfPCell(new Paragraph("Inspected by", font10N));
		cell1.setGrayFill(0.92f);
		cell1.setFixedHeight(25f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell1);

		PdfPCell cell3 = new PdfPCell(new Paragraph("Inspected by", font10N));
		cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell3.setGrayFill(0.92f);
		cell3.setFixedHeight(25f);
		table2.addCell(cell3);

		PdfPCell cell4 = new PdfPCell(new Paragraph("Witnessed by", font10N));
		cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell4.setGrayFill(0.92f);
		cell4.setFixedHeight(25f);
		table2.addCell(cell4);

		PdfPCell cell5 = new PdfPCell(new Paragraph("", font10N));
		cell5.setFixedHeight(25f);
		cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell5);

		PdfPCell cell6 = new PdfPCell(new Paragraph("", font10N));
		cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell6.setFixedHeight(25f);
		table2.addCell(cell6);

		PdfPCell cell19 = new PdfPCell(new Paragraph("", font10N));
		cell19.setFixedHeight(25f);
		cell19.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell19);

		PdfPCell cell55 = new PdfPCell(new Paragraph("", font10N));
		cell55.setFixedHeight(25f);
		cell55.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell55);

		PdfPCell cell66 = new PdfPCell(new Paragraph("", font10N));
		cell66.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell66.setFixedHeight(25f);
		table2.addCell(cell66);

		PdfPCell cell77 = new PdfPCell(new Paragraph("", font10N));
		cell77.setFixedHeight(25f);
		cell77.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell77);
		document.add(table2);

		float[] pointColumnWidths3 = { 100F };

		PdfPTable table3 = new PdfPTable(pointColumnWidths3);
		table3.setWidthPercentage(33); // Width 100%
		table3.setSpacingBefore(20f); // Space before table
		table3.getDefaultCell().setBorder(15);

		PdfPCell cell8 = new PdfPCell(new Paragraph("Reviewed by", font10N));
		cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell8.setGrayFill(0.92f);
		cell8.setFixedHeight(25f);
		table3.addCell(cell8);

		PdfPCell cell9 = new PdfPCell(new Paragraph("", font10N));
		cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell9.setFixedHeight(25f);
		table3.addCell(cell9);

		PdfPCell cell7 = new PdfPCell(new Paragraph("", font10N));
		cell7.setFixedHeight(25f);
		cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		table3.addCell(cell7);

		document.add(table3);

//		float[] pointColumnWidths3 = {100F };
		PdfPTable comment = new PdfPTable(pointColumnWidths3);
		comment.setSpacingBefore(15f); // Space before table
//		comment.setSpacingAfter(7f); // Space after table
		comment.setWidthPercentage(100);
		comment.getDefaultCell().setBorder(0);

		PdfPCell comment1 = new PdfPCell(new Paragraph("Comments", new Font(BaseFont.createFont(), 11, Font.NORMAL)));
		comment1.setBackgroundColor(new GrayColor(0.82f));
		comment1.setHorizontalAlignment(Element.ALIGN_LEFT);
		comment1.setBorder(PdfPCell.NO_BORDER);
		comment.addCell(comment1);

		document.add(comment);

		PdfPTable table41 = new PdfPTable(pointColumnWidths3);
		table41.setWidthPercentage(100); // Width 100%
		table41.setSpacingBefore(10f); // Space before table
		table41.getDefaultCell().setBorder(15);

		PdfPCell cell21 = new PdfPCell(new Paragraph("", font10N));
		cell21.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell21.setFixedHeight(200);
		table41.addCell(cell21);

		document.add(table41);
	}
}
