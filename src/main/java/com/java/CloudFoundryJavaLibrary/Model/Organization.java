package com.java.CloudFoundryJavaLibrary.Model;

import java.util.List;

public class Organization {
	
	private String organizationName;
	
	private List<Space> spaces;
	
	private List<ServiceInstance> serviceInstances;

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public List<Space> getSpaces() {
		return spaces;
	}

	public void setSpaces(List<Space> spaces) {
		this.spaces = spaces;
	}

	public List<ServiceInstance> getServiceInstances() {
		return serviceInstances;
	}

	public void setServiceInstances(List<ServiceInstance> serviceInstances) {
		this.serviceInstances = serviceInstances;
	}
	
	

}
