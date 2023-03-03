package com.capeelectric.util;

import java.util.LinkedHashMap;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class ContentEvent extends PdfPageEventHelper {
	private int page;
	
	public static int numberOfContents = 1;
	 
	Map<String, Integer> index = new LinkedHashMap<String, Integer>();

	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		page++;
	}

	@Override
	public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
		index.put(title.getContent(), page); 
	}
}