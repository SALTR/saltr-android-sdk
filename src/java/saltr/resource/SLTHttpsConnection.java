/*
 * Copyright (c) 2014 Plexonic Ltd
 */

package saltr.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import saltr.SLTIDataHandler;

import android.os.AsyncTask;

public class SLTHttpsConnection extends AsyncTask<String, Void, String> {
	
	private SLTIDataHandler dataHandler;
	private List<NameValuePair> params;
	private URL url;
	
	public SLTHttpsConnection(SLTIDataHandler dataHandler) {
        params = new ArrayList<NameValuePair>();
		this.dataHandler = dataHandler;
	}

	@Override
	protected String doInBackground(String... arg) {	
		try {		
			URLConnection connection = url.openConnection();

			String line;

			StringBuilder builder = new StringBuilder();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(Long result) {
		System.out.println("Downloaded " + result + " bytes");
	}
	
	public void setParameters(String key, Object value) {
		params.add(new BasicNameValuePair(key, value.toString()));
	}
	
	public void setUrl(String urlStr) throws MalformedURLException {
		String paramString = URLEncodedUtils.format(params, "utf-8");
		urlStr += "?" + paramString;
		url = new URL(urlStr);
	}
}
