/**
 * 
 */
package com.capeelectric.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * @author CAPE-SOFTWARE
 *
 */
@Entity
@Table(name = "SUMMARY_LPS_OBSERVATION_TABLE")
public class SummaryLpsObservation implements Serializable  {
	
	private static final long serialVersionUID = -7161836502468880542L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SUMMARY_LPS_OBSERVATION_ID")
	private Integer summaryLpsObservationId;
	
	@Column(name = "OBSERVATION")
	private String observation;
	
	@Column(name = "RECOMMENDATION")
	private String recommendation;
	
	@Column(name = "OBSERVATION_COMPONENT_DETAILS")
	private String observationComponentDetails;
	
	@Column(name = "HEADING_UI")
	private String headingUi;
	
	@Column(name = "REMARKS_ID")
	private Integer remarksId;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "SUMMARY_LPS_BUILDINGS_ID")
	private SummaryLpsBuildings summaryLpsBuildings;

	public Integer getSummaryLpsObservationId() {
		return summaryLpsObservationId;
	}

	public void setSummaryLpsObservationId(Integer summaryLpsObservationId) {
		this.summaryLpsObservationId = summaryLpsObservationId;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public String getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(String recommendation) {
		this.recommendation = recommendation;
	}

	public String getObservationComponentDetails() {
		return observationComponentDetails;
	}

	public void setObservationComponentDetails(String observationComponentDetails) {
		this.observationComponentDetails = observationComponentDetails;
	}

	public SummaryLpsBuildings getSummaryLpsBuildings() {
		return summaryLpsBuildings;
	}

	public void setSummaryLpsBuildings(SummaryLpsBuildings summaryLpsBuildings) {
		this.summaryLpsBuildings = summaryLpsBuildings;
	}

	public String getHeadingUi() {
		return headingUi;
	}

	public void setHeadingUi(String headingUi) {
		this.headingUi = headingUi;
	}

	public Integer getRemarksId() {
		return remarksId;
	}

	public void setRemarksId(Integer remarksId) {
		this.remarksId = remarksId;
	}
	
}
