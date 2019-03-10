package com.group17.feedback.ngram;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.group17.feedback.ngram.MultiTermVectorsResponseObject.MultiTermVectorsResponseObject;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.Analyze;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MultiTermVectorsRequest;
import org.elasticsearch.client.core.TermVectorsRequest;
import org.elasticsearch.client.core.MultiTermVectorsResponse;
import org.elasticsearch.client.core.TermVectorsResponse;
import org.elasticsearch.client.core.TermVectorsResponse.TermVector;
import org.elasticsearch.client.core.TermVectorsResponse.TermVector.Term;
import org.elasticsearch.client.RestClient;

import com.group17.util.LoggerUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.logging.log4j.Level;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpHost;

@Component
public class SearchboxGateway {

	private static final String API_KEY = "paas:a3bae525150e419cfe82fe2f52b1a5f4";

	private static final RequestOptions COMMON_OPTIONS;
	static {
		RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
		String encodedApiKey = Base64.getEncoder().encodeToString(API_KEY.getBytes());
		builder.addHeader("Authorization", "Basic " + encodedApiKey);
		COMMON_OPTIONS = builder.build();
	}

	// @Autowired
	// private JestClient client;

	RestHighLevelClient client;

	// private JsonObject getAnalysis(String text) {
	// Analyze analyze = getAnalyze(text);
	// try {
	// JestResult result = client.execute(analyze);
	// return result.getJsonObject();
	// } catch (IOException e) {
	// e.printStackTrace();
	// return null;
	// }
	// }

	// private Analyze getAnalyze(String text) {
	// Analyze analyze = new
	// Analyze.Builder().analyzer("evolutionAnalyzer").index("test").text(text).build();
	// return analyze;
	// }

	public Map<String, Integer> getMTermVectors(ArrayList<String> ids, ArrayList<String> fields) {
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

		// HttpHost host = new HttpHost("gloin-eu-west-1.searchly.com", 80, "http");

		// client = new RestHighLevelClient(RestClient.builder(host));

		// // Setup Multi Term Vector Request
		// MultiTermVectorsRequest request = new MultiTermVectorsRequest();

		// for (String id : ids) {
		// // create a request template the documents - set an index and type
		// TermVectorsRequest tvrequest = new TermVectorsRequest("master_test", "doc",
		// id);

		// // set the fields to be used on each document
		// tvrequest.setFields("text_field");

		// // set the termstatistics
		// tvrequest.setTermStatistics(true);

		// // add each request to the multi request
		// request.add(tvrequest);
		// }

		// System.out.println("Built the request");
		// System.out.println(COMMON_OPTIONS.getHeaders());

		// try {
		// // System.out.println(client.ping(COMMON_OPTIONS));
		// MultiTermVectorsResponse response = client.mtermvectors(request,
		// COMMON_OPTIONS);

		// System.out.println("successful");

		// // List<TermVectorsResponse> tvresponseList =
		// // response.getTermVectorsResponses();

		// // if (tvresponseList != null) {
		// // for (TermVectorsResponse tvresponse : tvresponseList) {
		// // for (TermVector tv : tvresponse.getTermVectorsList()) {
		// // if (tv.getTerms() != null) {
		// // List<Term> terms = tv.getTerms();
		// // for (Term term : terms) {
		// // System.out.println("Term: " + term + ", Doc_freq: " + term.getDocFreq());
		// // }
		// // }
		// // }
		// // }
		// // }
		// } catch (Exception e) {
		// System.out.println("Exception " + e.toString());
		//
		return null;
	}

}
