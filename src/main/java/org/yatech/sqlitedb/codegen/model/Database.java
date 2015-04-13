package org.yatech.sqlitedb.codegen.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Database implements Visitable {
	
	private String name;
	private List<Table> tables = new ArrayList<Table>();
	private Map<String, Object> generationSettings = new LinkedHashMap<String, Object>();
	
	public Database() { }

	public Database(String name) { 
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

	public void setTables(List<Table> tables) {
		this.tables = tables;
	}
	
	public List<Table> getTables() {
		return tables;
	}

	public Map<String, Object> getGenerationSettings() {
		return generationSettings;
	}

	public void setGenerationSettings(Map<String, Object> generationSettings) {
		this.generationSettings = generationSettings;
	}
	
	@Override
	public String toString() {
		return String.format("Database '%s'", name);
	}
	
}
