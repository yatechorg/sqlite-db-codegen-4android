package org.yatech.sqlitedb.codegen.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.json.simple.JSONObject;
import org.yatech.sqlitedb.codegen.model.Database;

class JsonModelWriter {

	private final Database database;

	private JsonModelWriter(Database database) {
		this.database = database;
	}

	public static String toString(Database database) {
		try {
			Writer writer = new StringWriter();
			write(database, writer);
			return writer.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void write(Database database, Writer writer) throws IOException {
		JsonModelWriter modelWriter = new JsonModelWriter(database);
		modelWriter.write(writer);
	}

	private void write(Writer writer) throws IOException {
		JSONObject model = databaseToJsonObject();
		writer.write(model.toJSONString());
	}

	private JSONObject databaseToJsonObject() {
		DatabaseToJsonObjectConverter converter = new DatabaseToJsonObjectConverter();
		database.accept(converter);
		return (JSONObject) converter.getResult();
	}

}
