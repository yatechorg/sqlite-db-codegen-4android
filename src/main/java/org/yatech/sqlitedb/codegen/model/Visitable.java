package org.yatech.sqlitedb.codegen.model;

public interface Visitable {

	void accept(Visitor visitor);
	
}
