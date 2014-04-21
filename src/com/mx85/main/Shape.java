package com.mx85.main;

import javax.swing.*;
import java.awt.*;

public abstract class Shape {

    public enum PIECE {
        LPIECE,
        ZPIECE,
        TPIECE,
        LONGPIECE,
        CUBEPIECE,
    }

    public enum DIRECTION {
        DOWN,
        RIGHT,
        LEFT,
        ROTATE,
    }

    protected int[][] cords;
    protected Color color;
    protected final int X_AXIS = 0;
    protected final int Y_AXIS = 1;
    protected final String name;

    public Shape(int[][] cords, Color color, String name) {
        this.cords = cords;
        this.color = color;
        this.name = name;
    }

    public abstract void erase (JButton [][] cells);
    public abstract void draw (JButton [][] cells);
    public abstract boolean move(DIRECTION direction, JButton[][] cells);
    public abstract PIECE getPieceType();

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+"{" +
                "name='" + name + '\'' +
                '}';
    }
}
