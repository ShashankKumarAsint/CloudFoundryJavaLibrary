package com.java.CloudFoundryJavaLibrary.Model;

import java.util.ArrayList;
import java.util.List;

public class Space {

	private String spaceName;
	
	private List<Application> applications = new ArrayList<>();

	public String getSpaceName() {
		return spaceName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public List<Application> getApplications() {
		return applications;
	}

	public void setApplications(List<Application> applications) {
		this.applications = applications;
	}
	
	
	
}
