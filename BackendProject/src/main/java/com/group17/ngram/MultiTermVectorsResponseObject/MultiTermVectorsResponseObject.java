package com.group17.ngram.MultiTermVectorsResponseObject;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiTermVectorsResponseObject {

    // for storing all teh terms and there frequency
    private Map<String, TermVector> terms = new HashMap<>();

    // Jackson method for converting the JSON docs property to what I want...
    // which is all the terms from each document
    // Map<String, Object> is used for JSON objects (with different typed values
    //      e.g. int, string, or another JSON object)
    //
    // List<Map<String, Object>> is for a list/ array of JSON objects
    @SuppressWarnings("unchecked")
    @JsonProperty("docs")
    private void unpackNested(List<Map<String, Object>> docs) {
        for (Map<String, Object> doc : docs) {
            if(((Boolean) doc.get("found")) == true) {
                Map<String, Object> terms = (Map<String, Object>)(
                        (Map<String, Object>) (
                            (Map<String, Object>) doc.get("term_vectors")
                        ).get("text_field")
                    ).get("terms");
                for (Map.Entry<String, Object> entry : terms.entrySet()) {
                    String term = entry.getKey();
                    Integer doc_freq = (Integer) (
                            (Map<String, Object>) entry.getValue()
                    ).get("doc_freq");

                    this.terms.put(term, new TermVector(term, doc_freq));
                }
            }
        }
    }

    public Map<String, TermVector> getTerms() {
        return terms;
    }
}
