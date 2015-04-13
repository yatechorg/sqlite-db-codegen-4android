package org.yatech.sqlitedb.codegen.model;

import java.util.ArrayList;
import java.util.List;

public class PrimaryKeyTableConstraint extends TableConstraint {
	
	private List<IndexedColumn> columns = new ArrayList<IndexedColumn>();
	
	public PrimaryKeyTableConstraint() { }
	
	public PrimaryKeyTableConstraint(List<IndexedColumn> columns) { 
		this.columns = columns;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}

	public List<IndexedColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<IndexedColumn> columns) {
		this.columns = columns;
	}
	
	@Override
	public String toString() {
		return String.format("PRIMARY KEY (%s)", columns);
	}
}
