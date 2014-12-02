package com.sap.dcode.agency.services;

public enum Operation {
	CreateAgency(10),
	DeleteAgency(20),
	UpdateAgency(30),
	OfflineFlush(40),
	OfflineRefresh(50),
	DeleteErrorArchive(80);
	
	private final int value;
	
	Operation(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
