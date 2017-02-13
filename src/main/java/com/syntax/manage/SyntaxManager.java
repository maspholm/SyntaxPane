package com.syntax.manage;

import java.awt.Color;
import java.awt.Font;

import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 * Hold color and attribute of {@link com.syntax.ui package}. Any characteristic can be gotten from getting function in
 * this class are not allowed dynamically to be changed. * 
 * Public static field in this class are the default value of SyntaxManager
 */
public class SyntaxManager {
    /**
     * Construct default SyntaxManager and set all filed to default.
     * All default value are the public final field in this class.
     */
    public SyntaxManager() {
        setAttributeSet(GLOBAL_ATTRIBUTESET);
        setCaretColor(CARET_COLOR);
        setAreaColor(AREA_COLOR);
        setHeaderBackgroundColor(HEADER_BACKGROUND_COLOR);
        setHeaderTextColor(HEADER_TEXT_COLOR);
        setHighlightColor(HIGHLIGHT_COLOR);
        setThumbColor(THUMB_COLOR);
    }
    /**
     * Get AttributeSet of {@link com.syntax.ui package}
     * 
     * @return attributeSet of {@link com.syntax.ui package}
     */
    public AttributeSet getAttributeSet() {
        return globalAttributeSet;
    }
    /**
     * Set AttributeSet of {@link com.syntax.ui package}
     * 
     * @param globalAttributeSet AttributeSet of {@link com.syntax.ui package}
     */
    public void setAttributeSet(AttributeSet globalAttributeSet) {
        this.globalAttributeSet = globalAttributeSet;
    }
    /**
     * Get color of {@link com.syntax.ui.DefaultSyntaxCaret DefaultSyntaxCaret}
     * 
     * @return color of {@link com.syntax.ui.DefaultSyntaxCaret DefaultSyntaxCaret}
     */
    public Color getCaretColor() {
        return caretColor;
    }
    /**
     * Set color of {@link com.syntax.ui.DefaultSyntaxCaret DefaultSyntaxCaret}
     * 
     * @param caretColor color of {@link com.syntax.ui.DefaultSyntaxCaret DefaultSyntaxCaret}
     */
    public void setCaretColor(Color caretColor) {
        this.caretColor = caretColor;
    }
    /**
     * Get color of {@link com.syntax.ui.SyntaxTextArea SyntaxTextArea}
     * 
     * @return color of {@link com.syntax.ui.SyntaxTextArea SyntaxTextArea}
     */
    public Color getAreaColor() {
        return syntaxAreaColor;
    }
    /**
     * Set background color of {@link com.syntax.ui package}
     * 
     * @param syntaxAreaColor background color of {@link com.syntax.ui package}
     */
    public void setAreaColor(Color syntaxAreaColor) {
        this.syntaxAreaColor = syntaxAreaColor;
    }
    /**
     * Get background color of {@link com.syntax.ui.DefaultSyntaxHeader DefaultSyntaxHeader}
     * 
     * @return background color {@link com.syntax.ui.DefaultSyntaxHeader DefaultSyntaxHeader}
     */
    public Color getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }
    /**
     * Set background color of {@link com.syntax.ui.DefaultSyntaxHeader DefaultSyntaxHeader}
     * 
     * @param headerBackgroundColor background color of {@link com.syntax.ui.DefaultSyntaxHeader DefaultSyntaxHeader}
     */
    public void setHeaderBackgroundColor(Color headerBackgroundColor) {
        this.headerBackgroundColor = headerBackgroundColor;
    }
    /**
     * Get text color of text on {@link com.syntax.ui.DefaultSyntaxHeader DefaultSyntaxHeader}
     * 
     * @return text color of {@link com.syntax.ui.DefaultSyntaxHeader DefaultSyntaxHeader}
     */
    public Color getHeaderTextColor() {
        return headerTextColor;
    }
    /**
     * Set text color of {@link com.syntax.ui.DefaultSyntaxHeader DefaultSyntaxHeader}
     * 
     * @param headerTextColor text color of {@link com.syntax.ui.DefaultSyntaxHeader DefaultSyntaxHeader}
     */
    public void setHeaderTextColor(Color headerTextColor) {
        this.headerTextColor = headerTextColor;
    }
    /**
     * Get color of selection highlighting
     * 
     * @return color of selection highlighting
     */
    public Color getHighlightColor() {
        return highlightColor;
    }
    /**
     * Set color of selection highlighting
     * @param highlightColor color of selection highlighting
     */
    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
    }
    /**
     * Get color of scrollbar thumb
     * 
     * @return color of scrollbar thumb
     */
    public Color getThumbColor() {
        return thumbColor;
    }
    /**
     * Set color of scrollbar thumb
     * 
     * @param thumbColor color of scrollbar thumb
     */
    public void setThumbColor(Color thumbColor) {
        this.thumbColor = thumbColor;
    }

    private AttributeSet globalAttributeSet;
    private Color caretColor;
    private Color syntaxAreaColor;
    private Color headerBackgroundColor;
    private Color headerTextColor;
    private Color highlightColor;
    private Color thumbColor;

    private static final StyleContext styleContext = StyleContext.getDefaultStyleContext();
    /**
     * Default AttributeSet of {@link com.syntax.ui package}
     */
    public static final AttributeSet GLOBAL_ATTRIBUTESET = StyleContext.getDefaultStyleContext().addAttribute(
                                                            StyleContext.getDefaultStyleContext().getEmptySet(),
                                                            StyleConstants.Foreground,
                                                            Color.WHITE);
    /**
     * Default {@link Font} of {@link com.syntax.ui package}
     */
    public static final Font FONT = new Font("Consolas", Font.PLAIN, 20);
    /**
     * Default color of {@link com.syntax.ui.DefaultSyntaxCaret DefaultSyntaxCaret}
     */
    public static final Color CARET_COLOR = getColor(0.8f, Color.WHITE);
    /**
     * Default background color of {@link com.syntax.ui package}
     */
    public static final Color AREA_COLOR = getColor("#212121");
    /**
     * Default background color of {@link com.syntax.ui.DefaultSyntaxHeader DefaultSyntaxHeader}
     */
    public static final Color HEADER_BACKGROUND_COLOR = getColor("#212121");
    /**
     * Default text color of {@link com.syntax.ui.DefaultSyntaxHeader DefaultSyntaxHeader}
     */
    public static final Color HEADER_TEXT_COLOR = Color.WHITE;
    /**
     * Default color of selection highlighting
     */
    public static final Color HIGHLIGHT_COLOR = getColor("#80007EE6");
    /**
     * Default color of scrollbar thumb.
     */
    public static final Color THUMB_COLOR = getColor(127, Color.WHITE);
    /**
     * Get the default implementation of AbstractCodeAdapter of SyntaxTextArea
     * @return new default CodeAdapter.
     * @see DefaultCodeAdapter
     */
    public static AbstractCodeAdapter getDefaultCodeAdpater() {
        return new DefaultCodeAdapter();
    }
    /**
     * Get a new AttributeSet containing nothing
     * 
     * @return empty AttributeSet.
     * @see javax.swing.text.AttributeSet
     */
    public static AttributeSet getEmptyAttributeSet() {
        return styleContext.getEmptySet();
    }
    /**
     * Add attribute (name, value) to specified AttributeSet
     * 
     * @param att specified AttributeSet
     * @param name name of attribtue. Use field of {@link javax.swing.text.StyleConstants StyleConstants} to assign name
     * @param value value of attribute
     * @return the result after add (name, value) to specified AttributeSet
     */
    public static AttributeSet addAttribute(AttributeSet att, Object name, Object value) {
        return styleContext.addAttribute(att, name, value);
    }
    /**
     * Parse hex color string
     * 
     * ex1.
     * alpha parameter are significant byte
     * getColor("#80007EE6") = new Color(0, 126, 230, 128)
     * 
     * ex2.
     * no alpha parameter
     * getColor("#007EE6") = new Color(0, 126, 230)
     * 
     * @param colorStr must have leading #
     * @return parsed color
     * @see java.awt.Color
     */
    public static Color getColor(String colorStr) {
        if(colorStr.length() == 7)
            return new Color(
                            Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                            Integer.valueOf( colorStr.substring( 5, 7 ), 16 )
                            );
        else
            return new Color(
                            Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                            Integer.valueOf( colorStr.substring( 5, 7 ), 16 ),
                            Integer.valueOf( colorStr.substring( 7, 9 ), 16 ),
                            Integer.valueOf( colorStr.substring( 1, 3 ), 16 )
                            );
    }
    /**
     * Combine opaque characteristic and color
     * 
     * @param alpha 0 &le; alpha &lt; 256. Make color be opaque by setting alpha to 255
     * @param colorStr hex string of color
     * @return combinded color
     * @see java.awt.Color#Color(int,int,int,int)
     */
    public static Color getColor(int alpha, String colorStr) {
        return new Color(
                        Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                        Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                        Integer.valueOf( colorStr.substring( 5, 7 ), 16 ),
                        alpha
                        );
    }
    /**
     * Combine opaque characteristic and color
     * 
     * @param alpha 0.0 &le; alpha &lt; 1.0. Make color be opaque by setting alpha to 1.0
     * @param colorStr hex string of color
     * @return combinded color
     * @see java.awt.Color#Color(int,int,int,int)
     */
    public static Color getColor(float alpha, String colorStr) {
        return new Color(
                        Integer.valueOf( colorStr.substring( 1, 3 ), 16 ),
                        Integer.valueOf( colorStr.substring( 3, 5 ), 16 ),
                        Integer.valueOf( colorStr.substring( 5, 7 ), 16 ),
                        (int)(alpha*255) );
    }
    /**
     * Combine opaque characteristic and color
     * 
     * @param alpha 0 &le; alpha &lt; 256. Make color be opaque by setting alpha to 255
     * @param color sepcified color
     * @return combinded color
     * @see java.awt.Color#Color(int,int,int,int)
     */
    public static Color getColor(int alpha, Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    /**
     * Combine opaque characteristic and color
     * 
     * @param alpha 0.0 &le; alpha &lt; 1.0. Make color be opaque by setting alpha to 1.0
     * @param color sepcified color
     * @return combinded color
     * @see java.awt.Color#Color(int,int,int,int)
     */
    public static Color getColor(float alpha, Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha*255));
    }
    /**
     * Get AttributeSet contains only Foreground attribute with specified CMMException
     * 
     * @param color foreground color
     * @return attributeSet with foreground
     */
    public static AttributeSet getAttributeSetWithForegroundColor(Color color) {
        return addAttribute(getEmptyAttributeSet(), StyleConstants.Foreground, color);
    }
}