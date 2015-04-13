package org.yatech.sqlitedb.codegen.model;

public class ForeignKeyColumnConstraint extends ColumnConstraint {
	
	private String foreignTableName;
	private String foreignColumnName;
	
	public ForeignKeyColumnConstraint() { }
	
	public ForeignKeyColumnConstraint(String foreignTableName, String foreignColumnName) { 
		this.foreignTableName = foreignTableName;
		this.foreignColumnName = foreignColumnName;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public String getForeignTableName() {
		return foreignTableName;
	}

	public void setForeignTableName(String foreignTableName) {
		this.foreignTableName = foreignTableName;
	}

	public String getForeignColumnName() {
		return foreignColumnName;
	}

	public void setForeignColumnName(String foreignColumnName) {
		this.foreignColumnName = foreignColumnName;
	}
	
	@Override
	public String toString() {
		return String.format("REFERENCES % (%s)", foreignTableName, foreignColumnName);
	}
	
}
