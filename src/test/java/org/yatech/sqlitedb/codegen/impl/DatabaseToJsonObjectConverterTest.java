package org.yatech.sqlitedb.codegen.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.yatech.sqlitedb.codegen.model.Column;
import org.yatech.sqlitedb.codegen.model.ColumnConstraint;
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
import org.yatech.sqlitedb.codegen.model.TableConstraint;
import org.yatech.sqlitedb.codegen.model.UniqueColumnConstraint;
import org.yatech.sqlitedb.codegen.model.UniqueTableConstraint;

public class DatabaseToJsonObjectConverterTest {
	
	private DatabaseToJsonObjectConverter converter = new DatabaseToJsonObjectConverter();

	@Test
	public void testVisitDatabase() {
		Database database = new Database();
		database.setName("MyDatabase");
		Map<String, Object> genSettings = new HashMap<String, Object>();
		genSettings.put("testKey", 77);
		database.setGenerationSettings(genSettings);
		Table table1 = new Table();
		Table table2 = new Table();
		database.setTables(Arrays.asList(table1, table2));
		
		database.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals("MyDatabase", jsonObject.get(JsonModelConstants.Database.DATABASE_NAME));
		assertEquals(77, ((JSONObject)jsonObject.get(JsonModelConstants.Database.GENERATION_SETTINGS)).get("testKey"));
		assertJsonArrayWithExpectedSize(2, jsonObject.get(JsonModelConstants.Database.TABLES));
	}

	@Test
	public void testVisitTable_WithConstraints() {
		Table table = new Table();
		table.setName("MyTable");
		Map<String, Object> genSettings = new HashMap<String, Object>();
		genSettings.put("testKey", true);
		table.setGenerationSettings(genSettings);
		Column column1 = new Column();
		Column column2 = new Column();
		table.setColumns(Arrays.asList(column1, column2));
		TableConstraint constraint1 = new PrimaryKeyTableConstraint();
		TableConstraint constraint2 = new PrimaryKeyTableConstraint();
		TableConstraint constraint3 = new PrimaryKeyTableConstraint();
		table.setConstraints(Arrays.asList(constraint1, constraint2, constraint3));
		
		table.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals("MyTable", jsonObject.get(JsonModelConstants.Table.TABLE_NAME));
		assertEquals(true, ((JSONObject)jsonObject.get(JsonModelConstants.Table.GENERATION_SETTINGS)).get("testKey"));
		assertJsonArrayWithExpectedSize(2, jsonObject.get(JsonModelConstants.Table.COLUMNS));
		assertJsonArrayWithExpectedSize(3, jsonObject.get(JsonModelConstants.Table.CONSTRAINTS));
	}

	@Test
	public void testVisitTable_ConstraintsNotSet() {
		Table table = new Table();
		table.setName("MyTable");
		Map<String, Object> genSettings = new HashMap<String, Object>();
		genSettings.put("testKey", true);
		table.setGenerationSettings(genSettings);
		Column column1 = new Column();
		Column column2 = new Column();
		table.setColumns(Arrays.asList(column1, column2));
		
		table.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals("MyTable", jsonObject.get(JsonModelConstants.Table.TABLE_NAME));
		assertEquals(true, ((JSONObject)jsonObject.get(JsonModelConstants.Table.GENERATION_SETTINGS)).get("testKey"));
		assertJsonArrayWithExpectedSize(2, jsonObject.get(JsonModelConstants.Table.COLUMNS));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Table.CONSTRAINTS));
	}

	@Test
	public void testVisitPrimaryKeyTableConstraint() {
		PrimaryKeyTableConstraint constraint = new PrimaryKeyTableConstraint();
		IndexedColumn column1 = new IndexedColumn();
		IndexedColumn column2 = new IndexedColumn();
		constraint.setColumns(Arrays.asList(column1, column2));
		
		constraint.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals(JsonModelConstants.Constraint.TYPE_PRIMARY_KEY, jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE));
		assertJsonArrayWithExpectedSize(2, jsonObject.get(JsonModelConstants.Constraint.COLUMNS));
	}

	@Test
	public void testVisitForeignKeyTableConstraint() {
		ForeignKeyTableConstraint constraint = new ForeignKeyTableConstraint();
		constraint.setColumnNames(Arrays.asList("A", "B", "C"));
		constraint.setForeignColumnNames(Arrays.asList("d", "e", "f", "g"));
		constraint.setForeignTableName("foo");
		
		constraint.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals(JsonModelConstants.Constraint.TYPE_FOREIGN_KEY, jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE));
		assertEquals("foo", jsonObject.get(JsonModelConstants.Constraint.FOREIGN_TABLE));
		assertJsonArrayWithExpectedSize(3, jsonObject.get(JsonModelConstants.Constraint.COLUMNS));
		assertEquals(Arrays.asList("A", "B", "C"), jsonObject.get(JsonModelConstants.Constraint.COLUMNS));
		assertJsonArrayWithExpectedSize(4, jsonObject.get(JsonModelConstants.Constraint.FOREIGN_COLUMNS));
		assertEquals(Arrays.asList("d", "e", "f", "g"), jsonObject.get(JsonModelConstants.Constraint.FOREIGN_COLUMNS));
	}

	@Test
	public void testVisitUniqueTableConstraint() {
		UniqueTableConstraint constraint = new UniqueTableConstraint();
		IndexedColumn column1 = new IndexedColumn();
		IndexedColumn column2 = new IndexedColumn();
		constraint.setColumns(Arrays.asList(column1, column2));
		
		constraint.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals(JsonModelConstants.Constraint.TYPE_UNIQUE, jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE));
		assertJsonArrayWithExpectedSize(2, jsonObject.get(JsonModelConstants.Constraint.COLUMNS));
	}

	@Test
	public void testVisitIndexedColumn_OrderDirectionASC() {
		IndexedColumn column = new IndexedColumn();
		column.setColumnName("Col1");
		column.setOrderDirection(OrderDirection.ASC);
		
		column.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals("Col1", jsonObject.get(JsonModelConstants.Constraint.INDEXED_COLUMN_NAME));
		assertEquals("ASC", jsonObject.get(JsonModelConstants.Constraint.ORDER_DIRECTION));
	}

	@Test
	public void testVisitIndexedColumn_OrderDirectionDESC() {
		IndexedColumn column = new IndexedColumn();
		column.setColumnName("Col1");
		column.setOrderDirection(OrderDirection.DESC);
		
		column.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals("Col1", jsonObject.get(JsonModelConstants.Constraint.INDEXED_COLUMN_NAME));
		assertEquals("DESC", jsonObject.get(JsonModelConstants.Constraint.ORDER_DIRECTION));
	}

	@Test
	public void testVisitIndexedColumn_OrderDirectionNotSet() {
		IndexedColumn column = new IndexedColumn();
		column.setColumnName("Col1");
		
		column.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals("Col1", jsonObject.get(JsonModelConstants.Constraint.INDEXED_COLUMN_NAME));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Constraint.ORDER_DIRECTION));
	}

	@Test
	public void testVisitColumn_DataTypeText() {
		Column column = new Column();
		column.setName("Col1");
		column.setType(DataType.TEXT);
		ColumnConstraint constraint1 = new PrimaryKeyColumnConstraint();
		ColumnConstraint constraint2 = new PrimaryKeyColumnConstraint();
		column.setConstraints(Arrays.asList(constraint1, constraint2));
		Map<String, Object> genSettings = new HashMap<String, Object>();
		genSettings.put("testKey", "hello-world!");
		column.setGenerationSettings(genSettings);
		
		column.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals("Col1", jsonObject.get(JsonModelConstants.Column.COLUMN_NAME));
		assertEquals("TEXT", jsonObject.get(JsonModelConstants.Column.DATA_TYPE));
		assertEquals("hello-world!", ((JSONObject)jsonObject.get(JsonModelConstants.Column.GENERATION_SETTINGS)).get("testKey"));
		assertJsonArrayWithExpectedSize(2, jsonObject.get(JsonModelConstants.Column.CONSTRAINTS));
	}

	@Test
	public void testVisitColumn_DataTypeBlob_ConstraintsNotSet_GenSettingsNotSet() {
		Column column = new Column();
		column.setName("Col1");
		column.setType(DataType.BLOB);
		
		column.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals("Col1", jsonObject.get(JsonModelConstants.Column.COLUMN_NAME));
		assertEquals("BLOB", jsonObject.get(JsonModelConstants.Column.DATA_TYPE));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Column.GENERATION_SETTINGS));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Column.CONSTRAINTS));
	}

	@Test
	public void testVisitColumn_DataTypeInteger_ConstraintsNotSet_GenSettingsNotSet() {
		Column column = new Column();
		column.setName("Col1");
		column.setType(DataType.INTEGER);
		
		column.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals("Col1", jsonObject.get(JsonModelConstants.Column.COLUMN_NAME));
		assertEquals("INTEGER", jsonObject.get(JsonModelConstants.Column.DATA_TYPE));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Column.GENERATION_SETTINGS));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Column.CONSTRAINTS));
	}

	@Test
	public void testVisitColumn_DataTypeReal_ConstraintsNotSet_GenSettingsNotSet() {
		Column column = new Column();
		column.setName("Col1");
		column.setType(DataType.REAL);
		
		column.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals("Col1", jsonObject.get(JsonModelConstants.Column.COLUMN_NAME));
		assertEquals("REAL", jsonObject.get(JsonModelConstants.Column.DATA_TYPE));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Column.GENERATION_SETTINGS));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Column.CONSTRAINTS));
	}

	@Test
	public void testVisitColumn_DataTypeNone_ConstraintsNotSet_GenSettingsNotSet() {
		Column column = new Column();
		column.setName("Col1");
		column.setType(DataType.NONE);
		
		column.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals("Col1", jsonObject.get(JsonModelConstants.Column.COLUMN_NAME));
		assertEquals("NONE", jsonObject.get(JsonModelConstants.Column.DATA_TYPE));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Column.GENERATION_SETTINGS));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Column.CONSTRAINTS));
	}

	@Test
	public void testVisitColumn_DataTypeNotSet_ConstraintsNotSet_GenSettingsNotSet() {
		Column column = new Column();
		column.setName("Col1");
		
		column.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals("Col1", jsonObject.get(JsonModelConstants.Column.COLUMN_NAME));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Column.DATA_TYPE));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Column.GENERATION_SETTINGS));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Column.CONSTRAINTS));
	}

	@Test
	public void testVisitPrimaryKeyColumnConstraint_AutoIncrementTrue_OrderDirectionASC() {
		PrimaryKeyColumnConstraint constraint = new PrimaryKeyColumnConstraint();
		constraint.setAutoIncrement(true);
		constraint.setOrderDirection(OrderDirection.ASC);
		
		constraint.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals(JsonModelConstants.Constraint.TYPE_PRIMARY_KEY, jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE));
		assertEquals(true, jsonObject.get(JsonModelConstants.Constraint.AUTO_INCREMENT));
		assertEquals("ASC", jsonObject.get(JsonModelConstants.Constraint.ORDER_DIRECTION));
	}

	@Test
	public void testVisitPrimaryKeyColumnConstraint_AutoIncrementFalse_OrderDirectionDESC() {
		PrimaryKeyColumnConstraint constraint = new PrimaryKeyColumnConstraint();
		constraint.setAutoIncrement(false);
		constraint.setOrderDirection(OrderDirection.DESC);
		
		constraint.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals(JsonModelConstants.Constraint.TYPE_PRIMARY_KEY, jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Constraint.AUTO_INCREMENT));
		assertEquals("DESC", jsonObject.get(JsonModelConstants.Constraint.ORDER_DIRECTION));
	}

	@Test
	public void testVisitPrimaryKeyColumnConstraint_AutoIncrementNotSet_OrderDirectionNotSet() {
		PrimaryKeyColumnConstraint constraint = new PrimaryKeyColumnConstraint();
		
		constraint.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals(JsonModelConstants.Constraint.TYPE_PRIMARY_KEY, jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Constraint.AUTO_INCREMENT));
		assertFalse(jsonObject.containsKey(JsonModelConstants.Constraint.ORDER_DIRECTION));
	}

	@Test
	public void testVisitForeignKeyColumnConstraint() {
		ForeignKeyColumnConstraint constraint = new ForeignKeyColumnConstraint();
		constraint.setForeignTableName("foo");
		constraint.setForeignColumnName("bar");
		
		constraint.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals(JsonModelConstants.Constraint.TYPE_FOREIGN_KEY, jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE));
		assertEquals("foo", jsonObject.get(JsonModelConstants.Constraint.FOREIGN_TABLE));
		assertEquals("bar", jsonObject.get(JsonModelConstants.Constraint.FOREIGN_COLUMN));
	}

	@Test
	public void testVisitUniqueColumnConstraint() {
		UniqueColumnConstraint constraint = new UniqueColumnConstraint();
		
		constraint.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals(JsonModelConstants.Constraint.TYPE_UNIQUE, jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE));
	}

	@Test
	public void testVisitNotNullColumnConstraint() {
		NotNullColumnConstraint constraint = new NotNullColumnConstraint();
		
		constraint.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals(JsonModelConstants.Constraint.TYPE_NOT_NULL, jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE));
	}

	@Test
	public void testVisitDefaultValueColumnConstraint_StringValue() {
		DefaultValueColumnConstraint constraint = new DefaultValueColumnConstraint();
		constraint.setValue("foo");
		
		constraint.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals(JsonModelConstants.Constraint.TYPE_DEFAULT_VALUE, jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE));
		assertEquals("foo", jsonObject.get(JsonModelConstants.Constraint.VALUE));
	}

	@Test
	public void testVisitDefaultValueColumnConstraint_IntValue() {
		DefaultValueColumnConstraint constraint = new DefaultValueColumnConstraint();
		constraint.setValue(77);
		
		constraint.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals(JsonModelConstants.Constraint.TYPE_DEFAULT_VALUE, jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE));
		assertEquals(77, jsonObject.get(JsonModelConstants.Constraint.VALUE));
	}

	@Test
	public void testVisitDefaultValueColumnConstraint_DoubleValue() {
		DefaultValueColumnConstraint constraint = new DefaultValueColumnConstraint();
		constraint.setValue(3.1415);
		
		constraint.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals(JsonModelConstants.Constraint.TYPE_DEFAULT_VALUE, jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE));
		assertEquals(3.1415, jsonObject.get(JsonModelConstants.Constraint.VALUE));
	}

	@Test
	public void testVisitDefaultValueColumnConstraint_NullValue() {
		DefaultValueColumnConstraint constraint = new DefaultValueColumnConstraint();
		constraint.setValue(null);
		
		constraint.accept(converter);
		Object result = converter.getResult();
		
		JSONObject jsonObject = assertAndGetJsonObject(result);
		assertEquals(JsonModelConstants.Constraint.TYPE_DEFAULT_VALUE, jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE));
		assertNull(jsonObject.get(JsonModelConstants.Constraint.VALUE));
	}

	private static JSONObject assertAndGetJsonObject(Object object) {
		assertNotNull(object);
		assertTrue(object instanceof JSONObject);
		return (JSONObject) object;
	}

	private static void assertJsonArrayWithExpectedSize(int expectedSize, Object object) {
		assertTrue("Not an instance of JSONArray, actual type: " + object.getClass().getName(), object instanceof JSONArray);
		JSONArray jsonArray = (JSONArray) object;
		assertEquals("JSONArray size not as expected. expected=" + expectedSize + ", actual=" + jsonArray.size(), expectedSize, jsonArray.size());
	}

}
