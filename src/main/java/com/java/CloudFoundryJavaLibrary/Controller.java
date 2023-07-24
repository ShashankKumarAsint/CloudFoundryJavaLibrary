package com.java.CloudFoundryJavaLibrary;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import com.java.CloudFoundryJavaLibrary.Model.CloudInformation;
import com.java.CloudFoundryJavaLibrary.Model.CloudOrg;

@RestController
@RequestMapping("/api/cloudfoundry")
public final class Controller {
	
	private Service service ;
	
	public Controller(Service service) {
		this.service = service;
	}

	@PostMapping("/resources")
	public CloudOrg getSubAccountDetails(@RequestBody CloudInformation information ) {
		return service.getInformationFromCloud(information);
	}
	
	@GetMapping("/getuser")
	public JsonNode getUserRoles() {	
		return service.getUserRoles();		
	}
	
	
	  @GetMapping("/userroles")
	  public Map<String,List<Map<String, String>>> getUserBySpecificRole(){
		  return service.getUserBySpecificRole();
	  }
	  

}
