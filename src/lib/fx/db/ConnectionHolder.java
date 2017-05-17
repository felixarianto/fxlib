package lib.fx.db;

import java.sql.Connection;


public class ConnectionHolder {

	protected Connection mConnection;
	protected long mCreatedTime;
	protected String mTag;
	
	protected ConnectionHolder(Connection pConnextion) {
		mConnection = pConnextion;
		mCreatedTime = System.currentTimeMillis(); 
	}
	
	public void setTag(String pTag) {
		mTag = pTag != null ? pTag : "";
	}
	
	public final String getTag() {
		return mTag;
	}
		
}
