package org.yatech.sqlitedb.codegen.model;

public class UniqueColumnConstraint extends ColumnConstraint {

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
	@Override
	public String toString() {
		return "UNIQUE";
	}
}
