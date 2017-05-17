package lib.fx.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public abstract class Database {
    
    public Database() {
    	mName = "";
    }    
    public Database(String p_name) {
    	mName = p_name == null ? "" : p_name;
    }

    private String mName;
    private String mTablePrefix;
    
    public void setName(String p_name) {
    	mName = p_name == null ? "" : p_name;
    }    
    
    public final String getName() {
    	return mName;
    }
    
    public int executeUpdate(Connection p_connection, String p_query) throws SQLException {
    	int result = -1;
    	if (p_connection != null && p_query != null) {
	    	result = p_connection.createStatement().executeUpdate(p_query); 
	    }
        return result; 
    }
    public ResultSet executeQuery(Connection p_connection, String p_query) throws SQLException {
        ResultSet result = null;
        if (p_connection != null && p_query != null) {
        	result = p_connection.createStatement().executeQuery(p_query);
	    }
        return result; 
    }    
    public final void close(ResultSet p_resultSet) {
    	if (p_resultSet != null) try { p_resultSet.close(); } catch (SQLException sqle) { }
    } 
    public final void close(Statement p_statement) {
    	if (p_statement != null) try { p_statement.close(); } catch (SQLException sqle) { }
    }
    
//    public Object[] getRecord(Connection p_connection, String p_query) throws SQLException {
//        Object[] result = null;
//        if (p_connection != null && p_query != null) {
//            ResultSet rset = null;
//			try {
//				rset = p_connection.createStatement().executeQuery(p_query);
//				if (rset.next()) {
//					int columns = rset.getMetaData().getColumnCount();
//					result = new Object[columns];
//					for (int x = 0; x < columns; x++) {
//						result[x] = rset.getObject(x + 1);
//					}
//				}
//			} catch (SQLException sqle) {
//                close(rset);
//                throw sqle;
//            }
//            close(rset);
//        }
//        return result;
//    }
//    public final ArrayList<Object[]> getRecords(Connection p_connection, String p_query) throws SQLException {
//        ArrayList<Object[]> result = new ArrayList<Object[]>();
//        if (p_connection != null && p_query != null) {
//            ResultSet rset = null;
//            try {
//				rset = p_connection.createStatement().executeQuery(p_query);
//				while (rset.next()) {
//					int columns = rset.getMetaData().getColumnCount();
//					Object[] object = new Object[columns];
//					for (int x = 0; x < columns; x++) {
//						object[x] = rset.getObject(x + 1);
//					}
//					result.add(object);
//				}
//            } catch (SQLException sqle) {
//            	close(rset);
//            	throw sqle;
//            }
//            close(rset);
//        }
//        return result;
//    }
        
    Object getColumn(ResultSetMetaData p_rsmd, int p_index, ResultSet rset) throws SQLException {
    	Object result = null;
    	switch (p_rsmd.getColumnClassName(p_index)) {
		case "java.lang.String":
			result = rset.getString(p_index);
			break;
		case "java.lang.Integer":
			result = rset.getInt(p_index);
			break;
		case "java.lang.Long":
			result = rset.getLong(p_index);
			break;
		case "java.lang.Float":
			result = rset.getFloat(p_index);
			break;
		case "java.lang.Double":
			result = rset.getDouble(p_index);
			break;
		case "java.lang.Byte":
			result = rset.getByte(p_index);
			break;
		case "java.lang.Short":
			result = rset.getShort(p_index);
			break;
		case "java.sql.Date":
			result = rset.getDate(p_index);
			break;
		case "java.sql.Time":
			result = rset.getTime(p_index);
			break;
		case "java.sql.Timestamp":
			result = rset.getTimestamp(p_index);
			break;
		default:
			result = rset.getObject(p_index);
			break;
		}
    	return result;
    }
    public Object[] getRecord(Connection p_connection, String p_query) throws SQLException {
        return getRecord(p_connection, p_query, 0);
    }
    public Object[] getRecord(Connection p_connection, String p_query, int p_timeout) throws SQLException {
        Object[] result = null;
        if (p_connection != null && p_query != null) {
        	ResultSet rset = null;
            Statement stmt = null;
			try {
				stmt = p_connection.createStatement();
				if (p_timeout > 0) {
					stmt.setQueryTimeout(p_timeout);
				}
				rset = stmt.executeQuery(p_query);
				if (rset.next()) {
					ResultSetMetaData rsmd = rset.getMetaData();
					result = new Object[rsmd.getColumnCount()];
					for (int x = 0; x < result.length; x++) {
						result[x] = getColumn(rsmd, x + 1, rset);
					}
				}
			} catch (SQLException sqle) {
                close(stmt);
                throw sqle;
            }
            close(stmt);
        }
        return result;
    }
    public final ArrayList<Object[]> getRecords(Connection p_connection, String p_query) throws SQLException {
    	return getRecords(p_connection, p_query, 0);
    }
    public final ArrayList<Object[]> getRecords(Connection p_connection, String p_query, int p_timeout) throws SQLException {
        ArrayList<Object[]> result = null;
        if (p_connection != null && p_query != null) {
            Statement stmt = null;
            ResultSet rset = null;
			try {
				stmt = p_connection.createStatement();
				if (p_timeout > 0) {
					stmt.setQueryTimeout(p_timeout);
				}
				rset = stmt.executeQuery(p_query);
				while (rset.next()) {
					ResultSetMetaData rsmd = rset.getMetaData();
					Object[] object = new Object[rsmd.getColumnCount()];
					for (int x = 0; x < object.length; x++) {
						object[x] = getColumn(rsmd, x + 1, rset);
					}
					if (result == null) {
						result = new ArrayList<>();
					}
					result.add(object);
				}
            } catch (SQLException sqle) {
            	close(stmt);
            	throw sqle;
            }
            close(stmt);
        }
        return result;
    }
    
    public void fetch(Connection p_connection, String p_query, int p_fetchId, FetchListener p_listener) throws FetchException {
    	long index = -1;
	    if (p_connection != null && p_query != null && p_listener != null) {
	     	ResultSet rset = null;
	     	int state = FetchException.STATE_EXECUTE_QUERY;
	      	try {
	      		rset  = p_connection.createStatement().executeQuery(p_query);
	      		state = FetchException.STATE_IN_LOOP;
	      		while (rset.next()) {
					boolean next = p_listener.onFetch(p_fetchId, rset, ++index);
					if (!next) {
						break;
					}
				}
//              p_listener.onFinish(p_fetchId);
			} catch (Exception e) {
				close(rset);
				throw new FetchException(e, state);
			} catch (Error e) {
				close(rset);
				throw e;
			}
			close(rset);
		}
	}
    
//    public void fetch(Connection p_connection, String p_query, FetchListener p_listener) throws SQLException {
//    	long index = -1;
//	    if (p_connection != null && p_query != null && p_listener != null) {
//	     	ResultSet rset = null;
//	      	try {
//	      		rset = p_connection.createStatement().executeQuery(p_query);
//			} catch (SQLException sqle) {
//				close(rset);
//				throw sqle;
//			}
//	      	try {
//				while (rset.next()) {
//					try {
//						boolean next = p_listener.onFetch(rset, ++index);
//						if (!next) {
//							break;
//						}
//					} catch (Exception e) {
//					}
//				}
//			} catch (SQLException sqle) {
//				close(rset);
//				try {
//					p_listener.onError(sqle, p_connection, ++index);
//				} catch (Exception e) {
//				}
//			}
//			close(rset);
//		}
//    }
	  
//    public static class FetchException extends Exception {
//    	
//    	public static final int STATE_EXECUTE_QUERY = 1;
//    	public static final int STATE_IN_LOOP = 2;
//    	
//    	private int mState;
//    	
//		FetchException() {
//			super ();
//			mState = STATE_EXECUTE_QUERY;
//		}
//    	
//    	public FetchException(Exception p_exception, int p_state) {
//    		super (p_exception);
//    		mState = p_state;
//    	}
//    	
//    	public final int getState() {
//    		return mState;
//    	}
//    	
//    }
    
//	public static interface FetchListener {
//
//		public boolean onFetch(int p_fetchId, ResultSet p_rs, long p_rowIndex) throws SQLException;
////		public void    onError(SQLException p_sqle, Connection p_connection, long p_rowIndex);
//
//	}
    
//    public final void fetch(Connection p_connection, String p_query, FetchListener p_listener) {
//        long    indx = -1;
//        boolean rslt = false;
//        long    time = System.currentTimeMillis();
//        if (p_connection != null && p_query != null && p_listener != null) {
//        	Statement statement = null;
//        	ResultSet rset = null;
//            try {
//            	statement = p_connection.createStatement();
//				if (statement != null) {
//					rset = statement.executeQuery(p_query);
//				}
//            } catch (SQLException sqle) {
//            	if (p_listener != null) {
//                	try {
//                		Connection newConnection = p_listener.onConnectionError(sqle, p_connection);
//                    	if (newConnection != null) {
//                			statement = newConnection.createStatement();
//            				if (statement != null) {
//            					rset = statement.executeQuery(p_query);
//            				}
//                		}
//                	} catch (Exception e) {
//                    }
//                }
//            }
//            try {
//				while (rset != null && rset.next()) {
//					try {
//						boolean abort = p_listener.onFetch(rset, ++indx);
//						if (abort) {
//							break;
//						}
//					} catch (Exception e) {
//					}
//					rslt = true;
//				}
//            } catch (SQLException sqle) {
//                rslt = false;
//            }
//            close(rset);
//        }
//        if (p_listener != null) {
//        	try {
//        		p_listener.onFinish(rslt, indx, System.currentTimeMillis() - time);
//        	} catch (Exception e) {
//            }
//        }
//    }
//    
//    public static interface FetchListener {
//    
//        public boolean onFetch(ResultSet p_rs, long p_rowIndex);
//        public void onFinish(boolean p_result, long p_lastRowIndex, long p_duration);
//        public Connection onConnectionError(SQLException p_sqle, Connection p_connection);
//        
//    }
    
}



//package com.es.database;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.SQLNonTransientConnectionException;
//import java.sql.SQLTransientConnectionException;
//import java.sql.Statement;
//import java.util.ArrayList;
//
//public abstract class Database {
//    
//    public static final String TAG = "Database";
//        
//    
//    
//    public Database() {
//    	mName = "";
//    	CONNECTION_POOL = new ConnectionPool(); 
//    }    
//    public Database(String p_name) {
//    	mName = p_name == null ? "" : p_name;
//    	CONNECTION_POOL = new ConnectionPool(); 
//    }
//
//    /*
//     * 
//     * 
//     * 
//     */
//    private String mName;
//    public void setName(String p_name) {
//    	mName = p_name == null ? "" : p_name;
//    }    
//    public final String getName() {
//    	return mName;
//    }
//    
//    /*
//     * 
//     * 
//     * 
//     */
//    private boolean mConnectionPoolEnabled = false;
//    public void setConnectionPoolEnabled(boolean p_enabled) {
//    	mConnectionPoolEnabled = p_enabled;
//    }
//    public final boolean isConnectionPoolEnabled() {
//    	return mConnectionPoolEnabled;
//    }
//    private final ConnectionPool CONNECTION_POOL;   
//    public final synchronized void initialize() throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {
//    	CONNECTION_POOL.initialize();
//    }
//    public final void setClass(String p_value) {
//        if (!CONNECTION_POOL.isInitialized()) {
//             CONNECTION_POOL.setClass(p_value);
//        }
//    } 
//    public final void setUsername(String p_value) {
//        if (!CONNECTION_POOL.isInitialized()) {
//             CONNECTION_POOL.setUsername(p_value);
//        }
//    }
//    public final void setPassword(String p_value) {
//        if (!CONNECTION_POOL.isInitialized()) {
//             CONNECTION_POOL.setPassword(p_value);
//        }
//    }
//    public final void setURL(String p_value) {
//        if (!CONNECTION_POOL.isInitialized()) {
//             CONNECTION_POOL.setURL(p_value);
//        }
//    }
//    public final void setConnectionPoolSize(int p_min, int p_max) {
//        if (!CONNECTION_POOL.isInitialized()) {
//             CONNECTION_POOL.setSize(p_min, p_max);
//        }
//    }    
//    public final int getBusyConnectionCount() {
//        return CONNECTION_POOL.getBookConnectionCount();
//    }
//    public final int getFreeConnectionCount() {
//        return CONNECTION_POOL.getFreeConnectionCount();
//    }
//    public final int getDirectConnectionCount() {
//        return CONNECTION_POOL.getDirectConnectionCount();
//    }    
//    public final Connection getConnection() {
//        Connection conn = null;
//        try {
//            conn = mConnectionPoolEnabled ? CONNECTION_POOL.getConnection() : CONNECTION_POOL.getDirectConnection();
//        } catch (SQLException sqle) {
//
//        }
//        return conn;
//    }
//
//	private final Connection getDirectConnection() {
//		Connection conn = null;
//		try {
//			conn = CONNECTION_POOL.getDirectConnection();
//		} catch (SQLException sqle) {
//
//		}
//		return conn;
//	}
//    public final void releaseConnection(Connection p_connection) {
//        try {
//            CONNECTION_POOL.releaseConnection(p_connection);
//        } catch (SQLException sqle) {
//        	
//        }
//    }
//    private final void close(Statement p_stmt) {
//        if (p_stmt != null) {
//            try {
//                p_stmt.close();
//            } catch (SQLException sqle) {
//            }
//        }
//    } 
//    private final void close(ResultSet p_rs) {
//        if (p_rs != null) {
//            try {
//                p_rs.close();
//            } catch (SQLException sqle) {
//            }
//        }
//    } 
//    private final void close(Connection p_connection) {
//        if (p_connection != null) {
//            try {
//                p_connection.close();
//            } catch (SQLException sqle) {
//            }
//        }
//    }
//
//    protected boolean onUpdateOrQueryException(SQLException p_sqle) {
//        boolean communication = p_sqle instanceof SQLNonTransientConnectionException ||
//        						p_sqle instanceof SQLTransientConnectionException;
//        return communication;
//    }
//    
//    private final int executeUpdate(Connection p_connection, String p_query) throws SQLException {
//    	int result = 0;
//    	Statement statement = p_connection.createStatement();
//    	if (statement == null) {
//    		result = 0;
//    	}
//    	else {
//    		result = statement.executeUpdate(p_query); 
//    	}
//        return result; 
//    }
//    
//    private final int executeUpdate(Connection p_connection, String p_query, boolean p_retry) throws SQLException {
//        int result = 0;
//        Statement statement = null;
//        SQLException e = null;
//        try {
//            statement = p_connection.createStatement();
//            result    = statement.executeUpdate(p_query);
//            if (result > 0) {
//            	p_connection.commit();
//            }
//        } catch (SQLException sqle) {
//            e = sqle;
//        } 
//        close(statement);
//        if (e != null) {
//            if (onUpdateOrQueryException(e)) {
//                close(p_connection);
//                if (p_retry) {
//                    result = executeUpdate(getDirectConnection(), p_query, false);
//                }
//                else {
//                    throw e;
//                }
//            }
//            else {
//                throw e;
//            }
//        }
//        return result; 
//    }
//    public final int update(String p_query, Connection p_connection) throws SQLException {
//        int result = 0;
//        if (p_query == null) {
//        	result = 0;
//        }
//        else if (p_connection == null) {
//        	result = 0;
//        }
//        else {
//        	result = executeUpdate(p_connection, p_query);
//        }
//        return result;
//    }
//    public final int update(String p_query) throws SQLException {
//        int result = 0;
//        if (p_query == null) {
//        	result = 0;
//        }
//        else {
//			Connection connection = null;
//			try {
//				connection = getConnection();
//				if (connection == null) {
//					result = 0;
//				}
//				else {
//					result = executeUpdate(connection, p_query, true);
//				}
//			} catch (SQLException sqle) {
//			}
//			if (connection != null) {
//				releaseConnection(connection);
//			}
//        }
//        return result;
//    }
//    private final ResultSet executeQuery(Connection p_connection, String p_query, boolean p_retry) throws SQLException {
//        ResultSet result = null;
//        Statement statement = null;
//        SQLException e = null;
//        try {
//            statement = p_connection.createStatement();
//            result    = statement.executeQuery(p_query);
//        } catch (SQLException sqle) {
//            e = sqle;
//        } 
//        if (e != null) {
//            if (onUpdateOrQueryException(e)) {
//                close(result);
//                close(statement);
//                close(p_connection);
//                if (p_retry) {
//                    result = executeQuery(getDirectConnection(), p_query, false);
//                }
//                else {
//                    close(result);
//                    close(statement);
//                    throw e;
//                }
//            }
//            else {
//                close(result);
//                close(statement);
//                throw e;
//            }
//        }
//        return result; 
//    }    
//    public final Object[] fetch(String p_query) {
//        Object[] result = null;
//        if (p_query != null) {
//            Connection conn = null;
//            ResultSet  rset = null;
//            try {
//                conn = getConnection();
//                if (conn == null) {
//                    return null;
//                }
//                else {
//                    rset = executeQuery(conn, p_query, true);
//                    if (rset.next()) {
//                        int columns = rset.getMetaData().getColumnCount();
//                        result = new Object[columns];
//                        for (int x = 0; x < columns; x++) {
//                            result[x] = rset.getObject(x + 1);
//                        }
//                    }
//                }
//            } catch (SQLException sqle) {
//                result = null;
//            }
//            close(rset);
//            releaseConnection(conn);
//        }
//        return result;
//    }
//    public final ArrayList<Object[]> fetch(String p_query, int p_maxRow) {
//        ArrayList<Object[]> result = new ArrayList<Object[]>();
//        Object[] object = null;
//        if (p_query != null) {
//            Connection conn = null;
//            ResultSet  rset = null;
//            try {
//                conn = getConnection();
//                if (conn == null) {
//                    return null;
//                }
//                else {
//                    rset = executeQuery(conn, p_query, true);
//                    while (rset.next()) {
//                        if (rset.getRow() == p_maxRow) {
//                            break;
//                        }
//                        int columns = rset.getMetaData().getColumnCount();
//                        object = new Object[columns];
//                        for (int x = 0; x < columns; x++) {
//                            object[x] = rset.getObject(x + 1);
//                        }
//                        result.add(object);
//                    }
//                }
//            } catch (SQLException sqle) {
//                object = null;
//            }
//            close(rset);
//            releaseConnection(conn);
//        }
//        return result;
//    }
//    public final void fetch(String p_query, FetchListener p_listener) {
//        long    indx = -1;
//        boolean rslt = false;
//        long    time = System.currentTimeMillis();
//        if (p_query != null && p_listener != null) {
//            Connection conn = null;
//            ResultSet  rset = null;
//            try {
//                conn = getConnection();
//                if (conn == null) {
//                    return;
//                }
//                else {
//                    rset = executeQuery(conn, p_query, true);
//                    while (rset.next()) {
//                        rslt = p_listener.onFetch(rset, ++indx);
//                        if (!rslt) {
//                            break;
//                        }
//                    }
//                }
//            } catch (SQLException sqle) {
//                rslt = false;
//                try {
//                    p_listener.onError(sqle);
//                } catch (Exception e) {
//                }
//            }
//            close(rset);
//            releaseConnection(conn);
//        }
//        try {
//            p_listener.onFinish(rslt, indx, System.currentTimeMillis() - time);
//        } catch (Exception e) {
//        }
//    }
//    
//    public static interface FetchListener {
//    
//        public boolean onFetch(ResultSet p_rs, long p_rowIndex);
//        public void onFinish(boolean p_result, long p_lastRowIndex, long p_duration);
//        public void onError(SQLException p_sqle);
//        
//    }
//    
//}