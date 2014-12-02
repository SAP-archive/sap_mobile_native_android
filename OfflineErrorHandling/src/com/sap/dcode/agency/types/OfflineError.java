package com.sap.dcode.agency.types;

import android.os.Parcel;
import android.os.Parcelable;

public class OfflineError  implements Parcelable {
	private String requestMethod;
	private String requestBody;
	private String httpStatusCode;
	private String message;
	private String customTag;
	private String requestURL;
	private String editResourcePath;

	public OfflineError(Parcel in){
		readFromParcel(in);
	}
	
	public OfflineError(String message) {
		super();
		this.message = message;
	}

	
	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public String getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(String httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getCustomTag() {
		return customTag;
	}

	public void setCustomTag(String customTag) {
		this.customTag = customTag;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getEditResourcePath() {
		return editResourcePath;
	}

	public void setEditResourcePath(String editResourcePath) {
		this.editResourcePath = editResourcePath;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.requestMethod);
		dest.writeString(this.requestBody);
		dest.writeString(this.httpStatusCode);
		dest.writeString(this.message);
		dest.writeString(this.customTag);
		dest.writeString(this.requestURL);
		dest.writeString(this.editResourcePath);
		
	}

	   public void readFromParcel(Parcel in)
	   {
	       this.requestMethod = in.readString();
	       this.requestBody = in.readString();
	       this.httpStatusCode = in.readString();
	       this.message = in.readString();
	       this.customTag = in.readString();
	       this.requestURL = in.readString();
	       this.editResourcePath = in.readString();
	   }
		
	   public static final Parcelable.Creator<OfflineError> CREATOR = new Parcelable.Creator<OfflineError>()
	   {
	       @Override
	       public OfflineError createFromParcel(Parcel in)
	       {
	           return new OfflineError(in);
	       }

	       @Override
	       public OfflineError[] newArray(int size)
	       {
	           return new OfflineError[size];
	       }
	   };


}
