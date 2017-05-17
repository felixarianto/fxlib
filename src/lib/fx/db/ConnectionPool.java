package lib.fx.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author angga
 */
public class ConnectionPool {
    
    public static final String TAG = "ConnectionPool";
    
//    public static final String USERNAME = "root";
//    public static final String PASSWORD = "f006efc56";
//    public static final String URL      = "jdbc:mysql://localhost/100CHAT";    
    
//    public static final String USERNAME = "bw_chat";
//    public static final String PASSWORD = "bwchat";
//    public static final String URL      = "jdbc:mysql://192.168.0.3/bw_chat";
    
//    public static final String USERNAME = "bwchat";
//    public static final String PASSWORD = "bwchat";
//    public static final String URL      = "jdbc:mysql://202.158.56.213/bw_chat";
//    public static final String URL      = "jdbc:mysql://localhost/bw_chat";

//    public static final int USED_CONNECTION = 2;
//    public static final int IDLE_CONNECTION = 2;
//    public static final int FREE_CONNECTION = 2;
    
    public ConnectionPool() {
		mClazz = null;
		mUsername = null;
		mPassword = null;
		mURL = null;
		mMinConnectionSize = DEFAULT_MIN_CONNECTION_SIZE;
		mMaxConnectionSize = DEFAULT_MAX_CONNECTION_SIZE;
		FREE_CONNECTION_LIST = new LinkedBlockingDeque<>();
		USED_CONNECTION_MAP = new ConcurrentHashMap<>();
//		DEAD_CONNECTION_LIST = new LinkedBlockingDeque<>();
		mInitialized = false;
		mConnectionLifeTime = DEFAULT_CONNECTION_LIFETIME;
        mSweepThread = getSweeperThread();
        mSweepInterval = DEFAULT_SWEEP_INTERVAL;
        mDestroyed = false;
    }
    
    public ConnectionPool(String p_class, String p_url, String p_username, String p_password) {
        mClazz = p_class;
        mUsername = p_username;
        mPassword = p_password;
		mURL = p_url;
		mMinConnectionSize = DEFAULT_MIN_CONNECTION_SIZE;
		mMaxConnectionSize = DEFAULT_MAX_CONNECTION_SIZE;
		FREE_CONNECTION_LIST = new LinkedBlockingDeque<>();
		USED_CONNECTION_MAP = new ConcurrentHashMap<>();
//		DEAD_CONNECTION_LIST = new LinkedBlockingDeque<>();
        mInitialized = false;
        mConnectionLifeTime = DEFAULT_CONNECTION_LIFETIME;
        mConnectionSweepAfterDeadTime = DEFAULT_CONNECTION_SWEEPAFTERDEADTIME;
        mSweepThread = getSweeperThread();
        mSweepInterval = DEFAULT_SWEEP_INTERVAL;
        mDestroyed = false;
    }

    /*
     * 
     * 
     * 
     */
    private String  mClazz;
    private String  mUsername;
    private String  mPassword;
    private String  mURL;
    public void setClazz(String p_clazz) {
        mClazz = p_clazz;
    }    
    public void setURL(String p_url) {
        mURL = p_url;
    }    
    public void setPassword(String p_url) {
        mPassword = p_url;
    }    
    public void setUsername(String p_url) {
        mUsername = p_url;
    }    
    public final String getClazz() {
    	return mClazz;
    }
    public final String getUsername() {
    	return mUsername;
    }
    public final String getPassword() {
    	return mPassword;
    }
    public final String getURL() {
    	return mURL;
    }
    
    /*
     * 
     * 
     * 
     */
    private final LinkedBlockingDeque<ConnectionHolder> FREE_CONNECTION_LIST;
//    private final LinkedBlockingDeque<ConnectionHolder> DEAD_CONNECTION_LIST; // Low Overhead, 
    private final ConcurrentHashMap<Connection, ConnectionHolder> USED_CONNECTION_MAP;
	private int mMinConnectionSize;
	private int mMaxConnectionSize;
	public static final int DEFAULT_MIN_CONNECTION_SIZE = 0;
	public static final int DEFAULT_MAX_CONNECTION_SIZE = 5;
    public boolean setMaxConnectionSize(int p_size) {
    	boolean result = false;
    	if (p_size < 0) {
    		p_size = 0;
    	}
        if (p_size >= mMinConnectionSize) {
        	mMaxConnectionSize = p_size;
        }
        return result;
    }    
    public boolean setMinConnectionSize(int p_size) {
    	boolean result = false;
    	if (p_size < 0) {
    		p_size = 0;
    	}
    	if (p_size <= mMaxConnectionSize) {
    		mMinConnectionSize = p_size;
        }
        mMinConnectionSize = p_size < 0 ? 0 : p_size;
        return result;
    }
    public final int getFreeConnectionSize() {
        return FREE_CONNECTION_LIST.size();
    }    
    public final int getUsedConnectionSize() {
        return USED_CONNECTION_MAP.size();
    }
    public final int getDeadConnectionSize() {
//        return DEAD_CONNECTION_LIST.size();
        return 0;
    }
    public final int getMaxConnectionSize() {
        return mMaxConnectionSize;
    }
    public final int getMinConnectionSize() {
        return mMinConnectionSize;
    }
    
    /*
     * 
     * 
     * 
     */
    public final ArrayList<String> getUsedConnectionTagList() {
    	ArrayList<String> result = new ArrayList<>(); 
    	Collection<ConnectionHolder> keys = USED_CONNECTION_MAP.values();
    	for (ConnectionHolder holder : keys) {
    		if (!result.contains(holder.getTag())) {
    			 result.add(holder.getTag());
    		}
    	}
        return result;
    }
    public final ArrayList<String> getFreeConnectionTagList() {
    	ArrayList<String> result = new ArrayList<>(); 
    	for (ConnectionHolder holder : FREE_CONNECTION_LIST) {
    		if (!result.contains(holder.getTag())) {
    			 result.add(holder.getTag());
    		}
    	}
        return result;
    }
    public final ArrayList<String> getDeadConnectionTagList() {
//    	ArrayList<String> result = new ArrayList<>(); 
//    	for (ConnectionHolder holder : DEAD_CONNECTION_LIST) {
//    		if (!result.contains(holder.getTag())) {
//    			 result.add(holder.getTag());
//    		}
//    	}
//        return result;
    	return new ArrayList<String>();
    }
    
    /*
     * 
     * 
     * 
     */
    public final int getUsedConnectionTagSize(String p_tag) {
    	int size = 0;
    	p_tag = p_tag == null ? "" : p_tag;
    	Collection<ConnectionHolder> keys = USED_CONNECTION_MAP.values();
    	for (ConnectionHolder holder : keys) {
    		if (holder.getTag().equals(p_tag)) {
    			size++;
    		}
    	}
        return size;
    }
    public final int getFreeConnectionTagSize(String p_tag) {
    	int size = 0;
    	p_tag = p_tag == null ? "" : p_tag;
    	for (ConnectionHolder holder : FREE_CONNECTION_LIST) {
    		if (holder.getTag().equals(p_tag)) {
    			size++;
    		}
    	}
        return size;
    }
    public final int getDeadConnectionTagSize(String p_tag) {
//    	int size = 0;
//    	p_tag = p_tag == null ? "" : p_tag;
//    	for (ConnectionHolder holder : DEAD_CONNECTION_LIST) {
//    		if (holder.getTag().equals(p_tag)) {
//    			size++;
//    		}
//    	}
//        return size;
    	return 0;
    }
    
    /*
     * 
     * 
     * 
     */
    private boolean mInitialized; 
    boolean mDestroyed;
    public synchronized final void create() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (!mInitialized) {
//        	System.out.println("CONNPOOL >> CREATE 1");
            mInitialized = true;
//        	System.out.println("CONNPOOL >> CREATE 2");
            Class.forName(mClazz).newInstance();
//        	System.out.println("CONNPOOL >> CREATE 3");
            USED_CONNECTION_MAP.clear();
            FREE_CONNECTION_LIST.clear();
//          DEAD_CONNECTION_LIST.clear();
            for (int x = 0; x < mMinConnectionSize; x++) {
//            	System.out.println("CONNPOOL >> CREATE 4." + x);
            	Connection connection = DriverManager.getConnection(mURL, mUsername, mPassword);
            	           connection.setAutoCommit(false);
//              System.out.println("CONNPOOL >> CREATE 5." + x);
                FREE_CONNECTION_LIST.addLast(new ConnectionHolder(connection));
            }
//        	System.out.println("CONNPOOL >> CREATE 6");
	        mSweepThread.start();
        }
        else {
//        	System.out.println("CONNPOOL >> CREATE FAILED");
        }
    }    
    public synchronized final void destroy() {
    	mDestroyed = true;
    	if (mSweepThread != null) {
    		mSweepThread.interrupt();
    	}
//    	while (USED_CONNECTION_MAP.size() > 0) {
//			for (ConnectionHolder ch : FREE_CONNECTION_LIST) {
//				try {
//					ch.mConnection.close();
//				} catch (Exception e) {
//				}
//			}
//			FREE_CONNECTION_LIST.clear();
//			for (ConnectionHolder ch : DEAD_CONNECTION_LIST) {
//				try {
//					ch.mConnection.close();
//				} catch (Exception e) {
//				}
//			}
//			DEAD_CONNECTION_LIST.clear();
//			try {
//				Thread.sleep(1000);
//			} catch (Exception e) {
//			}
//		}
    }    
    public final boolean isInitialized() {
        return mInitialized;
    }
    public final boolean isDestroyed() {
    	return mDestroyed;
    }

    /*
     * 
     * 
     * 
     */
    private int mConnectionLifeTime;
    public static final int DEFAULT_CONNECTION_LIFETIME = 60000 * 30;
    public void setConnectionLifeTime(int p_lifetime) {
    	mConnectionLifeTime = p_lifetime;
    }
    public final int getConnectionLifeTime() {
    	return mConnectionLifeTime;
    }
    boolean isDeadTime(ConnectionHolder p_holder) {
    	return (System.currentTimeMillis() - p_holder.mCreatedTime) > mConnectionLifeTime;
    }
    private int mConnectionSweepAfterDeadTime;
    public static final int DEFAULT_CONNECTION_SWEEPAFTERDEADTIME = 60000 * 15;
    public void setConnectionSweepAfterDeadTime(int p_sweeptime) {
    	mConnectionSweepAfterDeadTime = p_sweeptime;
    }
    public final int getConnectionSweepAfterDeadTime() {
    	return mConnectionSweepAfterDeadTime;
    }
    
    /*
     * 
     * 
     * 
     */
    private Thread mSweepThread;
    int mSweepInterval; 
    public static final int DEFAULT_SWEEP_INTERVAL = 60000;
    public void setSweepInterval(int p_interval) {
    	mSweepInterval = p_interval;
    }
    public final int getSweepInterval() {
    	return mSweepInterval;
    }
    Thread getSweeperThread() {
    	return new Thread(new Runnable() {
			@Override
			public void run() {
				while (!mDestroyed) {
					try {
						Thread.sleep(mSweepInterval);
					} catch (InterruptedException ie) {
						
					}
					if (!mDestroyed) {
						try {
							sweep();
						} catch (Throwable t) {
							if (mThrowListener != null) {
								mThrowListener.onThrow(ConnectionPool.this, t);
							}
						}
//						try {
//							ConnectionHolder holder = null;
//							boolean run = true;
//							while (run) {
//								holder = DEAD_CONNECTION_LIST.pollFirst();
//								if (holder != null) {
//									try {
//										holder.mConnection.close();
//									} catch (SQLException sqle) {
//									}
//								}
//								else {
//									run = false;
//								}
//							}
//							holder = null;
//						} catch (Throwable t) {
//							if (mThrowListener != null) {
//								mThrowListener.onThrow(ConnectionPool.this, t);
//							}
//						} 
//						try {
//							ConnectionHolder holder = null;
//							boolean run = true;
//							while (run) {
//								holder = DEAD_CONNECTION_LIST.pollFirst();
//								if (holder != null) {
//									try {
//										holder.mConnection.close();
//									} catch (SQLException sqle) {
//									}
//								}
//								else {
//									run = false;
//								}
//							}
//							holder = null;
//						} catch (Throwable t) {
//							if (mThrowListener != null) {
//								mThrowListener.onThrow(ConnectionPool.this, t);
//							}
//						} 
					}
				}
			}
		});
    }
    public void sweep() {
    	Set<Connection> keys = USED_CONNECTION_MAP.keySet();
    	for (Connection key : keys) {
    		ConnectionHolder holder = USED_CONNECTION_MAP.get(key);
    		if (holder != null && isSweepTime(holder)) {
//    			DEAD_CONNECTION_LIST.addLast(holder);
    			try {
	    			USED_CONNECTION_MAP.remove(holder);
	    			holder.mConnection.close();
    			} catch (Throwable t) {
    				if (mThrowListener != null) {
						mThrowListener.onThrow(ConnectionPool.this, t);
					}
    			}
    		}
    	}
    }
    public void sweep(String p_tag) {
    	p_tag = p_tag == null ? "" : p_tag;
    	Set<Connection> keys = USED_CONNECTION_MAP.keySet();
    	for (Connection key : keys) {
    		ConnectionHolder holder = USED_CONNECTION_MAP.get(key);
    		if (holder != null && isSweepTime(holder) && holder.getTag().equals(p_tag)) {
//   				DEAD_CONNECTION_LIST.addLast(holder);
    			try {
	    			USED_CONNECTION_MAP.remove(holder);
	    			holder.mConnection.close();
    			} catch (Throwable t) {
    				if (mThrowListener != null) {
						mThrowListener.onThrow(ConnectionPool.this, t);
					}
    			}
    		}
    	}
    }
    boolean isSweepTime(ConnectionHolder p_holder) {
    	return (System.currentTimeMillis() - p_holder.mCreatedTime) > (mConnectionLifeTime + mConnectionSweepAfterDeadTime);
    }
    
    /*
     * 
     * 
     * 
     */
    ThrowListener mThrowListener;    
    public void setThrowListener(ThrowListener p_listener) {
    	mThrowListener = p_listener;
    }    
    public final ThrowListener getThrowListener() {
    	return mThrowListener;
    }    
    public static interface ThrowListener {    	
    	
    	public void onThrow(ConnectionPool p_pool, Throwable p_t);    	
    	
    }
    
    /*
     * 
     * 
     * 
     */
//	public final Connection getConnection() throws SQLException {
//		boolean createNew = true;
//		ConnectionHolder result = FREE_CONNECTION_LIST.pollFirst();
//		if (result != null) {
//			if (isDeadTime(result) || result.mConnection.isClosed()) {
//				DEAD_CONNECTION_LIST.addLast(result);
//			}
//			else {
//				USED_CONNECTION_MAP.put(result.mConnection, result);
//				createNew = false;
//			}
//		}
//		if (createNew && USED_CONNECTION_MAP.size() < mMaxConnectionSize) {
//			Connection connection = DriverManager.getConnection(mURL, mUsername, mPassword);
//			 		   connection.setAutoCommit(false);
//			result = new ConnectionHolder(connection);
//			if (result != null) {
//				USED_CONNECTION_MAP.put(result.mConnection, result);
//			}
//		}
//		return result == null ? null : result.mConnection;
//	}
	public final Connection getConnection(String p_tag) throws SQLException {
		boolean createNew = true;
		ConnectionHolder result = FREE_CONNECTION_LIST.pollFirst();
		if (result != null) {
			result.setTag(p_tag);
			if (isDeadTime(result)) {
//				DEAD_CONNECTION_LIST.addLast(result);
				try {
					result.mConnection.close();
				} catch (Throwable t) {
    				if (mThrowListener != null) {
						mThrowListener.onThrow(ConnectionPool.this, t);
					}
    			}
			}
			else {
				if (!result.mConnection.isClosed()) {
					USED_CONNECTION_MAP.put(result.mConnection, result);
					createNew = false;
				}
			}
		}
		if (createNew && USED_CONNECTION_MAP.size() < mMaxConnectionSize) {
			Connection connection = DriverManager.getConnection(mURL, mUsername, mPassword);
			 		   connection.setAutoCommit(false);
			result = new ConnectionHolder(connection);
			result.setTag(p_tag);
			USED_CONNECTION_MAP.put(result.mConnection, result);
		}
		return result == null ? null : result.mConnection;
	}
	public final void releaseConnection(Connection p_connection) throws SQLException {
		if (p_connection != null) {
			if (p_connection.isClosed()) {
				USED_CONNECTION_MAP.remove(p_connection);
			}
			else {
				ConnectionHolder holder = USED_CONNECTION_MAP.remove(p_connection);
				if (holder != null) {
					if (isDeadTime(holder) || FREE_CONNECTION_LIST.size() >= mMaxConnectionSize) {
//						DEAD_CONNECTION_LIST.addLast(holder);
						try {
							holder.mConnection.close();
						} catch (Throwable t) {
		    				if (mThrowListener != null) {
								mThrowListener.onThrow(ConnectionPool.this, t);
							}
		    			}
					}
					else {
						FREE_CONNECTION_LIST.addLast(holder);
					}
				}
			}
		}
	}
    
//    public final Object GET_SYNC = new Object();    
//    public final Connection getConnection() throws SQLException {
//        ConnectionHolder result = null;
//        synchronized (GET_SYNC) {
//        	while (result == null && !FREE_CONNECTION_LIST.isEmpty()) {
//        		ConnectionHolder holder = FREE_CONNECTION_LIST.pollFirst();
//        		if (holder != null) {
//        			if (isDeadTime(holder)) {
//        				DEAD_CONNECTION_LIST.addLast(holder);
//        			}
//        			else if (holder.mConnection.isClosed()) {
//        				DEAD_CONNECTION_LIST.addLast(holder);
//        			}
//        			else {
//	        			USED_CONNECTION_MAP.put(holder.mConnection, holder);
//	        			result = holder;
//        			}
//        			holder = null;
//        		}
//        	}
//        }
//        if (result == null) {
//        	if (USED_CONNECTION_MAP.size() < mMaxConnectionSize) {
//        		Connection connection = DriverManager.getConnection(mURL, mUsername, mPassword);
//        				   connection.setAutoCommit(false);
//	        	result = new ConnectionHolder(connection);
//				if (result != null) {
//					USED_CONNECTION_MAP.put(result.mConnection, result);
//				}
//        	}
//        }
//        return result == null ? null : result.mConnection;
//    }    
//    public final void releaseConnection(Connection p_connection) throws SQLException {
//        if (p_connection != null) {
//            if (p_connection.isClosed()) {
//            	synchronized (GET_SYNC) {
//	            	USED_CONNECTION_MAP.remove(p_connection);
//            	}
//            }
//            else {
//				synchronized (GET_SYNC) {
//					ConnectionHolder holder = USED_CONNECTION_MAP.remove(p_connection);
//					if (holder != null) {
//						if (isDeadTime(holder) || FREE_CONNECTION_LIST.size() >= mMaxConnectionSize) {
//							DEAD_CONNECTION_LIST.addLast(holder);
//						}
//						else {
//							FREE_CONNECTION_LIST.addLast(holder);
//						}
//					}
//				}
//            }
//        }
//    } 
            
}