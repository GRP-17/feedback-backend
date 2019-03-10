package com.group17.feedback.ngram;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.group17.feedback.ngram.MultiTermVectorsResponseObject.MultiTermVectorsResponseObject;
import com.group17.feedback.ngram.MultiTermVectorsResponseObject.TermVector;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

@Component
public class SearchboxGateway {

	public Map<String, TermVector> getMTermVectors(ArrayList<String> ids, ArrayList<String> fields) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();

			//post http request to the mtermvectors url
			HttpPost req = new HttpPost(
					"http://paas:a3bae525150e419cfe82fe2f52b1a5f4@gloin-eu-west-1.searchly.com/master-test/doc/_mtermvectors");

			// build the data to send
			// attach ids
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
			json.add("parameters", parameters);

			//convert the json to a string entity ready to send in the request
			StringEntity data = new StringEntity(json.toString());
			data.setContentType("application/json");
			req.setEntity(data);

			// execute request and capture the response
			HttpResponse res = httpClient.execute(req);

			// use Jackson along with the custom Object to parse the response
			// down to just the terms and their frequencies
			MultiTermVectorsResponseObject response = new ObjectMapper().readValue(EntityUtils.toString(res.getEntity()),
					MultiTermVectorsResponseObject.class);
			System.out.println(response.getTerms());
			return response.getTerms();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
