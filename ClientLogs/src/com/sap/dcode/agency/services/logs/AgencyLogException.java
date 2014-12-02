package com.sap.dcode.agency.services.logs;

public class AgencyLogException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AgencyLogException(String errorMessage) {
        super(errorMessage);
    }

	public AgencyLogException(Throwable throwable) {
        super(throwable);
    }

}
