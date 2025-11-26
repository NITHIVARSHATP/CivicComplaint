package com.example.demo.models;


import jakarta.persistence.*;

import lombok.Data;
import java.util.Set;

@Data
@Entity
@Table(name = "departments")
public class Department {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @OneToMany(mappedBy = "department")
    private Set<User> officials;
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<User> getOfficials() {
		return officials;
	}

	public void setOfficials(Set<User> officials) {
		this.officials = officials;
	}

	
}
