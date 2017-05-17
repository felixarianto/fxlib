package lib.fx.db;


public class FetchException extends Exception {
	
	public static final int STATE_EXECUTE_QUERY = 1;
	public static final int STATE_IN_LOOP = 2;
	
	private int mState;
	
	FetchException() {
		super ();
		mState = STATE_EXECUTE_QUERY;
	}
	
	public FetchException(Exception p_exception, int p_state) {
		super (p_exception);
		mState = p_state;
	}
	
	public final int getState() {
		return mState;
	}
	
}