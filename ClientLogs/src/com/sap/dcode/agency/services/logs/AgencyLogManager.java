package com.sap.dcode.agency.services.logs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;
import java.util.List;

import android.content.Context;

import com.sap.dcode.agency.services.Operation;
import com.sap.dcode.agency.services.UIListener;
import com.sap.dcode.agency.services.online.CredentialsProvider;
import com.sap.dcode.agency.services.online.XCSRFTokenRequestFilter;
import com.sap.dcode.agency.services.online.XCSRFTokenResponseFilter;
import com.sap.dcode.util.TraceLog;
import com.sap.maf.tools.logon.core.LogonCore;
import com.sap.maf.tools.logon.core.LogonCoreContext;
import com.sap.maf.tools.logon.core.LogonCoreException;
import com.sap.smp.client.httpc.HttpConversationManager;
import com.sap.smp.client.httpc.IHttpConversation;
import com.sap.smp.client.httpc.SupportabilityUploaderImpl;
import com.sap.smp.client.httpc.authflows.CommonAuthFlowsConfigurator;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.httpc.utils.SAPLoggerUtils;
import com.sap.smp.client.supportability.ClientLogDestination;
import com.sap.smp.client.supportability.ClientLogEntry;
import com.sap.smp.client.supportability.ClientLogLevel;
import com.sap.smp.client.supportability.ClientLogManager;
import com.sap.smp.client.supportability.ClientLogger;
import com.sap.smp.client.supportability.Supportability;

public class AgencyLogManager {
	private static final String FILE_LOGGER_ID = "LoggerFile";
	private static final String CONSOLE_LOGGER_ID = "LoggerConsole";
	private static final String FILE_UPLOAD_PATH = "clientlogs";
	private static Context ctx;
	private static ClientLogManager logMan;
	private static ClientLogger fileLogger;
	private static ClientLogger consoleLogger;

	/**
	 * Initialize the Client Log Manager
	 * @param context application context
	 */
	public static void initialize(Context context){
		ctx = context;
		//Get client log manager
		logMan = Supportability.getInstance().getClientLogManager(ctx);
		//Set log levels. 
		//For the purpose of this exercise we will use ALL, 
		// but you can choose other log levels: DEBUG, ERROR, INFO, etc
		logMan.setLogLevel(ClientLogLevel.ALL);
		//write logs in file system 
		fileLogger = logMan.getLogger(FILE_LOGGER_ID);
		logMan.setLogDestination(EnumSet.of(ClientLogDestination.FILESYSTEM), 
				FILE_LOGGER_ID);
		//Write logs in console
		consoleLogger = logMan.getLogger(CONSOLE_LOGGER_ID);
		logMan.setLogDestination(EnumSet.of(ClientLogDestination.CONSOLE), 
				CONSOLE_LOGGER_ID);
	}

	/**
	 * Write an error message in the console and file logs
	 * @param message 
	 * @param e Exception
	 */
	public static void writeLogError(String message, Exception e) {
		if (fileLogger == null || consoleLogger == null) { 
			TraceLog.d("writeLogError::ClientLoggers are not intialized");
		} else {
			fileLogger.logError(message, e);
			consoleLogger.logError(message, e);
		}
	}
	
	/**
	 * Write information in the console and file logs
	 * @param message
	 */
	public static void writeLogInfo(String message){
		if (fileLogger == null || consoleLogger == null) { 
			TraceLog.d("writeLogInfo::ClientLoggers are not intialized");
		} else {
			fileLogger.logInfo(message);
			consoleLogger.logInfo(message);
		}
	}

	/**
	 * Write debug information in the console and file logs
	 * @param message
	 */
	public static void writeLogDebug(String message){
		if (fileLogger == null || consoleLogger == null) { 
			TraceLog.d("writeLogDebug::ClientLoggers are not intialized");
		} else {
			fileLogger.logDebug(message);
			consoleLogger.logDebug(message);
		}
	}
	
	/**
	 * Write event information in the console and file logs
	 * @param event
	 */
	public static void writeLogEvent(IReceiveEvent event) {
		try{
			if (fileLogger == null || consoleLogger == null) { 
				TraceLog.d("writeLogEvent::ClientLoggers are not intialized");
			} else {
				SAPLoggerUtils.logResponseDetails(event, fileLogger, ClientLogLevel.ALL, true, true);
				SAPLoggerUtils.logResponseDetails(event, consoleLogger, ClientLogLevel.ALL, true, true);
			}
		}catch (Exception e){
			TraceLog.e("writeLogEvent",e);
		}
	}

	/**
	 * Read entry logs from the file logs
	 * @return
	 * @throws AgencyLogException
	 */
	public static String getFileLogs() throws AgencyLogException{
		return getFileLogs(ClientLogLevel.ALL);
	}
	
	/**
	 * Read specific log level entries from the file logs
	 * @param logLevel
	 * @return 
	 * @throws AgencyLogException
	 */
	public static String getFileLogs(ClientLogLevel logLevel) throws AgencyLogException{
		TraceLog.d("getFileLogs");

		if (logMan == null) throw new AgencyLogException("ClientLogManager is not initialized");
		
		String strLogs = "";
		int numEntry = 0;
		//Warning: Use this following method with caution. In case of a large amount of log entries calling this method might cause memory issues
		//Future SP releases may contain some replacement for this deprecated method
		List<ClientLogEntry> logs = logMan.getLogEntriesForLogger(FILE_LOGGER_ID, logLevel);
		for (ClientLogEntry entry: logs){
			numEntry++;
			strLogs += "Entry " + numEntry + ": " + entry.getDate() + "-" + entry.getMessage() + "\n";
		}
		return strLogs;
		//return TextUtils.join("\n", logs);

	}
	
	/**
	 * return the URL to upload the logs
	 * @param path
	 * @return 
	 */
    private static URL getUploadURL(String path) {
    	 
        LogonCoreContext lgCtx = LogonCore.getInstance().getLogonContext();
        URL url = null;
       
        String host = lgCtx.getHost();
       
        int port = 8080;
        try {
               port = Integer.valueOf(lgCtx.getPort());
        } catch (NumberFormatException e) {
               TraceLog.e("getUploadURL: Invalid port value, default (8080) is being used.");
        }
       
        String protocol = lgCtx.isHttps() ? "https" : "http";

        try {
               url = new URL(protocol, host, port, path);
        } catch (MalformedURLException e) {
               String msg = "Failed to create upload URL";
               TraceLog.e(msg, e);                                        
               throw new RuntimeException(msg, e);
        }
        return url;
  }

    /**
     * Upload client logs to server
     * @param uiListener
     * @throws AgencyLogException
     */
	public static void UploadFileLogs(UIListener uiListener) throws AgencyLogException {
		TraceLog.d("UploadFileLogs");
		
		if (ctx == null) throw new AgencyLogException("Context is not initialized");
		if (logMan == null) throw new AgencyLogException("ClientLogger is not initialized");
		
        LogonCoreContext lgCtx = LogonCore.getInstance().getLogonContext();
        String appCID = null;
        
        try {
              appCID = lgCtx.getConnId();
        } catch (LogonCoreException e) {
              TraceLog.e("uploadLogs: Failed to get LogonCoreContext", e);                                        
              throw new AgencyLogException(e);
        }

        if (appCID != null) {
			URL logURL = getUploadURL(FILE_UPLOAD_PATH);
            TraceLog.d("uploadLogs: Starting Log Upload using URL: " + logURL);
            CredentialsProvider credProvider = CredentialsProvider.getInstance(lgCtx);
      		HttpConversationManager manager = new CommonAuthFlowsConfigurator(ctx).
        					supportBasicAuthUsing(credProvider).configure(new HttpConversationManager(ctx));
        	
       		XCSRFTokenRequestFilter requestFilter = XCSRFTokenRequestFilter.getInstance(lgCtx);
       		XCSRFTokenResponseFilter responseFilter = XCSRFTokenResponseFilter.getInstance(ctx, requestFilter);
       		manager.addFilter(requestFilter);
       		manager.addFilter(responseFilter);
            IHttpConversation conv = manager.create(logURL);
                     
            SupportabilityUploaderImpl uploader = new SupportabilityUploaderImpl(conv, ctx);
            logMan.uploadClientLogs(uploader, 
                    		 new AgencyLogUploadListener(Operation.UploadFileLogs.getValue(), uiListener));


        } else {
              final String msg = "uploadLogs: Log Upload is not initiated: no APPCID from registration";
              TraceLog.e(msg);
        }

	}
	

}
