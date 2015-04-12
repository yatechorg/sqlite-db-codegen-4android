package org.yatech.sqlitedb.codegen.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Column implements Visitable {
	
	private String name;
	private DataType type;
	private List<ColumnConstraint> constraints = new ArrayList<ColumnConstraint>();
	private Map<String, Object> generationSettings = new LinkedHashMap<String, Object>(); 

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public List<ColumnConstraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<ColumnConstraint> constraints) {
		this.constraints = constraints;
	}
	
	public Map<String, Object> getGenerationSettings() {
		return generationSettings;
	}

	public void setGenerationSettings(Map<String, Object> generationSettings) {
		this.generationSettings = generationSettings;
	}

	@Override
	public String toString() {
		return String.format("%s %s %s", name, type, constraints);
	}
	
}
