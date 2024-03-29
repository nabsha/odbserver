/*******************************************************************************
 * Copyright (c) 2012, Nabeel Shaheen	
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 ******************************************************************************/
package com.odb.collector;

import java.util.HashMap;

/**
 * The Class UserDataWrapper.
 * 
 * this class is just a wrapper for the java.util.HashMap
 * 
 * this is because of technology limitation, because the WS client does not understand
 * the Collection API. instead, it is using simple Arrays to represent the data structure.
 */
public class UserDataWrapper {
	
	/** The map. */
	public HashMap<String, Double> map;
}
