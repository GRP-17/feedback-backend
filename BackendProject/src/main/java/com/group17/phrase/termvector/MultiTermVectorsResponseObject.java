package com.group17.phrase.termvector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultiTermVectorsResponseObject {

	/** Stores the terms and their frequency. */
    private Map<String, TermVector> terms = new HashMap<String, TermVector>();

    /**
     * A Jackson method for converting the JSON docs property to all the
     * terms from each document.
     * 
     * @param docs a list/ array of JSON objects
     */
    @SuppressWarnings("unchecked")
    @JsonProperty("docs")
    private void unpackNested(List<Map<String, Object>> docs) {
        for (Map<String, Object> doc : docs) {
            
            // check if the doc has any terms
            if( ( (Boolean) doc.get("found") ) && ( ( (Map<String, Object>) doc.get("term_vectors") ).get("text_field") != null) ) {
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
                    Double score = (Double) (
                        (Map<String, Object>) entry.getValue()
                    ).get("score");

                    this.terms.put(term, new TermVector(term, doc_freq, score));
                }
            }
        }
    }

    public Map<String, TermVector> getTerms() {
        return terms;
    }
    
}
