package com.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjExt extends JSONObject{

	
	public  JSONObjExt(String str) throws JSONException{
		super(str);
	}
	
	@Override
	public JSONObject get(String key) throws JSONException {
		// TODO Auto-generated method stub
		return (JSONObject)super.get(key);
	}

}
