package com.group17.label;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group17.dashboard.Dashboard;
import com.group17.feedback.Feedback;

@Entity
@Table(name = "label", schema = "hy1xosk6o5taszzw")
public class Label {
	/**
	 * The id of the feedback, auto generated using the UUID generator (below).
	 * <p>
	 * It is always 36 characters long.
	 */
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(columnDefinition = "VARCHAR(36)")
	private String labelId;
	
	/**
	 * The {@link Dashboard} this Label belongs to.
	 */
	@NotNull(message = "Invalid dashboardId")
	@Column(columnDefinition = "VARCHAR(36)")
	private String dashboardId;
	
	/** The name of the label. */
	@NotNull(message = "Bad Label Format")
	@Column(columnDefinition = "VARCHAR(36)")
	private String name;
	
	@NotNull(message = "Bad Label Color")
	@Size(min=7, max=7)
	@Column(columnDefinition = "VARCHAR(7)")
	private String color = "#28AF61"; // Default value of #28AF61 (green)
	
	@JsonIgnore // This has to be ignored as there is a circular dependency inside Feedback
	@ManyToMany(mappedBy = "labels")
    private Set<Feedback> feedback = new HashSet<Feedback>();

	public Label() {}

	public Label(String dashboardId, String name, String color) {
		this.dashboardId = dashboardId;
		this.name = name;
		this.color = color;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public String getLabelId() {
		return labelId;
	}
	
	public String getDashboardId() {
		return dashboardId;
	}
	
	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public Set<Feedback> getFeedback() {
		return feedback;
	}
	
}
