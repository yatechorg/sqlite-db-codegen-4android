package org.yatech.sqlitedb.codegen.model;

import java.util.ArrayList;
import java.util.List;

public class ForeignKeyTableConstraint extends TableConstraint {

	private List<String> columnNames = new ArrayList<String>();
	private String foreignTableName;
	private List<String> foreignColumnNames = new ArrayList<String>();

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public String getForeignTableName() {
		return foreignTableName;
	}

	public void setForeignTableName(String foreignTableName) {
		this.foreignTableName = foreignTableName;
	}

	public List<String> getForeignColumnNames() {
		return foreignColumnNames;
	}

	public void setForeignColumnNames(List<String> foreignColumnNames) {
		this.foreignColumnNames = foreignColumnNames;
	}
	
	@Override
	public String toString() {
		return String.format("FOREIGN KEY (%s) REFERENCES %s (%s)", columnNames, foreignTableName, foreignColumnNames);
	}
	
}
