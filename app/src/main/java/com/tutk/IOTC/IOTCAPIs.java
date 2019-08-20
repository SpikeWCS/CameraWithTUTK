/******************************************************************************
 *                                                                            *
 * Copyright (c) 2011 by TUTK Co.LTD. All Rights Reserved.                    *
 * Class: IOTCAVApis.java                                                     *
 *                                                                            *
 * Author: joshua ju                                                          *
 * Date: 2011-05-14                                                           *

	Revisions of IOTCAVApis
	IOTCApis Version joined		Name		Date
	-------						----		----------
	0.5.0.0						Joshua		2011-06-30
	1.1.0.0						Joshua		2011-08-01
	1.2.0.0						Joshua      2011-08-30

 ******************************************************************************/

package com.tutk.IOTC;

public class IOTCAPIs {

	public static final int API_ER_ANDROID_NULL					=-10000;
	//IOTCApis error code===================================================================================
	/** The function is performed successfully. */
	public static final int	IOTC_ER_NoERROR							 =0;

	/** IOTC servers have no response, probably caused by many types of Internet connection issues.
	* See [Troubleshooting](..\Troubleshooting\index.htm#IOTC_ER_SERVER_NOT_RESPONSE) */
	public static final int	IOTC_ER_SERVER_NOT_RESPONSE				 =-1;

	/** IOTC masters cannot be resolved their domain name, probably caused
	* by network connection or DNS setting issues.
	* See [Troubleshooting](..\Troubleshooting\index.htm#IOTC_ER_FAIL_RESOLVE_HOSTNAME) */
	public static final int	IOTC_ER_FAIL_RESOLVE_HOSTNAME			 =-2;

	/** IOTC module is already initialized. It is not necessary to re-initialize. */
	public static final int IOTC_ER_ALREADY_INITIALIZED                 =-3;

	/** IOTC module fails to create Mutexs when doing initialization. Please
	* check if OS has sufficient Mutexs for IOTC platform. */
	public static final int IOTC_ER_FAIL_CREATE_MUTEX                   =-4;

	/** IOTC module fails to create threads. Please check if OS has ability
	* to create threads for IOTC module. */
	public static final int IOTC_ER_FAIL_CREATE_THREAD                  =-5;

	/** IOTC module fails to create sockets. Please check if OS supports socket service */
	public static final int IOTC_ER_FAIL_CREATE_SOCKET                  =-6;

	/** IOTC module fails to set up socket options. */
	public static final int IOTC_ER_FAIL_SOCKET_OPT                     =-7;

	/** IOTC module fails to bind sockets */
	public static final int IOTC_ER_FAIL_SOCKET_BIND                    =-8;

	/** The specified UID is not licensed.
	* See [Troubleshooting](..\Troubleshooting\index.htm#IOTC_ER_UNLICENSE) */
	public static final int IOTC_ER_UNLICENSE                           =-10;

	/** The device is already login successfully */
	public static final int IOTC_ER_LOGIN_ALREADY_CALLED                =-11;

	/** IOTC module is not initialized yet. Please use IOTC_Initialize() or
	* IOTC_Initialize2() for initialization. */
	public static final int IOTC_ER_NOT_INITIALIZED                     =-12;

	/** The specified timeout has expired during the execution of some IOTC
	* module service. For most cases, it is caused by slow response of remote
	* site or network connection issues */
	public static final int IOTC_ER_TIMEOUT                             =-13;

	/** The specified IOTC session ID is not valid */
	public static final int IOTC_ER_INVALID_SID                         =-14;

	/** The specified device's name is not unknown to the IOTC servers */
	public static final int IOTC_ER_UNKNOWN_DEVICE                      =-15;

	/** IOTC module fails to get the local IP address
	* See [Troubleshooting](..\Troubleshooting\index.htm#IOTC_ER_FAIL_GET_LOCAL_IP) */
	public static final int IOTC_ER_FAIL_GET_LOCAL_IP                   =-16;

	/** The device already start to listen for connections from clients. It is
	* not necessary to listen again. */
	public static final int IOTC_ER_LISTEN_ALREADY_CALLED               =-17;

	/** The number of IOTC sessions has reached maximum.
	* Please use IOTC_Set_Max_Session_Number() to set up the max number of IOTC sessions */
	public static final int IOTC_ER_EXCEED_MAX_SESSION                  =-18;

	/** IOTC servers cannot locate the specified device, probably caused by
	* disconnection from the device or that device does not login yet. */
	public static final int IOTC_ER_CAN_NOT_FIND_DEVICE                 =-19;

	/** The client is connecting to a device. It is prohibited to connect again. */
	public static final int IOTC_ER_CONNECT_IS_CALLING                  =-20;

	/** The remote site already closes this IOTC session.
	* Please call IOTC_Session_Close() to release IOTC session resource in locate site. */
	public static final int IOTC_ER_SESSION_CLOSE_BY_REMOTE             =-22;

	/** This IOTC session is disconnected because remote site has no any response
	* after a specified timeout expires. */
	public static final int IOTC_ER_REMOTE_TIMEOUT_DISCONNECT           =-23;

	/** The client fails to connect to a device because the device is not listening for connections.
	* See [Troubleshooting](..\Troubleshooting\index.htm#IOTC_ER_DEVICE_NOT_LISTENING) */
	public static final int IOTC_ER_DEVICE_NOT_LISTENING                =-24;

	/** The IOTC channel of specified channel ID is not turned on before transferring data. */
	public static final int IOTC_ER_CH_NOT_ON                           =-26;

	/** A client stops connecting to a device by calling IOTC_Connect_Stop() */
	public static final int IOTC_ER_FAIL_CONNECT_SEARCH                 =-27;

	/** Too few masters are specified when initializing IOTC module.
	* Two masters are required for initialization at minimum. */
	public static final int IOTC_ER_MASTER_TOO_FEW                      =-28;

	/** A client fails to pass certification of a device due to incorrect key. */
	public static final int IOTC_ER_AES_CERTIFY_FAIL                    =-29;

	/** The number of IOTC channels for a IOTC session has reached maximum, say, MAX_CHANNEL_NUMBER. */
	public static final int IOTC_ER_SESSION_NO_FREE_CHANNEL             =-31;

	/** ??? All tcp port 80, 443, 8000, 8080 cant use */
	public static final int		IOTC_ER_TCP_TRAVEL_FAILED				=-32;

	/** Cannot connect to IOTC servers in TCP
	* See [Troubleshooting](..\Troubleshooting\index.htm#IOTC_ER_TCP_CONNECT_TO_SERVER_FAILED) */
	public static final int IOTC_ER_TCP_CONNECT_TO_SERVER_FAILED        =-33;

	/** A client wants to connect to a device in non-secure mode while that device
	* supports secure mode only. */
	public static final int IOTC_ER_CLIENT_NOT_SECURE_MODE              =-34;

	/** A client wants to connect to a device in secure mode while that device does
	* not support secure mode. */
	public static final int IOTC_ER_CLIENT_SECURE_MODE					=-35;

	/** A device does not support connection in secure mode */
	public static final int IOTC_ER_DEVICE_NOT_SECURE_MODE              =-36;

	/** A device does not support connection in non-secure mode */
	public static final int IOTC_ER_DEVICE_SECURE_MODE					=-37;

	/** The IOTC session mode specified in IOTC_Listen2(), IOTC_Connect_ByUID2()
	* or IOTC_Connect_ByName2() is not valid.
	* Please see IOTCConnectionMode for possible modes. */
	public static final int IOTC_ER_INVALID_MODE                        =-38;

	/** A device stops listening for connections from clients. */
	public static final int IOTC_ER_EXIT_LISTEN                         =-39;

	/** The specified device does not support advance function
	 *(TCP relay and P2PTunnel module) */
	public static final int IOTC_ER_NO_PERMISSION                       =-40;

	/** Network is unreachable, please check the network settings */
	public static final int	IOTC_ER_NETWORK_UNREACHABLE     			=-41;

	/** A client fails to connect to a device via relay mode */
	public static final int IOTC_ER_FAIL_SETUP_RELAY					=-42;

	/** A client fails to use UDP relay mode to connect to a device
	 * because UDP relay mode is not supported for that device by IOTC servers */
	public static final int IOTC_ER_NOT_SUPPORT_RELAY					=-43;

	/** No IOTC server information while device login or client connect
	 * because no IOTC server is running or not add IOTC server list */
	public static final int IOTC_ER_NO_SERVER_LIST						=-44;

	/** The connecting device duplicated loggin and may unconnectable. */
	public static final int IOTC_ER_DEVICE_MULTI_LOGIN					=-45;

	/** The arguments passed to a function is invalid. */
	public static final int IOTC_ER_INVALID_ARG							=-46;

	/** The remote device not support partial encoding */
	public static final int IOTC_ER_NOT_SUPPORT_PE						=-47;

    /** The remote device no more free session can be connected. */
    public static final int IOTC_ER_DEVICE_EXCEED_MAX_SESSION			=-48;

	/** The function call is a blocking call and was called by other thread. */
    public static final int IOTC_ER_BLOCKED_CALL						=-49;

	/** The session was closed. */
    public static final int IOTC_ER_SESSION_CLOSED						=-50;

	/** Remote doesn't support this function. */
    public static final int IOTC_ER_REMOTE_NOT_SUPPORTED				=-51;

	/** The function is aborted by related function. */
    public static final int IOTC_ER_ABORTED								=-52;

	/** The buffer size exceed maximum packet size. */
	public static final int IOTC_ER_EXCEED_MAX_PACKET_SIZE				=-53;

	/** Server does not support this feature. */
	public static final int IOTC_ER_SERVER_NOT_SUPPORT					=-54;

	/** Cannot find a path to write data*/
	public static final int IOTC_ER_NO_PATH_TO_WRITE_DATA				=-55;

	/** Start function is not called */
	public static final int IOTC_ER_SERVICE_IS_NOT_STARTED				=-56;

	/** Already in processing */
	public static final int IOTC_ER_STILL_IN_PROCESSING					=-57;

	/** Out of memory */
	public static final int IOTC_ER_NOT_ENOUGH_MEMORY					=-58;

	/** The device is banned and locked*/
	public static final int IOTC_ER_DEVICE_IS_BANNED					=-59;

	/** IOTC master servers have no response, probably caused by many types of Internet connection issues. */
	public static final int IOTC_ER_MASTER_NOT_RESPONSE					=-60;

	/** IOTC module has some resource allocating problem. */
	public static final int IOTC_ER_RESOURCE_ERROR						=-61;

	/** IOTC Write reliable send queue is full. */
	public static final int IOTC_ER_QUEUE_FULL							=-62;

	/** The feature is not supported. */
	public static final int IOTC_ER_NOT_SUPPORT							=-63;

	/** Device is in sleep mode. */
	public static final int IOTC_ER_DEVICE_IS_SLEEP						=-64;

	/** Device doesn't support this feature on TCP mode. */
	public static final int IOTC_ER_TCP_NOT_SUPPORT						=-65;

	/** IOTC_WakeUp_Init isn't called */
	public static final int IOTC_ER_WAKEUP_NOT_INITIALIZED              =-66;

	/** All Server response can not find device */
	public static final int IOTC_ER_DEVICE_OFFLINE						=-90;

	/** IOTC master server is invalid */
	public static final int IOTC_ER_MASTER_INVALID						=-91;

	//IOTCApis interface
	public native static int  IOTC_Get_Login_Info(int[] LoginInfo);
	public native static void IOTC_Get_Version(int[] Version);
	public native static void IOTC_Set_Max_Session_Number(int num);
	public native static int  IOTC_Initialize(int UDPPort, String P2PHostNamePrimary,
									   String P2PHostNameSecondary,
									   String P2PHostNameThird,
									   String P2PHostNameFourth);
	public native static int  IOTC_Initialize2(int UDPPort);
	public native static int  IOTC_DeInitialize();
	public native static int  IOTC_Device_Login(String UID, String DeviceName, String DevicePWD);
	public native static int  IOTC_Listen(int Timeout_ms);
	public native static int  IOTC_Listen2(int Timeout_ms, String AesKey, int mode);	 // mode-> 0: equal IOTC_Listen(), 1: only accept secure connection, 2: accept non-secure and secure then use IOTC_Session_Check() to know which mode
	public native static int  IOTC_Connect_ByUID(String UID);
	public native static int  IOTC_Connect_ByUID2(String UID, String AesKey, int mode); // mode-> 0: equal IOTC_Listen(), 1: only accept secure connection, 2: accept non-secure and secure then use IOTC_Session_Check() to know which mode
	public native static void IOTC_Connect_Stop();
	public native static int  IOTC_Get_SessionID();
	public native static int  IOTC_Connect_ByUID_Parallel(String UID, int SID);
	public native static int IOTC_Connect_Stop_BySID(int SID);

	public native static int  IOTC_Session_Check(int SID, St_SInfo s_Info);
	public native static int  IOTC_Session_Check_Ex(int SID, St_SInfoEx s_Info);
	public native static int  IOTC_Session_Read(int SID, byte[] Buf, int Max_size,int Timeout_ms,int ChID);
	public native static int  IOTC_Session_Write(int SID, byte[] Buf, int Size, int ChID);
	public native static void IOTC_Session_Close(int SID);

	public native static int IOTC_Session_Get_Free_Channel(int SID);
	public native static int IOTC_Session_Channel_ON(int SID, int ChID);
	public native static int IOTC_Session_Channel_OFF(int SID,int ChID);
	public native static int IOTC_Get_Nat_Type();
	public native static int IOTC_IsDeviceSecureMode(int SID);
	public native static st_LanSearchInfo[]  IOTC_Lan_Search (int[] arrNum, int nWaitTimeMs);
	public native static st_LanSearchInfo2[] IOTC_Lan_Search2(int[] arrNum, int nWaitTimeMs);
	public native static st_LanSearchInfo2[] IOTC_Lan_Search2_Ex(int[] arrNum, int nWaitTimeMs, int nSendIntervalMs);

	public native static int IOTC_Search_Device_Start(int nWaitTimeMs, int nSendIntervalMs);
	public native static int IOTC_Search_Device_Stop();
	public native static st_SearchDeviceInfo[] IOTC_Search_Device_Result(int[] arrNum, int nGetAll);

	public native static void IOTC_Set_Log_Path(String path, int maxSize);
	public native static void IOTC_Listen_Exit();
	public native static void IOTC_Get_Login_Info_ByCallBackFn(Object obj);  			//Callback method, pass object itself.
	public native static int  IOTC_Session_Check_ByCallBackFn(int SID, Object obj);  	//Callback method, pass object itself.
	public native static int  IOTC_Set_Partial_Encryption(int SID, int bPartialEncryption);
	public native static int  IOTC_Session_Read_Check_Lost(int SID, byte[] Buf, int Max_size,int Timeout_ms, int[] PacketSN, int[] FlagLost, int ChID);
	public native static void IOTC_Set_Device_Name(String DeviceName);
	public native static void IOTC_TCPRelayOnly_TurnOn();
	public native static void IOTC_Setup_LANConnection_Timeout(int Timeout_ms);
	public native static void IOTC_Setup_P2PConnection_Timeout(int Timeout_ms);
	public native static void IOTC_Setup_DetectNetwork_Timeout(int Timeout_ms);
	public native static void IOTC_Setup_Session_Alive_Timeout(int Timeout_sec);
	public native static void IOTC_Setup_ErrorUpload(int bEnable);
	public native static int  IOTC_Session_Write_Reliable(int SID, byte[] Buf, int Size, int ChID, int nTimeout);
	public native static int  IOTC_Session_Write_Reliable_Abort(int SID, int ChID);
	/* WakeUp API for client to wake device up*/
	public native static int  IOTC_WakeUp_WakeDevice(String sUID);
	public native static int  IOTC_Set_LanSearchPort(int nPort);
	/* Callback function. When connection mode is changed, callback will be invoked. */
	public native static void IOTC_ConnModeChange_CallBack(Object obj);
	public native static int IOTC_Device_LoginNB(String cszUID, String cszDeviceName, String cszDevicePWD, byte[] userData, Object obj);
	public native static int IOTC_Get_Device_Status(st_DeviceStInfo pDevStInfo);
	public native static int IOTC_Accept(int[] SID);
	public native static int IOTC_Connect_ByUIDNB(String cszUID, byte[] userData, Object obj);
	public native static int IOTC_Connect_ByUID_ParallelNB(String cszUID, int SID, byte[] userData, Object obj);
	public native static int IOTC_Sessioin_Channel_Check_ON_OFF(int nIOTCSessionID, int nIOTCChannelID);
	public native static int IOTC_Set_Connection_Option(st_ConnectOption S_ConnectOption);
	public native static int  IOTC_ReInitSocket(int UDPPort);
	/*set Callback function. onLineResultCB */
	public native static int IOTC_Check_Device_On_Line(String UID, int timeOut, byte[] userData, Object obj);
	public native static int  IOTC_Add_MasterServer(String cszMasterHostName1, String cszMasterHostName2, 
							   		 				String cszMasterHostName3, String cszMasterHostName4);
	public native static void IOTC_WakeUp_Setup_Auto_WakeUp(int nEnableAutoWakeUp);

	private OnLineResultCBListener mListener;
	public void setOnLineResultCBListener(OnLineResultCBListener listener){
		this.mListener = listener;
	}

	public void onLineResultCB(int result, byte[] userData) {
		System.out.println("[parent] onLineResult Callback, result=" + result + " UID=" + new String(userData));
		if (mListener != null) {
			mListener.onLineResultCB(result, userData);
		}
	};

	public void loginInfoCB(int nLonInfo) {
		//This is callback method, we can modify it.
		System.out.println("[parent] LoginInfo Callback, nLogInfo=" + nLonInfo);
	};

	public void sessionStatusCB(int nSID, int nErrCode) {
		//This is callback method, we can modify it.
		System.out.println("[parent] SessionStatus Callback, nSID="+ nSID + ", nErrCode="+ nErrCode);
	};

	public void ConnectModeChangeCB(int nSID, int nConnMode)
	{
		System.out.println("[parent] ConnectModeChangeCB Callback, nSID = " + nSID +", nConnMode = " + nConnMode);
	};

	static { try { System.loadLibrary("IOTCAPIs"); }
			 catch(UnsatisfiedLinkError ule){
			   System.out.println("loadLibrary(IOTCAPIsT),"+ule.getMessage());
			 }
	}
}
