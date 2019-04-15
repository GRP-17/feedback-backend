package com.group17.ngram.termvector;

/**
 * A simple tuple object to store an n-gram term against
 * it's frequency used.
 */
public class TermVector implements Comparable<TermVector> {
    private final String term;
    private final int frequency;
    private final double score;

    public TermVector(String term, int frequency, double score) {
        this.term = term;
        this.frequency = frequency;
        this.score = score * frequency;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public String getTerm() {
        return term;
    }

    public Double getScore() {
        return score;
    }

    @Override
    public int compareTo(TermVector o) {
        return (int) (o.score - this.score);
    }
    
}
