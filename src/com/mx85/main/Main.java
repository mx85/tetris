package com.mx85.main;

import javax.swing.*;

public class Main {


    public static void main(String[] args) {
        new Main();
    }


    public Main() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TetrisGame();
            }
        });
    }
}
