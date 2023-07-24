package com.java.CloudFoundryJavaLibrary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CloudSpace;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.CloudFoundryJavaLibrary.Model.Application;
import com.java.CloudFoundryJavaLibrary.Model.CloudInformation;
import com.java.CloudFoundryJavaLibrary.Model.CloudOrg;
import com.java.CloudFoundryJavaLibrary.Model.ServiceInstance;
import com.java.CloudFoundryJavaLibrary.Model.Space;

@org.springframework.stereotype.Service
public class Service {
	
	private final String grantType = "client_credentials";
	private final String clientId = "clientid";
	private final String clientSecret = "clientsecret";
	private final String accessKeyUrl = "url";
	private final String apiUrl = "apiurl";
	private final String credentialsKey = "xsuaa";
	private final String credentials = "credentials";
	private final String serviceKey = "VCAP_SERVICES";
	private final String serviceRole = "Subaccount Administrator";
	private final String resources = "resources";
	private static Logger logger = Logger.getLogger(Service.class.getName());
	
	public CloudOrg getInformationFromCloud(CloudInformation information) {

		CloudFoundryClient client = CloudFoundryConnection.getConnection(information.getUrl(),
				information.getUsername(), information.getPassword());
		CloudOrg org = new CloudOrg();
		Map<String, Space> spaceMap = new HashMap<>();
		List<ServiceInstance> services = new ArrayList<>();
		List<CloudSpace> cloudSpaces = client.getSpaces();
		List<CloudApplication> cloudApplications = client.getApplications();
		List<CloudService> cloudServices = client.getServices();

		for (CloudSpace space : cloudSpaces) {
			Space spaceObj = new Space();
			spaceObj.setSpaceName(space.getName());
			spaceMap.put(space.getName(), spaceObj);
		}

		for (CloudApplication application : cloudApplications) {
			String spaceName = application.getSpace().getName();
			Application applicationObj = new Application();
			applicationObj.setApplicationName(application.getName());
			spaceMap.get(spaceName).getApplications().add(applicationObj);
		}

		for (CloudService service : cloudServices) {
			ServiceInstance serviceInstance = new ServiceInstance();
			serviceInstance.setServiceName(service.getName());
			serviceInstance.setLableName(service.getLabel());
			services.add(serviceInstance);
		}
		
		org.setOrganizationName(cloudSpaces.get(0).getOrganization().getName());
		org.setSpaces(new ArrayList<>(spaceMap.values()));
		org.setServiceInstances(services);

		return org;
	}

	public String getToken() {
		
		String jsonData = System.getenv(this.serviceKey);
		String urlData = getCredentials(jsonData, this.accessKeyUrl)+"/oauth/token";
		String clientIdData = getCredentials(jsonData, this.clientId);
		String clientSecretData = getCredentials(jsonData, this.clientSecret);

		logger.info("clientId : "+ clientIdData);
		logger.info("clientsecret : "+clientSecretData);
		logger.info("url : "+urlData);
		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
	        HttpPost httpPost = new HttpPost(urlData);
	        String authHeader = "Basic " + Base64.getEncoder().encodeToString((clientIdData + ":" + clientSecretData).getBytes());
	        httpPost.setHeader("Authorization", authHeader);
	        String requestBody = "grant_type=" + grantType;
	        httpPost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_FORM_URLENCODED));

	        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
	        	logger.info("httpResponse : "+httpResponse);
	            String responseBody = EntityUtils.toString(httpResponse.getEntity());
	            ObjectMapper objectMapper = new ObjectMapper();
	            JsonNode jsonNode = objectMapper.readTree(responseBody);
	            String accessToken = jsonNode.get("access_token").asText();
	            return accessToken;
	        } catch (IOException e) {
	        	e.printStackTrace();
	            throw new IOException("Failed to read response: " + e.getMessage());
	        }
	        catch(Exception e) {
	        	throw new RuntimeException(e.getMessage());
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	        throw new RuntimeException("Failed to create HTTP client: " + e.getMessage());
	    }
	}

	
	public String getUserDetailsFromUAA() {
		
	    String jsonData = System.getenv(this.serviceKey);
	    String apiUrl = getCredentials(jsonData, this.apiUrl);
	    logger.info("apiUrl : "+apiUrl);
		String usersEndpoint = apiUrl + "/Users";
	    String accessToken = null;

	        accessToken = getToken();
	        if (accessToken.equals("unauthorized")) {
	            throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED,"Unauthorized");
	        }
	        HttpGet httpGet = new HttpGet(usersEndpoint);
	        httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
	       
	        try (CloseableHttpClient httpClient = HttpClients.createDefault();
	            CloseableHttpResponse response = httpClient.execute(httpGet)) {
	            HttpEntity entity = response.getEntity();
	            if (entity != null) {
	                String jsonResponse = EntityUtils.toString(entity);
	                return jsonResponse;
	            }
	            return "";
	        } catch (HttpClientErrorException ex) {
	        	ex.printStackTrace();
	            throw new HttpClientErrorException(ex.getStatusCode(), ex.getMessage());
	        }
	        catch(HttpServerErrorException exc) {
	        	exc.printStackTrace();
	            throw new HttpServerErrorException( exc.getStatusCode(),exc.getMessage());
	        }
	        catch(Exception e) {
	        	throw new RuntimeException( e.getMessage());
	        }
	    
	}


	public Map<String, List<Map<String, String>>> getUserBySpecificRole() {

		String role = this.serviceRole;
		Map<String, List<Map<String, String>>> users = new HashMap<>();
		JsonNode nodes = getUserRoles();
		for (JsonNode node : nodes) {
			for (JsonNode innerNode : node.get("groups")) {
				if (innerNode.get("value").asText().equals(role)) {
					if (users.get(role) != null) {
						Map<String, String> userData = new HashMap<>();
						userData.put("email", node.get("emails").get(0).get("value").asText());
						userData.put("firstName", node.get("name").get("givenName").asText());
						userData.put("lastName", node.get("name").get("familyName").asText());
						users.get(role).add(userData);
					} else {
						List<Map<String, String>> tempList = new ArrayList<>();
						Map<String, String> userData = new HashMap<>();
						userData.put("email", node.get("emails").get(0).get("value").asText());
						userData.put("firstName", node.get("name").get("givenName").asText());
						userData.put("lastName", node.get("name").get("familyName").asText());
						tempList.add(userData);
						users.put(role, tempList);
					}
				}
			}
		}
		return users;
	}

	public JsonNode getUserRoles() {

		String jsonString = getUserDetailsFromUAA();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode jsonNode = objectMapper.readTree(jsonString);
			JsonNode resourcesArray = jsonNode.get(this.resources);
			return resourcesArray;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private String getCredentials(String jsonString, String credential) {
		
		ObjectMapper mapper = new ObjectMapper();
		logger.info("jsonString : "+jsonString);
		try {
			JsonNode jsonNode = mapper.readTree(jsonString);
			JsonNode xsuaNode = jsonNode.get(this.credentialsKey).get(0);
			if(xsuaNode!=null && !xsuaNode.isEmpty()) {
				JsonNode credentialNode = xsuaNode.get(this.credentials);
				if(credentialNode!=null && !credentialNode.isEmpty()) {
					return credentialNode.get(credential).asText();
				}
			}
			return "";
		}catch(Exception exc) {
			exc.printStackTrace();
			throw new RuntimeException(exc.getMessage());
		}
	}
}
