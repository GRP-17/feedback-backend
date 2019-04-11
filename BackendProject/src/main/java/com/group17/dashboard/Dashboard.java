package com.group17.dashboard;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

/**
 * The Java Object; defining the schema, of the table we want to map to.
 */
@Entity
@Table(name = "dashboard", schema = "hy1xosk6o5taszzw")
public class Dashboard {
	/**
	 * The id of the dashboard, auto generated using the UUID generator (below).
	 * <p>
	 * It is always 36 characters long.
	 */
	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", columnDefinition = "VARCHAR(36)")
	private String id;

	/**
	 * The name of this Dashboard.
	 */
	@NotNull(message = "Invalid Dashboard Name")
	@Size(max = 65535)
	@Column(name = "name", columnDefinition = "text")
	private String name;	

	public Dashboard() {
	}

	public Dashboard(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
