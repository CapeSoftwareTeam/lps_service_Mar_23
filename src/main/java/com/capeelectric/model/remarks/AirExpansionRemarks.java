/**
 * 
 */
package com.capeelectric.model.remarks;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * @author CAPE-SOFTWARE
 *
 */
@Entity
@Table(name = "AIR_EXPANSION_TABLE")
public class AirExpansionRemarks implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "EXPANSION_ID")
	private Integer expansionId;
	
	@Column(name = "FLAG")
	private String flag;

	@Column(name = "PHYSICAL_INSPECTION_REMARKS")
	private String physicalInspectionRe;

	@Column(name = "STRIGHTCONNECTOR_PIECS_REMARKS")
	private String strightConnectorPiecRe;
	
	@Column(name = "MATERIAL_OF_CONNECTOR_REMARKS")
	private String materialOfConnectorRe;

	@Column(name = "MATERIAL_OF_EXPANSIONPIECE_REMARKS")
	private String materialOfExpansionRe;
	
	@Column(name = "INTERVAL_BETWEEN_EXPANSION_REMARKS")
	private String intervalBwExpansionRe;

	@Column(name = "TOTALNO_EXPANSIONPIECE_REMARKS")
	private String totalNoExpansionRe;

	@Column(name = "INSP_NO_REM")
	private String inspectionNoRe;

	@Column(name = "INSP_PASSED_NO_REM ")
	private String inspectionPassedNoRe;

	@Column(name = "INSP_FAILED_NO_REM")
	private String inspectionFailedNoRe;

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "LPSAIRDESCRIPTION_ID")
	private LpsAirDiscriptionRemarks lpsAirDescription;

	public Integer getExpansionId() {
		return expansionId;
	}

	public void setExpansionId(Integer expansionId) {
		this.expansionId = expansionId;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getPhysicalInspectionRe() {
		return physicalInspectionRe;
	}

	public void setPhysicalInspectionRe(String physicalInspectionRe) {
		this.physicalInspectionRe = physicalInspectionRe;
	}

	public String getStrightConnectorPiecRe() {
		return strightConnectorPiecRe;
	}

	public void setStrightConnectorPiecRe(String strightConnectorPiecRe) {
		this.strightConnectorPiecRe = strightConnectorPiecRe;
	}

	public String getMaterialOfConnectorRe() {
		return materialOfConnectorRe;
	}

	public void setMaterialOfConnectorRe(String materialOfConnectorRe) {
		this.materialOfConnectorRe = materialOfConnectorRe;
	}

	public String getMaterialOfExpansionRe() {
		return materialOfExpansionRe;
	}

	public void setMaterialOfExpansionRe(String materialOfExpansionRe) {
		this.materialOfExpansionRe = materialOfExpansionRe;
	}

	public String getIntervalBwExpansionRe() {
		return intervalBwExpansionRe;
	}

	public void setIntervalBwExpansionRe(String intervalBwExpansionRe) {
		this.intervalBwExpansionRe = intervalBwExpansionRe;
	}

	public String getTotalNoExpansionRe() {
		return totalNoExpansionRe;
	}

	public void setTotalNoExpansionRe(String totalNoExpansionRe) {
		this.totalNoExpansionRe = totalNoExpansionRe;
	}

	public String getInspectionNoRe() {
		return inspectionNoRe;
	}

	public void setInspectionNoRe(String inspectionNoRe) {
		this.inspectionNoRe = inspectionNoRe;
	}

	public String getInspectionPassedNoRe() {
		return inspectionPassedNoRe;
	}

	public void setInspectionPassedNoRe(String inspectionPassedNoRe) {
		this.inspectionPassedNoRe = inspectionPassedNoRe;
	}

	public String getInspectionFailedNoRe() {
		return inspectionFailedNoRe;
	}

	public void setInspectionFailedNoRe(String inspectionFailedNoRe) {
		this.inspectionFailedNoRe = inspectionFailedNoRe;
	}

	public LpsAirDiscriptionRemarks getLpsAirDescription() {
		return lpsAirDescription;
	}

	public void setLpsAirDescription(LpsAirDiscriptionRemarks lpsAirDescription) {
		this.lpsAirDescription = lpsAirDescription;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
