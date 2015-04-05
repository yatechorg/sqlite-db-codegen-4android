package org.yatech.sqlitedb.codegen.model;

public interface Visitor {

	void visit(Database database);
	void visit(Table table);
	void visit(PrimaryKeyTableConstraint constraint);
	void visit(ForeignKeyTableConstraint constraint);
	void visit(UniqueTableConstraint constraint);
	void visit(Column column);
	void visit(IndexedColumn indexedColumn);
	void visit(PrimaryKeyColumnConstraint constraint);
	void visit(ForeignKeyColumnConstraint constraint);
	void visit(UniqueColumnConstraint constraint);
	void visit(NotNullColumnConstraint constraint);
	void visit(DefaultValueColumnConstraint constraint);

}
