package org.yatech.sqlitedb.codegen;

import java.util.List;
import java.util.Properties;

import org.yatech.sqlitedb.codegen.model.Database;

public interface CodeGenerator {
	
	List<ArtifactDescriptor> generate(String jsonModel, Properties properties) throws ParseException;
	
	List<ArtifactDescriptor> generate(String jsonModel, String prevJsonModel, Properties properties) throws ParseException;
	
	List<ArtifactDescriptor> generate(Database model, Properties properties);
	
	List<ArtifactDescriptor> generate(Database model, Database prevModel, Properties properties);

}
