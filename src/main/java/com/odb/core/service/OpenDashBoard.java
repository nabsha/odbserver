/*******************************************************************************
 * Copyright (c) 2012, Nabeel Shaheen	
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 ******************************************************************************/
package com.odb.core.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.odb.core.SubscriberDataSource;
import com.odb.core.dao.ODBDAO;
import com.odb.core.dao.dto.DataSourceAxisDetailInfo;
import com.odb.core.dao.dto.DataSourceAxisInfo;
import com.odb.core.dao.dto.DataSourceInfo;
import com.odb.core.dao.dto.DataSourceSeries;
import com.odb.core.dao.dto.PublisherInfo;
import com.odb.core.dao.dto.SubscriberInfo;
import com.odb.core.service.exceptions.InvalidAuthenticationException;

/**
 * The Class OpenDashBoard.
 * 
 * This class is the main service for the Open Dashboard system
 * 
 */
public class OpenDashBoard {

	/** The odb dao. */
	private ODBDAO odbDAO;

	private String pushURL;

	/** The log. */
	private static Logger log = Logger.getLogger(OpenDashBoard.class);

	/**
	 * Sets the odb dao.
	 * 
	 * @param odbDAO
	 *            the new odb dao
	 */
	public void setOdbDAO(ODBDAO odbDAO) {
		this.odbDAO = odbDAO;
	}

	/**
	 * Subscriber login.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @return the subscriber info
	 */
	public SubscriberInfo subscriberLogin(String username, String password) throws InvalidAuthenticationException {
		SubscriberInfo subInfo = null;
		try {
			subInfo = odbDAO.subscriberLogin(username, password);
			if (subInfo == null)
				throw new InvalidAuthenticationException("Invalid Credentials");
		} catch (SQLException e) {
			log.error("Subscriber Login failed..." + e);
		}
		return subInfo;
	}

	/**
	 * Register publisher.
	 * 
	 * @param pubName
	 *            the publisher name
	 * @return the string publisher Id
	 * @throws SQLException
	 *             the sQL exception
	 */
	public String registerPublisher(String pubName) throws SQLException {
		PublisherInfo pInfo = new PublisherInfo();
		String pubID = pubName + "_" + System.currentTimeMillis();
		pInfo.setPublisherID(pubID);
		pInfo.setPublisherName(pubName);
		odbDAO.addPublisher(pInfo);
		return pubID;
	}

	/**
	 * Register data source.
	 * 
	 * 
	 * @param pubID
	 *            the publisher id
	 * @param dsConfig
	 *            the data source configuration
	 * @return the string
	 * @throws SQLException
	 *             the sQL exception
	 */
	@Transactional(propagation = Propagation.MANDATORY)
	public String registerDataSource(DataSourceConfiguration dsConfig) throws SQLException {
		String dsID = dsConfig.getPublisherID() + "_" + System.currentTimeMillis();
		DataSourceInfo dsInfo = new DataSourceInfo();
		dsInfo.setPublisherID(dsConfig.getPublisherID());
		dsInfo.setDataSourceID(dsID);
		dsInfo.setDataSourceName(dsConfig.getDsName());
		dsInfo.setTimeoutInterval(dsConfig.getDsTimeoutInterval());
		dsInfo.setSeriesCount(dsConfig.getSeriesCount());
		odbDAO.addDataSource(dsInfo);

		for (int i = 0; i < dsConfig.getXsInfo().size(); i++) {
			DataSourceAxisInfo dsAxisInfo = new DataSourceAxisInfo();
			dsAxisInfo.setDataSourceID(dsID);
			dsAxisInfo.setDataSourceAxisID(dsID + "_" + System.currentTimeMillis());
			dsAxisInfo.setDataSourceAxisName(dsConfig.getXsInfo().get(i).getDataSourceAxisName());
			dsAxisInfo.setDataSourceAxisType(dsConfig.getXsInfo().get(i).getDataSourceAxisType());
			odbDAO.addDataSourceAxis(dsAxisInfo);
			ArrayList<String> axisLabels = dsConfig.getXsInfo().get(i).getAxisLabels();

			for (int j = 0; j < axisLabels.size(); j++) {
				DataSourceAxisDetailInfo dsAxisDetailInfo = new DataSourceAxisDetailInfo();
				dsAxisDetailInfo.setDataSourceAxisID(dsAxisInfo.getDataSourceAxisID());
				dsAxisDetailInfo.setAxisLabelIndex(j);
				dsAxisDetailInfo.setAxisLabelValue(axisLabels.get(j));
				odbDAO.addDataSourceAxisDetail(dsAxisDetailInfo);
			}
		}
		return dsID;
	}

	/**
	 * Adds the data series.
	 * 
	 * @param pubID
	 *            the publisher id
	 * @param dsID
	 *            the data source id
	 * @param userData
	 *            the user data
	 * @throws SQLException
	 *             the sQL exception
	 */
	public void addDataSeries(String pubID, String dsID, Map<String, Double> userData) throws SQLException {
		Iterator<String> it = userData.keySet().iterator();
		while (it.hasNext()) {
			DataSourceSeries dsSeries = new DataSourceSeries();
			dsSeries.setDataSourceID(dsID);
			String seriesNum = it.next();
			dsSeries.setSeriesIndex(seriesNum);
			dsSeries.setSeriesIndexSeqVal(userData.get(seriesNum));
			dsSeries.setDateTime(new Timestamp(System.currentTimeMillis()));
			odbDAO.addSeriesData(dsSeries);
		}
		publish(dsID);
	}

	public PublisherInfo getPublisher(String publisherID)  throws SQLException {
		return odbDAO.getPublisherByID(publisherID);
	}

	/**
	 * Gets the all publishers.
	 * 
	 * @return the all publishers
	 * @throws SQLException
	 *             the sQL exception
	 */
	public ArrayList<PublisherInfo> getAllPublishers() throws SQLException {
		return odbDAO.getAllPublishers();
	}

	/**
	 * Gets the latest series data.
	 * 
	 * @param dsID
	 *            the ds id
	 * @param rowNum
	 *            the row num
	 * @return the latest series data
	 */
	public List<DataSourceSeries> getLatestSeriesData(String dsID, int rowNum) {
		List<DataSourceSeries> dsList = null;
		try {
			dsList = odbDAO.getLatestSeriesData(dsID, rowNum);
		} catch (SQLException e) {
			log.error("Getting Data Series Failed for datasource ID:" + dsID + e);
		}

		return dsList;
	}

	/**
	 * Gets the all data source by subscriber ID.
	 * 
	 * @param pubID
	 *            the pub id
	 * @return the all data source by subscriber
	 * @throws SQLException
	 *             the sQL exception
	 */
	public ArrayList<SubscriberDataSource> getAllDataSourceBySubscriber(String subscriberID) throws SQLException {
		return odbDAO.getAllDataSourceBySubscriberID(subscriberID);
	}

	/**
	 * Gets the all data source by publisher.
	 * 
	 * @param pubID
	 *            the pub id
	 * @return the all data source by publisher
	 * @throws SQLException
	 *             the sQL exception
	 */
	public ArrayList<DataSourceInfo> getAllDataSourceByPublisher(String pubID) throws SQLException {
		return odbDAO.getAllDataSourceByPublisherID(pubID);
	}

	/**
	 * Gets the data source by data source id.
	 * 
	 * @param dsID
	 *            the ds id
	 * @return the data source by data source id
	 * @throws SQLException
	 *             the sQL exception
	 */
	public DataSourceInfo getDataSourceByDataSourceID(String dsID) throws SQLException {
		return odbDAO.getDataSourceByDataSourceID(dsID);
	}

	/**
	 * Gets the data source configuration by.
	 * 
	 * @param dsID
	 *            the ds id
	 * @return the data source configuration by
	 * @throws SQLException
	 *             the sQL exception
	 */
	public DataSourceConfiguration getDataSourceConfigurationBy(String dsID) throws SQLException {
		DataSourceConfiguration dsConfig = new DataSourceConfiguration();
		DataSourceInfo dsInfo = odbDAO.getDataSourceByDataSourceID(dsID);
		dsConfig.setDsName(dsInfo.getDataSourceName());
		dsConfig.setDsTimeoutInterval(dsInfo.getTimeoutInterval());
		dsConfig.setSeriesCount(dsInfo.getSeriesCount());
		dsConfig.setPublisherID(dsInfo.getPublisherID());
		dsConfig.setDsID(dsID);
		List<DataSourceAxisInfo> dsAxisInfoAll = odbDAO.getDataSourceAxisInfo(dsID);
		List<AxisInfo> xsInfoAll = new ArrayList<AxisInfo>();
		for (int i = 0; i < dsAxisInfoAll.size(); i++) {
			DataSourceAxisInfo dsAxisInfo = dsAxisInfoAll.get(i);
			AxisInfo xsInfo = new AxisInfo();
			xsInfo.setDataSourceAxisName(dsAxisInfo.getDataSourceAxisName());
			xsInfo.setDataSourceAxisType(dsAxisInfo.getDataSourceAxisType());
			List<DataSourceAxisDetailInfo> dsAxisInfoDetailAll = odbDAO.getDataSourceAxisDetailInfoListBy(dsAxisInfo.getDataSourceAxisID());
			List<String> axisLabels = new ArrayList<String>();
			for (int j = 0; j < dsAxisInfoDetailAll.size(); j++) {
				DataSourceAxisDetailInfo dsAxisInfoDetail = dsAxisInfoDetailAll.get(j);
				axisLabels.add(dsAxisInfoDetail.getAxisLabelValue());
			}
			xsInfo.setAxisLabels((ArrayList<String>) axisLabels);
			xsInfoAll.add(xsInfo);

		}
		dsConfig.setXsInfo((ArrayList<AxisInfo>) xsInfoAll);
		return dsConfig;
	}

	/**
	 * Publish.
	 * 
	 * this function is calling the publish action on the Dashboard view module
	 * 
	 * the publish action will update the view with the new data of the
	 * dataSourceId given.
	 * 
	 * 
	 * @param dataSourceId
	 *            the data source id
	 * @return true, if the return code of the publish action is reponse code
	 *         200, otherwise it will return false.
	 */
	public boolean publish(String dataSourceId) {
		URL url;
		try {
			url = new URL(pushURL + "?dataSourceId=" + dataSourceId);
			HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
			int code = httpConnection.getResponseCode();
			return (code == 200);
		} catch (MalformedURLException e) {
			log.error("error while Publishing dataSourceId: " + dataSourceId, e);
		} catch (IOException e) {
			log.error("error while Publishing dataSourceId: " + dataSourceId, e);
		}
		return false;
	}

	/**
	 * Gets the data source axis info.
	 * 
	 * @param dataSourceID
	 *            the data source id
	 * @return the data source axis info
	 */
	public List<DataSourceAxisInfo> getDataSourceAxisInfo(String dataSourceID) {
		List<DataSourceAxisInfo> dsAxisList = null;
		try {
			dsAxisList = odbDAO.getDataSourceAxisInfo(dataSourceID);
		} catch (SQLException e) {
			log.error("Getting Datasource Axis Info failed for datasoruce ID:" + dataSourceID + e);
		}
		return dsAxisList;
	}

	/**
	 * Gets the data source axis detail info list by.
	 * 
	 * @param axisId
	 *            the axis id
	 * @return the data source axis detail info list by
	 */
	public List<DataSourceAxisDetailInfo> getDataSourceAxisDetailInfoListBy(String axisId) {
		List<DataSourceAxisDetailInfo> dsAxisListAll=null;
		try {
			dsAxisListAll = odbDAO.getDataSourceAxisDetailInfoListBy(axisId);
		} catch (SQLException e) {
			log.error("Getting DataSource Axis Details failed for axisid:" + axisId + e);
		}
		return dsAxisListAll;
	}

	public void addSubscription(String subscriberID, String dsID, String graphID, String subDSID) {
		try {
			odbDAO.addSubscribeDataSource(subscriberID, dsID, graphID, subDSID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.error("Failed to add subscription to subscribers account" + e);;
		}
	}
	public DataSourceInfo getDataSourceInfo(String datasourceId) throws SQLException {
		return odbDAO.getDataSourceByDataSourceID(datasourceId);
	}

	public String getPushURL() {
		return pushURL;
	}

	public void setPushURL(String pushURL) {
		this.pushURL = pushURL;
	}

}
