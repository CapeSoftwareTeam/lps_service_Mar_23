package com.capeelectric.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "protection_table")
public class Protection implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PROTECTION_ID")
	private Integer protectionId;

// Loss of Human Life
	@Column(name = "PRO_PEB")
	private String protectionPEB;

	@Column(name = "PRO_PMS")
	private String protectionPMS;

	@Column(name = "PRO_PM")
	private String protectionPM;

	@Column(name = "PRO_PA")
	private String protectionPA;

	@Column(name = "PRO_PC")
	private String protectionPC;

	@Column(name = "PRO_PU")
	private String protectionPU;

	@Column(name = "PRO_PV")
	private String protectionPV;

	@Column(name = "PRO_PW")
	private String protectionPW;

	@Column(name = "PRO_PZ")
	private String protectionPZ;

// RISK OF LOSS OF HUMAN BEINGS (R1)	
	@Column(name = "R_PRO_RA1")
	private String riskProtectionRA1;

	@Column(name = "R_PRO_RB1")
	private String riskProtectionRB1;

	@Column(name = "R_PRO_RC1")
	private String riskProtectionRC1;

	@Column(name = "R_PRO_RM1")
	private String riskProtectionRM1;

	@Column(name = "R_PRO_RU1")
	private String riskProtectionRU1;

	@Column(name = "R_PRO_RV1")
	private String riskProtectionRV1;

	@Column(name = "R_PRO_RW1")
	private String riskProtectionRW1;

	@Column(name = "R_PRO_RZ1")
	private String riskProtectionRZ1;

	@Column(name = "R_PRO_RB2")
	private String riskProtectionRB2;

	@Column(name = "CULTURAL_RB")
	private String culturalRB;   //RB3

	@Column(name = "CULTURAL_RV")
	private String culturalRV;    //RV3

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "STRUCTURE_CHARACTERISTIC_ID")
	private StructureCharacteristics structureCharacteristics;

	public Integer getProtectionId() {
		return protectionId;
	}

	public void setProtectionId(Integer protectionId) {
		this.protectionId = protectionId;
	}

	public String getProtectionPEB() {
		return protectionPEB;
	}

	public void setProtectionPEB(String protectionPEB) {
		this.protectionPEB = protectionPEB;
	}

	public String getProtectionPMS() {
		return protectionPMS;
	}

	public void setProtectionPMS(String protectionPMS) {
		this.protectionPMS = protectionPMS;
	}

	public String getProtectionPM() {
		return protectionPM;
	}

	public void setProtectionPM(String protectionPM) {
		this.protectionPM = protectionPM;
	}

	public String getProtectionPA() {
		return protectionPA;
	}

	public void setProtectionPA(String protectionPA) {
		this.protectionPA = protectionPA;
	}

	public String getProtectionPC() {
		return protectionPC;
	}

	public void setProtectionPC(String protectionPC) {
		this.protectionPC = protectionPC;
	}

	public String getProtectionPU() {
		return protectionPU;
	}

	public void setProtectionPU(String protectionPU) {
		this.protectionPU = protectionPU;
	}

	public String getProtectionPV() {
		return protectionPV;
	}

	public void setProtectionPV(String protectionPV) {
		this.protectionPV = protectionPV;
	}

	public String getProtectionPW() {
		return protectionPW;
	}

	public void setProtectionPW(String protectionPW) {
		this.protectionPW = protectionPW;
	}

	public String getProtectionPZ() {
		return protectionPZ;
	}

	public void setProtectionPZ(String protectionPZ) {
		this.protectionPZ = protectionPZ;
	}

	public String getRiskProtectionRA1() {
		return riskProtectionRA1;
	}

	public void setRiskProtectionRA1(String riskProtectionRA1) {
		this.riskProtectionRA1 = riskProtectionRA1;
	}

	public String getRiskProtectionRB1() {
		return riskProtectionRB1;
	}

	public void setRiskProtectionRB1(String riskProtectionRB1) {
		this.riskProtectionRB1 = riskProtectionRB1;
	}

	public String getRiskProtectionRC1() {
		return riskProtectionRC1;
	}

	public void setRiskProtectionRC1(String riskProtectionRC1) {
		this.riskProtectionRC1 = riskProtectionRC1;
	}

	public String getRiskProtectionRM1() {
		return riskProtectionRM1;
	}

	public void setRiskProtectionRM1(String riskProtectionRM1) {
		this.riskProtectionRM1 = riskProtectionRM1;
	}

	public String getRiskProtectionRU1() {
		return riskProtectionRU1;
	}

	public void setRiskProtectionRU1(String riskProtectionRU1) {
		this.riskProtectionRU1 = riskProtectionRU1;
	}

	public String getRiskProtectionRV1() {
		return riskProtectionRV1;
	}

	public void setRiskProtectionRV1(String riskProtectionRV1) {
		this.riskProtectionRV1 = riskProtectionRV1;
	}

	public String getRiskProtectionRW1() {
		return riskProtectionRW1;
	}

	public void setRiskProtectionRW1(String riskProtectionRW1) {
		this.riskProtectionRW1 = riskProtectionRW1;
	}

	public String getRiskProtectionRZ1() {
		return riskProtectionRZ1;
	}

	public void setRiskProtectionRZ1(String riskProtectionRZ1) {
		this.riskProtectionRZ1 = riskProtectionRZ1;
	}

	public String getRiskProtectionRB2() {
		return riskProtectionRB2;
	}

	public void setRiskProtectionRB2(String riskProtectionRB2) {
		this.riskProtectionRB2 = riskProtectionRB2;
	}

	public String getCulturalRB() {
		return culturalRB;
	}

	public void setCulturalRB(String culturalRB) {
		this.culturalRB = culturalRB;
	}

	public String getCulturalRV() {
		return culturalRV;
	}

	public void setCulturalRV(String culturalRV) {
		this.culturalRV = culturalRV;
	}

	public StructureCharacteristics getStructureCharacteristics() {
		return structureCharacteristics;
	}

	public void setStructureCharacteristics(StructureCharacteristics structureCharacteristics) {
		this.structureCharacteristics = structureCharacteristics;
	}

}
