package lib.fx.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLTransientConnectionException;
import java.sql.ResultSet;
import java.util.ArrayList;
import lib.fx.logger.Log;

public class SQL extends Database {
	
    public static final String TAG = "SQL";
    
	private final ConnectionPool mConnectionPool;

	private int mConnectionRetryLimit = 0;
	private int mConnectionRetrySleep = 1000;
	
	public SQL(ConnectionPool p_pool) {
		mConnectionPool = p_pool;
	}
	
	public final void create() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		mConnectionPool.create();
	}
	
	public final void setConnectionRetryLimit(int pLimit) {
		mConnectionRetryLimit = pLimit < 0 ? 0 : pLimit;
	}
	
	public final int getConnectionRetryLimit() {
		return mConnectionRetryLimit;
	}
	
	public final void setConnectionRetrySleepTime(int pTime) {
		mConnectionRetrySleep = pTime < 1000 ? 1000 : pTime;
	}
	
	public final int getConnectionRetrySleepTime() {
		return mConnectionRetrySleep;
	}
	
	public final void setMaxConnectionSize(int p_size) {
		mConnectionPool.setMaxConnectionSize(p_size);
	}
	
	public final void setMinConnectionSize(int p_size) {
		mConnectionPool.setMinConnectionSize(p_size);
	}
	
	public final ArrayList<String> getDeadConnectionTagList() {
		return mConnectionPool.getDeadConnectionTagList();
	}
	
	public final ArrayList<String> getFreeConnectionTagList() {
		return mConnectionPool.getFreeConnectionTagList();
	}
	
	public final ArrayList<String> getUsedConnectionTagList() {
		return mConnectionPool.getUsedConnectionTagList();
	}
	
	public final int getUsedConnectionTagSize(String p_tag) {
		return mConnectionPool.getUsedConnectionTagSize(p_tag);
	}
	
	public final int getFreeConnectionTagSize(String p_tag) {
		return mConnectionPool.getFreeConnectionTagSize(p_tag);
	}
	
	public final int getDeadConnectionTagSize(String p_tag) {
		return mConnectionPool.getDeadConnectionTagSize(p_tag);
	}
	
	public final void sweep() {
		mConnectionPool.sweep();
	}
	
	public final void setConnectionLifeTime(int p_lifeTime) {
		mConnectionPool.setConnectionLifeTime(p_lifeTime);
	}
	
	public final int getConnectionLifeTime() {
		return mConnectionPool.getConnectionLifeTime();
	}
	
	public final int getDeadConnectionSize() {
		return mConnectionPool.getDeadConnectionSize();
	}
	
	public final int getFreeConnectionSize() {
		return mConnectionPool.getFreeConnectionSize();
	}
	
	public final int getUsedConnectionSize() {
		return mConnectionPool.getUsedConnectionSize();
	}
	
	public final Connection getConnection(String p_tag) {
		Connection connection = null;
		try {
			int retry = 0;
			while ((connection = mConnectionPool.getConnection(p_tag)) == null) {
				if (retry++ < mConnectionRetryLimit) {
					try {
						Thread.sleep(mConnectionRetrySleep);
					} catch (InterruptedException ie) {
					}
				}
				else {
					break;
				}
			}
		} catch (SQLException sqle) {
			Log.e(TAG, sqle);
		}
		return connection;
	}
	
	public final void releaseConnection(Connection p_connection) {
		try {
			mConnectionPool.releaseConnection(p_connection); 
		} catch (SQLException sqle) {
			Log.e(TAG, sqle);
		}
	}
	
	public final void releaseConnection(Connection p_connection, boolean p_close) {
		if (p_close) {
			try {
				if (p_connection != null) {
					p_connection.close();
				}
			} catch (SQLException sqle) {
			}
		}
		try {
			mConnectionPool.releaseConnection(p_connection); 
		} catch (SQLException sqle) {
			Log.e(TAG, sqle);
		}
	}
	
	public final void commit(Connection p_connection) {
		try {
			if (p_connection != null) {
				p_connection.commit(); 
			}
		} catch (SQLException sqle) {
			Log.e(TAG, sqle);
		}
	}
	
	public final void commit(Connection p_connection, boolean p_commit) {
		try {
			if (p_connection != null) {
				if (p_commit) {
					p_connection.commit();
				} 
				else {
					p_connection.rollback();
				}
			}
		} catch (SQLException sqle) {
			Log.e(TAG, sqle);
		}
	}
	
	public final void rollback(Connection p_connection) {
		try {
			if (p_connection != null) {
				p_connection.rollback(); 
			}
		} catch (SQLException sqle) {
			Log.e(TAG, sqle);
		}
	}
	
	@Override
	public Object[] getRecord(Connection p_connection, String p_query) {
		Object[] object = null;
		try {
			object = super.getRecord(p_connection, p_query);
		} catch (SQLException sqle) {
			Log.e(TAG, sqle);
		}
		return object;
	}
	
	public int[] getRecordAsInteger(Connection p_connection, String p_query) {
		int[] result = null;
        try {
            if (p_connection != null && p_query != null) {
                ResultSet rset = null;
                try {
                    rset = p_connection.createStatement().executeQuery(p_query);
                    if (rset.next()) {
                        int columns = rset.getMetaData().getColumnCount();
                        result = new int[columns];
                        for (int x = 0; x < columns; x++) {
                            result[x] = rset.getInt(x + 1);
                        }
                    }
                }
                catch (SQLException sqle) {
                    close(rset);
                    throw sqle;
                }
                close(rset);
            }
        }
        catch (SQLException sqle) {
            Log.e(TAG, sqle);
        }
		return result;
	}
    
    public String[] getRecordAsString(Connection p_connection, String p_query) {
		String[] result = null;
        try {
            if (p_connection != null && p_query != null) {
                ResultSet rset = null;
                try {
                    rset = p_connection.createStatement().executeQuery(p_query);
                    if (rset.next()) {
                        int columns = rset.getMetaData().getColumnCount();
                        result = new String[columns];
                        for (int x = 0; x < columns; x++) {
                            result[x] = rset.getString(x + 1);
                        }
                    }
                }
                catch (SQLException sqle) {
                    close(rset);
                    throw sqle;
                }
                close(rset);
            }
        }
        catch (SQLException sqle) {
            Log.e(TAG, sqle);
        }
		return result;
	}
	
	@Override
	public void fetch(Connection p_connection, String p_query, int p_fetchId, FetchListener p_listener) throws FetchException {
		boolean retry = false;
		try {
			super.fetch(p_connection, p_query, p_fetchId, p_listener);
		} catch (FetchException e) {
			if (e.getState() == FetchException.STATE_EXECUTE_QUERY) {
				Throwable t = e.getCause();
				if (t != null) {
					retry = t instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException ||
							t instanceof com.mysql.jdbc.CommunicationsException ||
							t instanceof SQLNonTransientConnectionException ||
							t instanceof SQLTransientConnectionException;
				}
			}
			if (!retry) {
				throw e;
			}
		} catch (Error e) {
			throw e;
		}
		if (retry) {
			Connection connection = getConnection(TAG);
			try {
				super.fetch(connection, p_query, p_fetchId, p_listener);
			} catch (Error | FetchException e) {
				releaseConnection(connection);
				throw e;	
			}
			releaseConnection(connection);
		}
	}
	public int insert(Connection p_connection, String p_insertQuery, String p_updateQuery) {
		int result = 0;
		boolean constraint = false;
		try {
			result = executeUpdate(p_connection, p_insertQuery);
		} catch (SQLIntegrityConstraintViolationException sqlicve) {
			constraint = true;
			e(null, sqlicve);
		} catch (SQLException sqle) {
			w("SQL3", p_insertQuery + " --> " + p_updateQuery);
			e(null, sqle);
		}
		if (constraint && p_updateQuery != null) {
			try {
				result = -executeUpdate(p_connection, p_updateQuery);
			} catch (SQLException sqle) {
				w("SQL6", p_insertQuery + " --> " + p_updateQuery);
				e(null, sqle);
			}
		}
        return result;
	}
    public int insertAndThrow(Connection p_connection, String p_insertQuery, String p_updateQuery) throws SQLException {
		int result = 0;
		boolean constraint = false;
		try {
			result = executeUpdate(p_connection, p_insertQuery);
		} catch (SQLIntegrityConstraintViolationException sqlicve) {
			constraint = true;
            if (p_updateQuery == null) {
                throw sqlicve;
            }
		} catch (SQLException sqle) {
			w("SQL3", p_insertQuery + " --> " + p_updateQuery);
			throw sqle;
		} 
		if (constraint) {
			try {
				result = -executeUpdate(p_connection, p_updateQuery);
			} catch (SQLException sqle) {
				w("SQL6", p_insertQuery + " --> " + p_updateQuery);
				throw sqle;
			}
		}
        return result;
	}
    public int update(Connection p_connection, String p_updateQuery, String p_insertQuery) {
        int result;
        try {
            result = executeUpdate(p_connection, p_updateQuery);
        } catch (SQLException sqle) {
            result = 0;
			w("SQL6", p_insertQuery + " --> " + p_updateQuery);
            e(null, sqle);
        }
        if (result == 0 && p_insertQuery != null) {
            try {
                result = -executeUpdate(p_connection, p_insertQuery);
	        } catch (SQLException sqle) {
	            result = 0;
				w("SQL3", p_insertQuery + " --> " + p_updateQuery);
	            e(null, sqle);
	        }
        }
        return result;
    }
    public int updateOrThrow(Connection p_connection, String p_updateQuery, String p_insertQuery) throws SQLException {
        int result = 0;
        try {
            result = executeUpdate(p_connection, p_updateQuery);
        } catch (SQLException sqle) {
			w("SQL6", p_insertQuery + " --> " + p_updateQuery);
            if (p_insertQuery == null) {
                throw sqle;
            }
        }
        if (result == 0 && p_insertQuery != null) {
        	try {
        		result = -executeUpdate(p_connection, p_insertQuery);
        	} catch (SQLException sqle) {
				w("SQL3", p_insertQuery + " --> " + p_updateQuery);
				throw sqle;
			}
        }
        return result;
    }
	
	/*
	 * 
	 * 
	 * 
	 */
    void e(String p_metalog, Throwable p_throwable) {
    	Log.e(TAG, p_metalog, p_throwable);
    }
    void w(String p_metalog, String p_message) {
    	Log.w(TAG, p_metalog, p_message);
    }
    
    
    
    
//	public static final String TAG = "SQL";
//	
//	private ConnectionManager mConnectionManager;
//	
//	public SQL(ConnectionManager p_connectionManager) {
//		mConnectionManager = p_connectionManager;
//	}
//	
//	public final Connection getConnection() {
//		Connection connection = null;
//		try {
//			connection = mConnectionManager.getConnection();
//		} catch (SQLException sqle) {
//			Log.e(TAG, sqle);
//		}
//		return connection;
//	}
//	
//	public final void releaseConnection(Connection p_connection) {
//		try {
//			mConnectionManager.releaseConnection(p_connection); 
//		} catch (SQLException sqle) {
//			Log.e(TAG, sqle);
//		}
//	}
//	
//	public final void commit(Connection p_connection) {
//		try {
//			if (p_connection != null) {
//				p_connection.commit(); 
//			}
//		} catch (SQLException sqle) {
//			Log.e(TAG, sqle);
//		}
//	}
//	
//	public final void commit(Connection p_connection, boolean p_commit) {
//		try {
//			if (p_connection != null) {
//				if (p_commit) {
////					long T0 = System.nanoTime();
//					p_connection.commit();
////					long T1 = System.nanoTime();
////					System.out.println("[[ SQL ]] COMMIT " + ((T1 - T0) / 1000));
//				} 
//				else {
//					p_connection.rollback();
//				}
//			}
//		} catch (SQLException sqle) {
//			Log.e(TAG, sqle);
//		}
//	}
//	
//	public final void rollback(Connection p_connection) {
//		try {
//			if (p_connection != null) {
//				p_connection.rollback(); 
//			}
//		} catch (SQLException sqle) {
//			Log.e(TAG, sqle);
//		}
//	}
//	
//	@Override
//	public Object[] getRecord(Connection p_connection, String p_query) {
//		Object[] object = null;
//		try {
//                       object = super.getRecord(p_connection, p_query);
//		} catch (SQLException sqle) {
//			Log.e(TAG, sqle);
//		}
//		return object;
//	}
//	
////	@Override
////	public void fetchAndCatch(Connection p_connection, String p_query, int p_fetchId, FetchListener p_listener) {
////		try {
////			fetch(p_connection, p_query, p_fetchId, p_listener);
////		} catch (Error | Exception e) {
////			Log.e(TAG, e);
////		}
////	}
//	
//	@Override
//	public void fetch(Connection p_connection, String p_query, int p_fetchId, FetchListener p_listener) throws FetchException {
//		boolean retry = false;
//		try {
//			super.fetch(p_connection, p_query, p_fetchId, p_listener);
//		} catch (FetchException e) {
//			if (e.getState() == FetchException.STATE_EXECUTE_QUERY) {
//				Throwable t = e.getCause();
//				if (t != null) {
//					retry = t instanceof com.mysql.jdbc.exceptions.jdbc4.CommunicationsException ||
//							t instanceof com.mysql.jdbc.CommunicationsException ||
//							t instanceof SQLNonTransientConnectionException ||
//							t instanceof SQLTransientConnectionException;
//				}
//			}
//			if (!retry) {
//				throw e;
//			}
//		} catch (Error e) {
//			throw e;
//		}
//		if (retry) {
//			Connection connection = getConnection();
//			try {
//				super.fetch(connection, p_query, p_fetchId, p_listener);
//			} catch (Error | FetchException e) {
//				releaseConnection(connection);
//				throw e;	
//			}
//			releaseConnection(connection);
//		}
//	}
//	
//	public int insert(Connection p_connection, String p_insertQuery, String p_updateQuery) {
//		int result = 0;
//		boolean constraint = false;
//		try {
//			long T0 = System.nanoTime();
//			result = executeUpdate(p_connection, p_insertQuery);
//			long T1 = System.nanoTime();
//    		System.out.println("[[ SQL ]] EXEC UPDATE >>>>> " + ((T1 - T0) / 1000));
//		} catch (SQLIntegrityConstraintViolationException sqlicve) {
//			constraint = true;
//			Log.e(TAG, sqlicve);
//		} catch (SQLException sqle) {
//			Log.e(TAG, sqle);
//		}
//		if (constraint && p_updateQuery != null) {
//			try {
//				result = -executeUpdate(p_connection, p_insertQuery);
//			} catch (SQLException sqle) {
//				Log.e(TAG, sqle);
//			}
//		}
//        return result;
//	}
//    
//    public int insertAndThrow(Connection p_connection, String p_insertQuery, String p_updateQuery) throws SQLException {
//		int result = 0;
//		boolean constraint = false;
//		try {
//			result = executeUpdate(p_connection, p_insertQuery);
//		} catch (SQLIntegrityConstraintViolationException sqlicve) {
//			constraint = true;
//			Log.e(TAG, sqlicve);
//            if (p_updateQuery == null) {
//                throw sqlicve;
//            }
//		} 
//		if (constraint) {
//            result = -executeUpdate(p_connection, p_insertQuery);
//		}
//        return result;
//	}
//    
//    public int update(Connection p_connection, String p_updateQuery, String p_insertQuery) {
//        int result;
//        try {
//            result = executeUpdate(p_connection, p_updateQuery);
//            if (result == 0 && p_insertQuery != null) {
//                result = -executeUpdate(p_connection, p_insertQuery);
//            }
//        } catch (SQLException sqle) {
//            result = 0;
//            Log.e(TAG, sqle);
//        }
//        return result;
//    }
    
}
