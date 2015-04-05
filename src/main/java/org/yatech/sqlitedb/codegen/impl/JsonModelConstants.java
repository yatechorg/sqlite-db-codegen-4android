package org.yatech.sqlitedb.codegen.impl;

final class JsonModelConstants {
	
	private JsonModelConstants() {}
	
	static class Database {
		private Database() {}
		static final String DATABASE_NAME = "databaseName";
		static final String TABLES = "tables";
	}
	
	static class Table {
		private Table() {}
		static final String TABLE_NAME = "tableName";
		static final String COLUMNS = "columns";
		static final String CONSTRAINTS = "constraints";
	}
	
	static class Column {
		private Column() {}
		static final String COLUMN_NAME = "columnName";
		static final String DATA_TYPE = "dataType";
		static final String CONSTRAINTS = "constraints";
	}
	
	static class Constraint {
		static final String CONSTRAINT_TYPE = "constraintType";
		static final String VALUE = "value";
		static final String FOREIGN_TABLE = "foreignTable";
		static final String FOREIGN_COLUMN = "foreignColumn";
		static final String FOREIGN_COLUMNS = "foreignColumns";
		static final String COLUMNS = "columns";
		static final String AUTO_INCREMENT = "autoIncrement";
		static final String ORDER_DIRECTION = "orderDirection";
		static final String INDEXED_COLUMN_NAME = "indexedColumnName";
		static final String TYPE_PRIMARY_KEY = "PrimaryKey";
		static final String TYPE_FOREIGN_KEY = "ForeignKey";
		static final String TYPE_UNIQUE = "Unique";
		static final String TYPE_DEFAULT_VALUE = "DefaultValue";
	}
}
