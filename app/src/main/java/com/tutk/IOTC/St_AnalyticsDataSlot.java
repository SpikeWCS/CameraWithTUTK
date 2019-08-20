/******************************************************************************
 *                                                                            *
 * Copyright (c) 2011 by TUTK Co.LTD. All Rights Reserved.                    *
 *                                                                            *
 *                                                                            *
 * Class: St_AnalyticsData                                                    *
 *                                                                            *
 * Author: Ferando                                                            *
 *                                                                            *
 * Date: 2016/04/14                                                           *
 *                                                                            *
 ******************************************************************************/

package com.tutk.IOTC;

//base on the struct AnalyticsDataSlot in AVAPIs2.h
public class St_AnalyticsDataSlot
{
	public int count;
	public int index;
	public int version;
	public int dataSize;
	public int timeStamp[] = new int[5];
	public int vDataByte[] = new int[5];
	public int vSendByte[] = new int[5];
	public int vDropByte[] = new int[5];
	public int vResendByte[] = new int[5];
	public int aDataByte[] = new int[5];
	public int aSendByte[] = new int[5];
	public int aDropByte[] = new int[5];
	public int aResendByte[] = new int[5];
	public int vFPS[] = new int[5];
	public int aFPS[] = new int[5];
}
