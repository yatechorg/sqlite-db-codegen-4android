package org.yatech.sqlitedb.codegen.impl;

import org.yatech.sqlitedb.codegen.model.Column;
import org.yatech.sqlitedb.codegen.model.Database;
import org.yatech.sqlitedb.codegen.model.DefaultValueColumnConstraint;
import org.yatech.sqlitedb.codegen.model.ForeignKeyColumnConstraint;
import org.yatech.sqlitedb.codegen.model.ForeignKeyTableConstraint;
import org.yatech.sqlitedb.codegen.model.IndexedColumn;
import org.yatech.sqlitedb.codegen.model.NotNullColumnConstraint;
import org.yatech.sqlitedb.codegen.model.PrimaryKeyColumnConstraint;
import org.yatech.sqlitedb.codegen.model.PrimaryKeyTableConstraint;
import org.yatech.sqlitedb.codegen.model.Table;
import org.yatech.sqlitedb.codegen.model.UniqueColumnConstraint;
import org.yatech.sqlitedb.codegen.model.UniqueTableConstraint;
import org.yatech.sqlitedb.codegen.model.Visitor;

public abstract class AbstractModelVisitor implements Visitor {

	public void visit(Database database) {
		throw new UnsupportedOperationException("NOT IMPLEMENTED");
	}

	public void visit(Table table) {
		throw new UnsupportedOperationException("NOT IMPLEMENTED");
	}

	public void visit(PrimaryKeyTableConstraint constraint) {
		throw new UnsupportedOperationException("NOT IMPLEMENTED");
	}

	public void visit(ForeignKeyTableConstraint constraint) {
		throw new UnsupportedOperationException("NOT IMPLEMENTED");
	}

	public void visit(UniqueTableConstraint constraint) {
		throw new UnsupportedOperationException("NOT IMPLEMENTED");
	}

	public void visit(Column column) {
		throw new UnsupportedOperationException("NOT IMPLEMENTED");
	}

	public void visit(IndexedColumn indexedColumn) {
		throw new UnsupportedOperationException("NOT IMPLEMENTED");
	}

	public void visit(PrimaryKeyColumnConstraint constraint) {
		throw new UnsupportedOperationException("NOT IMPLEMENTED");
	}

	public void visit(ForeignKeyColumnConstraint constraint) {
		throw new UnsupportedOperationException("NOT IMPLEMENTED");
	}

	public void visit(UniqueColumnConstraint constraint) {
		throw new UnsupportedOperationException("NOT IMPLEMENTED");
	}

	public void visit(NotNullColumnConstraint constraint) {
		throw new UnsupportedOperationException("NOT IMPLEMENTED");
	}

	public void visit(DefaultValueColumnConstraint constraint) {
		throw new UnsupportedOperationException("NOT IMPLEMENTED");
	}

}
