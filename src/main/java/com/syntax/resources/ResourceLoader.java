package com.syntax.resources;

import java.io.InputStream;

final public class ResourceLoader {
	/**
	 * Convert resource to {@link InputStream InputStream}
	 * 
	 * @param path source file path
	 * @return source input stream
	 */
	public static InputStream load(String path) {
		InputStream input = ResourceLoader.class.getResourceAsStream(path);
		if(input == null)
			input = ResourceLoader.class.getResourceAsStream("/" + path);
		return input;
	}
}
