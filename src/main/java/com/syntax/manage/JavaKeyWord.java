package com.syntax.manage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.syntax.resources.ResourceLoader;

public class JavaKeyWord {
    private static final String JAVAKEYWORD = "JavaKeyWord.json" ;
    private JSONObject keyWordJSON;
    private ArrayList<Set<String>> keyWordListSet;
    private ArrayList<KeyWordKind> kindList;
    /**
     * Construct with .json file
     * 
     * @throws SyntaxException if file is not found or file format error
     */
    public JavaKeyWord() throws SyntaxException {
		String keyWordJSONText;
		try {
			keyWordJSONText = IOUtils.toString(ResourceLoader.load(JAVAKEYWORD));
		} catch(IOException | NullPointerException e) {
			throw new SyntaxException("Can't find or open file " + JAVAKEYWORD);
		}
        try {
            keyWordJSON = new JSONObject(keyWordJSONText);
        } catch(JSONException e) {
			throw new SyntaxException(JAVAKEYWORD + " format error. " + e.getMessage());
        }
        keyWordListSet = new ArrayList<>();
        kindList = new ArrayList<>();
        for(KeyWordKind kind: KeyWordKind.values()) {
            keyWordListSet.add(getWordsSet(kind.key()));
            kindList.add(kind);
        }
    }
    
    private Set<String> getWordsSet(String key) throws SyntaxException {
        try {
            JSONArray arr = keyWordJSON.getJSONArray(key);
            Set<String> words = new TreeSet<>();
            for(int i = 0; i < arr.length(); i++)
                words.add(arr.getString(i));
            return words;
        } catch(JSONException e) {
			throw new SyntaxException("Can't find key \"" + key + "\" in " + JAVAKEYWORD);
        }
    }
    /**
     * Classify string with java key word
     * 
     * @param word specified word
     * @return kind of word
     */
    public KeyWordKind getKind(String word) {
        for(int i = 0; i < keyWordListSet.size(); i++) {
            if(keyWordListSet.get(i).contains(word))
                return kindList.get(i);
        }
        return null;
    }
    /**
     * Classification of java key word
     */
    public static enum KeyWordKind {
        CLASSMANAGINGTOOLS  ("KeyWord.ClassManagingTools"),
        EXCEPTION           ("KeyWord.Exception"),
        OBJECTREFERENCE     ("KeyWord.ObjectReference"),
        RESERVEDFORFUTURE   ("KeyWord.ReservedForFuture"),
        MODIFIERS           ("KeyWord.Modifiers"),
        FLOWCONTROL         ("KeyWord.FlowControl"),
        OPERATOR            ("KeyWord.Operator"),
        PRIMITIVEDATATYPE   ("KeyWord.PrimitiveDataType");

        private String key;
        private KeyWordKind(String key) {
            this.key = key;
        }
        /**
         * Key of this kind
         * 
         * @return key
         * 
         * @see JavaCodeAdapter
         */
        public String key() {
            return key;
        }
        /**
         * Convert this kind to string
         * 
         * @return string representation
         */
        @Override
        public String toString() {
            return key;
        }
    }
}