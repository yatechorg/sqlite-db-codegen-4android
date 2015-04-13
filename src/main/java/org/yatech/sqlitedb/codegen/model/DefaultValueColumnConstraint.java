package org.yatech.sqlitedb.codegen.model;

public class DefaultValueColumnConstraint extends ColumnConstraint {
	
	private Object value;
	
	public DefaultValueColumnConstraint() { }
	
	public DefaultValueColumnConstraint(Object value) {
		this.value = value;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public Object getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.format("DEFAULT %s", value);
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
	
}
