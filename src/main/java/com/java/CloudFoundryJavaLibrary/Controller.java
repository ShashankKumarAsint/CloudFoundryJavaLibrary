package com.java.CloudFoundryJavaLibrary;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.java.CloudFoundryJavaLibrary.Model.CloudInformation;
import com.java.CloudFoundryJavaLibrary.Model.Organization;

@RestController
@RequestMapping("/api/cloudfoundry")
public final class Controller {
	
	
	private Service service ;
	
	public Controller(Service service) {
		this.service = service;
	}

	@PostMapping("/resources")
	public Organization getSubAccountDetails(@RequestBody CloudInformation information ) {

		return service.getInformationFromCloud(information);
		
	}

	

}
