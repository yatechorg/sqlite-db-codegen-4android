package org.yatech.sqlitedb.codegen.model;

import java.util.List;

public class UniqueTableConstraint extends TableConstraint {
	
	private List<IndexedColumn> columns;

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
		return String.format("UNIQUE (%s)", columns);
	}
}
