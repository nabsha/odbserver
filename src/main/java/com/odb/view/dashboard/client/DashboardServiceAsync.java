/*******************************************************************************
 * Copyright (c) 2012, Nabeel Shaheen	
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 ******************************************************************************/
package com.odb.view.dashboard.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.odb.core.service.DataSourceConfiguration;
import com.odb.view.dashboard.client.dto.PublisherInfo;
import com.odb.view.dashboard.client.dto.SubscriberSubscription;
import com.odb.view.dashboard.client.dto.ViewSettings;


/**
 * The async counterpart of <code>DashboardService</code>.
 */
public interface DashboardServiceAsync {

	/**
	 * Gets the data update.
	 *
	 * @param dataSourceId the data source id
	 * @param graphID the graph id
	 * @param callback the callback
	 * @return the data update
	 */
	void getDataUpdate(String dataSourceId, String graphID, int seriesCount, int seriesSetCount, AsyncCallback<ArrayList<DataVO>> callback);

	/**
	 * Gets the current view settings.
	 *
	 * @param callback the callback
	 * @return the current view settings
	 */
//	void getCurrentViewSettings(AsyncCallback<ViewSettings> callback);
	void getPublisherInfo(AsyncCallback<ArrayList<com.odb.view.dashboard.client.dto.PublisherInfo>> callback);

	void getDataSources(String publisherID, AsyncCallback<ArrayList<com.odb.view.dashboard.client.dto.DataSourceInfo>> callback);
	void getDataSourceAllDetails(String dataSourceID, AsyncCallback<DataSourceConfiguration> callback);

	void getCurrentSubscriptions(AsyncCallback<ArrayList<SubscriberSubscription>> asyncCallback);

	void addSubscription(String dsID, String graphID, AsyncCallback callback);
}
