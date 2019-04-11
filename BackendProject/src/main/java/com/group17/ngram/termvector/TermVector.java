package com.group17.ngram.termvector;

/**
 * A simple tuple object to store an n-gram term against
 * it's frequency used.
 */
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
