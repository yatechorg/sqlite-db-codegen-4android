package org.yatech.sqlitedb.codegen.model;

public class NotNullColumnConstraint extends ColumnConstraint {

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return "NOT NULL";
	}
}
