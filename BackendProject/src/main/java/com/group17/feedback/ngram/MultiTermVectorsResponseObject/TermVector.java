package com.group17.feedback.ngram.MultiTermVectorsResponseObject;

public class TermVector implements Comparable<TermVector>{
    private final String term;
    private final Integer doc_freq;

    public TermVector(String term, Integer doc_freq) {
        this.term = term;
        this.doc_freq = doc_freq;
    }

    public Integer getDoc_freq() {
        return doc_freq;
    }

    public String getTerm() {
        return term;
    }

    @Override
    public int compareTo(TermVector o) {
        return this.doc_freq - o.getDoc_freq();
    }
}
