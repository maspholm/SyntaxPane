# SyntaxPane

SyntaxPane is a basic syntax pane supports:
* customize syntax coloring
* customize keyboard shortcuts
* listen to user input easily

The libarary has implemented java syntax coloring adapter and other basic keyboard shortcuts.

## Screenshots

![ScreenShot](https://raw.github.com/w86763777/SyntaxPane/master/screenshots/1.PNG)

## Getting Started
* Add [jar](https://github.com/w86763777/SyntaxPane/blob/master/build/libs/SyntaxPane-all-1.0.jar) file to your classpath

* Java SyntaxEditPane example  
Change fixed UI color which show in SyntaxEditPane by setter function of SyntaxManager. Other dynamically changeable characteristic can be changed by setter function of SyntaxEditPane.
```java
JFrame frame = new JFrame();
try {
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	SyntaxEditPane pane = new SyntaxEditPane(new SyntaxManager(), new JavaCodeAdapter());
	frame.add(pane);
	frame.setSize(700, 500);
	frame.setVisible(true);
} catch (SyntaxException e) {
	e.printStackTrace();
}
```
* Design customized CodeAdapter

 1. implement AbstractCodeAdapter
 2. use textArea.getStyledTextBody() to get all text
 3. use textArea.getSyntaxPainter() to get painter which help you color the syntax
 4. use textArea.getSyntaxHighlightingTool() to get highlight tool which help you highligh the text
 5. use textArea.getSyntaxDocumentToo() to get document tool which help you analyze text

```java
class YourAdapter implements AbstractCodeAdapter {
	@Override
	public void insertString(int offset, String text, SyntaxTextArea textArea) {
		// listen to text insertsion
	}

	@Override
	public void remove(int offset, String text, SyntaxTextArea textArea) {
		// listen to text removement
	}

	@Override
	public boolean replace(int offset, int length, String text, SyntaxTextArea textArea) {
		// listen to text replacement.
		// return true to make SyntaxTextArea replace the text by calling remove and
		// insertString as referred to above.
		// That is if you want to do some changes by yourself, return false, otherwise return true.
	}

	@Override
	public AttributeSet getDefaultAttributeSet() {
		// SyntaxTextArea gets default AttributeSet through this function
		// and use it to set the characteristic attribute before call insertString()
	}
}
```
* Design customized row header
 * comming soon

* Design customized caret
 * comming soon

* Listen to caret update
 * comming soon

* Listen to selection update
 * comming soon

## Document
* [Document](https://github.com/w86763777/SyntaxPane/tree/master/build/docs/javadoc)

## Built With

* [Gradle](https://gradle.org/) - Dependency Management
* JRE version 1.8

## Authors

* **Alan Wu** - *Initial work* - [Github](https://github.com/w86763777)

## License

This project is licensed under the MIT License - see the [LICENSE.md](https://github.com/w86763777/SyntaxPane/blob/master/LICENSE) file for details
