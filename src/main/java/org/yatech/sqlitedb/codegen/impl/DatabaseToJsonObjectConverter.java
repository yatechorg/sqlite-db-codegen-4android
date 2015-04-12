package org.yatech.sqlitedb.codegen.impl;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import org.yatech.sqlitedb.codegen.model.Visitor;

class DatabaseToJsonObjectConverter implements Visitor {
	
	private Object result;
	
	public Object getResult() {
		return result;
	}

	@SuppressWarnings("unchecked")
	public void visit(Database database) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonModelConstants.Database.DATABASE_NAME, database.getName());
		jsonObject.put(JsonModelConstants.Database.TABLES, tableListToJsonArray(database.getTables()));
		jsonObject.put(JsonModelConstants.Database.GENERATION_SETTINGS, generationSettingsToJsonObject(database.getGenerationSettings()));
		result = jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject generationSettingsToJsonObject(Map<String, Object> generationSettings) {
		JSONObject jsonObject = new JSONObject();
		if (generationSettings != null) {
			jsonObject.putAll(generationSettings);
		}
		return jsonObject;
	}

	@SuppressWarnings("unchecked")
	private JSONArray tableListToJsonArray(List<Table> tables) {
		JSONArray jsonArray = new JSONArray();
		if (tables != null) {
			for (Table table : tables) {
				table.accept(this);
				jsonArray.add(result);
			}
		}
		return jsonArray;
	}

	@SuppressWarnings("unchecked")
	public void visit(Table table) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonModelConstants.Table.TABLE_NAME, table.getName());
		jsonObject.put(JsonModelConstants.Table.COLUMNS, columnListToJsonArray(table.getColumns()));
		jsonObject.put(JsonModelConstants.Table.CONSTRAINTS, tableConstraintListToJsonArray(table.getConstraints()));
		jsonObject.put(JsonModelConstants.Table.GENERATION_SETTINGS, generationSettingsToJsonObject(table.getGenerationSettings()));
		result = jsonObject;
	}

	@SuppressWarnings("unchecked")
	private JSONArray columnListToJsonArray(List<Column> columns) {
		JSONArray jsonArray = new JSONArray();
		if (columns != null) {
			for (Column column : columns) {
				column.accept(this);
				jsonArray.add(result);
			}
		}
		return jsonArray;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray tableConstraintListToJsonArray(List<TableConstraint> constraints) {
		JSONArray jsonArray = new JSONArray();
		if (constraints != null) {
			for (TableConstraint constraint : constraints) {
				constraint.accept(this);
				jsonArray.add(result);
			}
		}
		return jsonArray;
	}

	@SuppressWarnings("unchecked")
	public void visit(PrimaryKeyTableConstraint constraint) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonModelConstants.Constraint.CONSTRAINT_TYPE, JsonModelConstants.Constraint.TYPE_PRIMARY_KEY);
		jsonObject.put(JsonModelConstants.Constraint.COLUMNS, indexedColumnListToJsonArray(constraint.getColumns()));
		result = jsonObject;
	}

	@SuppressWarnings("unchecked")
	public void visit(ForeignKeyTableConstraint constraint) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonModelConstants.Constraint.CONSTRAINT_TYPE, JsonModelConstants.Constraint.TYPE_FOREIGN_KEY);
		jsonObject.put(JsonModelConstants.Constraint.COLUMNS, stringListToJsonArray(constraint.getColumnNames()));
		jsonObject.put(JsonModelConstants.Constraint.FOREIGN_TABLE, constraint.getForeignTableName());
		jsonObject.put(JsonModelConstants.Constraint.FOREIGN_COLUMNS, stringListToJsonArray(constraint.getForeignColumnNames()));
		result = jsonObject;
	}

	@SuppressWarnings("unchecked")
	private Object stringListToJsonArray(List<String> strings) {
		JSONArray jsonArray = new JSONArray();
		if (strings != null) {
			jsonArray.addAll(strings);
		}
		return jsonArray;
	}

	@SuppressWarnings("unchecked")
	public void visit(UniqueTableConstraint constraint) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonModelConstants.Constraint.CONSTRAINT_TYPE, JsonModelConstants.Constraint.TYPE_UNIQUE);
		jsonObject.put(JsonModelConstants.Constraint.COLUMNS, indexedColumnListToJsonArray(constraint.getColumns()));
		result = jsonObject;
	}

	@SuppressWarnings("unchecked")
	private Object indexedColumnListToJsonArray(List<IndexedColumn> columns) {
		JSONArray jsonArray = new JSONArray();
		if (columns != null) {
			for (IndexedColumn column : columns) {
				column.accept(this);
				jsonArray.add(result);
			}
		}
		return jsonArray;
	}

	@SuppressWarnings("unchecked")
	public void visit(IndexedColumn indexedColumn) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonModelConstants.Constraint.INDEXED_COLUMN_NAME, indexedColumn.getColumnName());
		OrderDirection orderDirection = indexedColumn.getOrderDirection();
		if (orderDirection != null) {
			jsonObject.put(JsonModelConstants.Constraint.ORDER_DIRECTION, orderDirection.toString());	
		}
		result = jsonObject;
	}

	@SuppressWarnings("unchecked")
	public void visit(Column column) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonModelConstants.Column.COLUMN_NAME, column.getName());
		DataType type = column.getType();
		if (type != null) {
			jsonObject.put(JsonModelConstants.Column.DATA_TYPE, type.toString());
		}
		jsonObject.put(JsonModelConstants.Column.CONSTRAINTS, columnConstraintListToJsonArray(column.getConstraints()));
		jsonObject.put(JsonModelConstants.Column.GENERATION_SETTINGS, generationSettingsToJsonObject(column.getGenerationSettings()));
		result = jsonObject;
	}

	@SuppressWarnings("unchecked")
	private Object columnConstraintListToJsonArray(List<ColumnConstraint> constraints) {
		JSONArray jsonArray = new JSONArray();
		if (constraints != null) {
			for (ColumnConstraint constraint : constraints) {
				constraint.accept(this);
				jsonArray.add(result);
			}
		}
		return jsonArray;
	}

	@SuppressWarnings("unchecked")
	public void visit(PrimaryKeyColumnConstraint constraint) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonModelConstants.Constraint.CONSTRAINT_TYPE, JsonModelConstants.Constraint.TYPE_PRIMARY_KEY);
		OrderDirection orderDirection = constraint.getOrderDirection();
		if (orderDirection != null) {
			jsonObject.put(JsonModelConstants.Constraint.ORDER_DIRECTION, orderDirection.toString());	
		}
		result = jsonObject;
	}

	@SuppressWarnings("unchecked")
	public void visit(ForeignKeyColumnConstraint constraint) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonModelConstants.Constraint.CONSTRAINT_TYPE, JsonModelConstants.Constraint.TYPE_FOREIGN_KEY);
		jsonObject.put(JsonModelConstants.Constraint.FOREIGN_TABLE, constraint.getForeignTableName());
		jsonObject.put(JsonModelConstants.Constraint.FOREIGN_COLUMN, constraint.getForeignColumnName());
		result = jsonObject;
	}

	@SuppressWarnings("unchecked")
	public void visit(UniqueColumnConstraint constraint) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonModelConstants.Constraint.CONSTRAINT_TYPE, JsonModelConstants.Constraint.TYPE_UNIQUE);
		result = jsonObject;
	}

	@SuppressWarnings("unchecked")
	public void visit(NotNullColumnConstraint constraint) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonModelConstants.Constraint.CONSTRAINT_TYPE, JsonModelConstants.Constraint.TYPE_NOT_NULL);
		result = jsonObject;
	}

	@SuppressWarnings("unchecked")
	public void visit(DefaultValueColumnConstraint constraint) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonModelConstants.Constraint.CONSTRAINT_TYPE, JsonModelConstants.Constraint.TYPE_DEFAULT_VALUE);
		jsonObject.put(JsonModelConstants.Constraint.VALUE, constraint.getValue());
		result = jsonObject;
	}

}
