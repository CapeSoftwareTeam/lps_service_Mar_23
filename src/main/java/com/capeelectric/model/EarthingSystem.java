package com.capeelectric.model;

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

@Entity
@Table(name = "EARTHING_SYSTEM_TABLE")
public class EarthingSystem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "EARTHING_SYSTEM_ID")
	private Integer earthingSystemId;

	@Column(name = "FLAG")
	private String flag;

	@Column(name = "EAST_OB")
	private Float eastOb;

	@Column(name = "EAST_REM")
	private String eastRem;

	@Column(name = "WEST_OB")
	private Float westOb;

	@Column(name = "WEST_REM")
	private String westRem;

	@Column(name = "NORTH_OB")
	private Float northOb;

	@Column(name = "NORTH_REM")
	private String northRem;

	@Column(name = "SOUTH_OB")
	private Float southOb;

	@Column(name = "SOUTH_REM")
	private String southRem;

	@Column(name = "RINGEARTHWALL_EAST_OB")
	private Float ringWallEarthEastOb;

	@Column(name = "RINGEARTHWALL_EAST_REM")
	private String ringWallEarthEastRem;

	@Column(name = "RINGEARTHWALL_WEST_OB")
	private Float ringWallEarthWestOb;

	@Column(name = "RINGEARTHWALL_WEST_REM")
	private String ringWallEarthWestRem;

	@Column(name = "RINGEARTHWALL_NORTH_OB")
	private Float ringWallEarthNorthOb;

	@Column(name = "RINGEARTHWALL_NORTH_REM")
	private String ringWallEarthNorthRem;

	@Column(name = "RINGEARTHWALL_SOUTH_OB")
	private Float ringWallEarthSouthOb;

	@Column(name = "RINGEARTHWALL_SOUTH_REM")
	private String ringWallEarthSouthRem;
	
	@Column(name = "CONNECTED_EARTH_ELECTRODE_OB")
	private String connectedEarthElectrodeOb;

	@Column(name = "CONNECTED_EARTH_ELECTRODE_REM")
	private String connectedEarthElectrodeRem;

	@Column(name = "JOINTSMADE_BRAZING_OB")
	private String jointsMadeBrazingOb;

	@Column(name = "JOINTSMADE_BRAZING_REM")
	private String jointsMadeBrazingRem;

	@Column(name = "MATERIAL_OF_EARTHELECTRODE_OB")
	private String materialOfEartElectrodeOb;

	@Column(name = "MATERIAL_OF_EARTHELECTRODE_REM")
	private String materialOfEartElectrodeRem;
	
	@Column(name = "TYPE_OF_EARTHELECTRODE_OB")
	private String typeOfEarthElectrodeOb;

	@Column(name = "TYPE_OF_EARTHELECTRODE_REM")
	private String typeOfEarthElectrodeRem;

	@Column(name = "SIZE_OFEARTHELECTRODE_OB")
	private String sizeOfEarthElectrodeOb;

	@Column(name = "SIZE_OFEARTHELECTRODE_REM")
	private String sizeOfEarthElectrodeRem;

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "EARTHING_ID")
	private EarthingLpsDescription earthingLpsDescription;

	public Integer getEarthingSystemId() {
		return earthingSystemId;
	}


	public String getRingWallEarthEastRem() {
		return ringWallEarthEastRem;
	}

	public void setRingWallEarthEastRem(String ringWallEarthEastRem) {
		this.ringWallEarthEastRem = ringWallEarthEastRem;
	}


	public String getRingWallEarthWestRem() {
		return ringWallEarthWestRem;
	}

	public void setRingWallEarthWestRem(String ringWallEarthWestRem) {
		this.ringWallEarthWestRem = ringWallEarthWestRem;
	}

	public String getRingWallEarthNorthRem() {
		return ringWallEarthNorthRem;
	}

	public void setRingWallEarthNorthRem(String ringWallEarthNorthRem) {
		this.ringWallEarthNorthRem = ringWallEarthNorthRem;
	}


	public String getRingWallEarthSouthRem() {
		return ringWallEarthSouthRem;
	}

	public void setRingWallEarthSouthRem(String ringWallEarthSouthRem) {
		this.ringWallEarthSouthRem = ringWallEarthSouthRem;
	}

	public void setEarthingSystemId(Integer earthingSystemId) {
		this.earthingSystemId = earthingSystemId;
	}

	public String getWestRem() {
		return westRem;
	}

	public void setWestRem(String westRem) {
		this.westRem = westRem;
	}

	public String getNorthRem() {
		return northRem;
	}

	public void setNorthRem(String northRem) {
		this.northRem = northRem;
	}

	public String getSouthRem() {
		return southRem;
	}

	public void setSouthRem(String southRem) {
		this.southRem = southRem;
	}


	public String getJointsMadeBrazingOb() {
		return jointsMadeBrazingOb;
	}

	public void setJointsMadeBrazingOb(String jointsMadeBrazingOb) {
		this.jointsMadeBrazingOb = jointsMadeBrazingOb;
	}

	public String getJointsMadeBrazingRem() {
		return jointsMadeBrazingRem;
	}

	public void setJointsMadeBrazingRem(String jointsMadeBrazingRem) {
		this.jointsMadeBrazingRem = jointsMadeBrazingRem;
	}

	public String getMaterialOfEartElectrodeOb() {
		return materialOfEartElectrodeOb;
	}

	public void setMaterialOfEartElectrodeOb(String materialOfEartElectrodeOb) {
		this.materialOfEartElectrodeOb = materialOfEartElectrodeOb;
	}

	public String getMaterialOfEartElectrodeRem() {
		return materialOfEartElectrodeRem;
	}

	public void setMaterialOfEartElectrodeRem(String materialOfEartElectrodeRem) {
		this.materialOfEartElectrodeRem = materialOfEartElectrodeRem;
	}

	public String getSizeOfEarthElectrodeOb() {
		return sizeOfEarthElectrodeOb;
	}

	public void setSizeOfEarthElectrodeOb(String sizeOfEarthElectrodeOb) {
		this.sizeOfEarthElectrodeOb = sizeOfEarthElectrodeOb;
	}

	public String getSizeOfEarthElectrodeRem() {
		return sizeOfEarthElectrodeRem;
	}

	public void setSizeOfEarthElectrodeRem(String sizeOfEarthElectrodeRem) {
		this.sizeOfEarthElectrodeRem = sizeOfEarthElectrodeRem;
	}
//	
//	public String getMaximumDistanceEartElectrodeWalRem() {
//		return maximumDistanceEartElectrodeWalRem;
//	}
//
//	public void setMaximumDistanceEartElectrodeWalRem(String maximumDistanceEartElectrodeWalRem) {
//		this.maximumDistanceEartElectrodeWalRem = maximumDistanceEartElectrodeWalRem;
//	}
//
//	public String getManimumDistanceEartElectrodeWalRem() {
//		return manimumDistanceEartElectrodeWalRem;
//	}
//
//	public void setManimumDistanceEartElectrodeWalRem(String manimumDistanceEartElectrodeWalRem) {
//		this.manimumDistanceEartElectrodeWalRem = manimumDistanceEartElectrodeWalRem;
//	}

	public EarthingLpsDescription getEarthingLpsDescription() {
		return earthingLpsDescription;
	}

	public void setEarthingLpsDescription(EarthingLpsDescription earthingLpsDescription) {
		this.earthingLpsDescription = earthingLpsDescription;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getEastRem() {
		return eastRem;
	}

	public void setEastRem(String eastRem) {
		this.eastRem = eastRem;
	}

	public String getConnectedEarthElectrodeOb() {
		return connectedEarthElectrodeOb;
	}

	public void setConnectedEarthElectrodeOb(String connectedEarthElectrodeOb) {
		this.connectedEarthElectrodeOb = connectedEarthElectrodeOb;
	}

	public String getConnectedEarthElectrodeRem() {
		return connectedEarthElectrodeRem;
	}

	public void setConnectedEarthElectrodeRem(String connectedEarthElectrodeRem) {
		this.connectedEarthElectrodeRem = connectedEarthElectrodeRem;
	}

	public String getTypeOfEarthElectrodeOb() {
		return typeOfEarthElectrodeOb;
	}

	public void setTypeOfEarthElectrodeOb(String typeOfEarthElectrodeOb) {
		this.typeOfEarthElectrodeOb = typeOfEarthElectrodeOb;
	}

	public String getTypeOfEarthElectrodeRem() {
		return typeOfEarthElectrodeRem;
	}

	public void setTypeOfEarthElectrodeRem(String typeOfEarthElectrodeRem) {
		this.typeOfEarthElectrodeRem = typeOfEarthElectrodeRem;
	}

	public Float getEastOb() {
		return eastOb;
	}


	public void setEastOb(Float eastOb) {
		this.eastOb = eastOb;
	}


	public Float getWestOb() {
		return westOb;
	}


	public void setWestOb(Float westOb) {
		this.westOb = westOb;
	}


	public Float getNorthOb() {
		return northOb;
	}


	public void setNorthOb(Float northOb) {
		this.northOb = northOb;
	}


	public Float getSouthOb() {
		return southOb;
	}


	public void setSouthOb(Float southOb) {
		this.southOb = southOb;
	}


	public Float getRingWallEarthEastOb() {
		return ringWallEarthEastOb;
	}


	public void setRingWallEarthEastOb(Float ringWallEarthEastOb) {
		this.ringWallEarthEastOb = ringWallEarthEastOb;
	}


	public Float getRingWallEarthWestOb() {
		return ringWallEarthWestOb;
	}


	public void setRingWallEarthWestOb(Float ringWallEarthWestOb) {
		this.ringWallEarthWestOb = ringWallEarthWestOb;
	}


	public Float getRingWallEarthNorthOb() {
		return ringWallEarthNorthOb;
	}


	public void setRingWallEarthNorthOb(Float ringWallEarthNorthOb) {
		this.ringWallEarthNorthOb = ringWallEarthNorthOb;
	}


	public Float getRingWallEarthSouthOb() {
		return ringWallEarthSouthOb;
	}


	public void setRingWallEarthSouthOb(Float ringWallEarthSouthOb) {
		this.ringWallEarthSouthOb = ringWallEarthSouthOb;
	}


//	public Integer getMaximumDistanceEartElectrodeWalOb() {
//		return maximumDistanceEartElectrodeWalOb;
//	}
//
//
//	public void setMaximumDistanceEartElectrodeWalOb(Integer maximumDistanceEartElectrodeWalOb) {
//		this.maximumDistanceEartElectrodeWalOb = maximumDistanceEartElectrodeWalOb;
//	}
//
//
//	public Integer getManimumDistanceEartElectrodeWalOb() {
//		return manimumDistanceEartElectrodeWalOb;
//	}
//
//
//	public void setManimumDistanceEartElectrodeWalOb(Integer manimumDistanceEartElectrodeWalOb) {
//		this.manimumDistanceEartElectrodeWalOb = manimumDistanceEartElectrodeWalOb;
//	}


	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}