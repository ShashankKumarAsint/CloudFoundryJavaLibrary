package com.java.CloudFoundryJavaLibrary;

import java.net.MalformedURLException;
import java.net.URL;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;

public class CloudFoundryConnection {
	
	public static CloudFoundryClient getConnection(String url, String username, String password) {
		CloudCredentials credentials = new CloudCredentials(username, password);
		CloudFoundryClient client = new CloudFoundryClient(credentials, getUrlFromString(url));
		client.login();
		return client;
	}
	
	private static URL getUrlFromString(String api) {
		try {
			return new URL(api);
		} catch (MalformedURLException e) {
			throw new RuntimeException("Invalid URL");
		}
	}

}
