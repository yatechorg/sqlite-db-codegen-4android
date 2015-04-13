package org.yatech.sqlitedb.codegen.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Table implements Visitable {
	
	private String name;
	private List<Column> columns = new ArrayList<Column>();
	private List<TableConstraint> constraints = new ArrayList<TableConstraint>();
	private Map<String, Object> generationSettings = new LinkedHashMap<String, Object>();
	
	public Table() { }
	
	public Table(String name) {
		this.name = name;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public List<TableConstraint> getConstraints() {
		return constraints;
	}

	public void setConstraints(List<TableConstraint> constraints) {
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
		return String.valueOf(name);
	}
}
