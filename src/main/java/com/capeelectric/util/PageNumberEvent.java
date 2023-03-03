package com.capeelectric.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class PageNumberEvent extends PdfPageEventHelper {
	
	private Integer pageNo;
	
	private static PageNumberEvent pageNumEvent = null;
	
	private PageNumberEvent() {
		
	}
	
	public static PageNumberEvent getPageNumObject() {
		if (null == pageNumEvent) {
			pageNumEvent = new PageNumberEvent();
			pageNumEvent.setPage(0);
		}
		return pageNumEvent;
	}

	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		pageNo++;
	}

	public Integer getPage() {
		return pageNo;
	}

	public void setPage(Integer pageNo) {
		this.pageNo = pageNo;
	}

}