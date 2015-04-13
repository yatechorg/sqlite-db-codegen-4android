package org.yatech.sqlitedb.codegen.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Test;
import org.yatech.sqlitedb.codegen.model.Column;
import org.yatech.sqlitedb.codegen.model.DataType;
import org.yatech.sqlitedb.codegen.model.Database;
import org.yatech.sqlitedb.codegen.model.DefaultValueColumnConstraint;
import org.yatech.sqlitedb.codegen.model.ForeignKeyColumnConstraint;
import org.yatech.sqlitedb.codegen.model.ForeignKeyTableConstraint;
import org.yatech.sqlitedb.codegen.model.IndexedColumn;
import org.yatech.sqlitedb.codegen.model.NotNullColumnConstraint;
import org.yatech.sqlitedb.codegen.model.OrderDirection;
import org.yatech.sqlitedb.codegen.model.PrimaryKeyColumnConstraint;
import org.yatech.sqlitedb.codegen.model.PrimaryKeyTableConstraint;
import org.yatech.sqlitedb.codegen.model.Table;
import org.yatech.sqlitedb.codegen.model.UniqueColumnConstraint;

public class JsonModelWriterTest {

	@Test
	public void testToStringDatabase() throws IOException {
		Database database = createTestDatabase();
		String json = JsonModelWriter.toString(database);
		String expectedJson = readResourceAsString("/JsonModelWriterTest_expectedJson.json");
		assertEquals(expectedJson, json);
	}

	private Database createTestDatabase() {
		Database database = new Database("MY_DB");
		database.getGenerationSettings().put("foo1", "bar");
		database.getGenerationSettings().put("foo2", 17);
		database.getGenerationSettings().put("foo3", 1.618);
		database.setTables(Arrays.asList(createTable1(), createTable2()));
		return database;
	}

	private Table createTable1() {
		Table table = new Table("MY_TABLE1");
		// Column1
		Column column1 = new Column("MY_COL1", DataType.INTEGER);
		column1.getConstraints().add(new PrimaryKeyColumnConstraint(OrderDirection.ASC, true));
		column1.getGenerationSettings().put("hello","world!");
		// Column2
		Column column2 = new Column("MY_COL2", DataType.TEXT);
		
		table.setColumns(Arrays.asList(column1, column2));
		// No table constraints
		return table;
	}

	private Table createTable2() {
		Table table = new Table("MY_TABLE2");
		// Column1
		Column column1 = new Column("MY_COL11", DataType.NONE);
		column1.getConstraints().add(new NotNullColumnConstraint());
		column1.getConstraints().add(new UniqueColumnConstraint());
		// Column2
		Column column2 = new Column("MY_COL12", DataType.REAL);
		column2.getConstraints().add(new DefaultValueColumnConstraint(3.1415));
		column2.getGenerationSettings().put("hello","world!");
		// Column3
		Column column3 = new Column("MY_COL13", DataType.BLOB);
		// Column4
		Column column4 = new Column("MY_COL14");
		column4.getConstraints().add(new ForeignKeyColumnConstraint("MY_TABLE1", "MY_COL1"));
		// No data type
		table.setColumns(Arrays.asList(column1, column2, column3, column4));
		table.getConstraints().add(
				new PrimaryKeyTableConstraint(Arrays.asList(
						new IndexedColumn("MY_COL11"),
						new IndexedColumn("MY_COL12", OrderDirection.DESC))));
		table.getConstraints().add(
				new ForeignKeyTableConstraint(
						Arrays.asList("MY_COL11"),
						"MY_TABLE1",
						Arrays.asList("MY_COL1")));
		return table;
	}

	private String readResourceAsString(String resourcePath) throws IOException {
		InputStream inputStream = JsonModelWriterTest.class.getResourceAsStream(resourcePath);
		try {
			Writer writer = new StringWriter();
			Reader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
			char[] cbuf = new char[4096];
			int len, off = 0;
			while ((len = reader.read(cbuf)) > 0) {
				writer.write(cbuf, off, len);
				off += len;
			}
			writer.flush();
			return writer.toString();
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
	}

}
