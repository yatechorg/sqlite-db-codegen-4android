package org.yatech.sqlitedb.codegen.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.yatech.sqlitedb.codegen.ArtifactDescriptor;
import org.yatech.sqlitedb.codegen.CodeGenerator;
import org.yatech.sqlitedb.codegen.ParseException;
import org.yatech.sqlitedb.codegen.model.Database;

public class SQLiteDBCodeGenerator implements CodeGenerator {
	
	private static final String PROP_INPUT_JSON_MODEL = "input.json.model";
	private static final String PROP_GENERATE_DATA_ACCESS = "generate.data.access";
	private static final String PROP_GENERATED_PACKAGE = "generated.package";

	public List<ArtifactDescriptor> generate(String jsonModel, Properties properties) throws ParseException {
		Database model = parse(jsonModel);
		return generate(model, properties);
	}

	public List<ArtifactDescriptor> generate(String jsonModel, String prevJsonModel, Properties properties) throws ParseException {
		Database model = parse(jsonModel);
		Database prevModel = parse(prevJsonModel);
		return generate(model, prevModel, properties);
	}

	public List<ArtifactDescriptor> generate(Database model, Properties properties) {
		return generate(model, null, properties);
	}

	public List<ArtifactDescriptor> generate(Database model, Database prevModel, Properties properties) {
		List<ArtifactDescriptor> artifacts = new ArrayList<ArtifactDescriptor>();
		artifacts.addAll(generateDBManagementArtifacts(model, prevModel, properties));
		artifacts.addAll(generateDataAccessArtifacts(model, properties));
		artifacts.add(generateJsonModelArtifact(model, properties));
		return artifacts ;
	}

	private Collection<? extends ArtifactDescriptor> generateDBManagementArtifacts(Database model, Database prevModel, Properties properties) {
		// TODO Auto-generated method stub
		return null;
	}

	private Collection<? extends ArtifactDescriptor> generateDataAccessArtifacts(Database model, Properties properties) {
		// TODO Auto-generated method stub
		return null;
	}

	private ArtifactDescriptor generateJsonModelArtifact(Database model, Properties properties) {
		String jsonModel = getNonNullProperty(properties, PROP_INPUT_JSON_MODEL, null);
		if (jsonModel == null) {
			jsonModel = writeJsonModel(model);
		}
		String path = rootPath(properties);
		ArtifactDescriptor artifact = new ArtifactDescriptorImpl("database_model.json", path, jsonModel);
		return artifact;
	}

	private Database parse(String jsonModel) throws ParseException {
		return JsonModelParser.parse(jsonModel);
	}

	private String writeJsonModel(Database model) {
		return JsonModelWriter.toString(model);
	}

	private String rootPath(Properties properties) {
		String pkg = getGeneratedPackageName(properties);
		return pkg.replaceAll("\\.", "/");
	}
	
	private String getGeneratedPackageName(Properties properties) {
		return getNonNullProperty(properties, PROP_GENERATED_PACKAGE, "");
	}
	
	private boolean isGenerateDataAccess(Properties properties) {
		return getBooleanProperty(properties, PROP_GENERATE_DATA_ACCESS, true);
	}

	private boolean getBooleanProperty(Properties properties, String key, boolean defaultValue) {
		String value = getNonNullProperty(properties, key, Boolean.toString(defaultValue));
		return Boolean.valueOf(value);
	}
	
	private String getNonNullProperty(Properties properties, String key, String defaultValue) {
		String result = defaultValue;
		if (properties != null) {
			String value = properties.getProperty(key, result);
			if (value != null) {
				result = value;
			}
		}
		return result;
	}

}
