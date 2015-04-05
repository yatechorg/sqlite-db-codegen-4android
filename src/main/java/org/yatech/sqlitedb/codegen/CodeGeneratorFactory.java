package org.yatech.sqlitedb.codegen;

import org.yatech.sqlitedb.codegen.impl.SQLiteDBCodeGenerator;

public class CodeGeneratorFactory {
	
	private static final CodeGeneratorFactory instance = new CodeGeneratorFactory();
	
	private CodeGeneratorFactory() {}
	
	public static CodeGeneratorFactory getInstance() {
		return instance;
	}
	
	public CodeGenerator createSQLiteDBCodeGenerator() {
		return new SQLiteDBCodeGenerator();
	}

}
