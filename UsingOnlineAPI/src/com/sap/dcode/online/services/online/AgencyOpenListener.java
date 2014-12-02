package com.sap.dcode.online.services.online;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.online.OnlineODataStore;
import com.sap.smp.client.odata.online.OnlineODataStore.OpenListener;

public class AgencyOpenListener implements OpenListener {
	private static AgencyOpenListener instance;

	private final CountDownLatch latch = new CountDownLatch(1);
	OnlineODataStore store;
	Exception error;
	
	private AgencyOpenListener() {
	}

	/**
	 * @return FlightOpenListener
	 */
	public static AgencyOpenListener getInstance() {
		if (instance == null) {
			instance = new AgencyOpenListener();
		}
		return instance;
	}


	@Override
	public void storeOpenError(ODataException e) {
		this.error = e;
		latch.countDown();
	}

	@Override
	public void storeOpened(OnlineODataStore store) {
		this.store = store;
		latch.countDown();
	}

	public synchronized boolean finished() {
		return (store != null || error != null);
	}

	public synchronized Exception getError() {
		return error;
	}

	public synchronized OnlineODataStore getStore() {
		return store;
	}

	/**
	 * Waits for the completion of the asynchronous process. In case this listener is not invoked within 30 seconds then it fails with an exception.
	 */
	public void waitForCompletion() {
		try {
			if (!latch.await(30, TimeUnit.SECONDS))
				throw new IllegalStateException("Open listener was not called within 30 seconds.");
			else if (!finished())
				throw new IllegalStateException("Open listener is not in finished state after having completed successfully");
		} catch (InterruptedException e) {
			throw new IllegalStateException("Open listener waiting for results was interrupted.", e);
		}
	}

}
