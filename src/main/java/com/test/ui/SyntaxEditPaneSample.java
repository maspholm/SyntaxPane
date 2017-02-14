package com.test.ui;

import javax.swing.JFrame;

import com.syntax.manage.*;
import com.syntax.ui.*;

public class SyntaxEditPaneSample {
    public static void main(String args[]) {
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
    }
}