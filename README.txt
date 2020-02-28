====================
= Important Notice =
====================
We have decided to stop the maintenance of this public GitHub repository.


This GIT repository contains android started projects that will help developers get started with SMP OData SDK SP05.

The How To... guides associated to each project can be found on http://scn.sap.com/docs/DOC-58677#Android and they should be completed in the following order:
1.How To... Enable user On-boarding using MAF Logon with Template Project (Android): explains how to on-board users with MAF logon 
2.How To...Consume OData Services in Online Mode (Android) [UsingOnlineAPI project]: explains how to open an online store, read, create, update and delete entities in online mode
3.How To...Consume OData Services in Offline Mode (Android) [UsingOfflineAPI project]: explains how to open an offline store, read, create, update and delete entities in offline mode
4.How to... Handle Synchronization Errors (Android) [OfflineErrorHandling project]: explains how to handle synchronization errors with the offline store
5.How To...Write and Upload Client Logs to the SMP Server (Android) [ClientLogs]: explains how to write client logs and upload them into the SMP server for further analysis

SMP APPLICATION CONFIGURATION
1. Application ID: All projects you have configured an application in SMP 3.0 called "com.sap.flight" 
2. Backend Endpoint URL: The SMP backend URL must point to the RMTSAMPLEFLIGHT service that is available in all SAP Gateway installations (i.e http://gateway server:gateway port/sap/opu/odata/iwfnd/RMTSAMPLEFLIGHT/)
For more information on how to create an application configuration, please visit http://help.sap.com/saphelp_smp304svr/helpdata/en/7c/2a3c6070061014bc14ea56b0ab4883/content.htm

ADDITIONAL LIBRARY
For some projects you may need to add the android-support-v4.jar file
1. Right-click on your android project and select Android Tools -> Add Support Library
2. Read and accept the license and click install
3. the android-support-v4.jar file will appear in the libs folder of your project
