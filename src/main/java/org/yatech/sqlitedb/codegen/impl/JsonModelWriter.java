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
			JsonModelWriter modelWriter = new JsonModelWriter(database);
			Writer writer = new StringWriter();
			modelWriter.write(writer);
			return writer.toString();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
