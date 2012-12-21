/*******************************************************************************
 * Copyright (c) 2012, Nabeel Shaheen	
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 ******************************************************************************/
package com.odb.core.dao.rowmappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.odb.core.SubscriberDataSource;
import com.odb.core.dao.dto.DataSourceInfo;

/**
 * The Class DataSourceInfoRowMapper.
 */
public class SubscriberDataSourceRowMapper implements RowMapper<SubscriberDataSource>{

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	public SubscriberDataSource mapRow(ResultSet rs, int rowNum) throws SQLException {
		SubscriberDataSource sdInfo = new SubscriberDataSource();
		sdInfo.setDataSourceID(rs.getString("DATASOURCE_ID"));
		sdInfo.setGraphID(rs.getString("GRAPH_ID"));
		sdInfo.setSubscriberDataSourceID(rs.getString("SUBSCRIBER_DATASOURCE_ID"));
		sdInfo.setSubscriberID(rs.getString("SUBSCRIBER_ID"));
		return sdInfo;
	}

}
