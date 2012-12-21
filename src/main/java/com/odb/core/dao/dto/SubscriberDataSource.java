/*******************************************************************************
 * Copyright (c) 2012, Nabeel Shaheen	
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 ******************************************************************************/
package com.odb.core.dao.dto;

import java.io.Serializable;


/**
 * The Class SubscriberDataSource.
 */
@SuppressWarnings("serial")
public class SubscriberDataSource implements Serializable{
	
	/** The subscriber data source id. */
	private String subscriberID, dataSourceID, graphID, subscriberDataSourceID;
	
	/**
	 * Gets the subscriber id.
	 *
	 * @return the subscriber id
	 */
	public String getSubscriberID() {
		return subscriberID;
	}

	/**
	 * Sets the subscriber id.
	 *
	 * @param subscriberID the new subscriber id
	 */
	public void setSubscriberID(String subscriberID) {
		this.subscriberID = subscriberID;
	}

	/**
	 * Gets the data source id.
	 *
	 * @return the data source id
	 */
	public String getDataSourceID() {
		return dataSourceID;
	}

	/**
	 * Sets the data source id.
	 *
	 * @param dataSourceID the new data source id
	 */
	public void setDataSourceID(String dataSourceID) {
		this.dataSourceID = dataSourceID;
	}

	/**
	 * Gets the graph id.
	 *
	 * @return the graph id
	 */
	public String getGraphID() {
		return graphID;
	}

	/**
	 * Sets the graph id.
	 *
	 * @param graphID the new graph id
	 */
	public void setGraphID(String graphID) {
		this.graphID = graphID;
	}

	/**
	 * Gets the subscriber data source id.
	 *
	 * @return the subscriber data source id
	 */
	public String getSubscriberDataSourceID() {
		return subscriberDataSourceID;
	}

	/**
	 * Sets the subscriber data source id.
	 *
	 * @param subscriberDataSourceID the new subscriber data source id
	 */
	public void setSubscriberDataSourceID(String subscriberDataSourceID) {
		this.subscriberDataSourceID = subscriberDataSourceID;
	}
}
