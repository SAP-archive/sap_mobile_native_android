package com.sap.dcode.online.types;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class Agency implements Parcelable{
	private String agencyId;
	private String agencyName;
	private String street;
	private String city;
	private String country;
	private String website;
	private String editResourceURL;

	
	public Agency(String agencyId) {
		super();
		this.agencyId = agencyId;
	}
	
	public Agency(Parcel in){
		readFromParcel(in);
	}
	
	public boolean isInitialized(){
		return (!TextUtils.isEmpty(this.agencyId));
	}
	
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	public String getAgencyName() {
		return agencyName;
	}
	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	
	public String getEditResourceURL() {
		return editResourceURL;
	}

	public void setEditResourceURL(String editResourceURL) {
		this.editResourceURL = editResourceURL;
	}



	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.agencyId);
		dest.writeString(this.agencyName);
		dest.writeString(this.street);
		dest.writeString(this.city);
		dest.writeString(this.country);
		dest.writeString(this.website);
		dest.writeString(this.editResourceURL);

	}

   public void readFromParcel(Parcel in)
   {
       this.agencyId = in.readString();
       this.agencyName = in.readString();
       this.street = in.readString();
       this.city = in.readString();
       this.country = in.readString();
       this.website = in.readString();
       this.editResourceURL = in.readString();

   }
	
   public static final Parcelable.Creator<Agency> CREATOR = new Parcelable.Creator<Agency>()
   {
       @Override
       public Agency createFromParcel(Parcel in)
       {
           return new Agency(in);
       }

       @Override
       public Agency[] newArray(int size)
       {
           return new Agency[size];
       }
   };
}
