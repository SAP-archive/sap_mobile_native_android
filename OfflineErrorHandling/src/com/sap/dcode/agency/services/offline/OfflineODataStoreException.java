package com.sap.dcode.agency.services.offline;

public class OfflineODataStoreException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OfflineODataStoreException(String errorMessage) {
        super(errorMessage);
    }

	public OfflineODataStoreException(Throwable throwable) {
        super(throwable);
    }

}
