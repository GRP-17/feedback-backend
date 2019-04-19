package com.group17.label;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "feedback_label", schema = "hy1xosk6o5taszzw")
public class FeedbackLabel {
	@EmbeddedId
	private FeedbackLabelKey id;
	
	@Embeddable
	public static class FeedbackLabelKey implements Serializable {
	 
	    @Column(name = "feedback_id")
	    String feedbackId;
	 
	    @Column(name = "label_id")
	    String labelId;
	 
	}

}
