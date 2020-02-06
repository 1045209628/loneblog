package com.xz.blog.common;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

@Component
public class OkHttpCli {

	private static final Logger log = LoggerFactory.getLogger(OkHttpCli.class);
	private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	@Autowired
	private OkHttpClient okHttpClient;

	public String doGet(String url, Map<String, String> params, Map<String, String> headers) {
		StringBuilder sb = new StringBuilder(url);
		if (params != null && params.keySet().size() > 0) {
			boolean isFirst = true;
			for (String name : params.keySet()) {
				if (isFirst) {
					sb.append("?");
					sb.append(name).append("=").append(params.get(name));
					isFirst = false;
				} else {
					sb.append(name).append("=").append(params.get(name));
				}
			}
		}
		Builder builder = new Request.Builder();
		if (headers != null && headers.keySet().size() > 0) {
			for (Entry<String, String> entry : headers.entrySet()) {
				if (entry.getKey() != null && entry.getValue() != null)
					builder.addHeader(entry.getKey(), entry.getValue());
			}
		}
		// Request request= new Request.Builder().url(sb.toString()).build();
		Request request = builder.url(sb.toString()).build();
		log.info("okhttp doGet :{}", sb.toString());
		return execute(request);
	}

	/**
	 * postjson
	 * 
	 * @param url
	 * @param json
	 * @return json数据格式
	 */
	public String doPostJson(String url, String json) {
		RequestBody requestBody = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(requestBody).build();
		log.info("okhttp doPostJson :{}", url);
		return execute(request);
	}

	private String execute(Request request) {
		Response rep = null;
		try {
			rep = okHttpClient.newCall(request).execute();
			if (rep.isSuccessful())
				return rep.body().string();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			if (rep != null)
				rep.close();
		}
		return null;
	}

}
