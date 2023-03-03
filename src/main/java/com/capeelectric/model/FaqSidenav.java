package com.capeelectric.model;

import java.util.List;
import java.util.Map;

public class FaqSidenav {

	private List<FqaModelSideNav> sideNav;

	private Map<String, List<FrequentlyAskedQuestions>> contents;

	public List<FqaModelSideNav> getSideNav() {
		return sideNav;
	}

	public void setSideNav(List<FqaModelSideNav> sideNav) {
		this.sideNav = sideNav;
	}

	public Map<String, List<FrequentlyAskedQuestions>> getContents() {
		return contents;
	}

	public void setContents(Map<String, List<FrequentlyAskedQuestions>> contents) {
		this.contents = contents;
	}

}
