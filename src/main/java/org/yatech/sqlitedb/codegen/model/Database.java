package org.yatech.sqlitedb.codegen.model;

import java.util.ArrayList;
import java.util.List;

public class Database implements Visitable {
	
	private String name;
	private List<Table> tables = new ArrayList<Table>();

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
	
}
