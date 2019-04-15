
package com.group17.ngram;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.apache.logging.log4j.Level;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.group17.ngram.termvector.MultiTermVectorsResponseObject;
import com.group17.ngram.termvector.TermVector;
import com.group17.util.LoggerUtil;

@Component  // for spring bean autowiring (dependency injection)
public class SearchboxGateway {
	/** The content type we're sending. */
	private static final String CONTENT_TYPE = "application/json";

	/** The base URL that all other request URLs derive from. */
	private static final String BASE_URL = "http://paas:a3bae525150e419cfe82fe2f52b1a5f4@gloin-eu-west-1.searchly.com/";
	/** The URL to do put requests to. */
	private static final String PUT_URL = BASE_URL + "master-test/doc/";
	/** The URL to do POST requests to. */
	private static final String POST_URL  = BASE_URL + "master-test/doc/_mtermvectors";
	/** The URL to do DELETE requests to. */
	private static final String DELETE_URL = PUT_URL;
	
	/**
	 * Insert a {@link Feedback} into the Searchbox database through a request.
	 * 
	 * @param id the identifier of the {@link Feedback}
	 * @param text the text of the {@link Feedback}
	 * @return whether it was successful
	 */
	protected boolean put(String id, String text) {
		try {
			delete(id); // Ensure there's no duplicates
			
			HttpClient httpClient = HttpClientBuilder.create().build();

			//post http request to the mtermvectors url
			HttpPost req = new HttpPost(PUT_URL + id);

			// build the data to send
			JsonObject body = new JsonObject();
			body.add("text_field", new JsonPrimitive(text));
			
			//convert the json to a string entity ready to send in the request
			StringEntity data = new StringEntity(body.toString());
			data.setContentType(CONTENT_TYPE);
			req.setEntity(data);

			// execute request
			httpClient.execute(req);
			
			LoggerUtil.log(Level.INFO, "PUT (" + id + ", " + text + ") " + " into searchbox");
			return true;
		} catch (Exception exception) {
			LoggerUtil.logException(exception);
			return false;
		}
	}
	
	/**
	 * Delete a {@link Feedback} from the Searchbox database through a request.
	 * 
	 * @param id the identifier of the {@link Feedback}
	 * @return whether it was successful
	 */
	protected boolean delete(String id) {
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();

			//post http request to the mtermvectors url
			HttpPost req = new HttpPost(DELETE_URL + id);
			httpClient.execute(req);
			
			LoggerUtil.log(Level.INFO, "DELETED " + id + " from searchbox");
			return true;
		} catch (Exception exception) {
			LoggerUtil.logException(exception);
			return false;
		}
	}
	
	/**
	 * Get the mTermVectors from the Searchbox database through a request.
	 * 
	 * @param ids the identifiers of the {@link Feedback} objects
	 * @param fields the fields of the mTermVectors to include
	 * @return a map of terms against their frequency
	 */
	protected Map<String, TermVector> getMTermVectors(Collection<String> ids, 
												  	  ArrayList<String> fields) {
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();

			//post http request to the mtermvectors url
			HttpPost req = new HttpPost(POST_URL);

			// build the data to send
			// attach ids
			JsonObject body = buildMTermVectorsBody(ids, fields);
			
			//convert the json to a string entity ready to send in the request
			StringEntity data = new StringEntity(body.toString());
			data.setContentType(CONTENT_TYPE);
			req.setEntity(data);

			// execute request and capture the response
			HttpResponse res = httpClient.execute(req);

			// use Jackson along with the custom Object to parse the response
			// down to just the terms and their frequencies
			MultiTermVectorsResponseObject response 
						= new ObjectMapper()
							.readValue(EntityUtils.toString(res.getEntity()),
									   MultiTermVectorsResponseObject.class);
			return response.getTerms();
		} catch (Exception exception) {
			LoggerUtil.logException(exception);
			return null;
		}
	}
	
	/**
	 * Build the body for requesting term vectors.
	 * 
	 * @param ids the identifiers of the {@link Feedback} objects
	 * @param fields the fields of the mTermVectors to include
	 * @return the body
	 */
	private JsonObject buildMTermVectorsBody(Collection<String> ids, Collection<String> fields) {
		JsonObject json = new JsonObject();
		JsonArray idJArray = new JsonArray();
		for(String id : ids) {
			idJArray.add(id);
		}
		json.add("ids", idJArray);

		// attach parameters, containing the fields to be requested
		JsonObject parameters = new JsonObject();
		JsonArray fieldsJArray = new JsonArray();
		for(String field : fields) {
			fieldsJArray.add(field);
		}
		parameters.add("fields", fieldsJArray);

		// add option parameters for term_stats and field_stats
		JsonPrimitive term_stats = new JsonPrimitive(true);
		JsonPrimitive field_statistics = new JsonPrimitive(false);
		parameters.add("term_statistics", term_stats);
		parameters.add("field_statistics", field_statistics);

		JsonObject filter = new JsonObject();
		filter.add("max_num_terms", new JsonPrimitive(10));
		filter.add("min_term_freq", new JsonPrimitive(1));
		filter.add("min_doc_freq", new JsonPrimitive(1));
		filter.add("min_word_length", new JsonPrimitive(10));
		parameters.add("filter", filter);
		json.add("parameters", parameters);
		
		return json;
	}
	
}
