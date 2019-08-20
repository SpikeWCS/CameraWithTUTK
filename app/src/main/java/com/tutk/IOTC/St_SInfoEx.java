
/******************************************************************************
 *                                                                            *
 * Copyright (c) 2011 by TUTK Co.LTD. All Rights Reserved.                    *
 *                                                                            *
 *                                                                            *
 * Class: St_SInfo.java                                                       *
 *                                                                            *
 * Author: joshua ju                                                          *
 *                                                                            *
 * Date: 2011-05-14                                                           *
 *                                                                            *
 ******************************************************************************/

package com.tutk.IOTC;

//base on struct st_SInfo of IOTCAPIs.h
public class St_SInfoEx {
	public int		size;
	public byte 	Mode;
	public byte		CorD;
	public byte[] 	UID=new byte[21];
	public byte[]	RemoteIP=new byte[17];//RemoteIP[17]
	//char reserve1[2];
	public int		RemotePort;
	public int		TX_count;
	public int		RX_count;
	//----------------------
	public int		VID;
	public int		PID;
	public int 		GID;
	public int		IOTCVersion;
	public byte		isSecure;
	public byte		LocalNatType;
	public byte		RemoteNatType;
	public byte		RelayType;
	public int		NetState;
	public byte[]	RemoteWANIP=new byte[17];//RemoteWANIP[17]
	public int		RemoteWANPort;
}
