package com.syntax.manage;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import com.syntax.code.StyledTextBody;
import com.syntax.manage.AbstractCodeAdapter;
import com.syntax.manage.JavaKeyWord.KeyWordKind;
import com.syntax.manage.JavaSyntaxAttributes.SyntaxKind;
/**
 * An implementation of AbstractCodeAdapter. It can handle syntax coloring of java file
 * 
 * @see AbstractCodeAdapter
 */
public class JavaCodeAdapter implements AbstractCodeAdapter {
	private static final int NONE = 0;
	private static final int LEFT_STYLE_LEFT_BRACKET	= (1 << 0);
	private static final int RIGHT_STYLE_LEFT_BRACKET	= (1 << 1);
	private static final int RIGHT_CLEAR_RIGHT_BRACKET	= (1 << 2);
	private static final int LEFT_CLEAR_RIGHT_BRACKET	= (1 << 3);
	private static final int WHILE_DO_FOR_IF_ELSE		= (1 << 4);
	private static final char bracket[] = "-+*/()\\[\\]{}<>:".toCharArray();
    private JavaSyntaxAttributes mJavaAttributes;
	private JavaKeyWord mJavaKeyWord;
	/**
	 * Construct JavaCodeAdapter
	 * 
	 * @throws SyntaxException if fail to read java attribute or key word
	 */
    public JavaCodeAdapter() throws SyntaxException {
        mJavaAttributes = new JavaSyntaxAttributes();
		mJavaKeyWord = new JavaKeyWord();
    }
    /**
     * Get default attribute from SyntaxManager which is specified by
	 * {@link #JavaCodeAdapter() constructor}
     * 
     * @return attribute for SyntaxTextArea
     */
    @Override
    public AttributeSet getDefaultAttributeSet() {
        return SyntaxManager.GLOBAL_ATTRIBUTESET;
    }
	/**
	 * Color syntax after text inserted into SyntaxTextArea. Color and Keyword
	 * collected by JavaSyntaxAttributes and JavaKeyWord
	 * 
	 * @see JavaSyntaxAttributes
	 * @see JavaKeyWord
	 */
	@Override
    public synchronized void insertString(int start, String text, StyledTextBody textBody, SyntaxPainter painter) {
		int begin, end;
        int textLength = textBody.length();
		char txt[] = textBody.getText().toCharArray();
		String relateChange = textBody.getText().substring(Math.max(start - 1, 0), Math.min(start + text.length() + 1, textLength));
		if(containAnnotation(relateChange)) {
			begin = 0;
			end = textLength - 1;
		} else {
			begin = lineBegin(start, txt);
			end = lineEnd(start + text.length() - 1, txt);
		}
		startPainting(begin, end, txt, painter);

		addTabs(start, text, textBody, painter);
    }
	/**
	 * Color syntax after text removed from SyntaxTextArea. Color and Keyword
	 * collected by JavaSyntaxAttributes and JavaKeyWord
	 * 
	 * @see JavaSyntaxAttributes
	 * @see JavaKeyWord
	 */
	@Override
    public synchronized void remove(int start, String text, StyledTextBody textBody, SyntaxPainter painter) {
		String relate = textBody.getText().substring(Math.max(start - 1, 0), Math.min(start + text.length() + 1, textBody.length()));
		boolean flag = containAnnotation(relate);
		int textLength= textBody.length();
		if(textLength != 0) {
			char txt[] = textBody.getText().toCharArray();
			int begin, end;
			if(flag) {
				begin = 0;
				end = textLength - 1;
			} else {
				begin = lineBegin(Math.min(start, textLength - 1), txt);
				end = lineEnd(Math.min(start, textLength - 1), txt);
			}
			startPainting(begin, end, txt, painter);
		}
    }

    private void startPainting(int begin, int end, char txt[], SyntaxPainter painter) {
		words(begin, end, txt, painter);
		bracketsAndOperator(begin, end, txt, painter);
		constantNumber(begin, end, txt, painter);
		constString(begin, end, txt, '"', painter);
		constString(begin, end, txt, '\'', painter);
		javaAnnotation(begin, end, txt, painter);
		annotationOneLine(begin, end, txt, painter);
		annotationMultiLine(txt, painter);
	}
    private void words(int start, int end, char txt[],  SyntaxPainter painter) {
		int left;
		for(left = start; left <= end; left++)
			if(isNameAllowed(txt[left]))
				break;
		for(int begin = left, right = left; right <= end + 1; right++)
			if(right == end + 1 || isNameAllowed(txt[right]) == false) {
				String substr = new String(txt, begin, right - begin);
				KeyWordKind kind = mJavaKeyWord.getKind(substr);
				if(kind != null)
                    painter.paintSyntax(begin, right - begin, mJavaAttributes.getAttribute(SyntaxKind.getKind(kind.key())), true);
				/*else if(isConstant(substr))
				    painter.paintSyntax(begin, right - begin, mJavaAttributes.getAttribute(SyntaxKind.CONSTANTVARIABLE), true);*/
				else if(isClassName(substr))
				    painter.paintSyntax(begin, right - begin, mJavaAttributes.getAttribute(SyntaxKind.CLASSNAME), true);
				else
				    painter.paintSyntax(begin, right - begin, mJavaAttributes.getAttribute(SyntaxKind.NORMAL), true);
				while(right <= end && isNameAllowed(txt[right]) == false)
					right++;
				begin = right;
			}
	}
	private void constantNumber(int start, int end, char txt[],  SyntaxPainter painter) {
		int left;
		for(left = start; left <= end; left++)
			if(isConstantNumberAllowed(txt[left]))
				break;
		for(int begin = left, right = left; right <= end + 1; right++)
			if(right == end + 1 || isConstantNumberAllowed(txt[right]) == false) {
				String substr = new String(txt, begin, right - begin);
                if(isNumber(substr))
				    painter.paintSyntax(begin, right - begin, mJavaAttributes.getAttribute(SyntaxKind.CONSTANTNUMBER), true);
				while(right <= end && isConstantNumberAllowed(txt[right]) == false)
					right++;
				begin = right;
			}
	}
    private void bracketsAndOperator(int offset, int endPoint, char txt[],  SyntaxPainter painter) {	//close interval
		for(int i = offset; i <= endPoint; i++)
			if(isBracket(txt[i]))
                    painter.paintSyntax(i, 1, mJavaAttributes.getAttribute(SyntaxKind.BRACKETS), true);
	}
    private void constString(int offset, int endPoint, char txt[], char Flag, SyntaxPainter painter) {
		boolean inTheString = false;
		boolean findEndLine = false;
		int textLength = txt.length;
		int begin = offset;
		for(int index = offset; index < textLength && (index <= endPoint || !findEndLine); index++) {
			if(inTheString && txt[index] == Flag && (index == 0 || txt[index-1] != '\\'))
                painter.paintSyntax(begin, index - begin + 1, mJavaAttributes.getAttribute(SyntaxKind.CONSTANTSTRING), true);
			if(txt[index] == Flag && (index == 0 || txt[index-1] != '\\')) {
				inTheString = !inTheString;
				begin = index;
			}
			if(txt[index] == '\n') {
				findEndLine = true;
				inTheString = false;
				begin = index + 1;
			}
		}
	}
    private void javaAnnotation(int offset, int endPoint, char txt[], SyntaxPainter painter) {
		int textLength = txt.length;
		int index = offset;
		while(index <= endPoint) {
			int begin;
			for(begin = index; begin <= endPoint && txt[begin] != '\n'; begin++)
				if(isSpaceWord(txt[begin]) == false)
					break;
			if(begin <= endPoint && txt[begin] == '@')
				for(int end = begin; end < textLength; end++)
					if(end + 1 == textLength || isSpaceWord(txt[end])) {
						int length = end - begin + (end + 1 == textLength ? 1 : 0);
                        painter.paintSyntax(begin, length, mJavaAttributes.getAttribute(SyntaxKind.JAVAANNOTATION), true);
						break;
					}
			for(index = begin; index <= endPoint; index++)
				if(txt[index]=='\n') {
					index++; break;
				}
		}
	}
    private void annotationOneLine(int offset, int endPoint, char txt[], SyntaxPainter painter) {
		int textLength = txt.length;
		int index = offset;
		while(index <= endPoint) {
			int begin;
			for(begin = index; begin + 1 <= endPoint && txt[begin] != '\n' ; begin++)
				if(txt[begin] == '/' && txt[begin+1] == '/')
					break;
			if(begin + 1 <= endPoint && txt[begin] == '/' && txt[begin+1] == '/')
				for(int end = begin; end < textLength; end++)
					if(end + 1 == textLength || txt[end] == '\n') {
						int length = end - begin + (end + 1 == textLength ? 1 : 0);
                        painter.paintSyntax(begin, length, mJavaAttributes.getAttribute(SyntaxKind.ANNOTATION), true);
						break;
					}
			for(index = begin; index <= endPoint; index++)
				if(txt[index]=='\n') {
					index++; break;
				}
		}
    }
    private void annotationMultiLine(char txt[], SyntaxPainter painter) {
		int textLength = txt.length;
		int begin = 0;
		boolean inTheAnnotation = false;
		for(int index = 0; index < textLength; index++) {
			if(inTheAnnotation == false && index + 1 < textLength && txt[index] == '/' && txt[index + 1] == '*') {
				inTheAnnotation = true;
				begin = index;
			}
			if(inTheAnnotation == true)
				if(index + 1 == textLength || (index + 1 < textLength && index - begin > 1 && txt[index] == '*' && txt[index + 1] == '/')) {
					int length =  Math.min(index + 2, textLength) - begin;
                    painter.paintSyntax(begin, length, mJavaAttributes.getAttribute(SyntaxKind.ANNOTATION), true);
					inTheAnnotation = false;
				}
		}
	}
	private void addTabs(int offset, String changeStr, StyledTextBody textBody, SyntaxPainter painter) {
		if(offset > 0) {
			String paragraph = textBody.getText();
			char txt[] = paragraph.toCharArray();
			if(changeStr.equals("\n")) {
				int tabs = countPrefixTabs(offset - 1, txt);
				int preLineBegin = lineBegin(offset - 1, txt);
				int preLineEnd = lineEnd(offset - 1, txt);
				String preLine = paragraph.substring(preLineBegin, preLineEnd + 1);
				if(txt[offset - 1] == '{' || newLineTabPolicy(preLine) == WHILE_DO_FOR_IF_ELSE)
					tabs++;
				else if(preLineBegin - 2 >= 0) {
					int prepreLineBegin = lineBegin(preLineBegin - 2,txt);
					int prepreLineEnd = lineEnd(preLineBegin - 2,txt);
					String prepreLine = paragraph.substring(prepreLineBegin, prepreLineEnd + 1);
					if(newLineTabPolicy(prepreLine) == WHILE_DO_FOR_IF_ELSE)
						tabs--;
				}
				while(tabs-- > 0)
					try {
						textBody.insertStyledText(offset + 1, "\t", getDefaultAttributeSet());
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
			}

			if(changeStr.equals("{")) {
				int thisLineBegin = lineBegin(offset, txt);
				int thisLineEnd = lineEnd(offset, txt);
				String line = paragraph.substring(thisLineBegin, thisLineEnd + 1);
				if(newLineTabPolicy(line) == LEFT_STYLE_LEFT_BRACKET)
					for(int i = offset - 1; i >= thisLineBegin; i--)
						if(txt[i] == '\t') {
							try {
								textBody.removeStyledText(i, 1);
							} catch(BadLocationException e) {
								e.printStackTrace();
							}
							break;
						}
			}
			
			if(changeStr.equals("}")) {
				int thisLineBegin = lineBegin(offset, txt);
				int thisLineEnd = lineEnd(offset, txt);
				String line = paragraph.substring(thisLineBegin, thisLineEnd + 1);
				if(newLineTabPolicy(line) == (LEFT_CLEAR_RIGHT_BRACKET | RIGHT_CLEAR_RIGHT_BRACKET))
					for(int i = offset - 1; i >= thisLineBegin; i--)
						if(txt[i] == '\t') {
							try {
								textBody.removeStyledText(i, 1);
							} catch(BadLocationException e) {
								e.printStackTrace();
							}
							break;
						}
			}
		}
	}
	private synchronized int newLineTabPolicy(String thisLine) {
		String modifyLine = clearSpaceWord(removeInvalidWord(thisLine));
		if(modifyLine.length() == 0) return 0;
		
		char first = modifyLine.charAt(0);
		char last = modifyLine.charAt(modifyLine.length() - 1);
		if(first == '}' && last == '{') return RIGHT_STYLE_LEFT_BRACKET + RIGHT_CLEAR_RIGHT_BRACKET + LEFT_CLEAR_RIGHT_BRACKET;
		if(last == '{' && modifyLine.length() > 1) return RIGHT_STYLE_LEFT_BRACKET;
		if(last == '{' && modifyLine.length() == 1) return LEFT_STYLE_LEFT_BRACKET;
		if(last == '}' && modifyLine.length() == 1) return RIGHT_CLEAR_RIGHT_BRACKET + LEFT_CLEAR_RIGHT_BRACKET;
		if(first == '}' && modifyLine.length() > 1) return LEFT_CLEAR_RIGHT_BRACKET;
		if(first == '}' || first == '{' || last == '{' || last == '}') return NONE;
		
		for(int begin = 0, end = 0, R = thisLine.length() - 1; end <= R + 1; end++)
			if(end == R + 1 || isNameAllowed(thisLine.charAt(end)) == false) {
				String substr = thisLine.substring(begin, end);
				if(substr.matches("for|while|do|if|else") && thisLine.charAt(R)!=';')
					return WHILE_DO_FOR_IF_ELSE;
				while(end <= R && isNameAllowed(thisLine.charAt(end)) == false)
					end++;
				begin = end;
			}
		return NONE;
	}
	private String removeInvalidWord(String str) {
		boolean inPs = false;
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < str.length(); i++) {
			if(inPs) {
				if(i + 1 < str.length() && str.charAt(i) == '*' && str.charAt(i+1) == '/') {
					inPs = false;
					i++;
				}
				continue;
			}
			if(i + 1 < str.length() && str.charAt(i) == '/' && str.charAt(i+1) == '*') {
				inPs = true;
				i++;
			} else if(i + 1 < str.length() && str.charAt(i) == '/' && str.charAt(i+1) == '/') {
				return builder.toString();
			} else
				builder.append(str.charAt(i));
		}
		return builder.toString();
	}
	private String clearSpaceWord(String str) {
		if(str.length() == 0) return str;
		for(int j = 0; j <= str.length(); j++)
			if(j == str.length() || isSpaceWord(str.charAt(j)) == false) {
				str = str.substring(j);
				break;
			}
		for(int j = str.length() - 1; j >= 0; j--)
			if(j == 0 || isSpaceWord(str.charAt(j)) == false) {
				str = str.substring(0, j + 1);
				break;
			}
		return str;
	}
    private boolean containAnnotation(String change) {
		for(int i = 0; i + 1 < change.length(); i++) {
			if(change.charAt(i) == '/' && change.charAt(i + 1) == '*')
				return true;
			if(change.charAt(i) ==  '*' && change.charAt(i + 1) == '/')
				return true;
		}
		return false;
	}
    private int lineBegin(int offset,char txt[]) {
		for(int index = txt[offset]=='\n' ? offset - 1: offset ; index >= 0; index--)
			if(txt[index] == '\n')
				return index + 1;
		return 0;
	}
    private int lineEnd(int offset,char txt[]) {
		int textLength = txt.length;
		for(int index = txt[offset]=='\n' ? offset + 1: offset; index < textLength; index++)
			if(txt[index] == '\n')
				return index - 1;
		return textLength - 1;
	}
	private int countPrefixTabs(int offset,char txt[]) {
		int index = offset;
		int tabs = 0;
		int spaces = 0;
		while(index >= 0 && txt[index] != '\n') {
			if(txt[index] == '\t' || txt[index] == ' ') {
				if(txt[index] == '\t')
					tabs++;
				else
					spaces++;
			} else {
				tabs = 0;
				spaces = 0;
			}
			index--;
		}
		return tabs + spaces/4 + ((spaces%4 != 0) ? 1 : 0);
	}
	
	/**
	 * Identify the string is number constant
	 * @return True if str follow the number format of java.
	 */
	private boolean isNumber(String str) {
		/**
		 * float and double:	"\\d+\\.\\d*[fFdD]?"
		 * 						"\\.\\d+[fFdD]?"
		 * int and long:		"\\d+[fFdDlL]?"
		 */
		
		if(str.matches("\\d+\\.\\d*[fFdD]?|\\.\\d+[fFdD]?|\\d+[fFdDlL]?"))
			return true;
		return false;
	}
	/**
	 * Identify the string is class name.
	 * @return True if the leading character is capital letter.
	 */
	private boolean isClassName(String str) {
		if(str.length() > 0 && Character.isUpperCase(str.charAt(0)))
			return true;
		return false;
	}
	private boolean isConstantNumberAllowed(char c) {
		if(String.valueOf(c).matches("[a-zA-Z0-9\\.]"))
			return true;
		return false;
	}
    private boolean isNameAllowed(char c) {
		if(String.valueOf(c).matches("[a-zA-Z0-9_]"))
			return true;
		return false;
	}
    private boolean isSpaceWord(char c) {
		if(c == '\t' || c == '\n' || c == '\f' || c == '\r' || c == ' ')
			return true;
		return false;
	}
	//[-+*/()\\[\\]{}<>:]
	private boolean isBracket(char c) {
		for(int i = 0; i < bracket.length; i++)
			if(c == bracket[i])
				return true;
		return false;
	}
}