package com.capeelectric.util;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * @author CAPE-SOFTWARE
 *
 */

public class PdfPTables {
		 
	public static PdfPTable noRemarksPdfPTable() {
		
		float[] pointColumnWidths4 = { 50F, 50F };
		PdfPTable pdfPTable = new PdfPTable(pointColumnWidths4);
		pdfPTable.setWidthPercentage(100); // Width 100%
		pdfPTable.setSpacingBefore(10f); // Space before table
		
		return pdfPTable;
	}
	
	// Main Heading  Label
	public static PdfPTable remarksPDFTable() {
		float[] pointColumnWidths40 = { 100F };
		PdfPTable pdfPTable = new PdfPTable(pointColumnWidths40);
		pdfPTable.setWidthPercentage(100); // Width 100%
		pdfPTable.setSpacingBefore(10f); // Space before table
		return pdfPTable;
	}
	
	//
	public static PdfPCell pdfCellHeading(String heading) throws DocumentException, IOException {
		Font font10B = new Font(BaseFont.createFont(), 10, Font.NORMAL | Font.BOLD, BaseColor.BLACK);

		PdfPCell headingCell = new PdfPCell(
				new Paragraph(heading, font10B));
		headingCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		headingCell.setGrayFill(0.92f);
		headingCell.setFixedHeight(20f);
		return headingCell;
	}
	
	//No Remarks Available
	public static PdfPTable pdfCellHeadingWithNoRemarks(String heading) throws DocumentException, IOException {
		 Font font10N = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);

		PdfPTable noRemarksPdfPTable = noRemarksPdfPTable();
		noRemarksPdfPTable.addCell(pdfCellHeading(heading));

		PdfPCell cell37 = new PdfPCell(new Paragraph("No Remarks Available", font10N));
		cell37.setHorizontalAlignment(Element.ALIGN_LEFT);
		noRemarksPdfPTable.addCell(cell37);

		return noRemarksPdfPTable;
	}
	
	// observationCell
	public static PdfPCell pdfCellObservation(String observation) throws DocumentException, IOException {
		 Font font10N = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);

		PdfPCell observationCell = new PdfPCell();
		observationCell.setPhrase(new Phrase(observation, font10N));
		observationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		observationCell.setBackgroundColor(new GrayColor(0.93f));
		return observationCell;
	
	}
	
	// recommendation cell
	public static PdfPCell pdfCellRecommendation(String recommendation) throws DocumentException, IOException {
		 Font font10N = new Font(BaseFont.createFont(), 10, Font.NORMAL, BaseColor.BLACK);

		PdfPCell recommendationCell = new PdfPCell(
				new Paragraph(recommendation, font10N));
		recommendationCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		
		return recommendationCell;
		
	}
}
