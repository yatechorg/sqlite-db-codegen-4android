package org.yatech.sqlitedb.codegen.model;

import java.util.List;

public class Column implements Visitable {
	
	private String name;
	private DataType type;
	private List<ColumnConstraint> constraints;

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
	
	@Override
	public String toString() {
		return String.format("%s %s %s", name, type, constraints);
	}
	
}
