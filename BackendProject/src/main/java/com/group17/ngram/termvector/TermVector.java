package com.group17.ngram.termvector;

public class TermVector implements Comparable<TermVector> {
    private final String term;
    private final int frequency;

    public TermVector(String term, int frequency) {
        this.term = term;
        this.frequency = frequency;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public String getTerm() {
        return term;
    }

    @Override
    public int compareTo(TermVector o) {
        return o.frequency - this.frequency;
    }
    
}
