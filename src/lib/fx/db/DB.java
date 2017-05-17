/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib.fx.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Properties;
import lib.fx.logger.Log;

/**
 *
 * @author febri
 */
public class DB {

    private static final String TAG = "DB";
    public static void create(Properties properties) {
	        String database_username 		 = properties.getProperty("database_username", "guest");
	        String database_password 		 = properties.getProperty("database_password", "guest");
	        String database_url              = properties.getProperty("database_url", "jdbc:mysql://localhost/modem");
	        int database_max_connection_size = 100;
	        int database_min_connection_size = 8;
	        int database_connection_lifetime = 60000 * 5;
	        int database_retry_limit         = 0;
	        int database_retry_sleep_time    = 1000;
	        
			ConnectionPool connectionPool = new ConnectionPool();
	        connectionPool.setClazz   			("com.mysql.jdbc.Driver");
			connectionPool.setUsername			(database_username);
			connectionPool.setPassword			(database_password); 
			connectionPool.setURL     			(database_url);
			connectionPool.setMaxConnectionSize (database_max_connection_size);
			connectionPool.setMinConnectionSize (database_min_connection_size);
			connectionPool.setConnectionLifeTime(database_connection_lifetime);
			SQL sql = new SQL(connectionPool);
			    sql.setConnectionRetryLimit(database_retry_limit);
			    sql.setConnectionRetrySleepTime(database_retry_sleep_time);
			Log.i(TAG, "CREATE", connectionPool.getClazz() + ", " 
												+ connectionPool.getUsername() + ", " 
												+ connectionPool.getURL() + ", "
												+ "maxCon:" + connectionPool.getMaxConnectionSize() + ", " 
												+ "minCon:" + connectionPool.getMinConnectionSize() + ", "
												+ connectionPool.getConnectionLifeTime());
            mSql = sql;
    }
    private static SQL mSql = null;
    public static Connection getConnection(String TAG) {
        return mSql.getConnection(TAG);
    }

    public static void releaseConnection(Connection conn) {
        mSql.rollback(conn);
        mSql.releaseConnection(conn);
    }

    public static void commit(Connection conn) {
        mSql.commit(conn);
    }

    public static void rollback(Connection conn) {
        mSql.rollback(conn);
    }
    
    public static ArrayList<Object[]> getRecords(String p_query) {
        ArrayList<Object[]> records = null;
        Connection conn = mSql.getConnection(TAG);
        try {
            records = mSql.getRecords(conn, p_query);
        }
        catch (Exception e) {
        }
        finally {
            mSql.releaseConnection(conn);
        }
        return records == null ? new ArrayList<>() : records;
    }
    
    public static Object[] getRecord(String p_query) {
        Object[] record = null;
        Connection conn = mSql.getConnection(TAG);
        try {
            record = mSql.getRecord(conn, p_query);
        }
        catch (Exception e) {
        }
        finally {
            mSql.releaseConnection(conn);
        }
        return record;
    }
    
    
    
}
