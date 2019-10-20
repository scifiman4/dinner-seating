/*
 * File Name	:		ValidatorConstants.java
 * 
 * Last Updated	:		Dec 28, 2012 11:03:24 AM
 * 
 */

package org.ssfs.dorm.seating.domain;

@SuppressWarnings("javadoc")
interface ValidatorConstants {

	// Higher value = lower priority

	public static final int SEATED = 0;

	public static final int LAST_TIME = 10;

	public static final int COUNTRIES = 20;

	public static final int TOO_MANY_TIMES = 30;

	public static final int DISJOINT = 40;

	public static final int FULL = 50;

}
