package org.yatech.sqlitedb.codegen.impl;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.yatech.sqlitedb.codegen.ParseException;
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

class JsonModelParser {
	
	private final Reader reader;
	private final Database database;
	private boolean executed = false;
	
	private JsonModelParser(Reader jsonReader) {
		this.reader = jsonReader;
		this.database = new Database();
	}

	public static Database parse(String jsonModel) throws ParseException {
		Reader reader = new StringReader(jsonModel);
		return parse(reader);
	}

	public static Database parse(Reader reader) throws ParseException {
		JsonModelParser parser = new JsonModelParser(reader);
		return parser.parse();
	}

	private Database parse() throws ParseException {
		checkExecutedState();
		JSONObject model = readAndParseJson();
		populateDatabase(model);
		return database;
	}

	private void checkExecutedState() {
		if (executed) {
			throw new IllegalStateException("Input already parsed");
		}
		executed = true;
	}

	private JSONObject readAndParseJson() throws ParseException {
		JSONParser parser = new JSONParser();
		try {
			return (JSONObject) parser.parse(reader);
		} catch (IOException e) {
			throw new ParseException("Failed reading JSON", e);
		} catch (org.json.simple.parser.ParseException e) {
			throw new ParseException("Failed parsing JSON", e);
		}
	}

	private void populateDatabase(JSONObject model) throws ParseException {
		try {
			database.setName((String)model.get(JsonModelConstants.Database.DATABASE_NAME));
			database.setTables(toModelTableList((JSONArray)model.get(JsonModelConstants.Database.TABLES)));
			database.setGenerationSettings(toModelGenerationSettings((JSONObject)model.get(JsonModelConstants.Database.GENERATION_SETTINGS)));
		} catch (Exception e) {
			throw new ParseException("Failed to read database model fom JSON", e);
		}
	}

	private List<Table> toModelTableList(JSONArray jsonArray) {
		List<Table> tables = new ArrayList<Table>();
		if (jsonArray != null) {
			for (Object jsonObject : jsonArray) {
				tables.add(toModelTable((JSONObject)jsonObject));
			}
		}
		return tables;
	}

	private Table toModelTable(JSONObject jsonObject) {
		Table table = new Table();
		table.setName((String)jsonObject.get(JsonModelConstants.Table.TABLE_NAME));
		table.setColumns(toModelColumnList((JSONArray)jsonObject.get(JsonModelConstants.Table.COLUMNS)));
		table.setConstraints(toModelTableConstraintList((JSONArray)jsonObject.get(JsonModelConstants.Table.CONSTRAINTS)));
		table.setGenerationSettings(toModelGenerationSettings((JSONObject)jsonObject.get(JsonModelConstants.Table.GENERATION_SETTINGS)));
		return table;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> toModelGenerationSettings(JSONObject jsonObject) {
		Map<String, Object> genSettings = new LinkedHashMap<String, Object>();
		genSettings.putAll(jsonObject);
		return genSettings;
	}

	private List<Column> toModelColumnList(JSONArray jsonArray) {
		List<Column> columns = new ArrayList<Column>();
		if (jsonArray != null) {
			for (Object jsonObject : jsonArray) {
				columns.add(toModelColumn((JSONObject)jsonObject));
			}
		}
		return columns;
	}

	private Column toModelColumn(JSONObject jsonObject) {
		Column column = new Column();
		column.setName((String)jsonObject.get(JsonModelConstants.Column.COLUMN_NAME));
		String dataTypeName = (String)jsonObject.get(JsonModelConstants.Column.DATA_TYPE);
		DataType dataType = dataTypeName == null ? DataType.NONE : DataType.valueOf(dataTypeName.toUpperCase());
		column.setType(dataType);
		column.setConstraints(toModelColumnConstraintList((JSONArray)jsonObject.get(JsonModelConstants.Column.CONSTRAINTS)));
		column.setGenerationSettings(toModelGenerationSettings((JSONObject)jsonObject.get(JsonModelConstants.Column.GENERATION_SETTINGS)));
		return column;
	}

	private List<ColumnConstraint> toModelColumnConstraintList(JSONArray jsonArray) {
		List<ColumnConstraint> constraints = new ArrayList<ColumnConstraint>();
		if (jsonArray != null) {
			for (Object jsonObject : jsonArray) {
				constraints.add(toModelColumnConstraint((JSONObject)jsonObject));
			}
		}
		return constraints;
	}

	private ColumnConstraint toModelColumnConstraint(JSONObject jsonObject) {
		String constraintType = (String)jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE);
		if (JsonModelConstants.Constraint.TYPE_PRIMARY_KEY.equals(constraintType)) {
			return toModelPrimaryKeyColumnConstraint(jsonObject);
		} else if (JsonModelConstants.Constraint.TYPE_FOREIGN_KEY.equals(constraintType)) {
			return toModelForeignKeyColumnConstraint(jsonObject);			
		} else if (JsonModelConstants.Constraint.TYPE_UNIQUE.equals(constraintType)) {
			return toModelUniqueColumnConstraint(jsonObject);
		} else if (JsonModelConstants.Constraint.TYPE_NOT_NULL.equals(constraintType)) {
			return toModelNotNullColumnConstraint(jsonObject);
		} else if (JsonModelConstants.Constraint.TYPE_DEFAULT_VALUE.equals(constraintType)) {
			return toModelDefauleValueColumnConstraint(jsonObject);
		}
		throw new InternalParseException(String.format("Unknown column constraint type: '%s'", constraintType));
	}

	private ColumnConstraint toModelPrimaryKeyColumnConstraint(JSONObject jsonObject) {
		PrimaryKeyColumnConstraint constraint = new PrimaryKeyColumnConstraint();
		Boolean autoIncrement = (Boolean)jsonObject.get(JsonModelConstants.Constraint.AUTO_INCREMENT);
		autoIncrement = autoIncrement == null ? false : autoIncrement;
		constraint.setAutoIncrement(autoIncrement);
		String sOrderDirection = (String)jsonObject.get(JsonModelConstants.Constraint.ORDER_DIRECTION);
		if (sOrderDirection != null) {
			constraint.setOrderDirection(OrderDirection.valueOf(sOrderDirection));			
		}
		return constraint;
	}

	private ColumnConstraint toModelForeignKeyColumnConstraint(JSONObject jsonObject) {
		ForeignKeyColumnConstraint constraint = new ForeignKeyColumnConstraint();
		constraint.setForeignTableName((String)jsonObject.get(JsonModelConstants.Constraint.FOREIGN_TABLE));
		constraint.setForeignColumnName((String)jsonObject.get(JsonModelConstants.Constraint.FOREIGN_COLUMN));
		return constraint;
	}

	private ColumnConstraint toModelUniqueColumnConstraint(JSONObject jsonObject) {
		UniqueColumnConstraint constraint = new UniqueColumnConstraint();
		return constraint;
	}

	private ColumnConstraint toModelNotNullColumnConstraint(JSONObject jsonObject) {
		NotNullColumnConstraint constraint = new NotNullColumnConstraint();
		return constraint;
	}

	private ColumnConstraint toModelDefauleValueColumnConstraint(JSONObject jsonObject) {
		DefaultValueColumnConstraint constraint = new DefaultValueColumnConstraint();
		constraint.setValue(jsonObject.get(JsonModelConstants.Constraint.VALUE));
		return constraint;
	}

	private List<TableConstraint> toModelTableConstraintList(JSONArray jsonArray) {
		List<TableConstraint> constraints = new ArrayList<TableConstraint>();
		if (jsonArray != null) {
			for (Object jsonObject : jsonArray) {
				constraints.add(toModelTableConstraint((JSONObject)jsonObject));
			}
		}
		return constraints;
	}

	private TableConstraint toModelTableConstraint(JSONObject jsonObject) {
		String constraintType = (String)jsonObject.get(JsonModelConstants.Constraint.CONSTRAINT_TYPE);
		if (JsonModelConstants.Constraint.TYPE_PRIMARY_KEY.equals(constraintType)) {
			return toModelPrimaryKeyTableConstraint(jsonObject);
		} else if (JsonModelConstants.Constraint.TYPE_FOREIGN_KEY.equals(constraintType)) {
			return toModelForeignKeyTableConstraint(jsonObject);			
		} else if (JsonModelConstants.Constraint.TYPE_UNIQUE.equals(constraintType)) {
			return toModelUniqueTableConstraint(jsonObject);
		}
		throw new InternalParseException(String.format("Unknown column constraint type: '%s'", constraintType));
	}

	private TableConstraint toModelPrimaryKeyTableConstraint(JSONObject jsonObject) {
		PrimaryKeyTableConstraint constraint = new PrimaryKeyTableConstraint();
		constraint.setColumns(toModelIndexedColumnList((JSONArray)jsonObject.get(JsonModelConstants.Constraint.COLUMNS)));
		return constraint;
	}

	private TableConstraint toModelForeignKeyTableConstraint(JSONObject jsonObject) {
		ForeignKeyTableConstraint constraint = new ForeignKeyTableConstraint();
		constraint.setColumnNames(toStringList((JSONArray)jsonObject.get(JsonModelConstants.Constraint.COLUMNS)));
		constraint.setForeignTableName((String)jsonObject.get(JsonModelConstants.Constraint.FOREIGN_TABLE));
		constraint.setForeignColumnNames(toStringList((JSONArray)jsonObject.get(JsonModelConstants.Constraint.FOREIGN_COLUMNS)));
		return constraint;
	}

	private List<String> toStringList(JSONArray jsonArray) {
		List<String> values = new ArrayList<String>();
		if (jsonArray != null) {
			for (Object value : jsonArray) {
				values.add(String.valueOf(value));
			}
		}
		return values;
	}

	private TableConstraint toModelUniqueTableConstraint(JSONObject jsonObject) {
		UniqueTableConstraint constraint = new UniqueTableConstraint();
		constraint.setColumns(toModelIndexedColumnList((JSONArray)jsonObject.get(JsonModelConstants.Constraint.COLUMNS)));
		return constraint;
	}

	private List<IndexedColumn> toModelIndexedColumnList(JSONArray jsonArray) {
		List<IndexedColumn> columns = new ArrayList<IndexedColumn>();
		if (jsonArray != null) {
			for (Object jsonObject : jsonArray) {
				columns.add(toModelIndexedColumn((JSONObject)jsonObject));
			}
		}
		return columns;
	}

	private IndexedColumn toModelIndexedColumn(JSONObject jsonObject) {
		IndexedColumn column = new IndexedColumn();
		column.setColumnName((String)jsonObject.get(JsonModelConstants.Constraint.INDEXED_COLUMN_NAME));
		String sOrderDirection = (String)jsonObject.get(JsonModelConstants.Constraint.ORDER_DIRECTION);
		if (sOrderDirection != null) {
			column.setOrderDirection(OrderDirection.valueOf(sOrderDirection));			
		}
		return column;
	}
	
	@SuppressWarnings("serial")
	private class InternalParseException extends RuntimeException {
		InternalParseException(String message) {
			super(message);
		}
	}

}
