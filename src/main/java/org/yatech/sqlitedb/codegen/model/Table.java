package org.yatech.sqlitedb.codegen.model;

import java.util.ArrayList;
import java.util.List;

public class Table implements Visitable {
	
	private String name;
	private List<Column> columns = new ArrayList<Column>();
	private List<TableConstraint> constraints = new ArrayList<TableConstraint>();

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
	
	@Override
	public String toString() {
		return String.valueOf(name);
	}
}
