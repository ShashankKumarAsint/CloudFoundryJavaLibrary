package com.java.CloudFoundryJavaLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CloudSpace;

import com.java.CloudFoundryJavaLibrary.Model.Application;
import com.java.CloudFoundryJavaLibrary.Model.CloudInformation;
import com.java.CloudFoundryJavaLibrary.Model.Organization;
import com.java.CloudFoundryJavaLibrary.Model.ServiceInstance;
import com.java.CloudFoundryJavaLibrary.Model.Space;

@org.springframework.stereotype.Service
public class Service {

	public Organization getInformationFromCloud(CloudInformation information) {
		
		CloudFoundryClient client = CloudFoundryConnection.getConnection(information.getUrl(), information.getUsername(), information.getPassword());
		
		Organization org = new Organization();
		Map<String, Space> spaceMap = new HashMap<>();
		List<ServiceInstance> services = new ArrayList<>();

		List<CloudSpace> cloudSpaces = client.getSpaces();
		List<CloudApplication> cloudApplications = client.getApplications();
		List<CloudService> cloudServices = client.getServices();

		// Create spaces and applications
		for (CloudSpace space : cloudSpaces) {
		    Space spaceObj = new Space();
		    spaceObj.setSpaceName(space.getName());
		    spaceMap.put(space.getName(), spaceObj);
		}
		
		// Mapping applications available at specific space
		for (CloudApplication application : cloudApplications) {
		    String spaceName = application.getSpace().getName();
		    Application applicationObj = new Application();
		    applicationObj.setApplicationName(application.getName());
		    spaceMap.get(spaceName).getApplications().add(applicationObj);
		}

		// Create services
		for (CloudService service : cloudServices) {
		    ServiceInstance serviceInstance = new ServiceInstance();
		    serviceInstance.setServiceName(service.getName());
		    serviceInstance.setLableName(service.getLabel());
		    services.add(serviceInstance);
		}

		// Set spaces and services in org object
		org.setOrganizationName(cloudSpaces.get(0).getOrganization().getName());
		org.setSpaces(new ArrayList<>(spaceMap.values()));
		org.setServiceInstances(services);


		return org;
		
		
	}
	
}
