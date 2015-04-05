package org.yatech.sqlitedb.codegen.impl;

import org.yatech.sqlitedb.codegen.ArtifactDescriptor;

public class ArtifactDescriptorImpl implements ArtifactDescriptor {
	
	private final String name;
	private final String path;
	private final String content;
	
	public ArtifactDescriptorImpl(String name, String path, String content) {
		this.name = name;
		this.path = path;
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public String getContent() {
		return content;
	}
	
	@Override
	public String toString() {
		return String.format("%s/%s", path, name);
	}

}
