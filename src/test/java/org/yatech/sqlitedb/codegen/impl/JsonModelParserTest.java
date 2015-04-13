package org.yatech.sqlitedb.codegen.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.junit.Test;
import org.yatech.sqlitedb.codegen.ParseException;
import org.yatech.sqlitedb.codegen.model.Column;
import org.yatech.sqlitedb.codegen.model.ColumnConstraint;
import org.yatech.sqlitedb.codegen.model.DataType;
import org.yatech.sqlitedb.codegen.model.Database;
import org.yatech.sqlitedb.codegen.model.DefaultValueColumnConstraint;
import org.yatech.sqlitedb.codegen.model.ForeignKeyColumnConstraint;
import org.yatech.sqlitedb.codegen.model.NotNullColumnConstraint;
import org.yatech.sqlitedb.codegen.model.OrderDirection;
import org.yatech.sqlitedb.codegen.model.PrimaryKeyColumnConstraint;
import org.yatech.sqlitedb.codegen.model.PrimaryKeyTableConstraint;
import org.yatech.sqlitedb.codegen.model.Table;
import org.yatech.sqlitedb.codegen.model.TableConstraint;
import org.yatech.sqlitedb.codegen.model.UniqueColumnConstraint;
import org.yatech.sqlitedb.codegen.model.Visitor;

public class JsonModelParserTest {

	@Test
	public void testParse() throws IOException, ParseException {
		InputStream inputStream = JsonModelParserTest.class.getResourceAsStream("/full_example.json");
		try {
			Database database = JsonModelParser.parse(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
			assertDatabase(database);
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

	private static void assertDatabase(Database database) {
		assertEquals("MY_DB", database.getName());
		assertEquals(2, database.getTables().size());
		assertTable1(database.getTables().get(0));
		assertTable2(database.getTables().get(1));
		assertEquals("mydb", database.getGenerationSettings().get("databaseFileName"));
	}
	
	private static void assertTable1(Table table) {
		assertEquals("TABLE1", table.getName());
		assertEquals(2, table.getColumns().size());
		assertColumn11(table.getColumns().get(0));
		assertColumn12(table.getColumns().get(1));
		assertEquals(0, table.getConstraints().size());
		assertEquals("Table1", table.getGenerationSettings().get("className"));
	}
	
	private static void assertColumn11(Column column) {
		assertEquals("COL1", column.getName());
		assertEquals(DataType.INTEGER, column.getType());
		assertEquals(1, column.getConstraints().size());
		assertConstraints(column, new AbstractModelVisitor() {
			@Override
			public void visit(PrimaryKeyColumnConstraint constraint) {
				assertEquals(OrderDirection.DESC, constraint.getOrderDirection());
				assertEquals(true, constraint.isAutoIncrement());
			}
		});
		assertEquals("col1", column.getGenerationSettings().get("fieldName"));
	}
	
	private static void assertColumn12(Column column) {
		assertEquals("COL2", column.getName());
		assertEquals(DataType.TEXT, column.getType());
		assertEquals(3, column.getConstraints().size());
		assertConstraints(column, new AbstractModelVisitor() {
			@Override
			public void visit(DefaultValueColumnConstraint constraint) {
				assertEquals("hello world!", constraint.getValue());
			}
			@Override
			public void visit(UniqueColumnConstraint constraint) { }
			@Override
			public void visit(NotNullColumnConstraint constraint) { }
		});
		assertEquals("col2", column.getGenerationSettings().get("fieldName"));
	}

	private static void assertConstraints(Column column, Visitor validator) {
		for (ColumnConstraint constraint : column.getConstraints()) {
			constraint.accept(validator);	
		}
	}
	
	private static void assertTable2(Table table) {
		assertEquals("TABLE2", table.getName());
		assertEquals(2, table.getColumns().size());
		assertColumn21(table.getColumns().get(0));
		assertColumn22(table.getColumns().get(1));
		assertEquals(1, table.getConstraints().size());
		assertConstraints(table, new AbstractModelVisitor() {
			@Override
			public void visit(PrimaryKeyTableConstraint constraint) {
				assertEquals(1, constraint.getColumns().size());
				assertEquals("COL1", constraint.getColumns().get(0).getColumnName());
				assertEquals(OrderDirection.ASC, constraint.getColumns().get(0).getOrderDirection());
			}
		});
		assertEquals("Table2", table.getGenerationSettings().get("className"));
		assertEquals("PREV_TABLE2", table.getGenerationSettings().get("renamedFrom"));
	}
	
	private static void assertColumn21(Column column) {
		assertEquals("COL1", column.getName());
		assertEquals(DataType.INTEGER, column.getType());
		assertEquals(0, column.getConstraints().size());
		assertEquals("col1", column.getGenerationSettings().get("fieldName"));
	}
	
	private static void assertColumn22(Column column) {
		assertEquals("COL2", column.getName());
		assertEquals(DataType.INTEGER, column.getType());
		assertEquals(1, column.getConstraints().size());
		assertConstraints(column, new AbstractModelVisitor() {
			@Override
			public void visit(ForeignKeyColumnConstraint constraint) {
				assertEquals("TABLE1", constraint.getForeignTableName());
				assertEquals("COL1", constraint.getForeignColumnName());
			}
		});
		assertEquals("col2", column.getGenerationSettings().get("fieldName"));
		assertEquals("PREV_COL2", column.getGenerationSettings().get("renamedFrom"));
	}

	private static void assertConstraints(Table table, Visitor validator) {
		for (TableConstraint constraint : table.getConstraints()) {
			constraint.accept(validator);	
		}
	}

}
