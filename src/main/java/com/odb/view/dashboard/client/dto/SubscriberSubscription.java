/*******************************************************************************
 * Copyright (c) 2012, Nabeel Shaheen	
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 ******************************************************************************/
package com.odb.view.dashboard.client.dto;

import java.io.Serializable;
import java.util.ArrayList;

import com.odb.core.service.AxisInfo;

/**
 * The Class DataSourceConfiguration.
 */
public class SubscriberSubscription  implements Serializable {
	
	/** The ds name. */
	private String dsName;
	private String dsID;
	private int seriesCount;
	private String publisherID;
	private String graphID;
	public String getGraphID() {
		return graphID;
	}

	public void setGraphID(String graphID) {
		this.graphID = graphID;
	}

	/** The xs info. */
	private ArrayList<AxisInfo> xsInfo;
	
	/** The ds timeout interval. */
	private Long dsTimeoutInterval;

	/**
	 * Gets the xs info.
	 *
	 * @return the xs info
	 */
	public ArrayList<AxisInfo> getXsInfo() {
		return xsInfo;
	}

	/**
	 * Sets the xs info.
	 *
	 * @param xsInfo the new xs info
	 */
	public void setXsInfo(ArrayList<AxisInfo> xsInfo) {
		this.xsInfo = xsInfo;
	}

	/**
	 * Gets the ds name.
	 *
	 * @return the ds name
	 */
	public String getDsName() {
		return dsName;
	}

	/**
	 * Sets the ds name.
	 *
	 * @param dsName the new ds name
	 */
	public void setDsName(String dsName) {
		this.dsName = dsName;
	}

	/**
	 * Gets the ds timeout interval.
	 *
	 * @return the ds timeout interval
	 */
	public Long getDsTimeoutInterval() {
		return dsTimeoutInterval;
	}

	/**
	 * Sets the ds timeout interval.
	 *
	 * @param dsTimeoutInterval the new ds timeout interval
	 */
	public void setDsTimeoutInterval(Long dsTimeoutInterval) {
		this.dsTimeoutInterval = dsTimeoutInterval;
	}

	public String getDsID() {
		return dsID;
	}

//	NOTE: No Client should set DataSource SID 
	public void setDsID(String dsID) {
		this.dsID = dsID;
	}

	public int getSeriesCount() {
		return seriesCount;
	}

	public void setSeriesCount(int seriesCount) {
		this.seriesCount = seriesCount;
	}

	public String getPublisherID() {
		return publisherID;
	}

	public void setPublisherID(String publisherID) {
		this.publisherID = publisherID;
	}

	
}
