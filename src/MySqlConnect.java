import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class MySqlConnect {

	public static void main(String[] args) throws JSONException {
		
		
		
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";

		// URL指向要访问的数据库名
		String url = "jdbc:mysql://192.168.4.11/cboard";

		// MySQL配置时的用户名
		String user = "root";

		// MySQL配置时的密码
		String password = "root";

		try {
			// 加载驱动程序
			Class.forName(driver);

			// 连接数据库
			Connection conn = (Connection) DriverManager.getConnection(url,
					user, password);

			if (!conn.isClosed()) {
				System.out.println("连接成功");
			}

			// statement用来执行SQL语句
			Statement statement = (Statement) conn.createStatement();

			// 要执行的SQL语句
			String sql = "SELECT * FROM dashboard_datasetinfo, dashboard_datasource WHERE dashboard_datasetinfo.`id`=28 AND dashboard_datasetinfo.`datasource`= dashboard_datasource.`datasource_id`";

			ResultSet rs = statement.executeQuery(sql);

			while (rs.next()) {
				String datacolumn = rs.getString("datacolumn");
				// System.out.println(datacolumn);
				String config = rs.getString("config");
				// 获得DriverType
				 String driverType=getDriver(config);
				 //获得JDBCRUL
				 String jdbcUrl=getJdbcUrl(config);
				 System.out.println(ReverseJ.getJSON(datacolumn,driverType,jdbcUrl));
				
				
			}
			//关闭连接
			rs.close();
			conn.close();
			

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 获取driver的类型
	public static String getDriver(String config) throws JSONException{
		JSONObject configJ=new JSONObject(config);
		String driverName=configJ.getString("driver");
		
		//疑问
		String arr[]=driverName.split("\\.");
		return arr[1];
	}
	
	//获取jdbcUrl
	public static String getJdbcUrl(String config) throws JSONException{
		JSONObject configJ=new JSONObject(config);
		String JdbcUrl=configJ.getString("jdbcurl");
		return JdbcUrl;
	}
}
