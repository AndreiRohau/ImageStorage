package com.epam.tmpl.dao.domain;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Essential domain object
 * 
 * @author Andrei_Rohau
 */
@Entity
@Table(name = "image")
public class Image extends BaseEntity {

	@Column
	private String name;
	@Column
	private String description;
	@Lob
	@Basic
	private String content;

	public Image() {
		super();
	}

	public Image(long id) {
		super();
		setId(id);
	}

	public Image(String name, String content) {
		super();
		this.name = name;
		this.description = "DATABASE";
		this.content = content;
	}

	public Image(String name, String description, String content) {
		super();
		this.name = name;
		this.description = description;
		this.content = content;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Image other = (Image) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Image [" + getId() + ", name=" + name + ", description=" + description + ", Content="
				+ content.length() + "]";
	}

}
