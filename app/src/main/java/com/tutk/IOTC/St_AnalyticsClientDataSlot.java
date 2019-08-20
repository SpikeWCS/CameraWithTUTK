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

// base on the struct AnalyticsClientDataSlot in AVAPIs2.h
public class St_AnalyticsClientDataSlot
{
	public int count;
	public int index;
	public int version;
	public int dataSize;
	public int timeStamp[] = new int[5];
	public int vRecvByte[] = new int[5];
	public int vUserByte[] = new int[5];
	public int vDropByte[] = new int[5];
	public int vResendByte[] = new int[5];
	public int vResendReqCnt[] = new int[5];
	public int aRecvByte[] = new int[5];
	public int aUserByte[] = new int[5];
	public int aDropCnt[] = new int[5];
	public int aResendByte[] = new int[5];
	public int aResendReqCnt[] = new int[5];
	public int vFPS[] = new int[5];
	public int aFPS[] = new int[5];
}
