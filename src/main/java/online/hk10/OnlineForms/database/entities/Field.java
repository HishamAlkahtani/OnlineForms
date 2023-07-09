package online.hk10.OnlineForms.database.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

// Represents a question
@JsonInclude(Include.NON_NULL)
public class Field {
	String title; // Title of group of field (question)
	String description;
	FieldType type;
	// For checkboxes or mutliple choices, NULL otherwise.
	String[] values;

	
	public Field(String title, String description, FieldType type, String... str) {
		this.title = title;
		this.description = description;
		this.type = type;
		this.values = str;
	}
	
	// For text fields
	public Field(String title, String description) {
		this.title = title;
		this.description = description;
		this.type = FieldType.TEXT_FIELD;
	}
	
	public Field() {
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

}
