package com.group17.dashboard;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "dashboard", schema = "hy1xosk6o5taszzw")
public class Dashboard {
	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "INT(11)")
	private int id;

	@NotNull(message = "Invalid Dashboard Name")
	@Size(max = 65535)
	@Column(name = "name", columnDefinition = "text")
	private String name;	

	public Dashboard() {
	}

	public Dashboard(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
}
