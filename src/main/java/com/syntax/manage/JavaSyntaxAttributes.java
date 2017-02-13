package com.syntax.manage;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.text.AttributeSet;

import org.json.JSONException;
import org.json.JSONObject;

import com.syntax.resources.ResourceLoader;

import org.apache.commons.io.IOUtils;
/**
 * Distribute syntax AttributeSet according SyntaxKind
 * 
 * @see javax.swing.text.AttributeSet
 * @see SyntaxKind
 */
public class JavaSyntaxAttributes {
	private static final String JAVA_ATTRIBUTESET = "JavaAttribute.json";
	private static final String COLOR = "Color";

	private ArrayList<AttributeSet> attributeSetList;
	private JSONObject attJSON;
	/**
	 * Construct with .json file
	 * 
	 * @throws SyntaxException if fail to build with json file
	 */
	public JavaSyntaxAttributes() throws SyntaxException {
		String attributeText;
		try{
			attributeText = IOUtils.toString(ResourceLoader.load(JAVA_ATTRIBUTESET));
		} catch(IOException | NullPointerException e) {
			throw new SyntaxException("Can't find or open file " + JAVA_ATTRIBUTESET);
		}

		try {
			attJSON = new JSONObject(attributeText);
		} catch(JSONException e) {
			throw new SyntaxException(JAVA_ATTRIBUTESET + " format error. " + e.getMessage());
		}

		attributeSetList = new ArrayList<>();
		for(SyntaxKind kind: SyntaxKind.values())
			try {
				attributeSetList.add(getAttributeSetFromHexString(attJSON.getJSONObject(kind.key()).getString(COLOR)));
			} catch(JSONException e) {
				throw new SyntaxException("Can't find key \"" + kind.key() + "\" in " + JAVA_ATTRIBUTESET);
			}
	}
	private AttributeSet getAttributeSetFromHexString(String color) {
		return SyntaxManager.getAttributeSetWithForegroundColor(SyntaxManager.getColor(color));
	}
	/**
	 * Get attribute set of specified kind
	 * 
	 * @param kind sepcified kind
	 * @return attribute of this kind
	 */
	public AttributeSet getAttribute(SyntaxKind kind) {
		return attributeSetList.get(kind.code());
	}
	/**
	 * Classification of syntax
	 */
	public static enum SyntaxKind {
		CLASSMANAGINGTOOLS	("KeyWord.ClassManagingTools"),
		EXCEPTION			("KeyWord.Exception"),
		OBJECTREFERENCE		("KeyWord.ObjectReference"),
		RESERVEDFORFUTURE	("KeyWord.ReservedForFuture"),
		MODIFIERS			("KeyWord.Modifiers"),
		FLOWCONTROL			("KeyWord.FlowControl"),
		OPERATOR			("KeyWord.Operator"),
		PRIMITIVEDATATYPE	("KeyWord.PrimitiveDataType"),

		CLASSNAME		("ClassName"),
		BRACKETS		("Brackets"),
		CONSTANTSTRING	("ConstantString"),
		JAVAANNOTATION	("JavaAnnotation"),
		ANNOTATION		("Annotation"),
		CONSTANTNUMBER	("ConstantNumber"),
		CONSTANTVARIABLE("ConstantVariable"),
		NORMAL			("Normal");
		
		private String key;
		private int code;
		private static int counter = 0;
		private SyntaxKind(String key) {
			this.key = key;
			this.code = getCounter();
		}
		private int getCounter() {
			return counter++;
		}
		/**
		 * Get identity code of this kind
		 * @return identity code
		 */
		public int code() {
			return code;
		}
		/**
		 * Get key of this kind
		 * @return key
		 */
		public String key() {
			return key;
		}
		/**
		 * Convert string to SyntaxKind
		 * @param key key of kind
		 * @return null if key not match any key from this enumeration
		 */
		public static SyntaxKind getKind(String key) {
			for(SyntaxKind kind: SyntaxKind.values())
				if(kind.key().equals(key))
					return kind;
			return null;
		}
		/**
		 * Get string representation
		 * @return string representation
		 */
		@Override
		public String toString() {
			return key;
		}
	}
}