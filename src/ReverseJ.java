import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.utils.JSONObjExt;

public class ReverseJ {

	public static void main(String[] args) throws IOException, JSONException {
		// 原料转成字符串
		String start = ReverseJ.changToStr("src/aaa.json");

//		// Reader里的parameter里connection里的table即json里的beforeTable的tableName
//		// table为[];
//		String rTable = null;
//
//		// Writer里的parameter里connection里的table即json里的afterTable的tableName
//		// table为[]
//		// presql里也是
//		String wTable = null;
//
//		List<String> beforeCols = new LinkedList<String>();
//
//		List<String> afterCols = new LinkedList<String>();
//
//		/**
//		 * 解析一个数据段
//		 */
//		rTable = getRTable(start);
//		wTable = getWTable(start);
//		beforeCols = getBeforeCols(start);
//		afterCols = getafterCols(start);
//
//		/**
//		 * 生成JSON
//		 */
//		JSONObject end = makeJson(rTable, wTable, beforeCols, afterCols,
//				"mongoDB", "jdbc:mysql://192.168.4.11:3306/cboard_demo2");
//		System.out.println(end);
		System.out.println(getJSON(start,"mongodb","jdbc:mysql://192.168.4.11:3306/cboard_demo2"));

	}

	public static String changToStr(String path) throws IOException {
		File f = new File(path);
		InputStreamReader read = new InputStreamReader(new FileInputStream(f));// 考虑到编码格式
		BufferedReader bufferedReader = new BufferedReader(read);
		String total = "";
		String lineTxt = null;
		while ((lineTxt = bufferedReader.readLine()) != null) {
			lineTxt = lineTxt + "\n";
			total += lineTxt;
		}

		return total;
	}

	// get rTable;
	public static String getRTable(String start) throws JSONException {
		JSONObjExt json = new JSONObjExt(start);
		// System.out.println(json.get("beforeTable"));
		String rTable = json.get("beforeTable").get("tableName").toString();
		return rTable;

	}

	// get wTable
	public static String getWTable(String start) throws JSONException {
		JSONObjExt json = new JSONObjExt(start);
		String wTable = json.get("afterTable").get("tableName").toString();
		return wTable;
	}

	// 获取beforeTable的fieldList
	public static List<String> getBeforeCols(String start) throws JSONException {
		List<String> beforeCols = new LinkedList<String>();
		JSONObjExt json = new JSONObjExt(start);
		JSONArray beforeJa = (JSONArray) json.get("beforeTable").get(
				"fieldList");
		for (int i = 0; i < beforeJa.length(); i++) {
			JSONObject ob = new JSONObject();
			ob = (JSONObject) beforeJa.get(i);
			beforeCols.add(ob.get("fieldName").toString());
		}
		return beforeCols;
	}

	// 获取afterTable的fieldList
	public static List<String> getafterCols(String start) throws JSONException {
		List<String> afterCols = new LinkedList<String>();
		JSONObjExt json = new JSONObjExt(start);
		JSONArray afterJa = (JSONArray) json.get("afterTable").get("fieldList");
		for (int i = 0; i < afterJa.length(); i++) {
			JSONObject ob;
			ob = (JSONObject) afterJa.get(i);
			afterCols.add(ob.get("fieldName").toString());
		}
		return afterCols;
	}

	// 整合json
	public static JSONObject makeJson(String rTable, String wTable,
			List<String> beforeCols, List<String> afterCols, String driver,
			String jdbcUrlStr) throws JSONException {

		/**
		 * 赋值
		 */

		// reader
		JSONObject reader = new JSONObject();
		reader.put("name", driver + "reader");
		JSONArray connection = new JSONArray();
		JSONObject con1 = new JSONObject();
		JSONArray jdbcUrl = new JSONArray();
		jdbcUrl.put(jdbcUrlStr);
		con1.put("jdbcUrl", jdbcUrl);
		JSONArray table = new JSONArray();
		table.put(rTable);
		con1.put("table", table);
		connection.put(con1);
		// reader.put("connection", connectionJS);
		JSONObject parameter = new JSONObject();
		parameter.put("connection", connection);
		parameter.put("password", "root");
		parameter.put("username", "root");
		reader.put("parameter", parameter);
		JSONArray column = new JSONArray();
		for (int i = 0; i < beforeCols.size(); i++) {
			column.put(beforeCols.get(i));
		}
		parameter.put("column", column);
		// System.out.println(reader);

		// writer
		JSONObject writer = new JSONObject();
		writer.put("name", driver + "writer");
		JSONArray connection_a = new JSONArray();
		JSONObject con1_a = new JSONObject();
		JSONArray jdbcUrl_a = new JSONArray();
		jdbcUrl_a.put("jdbc:mysql://192.168.4.11:3306/cboard");
		con1_a.put("jdbcUrl", jdbcUrl_a);
		JSONArray table_a = new JSONArray();
		table_a.put(wTable);
		con1_a.put("table", table_a);
		connection_a.put(con1_a);
		// reader.put("connection", connectionJS);
		JSONObject parameter_a = new JSONObject();
		parameter_a.put("connection", connection_a);
		parameter_a.put("password", "root");
		parameter_a.put("username", "root");
		JSONArray preSql = new JSONArray();
		preSql.put("delete from 2018041101");
		parameter_a.put("preSql", preSql);
		writer.put("parameter", parameter_a);
		JSONArray column_a = new JSONArray();
		for (int i = 0; i < afterCols.size(); i++) {
			column_a.put(afterCols.get(i));
		}
		parameter.put("column", column_a);
		// System.out.println(writer);

		JSONObject rwPair = new JSONObject();
		rwPair.put("reader", reader);
		rwPair.put("writer", writer);
		JSONArray content = new JSONArray();
		content.put(rwPair);

		// setting
		JSONObject speed = new JSONObject();
		speed.put("channel", "5");
		JSONObject setting = new JSONObject();
		setting.put("speed", speed);

		JSONObject job = new JSONObject();
		job.put("content", content);
		job.put("setting", setting);

		JSONObject end = new JSONObject();
		end.put("job", job);
		return end;

	}

	// 三个参数解决
	public static JSONObject getJSON(String datacolumn,String config,String jdbcUrl ) throws JSONException {
		String rTable = null;
		String wTable = null;
		List<String> beforeCols = new LinkedList<String>();

		List<String> afterCols = new LinkedList<String>();

		/**
		 * 解析一个数据段
		 */
		rTable = getRTable(datacolumn);
		wTable = getWTable(datacolumn);
		beforeCols = getBeforeCols(datacolumn);
		afterCols = getafterCols(datacolumn);

		/**
		 * 生成JSON
		 */
		JSONObject end = makeJson(rTable, wTable, beforeCols, afterCols,
				config, jdbcUrl);
		return end;

	}
}
