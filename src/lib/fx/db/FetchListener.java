package lib.fx.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface FetchListener {

	public boolean onFetch(int p_fetchId, ResultSet p_rs, long p_rowIndex) throws SQLException;
//  public boolean onFinish(int p_fetchId) throws SQLException;
//	public void    onError(SQLException p_sqle, Connection p_connection, long p_rowIndex);

}
