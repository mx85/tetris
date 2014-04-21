package com.mx85.main;

import javax.swing.*;
import java.awt.*;

public class PieceFactory {

    public static Shape createRandomPiece() {
        Shape piece;
        int shape = 1+(int)(5*Math.random());
        switch (shape) {
            case 1: piece = new ZPiece(); break;
            case 2: piece = new LPiece(); break;
            case 3: piece = new CubePiece(); break;
            case 4: piece = new LongPiece(); break;
            case 5: piece = new TPiece(); break;
            default: piece = new ZPiece();
        }
        return piece;
    }

    private static class LPiece extends AbstractPiece {
        public LPiece() {
            super(new int[][] {
                    {10, 10, 10, 11},
                    {1, 2, 3, 3},},
                    Color.red, "LPiece");
        }

        @Override
        public PIECE getPieceType() {
            return PIECE.LPIECE;
        }
    }

    private static class ZPiece extends AbstractPiece {
        public ZPiece() {
            super(new int[][] {
                    {10, 11, 11, 12},
                    {2, 2, 1, 1},},
                    Color.blue, "ZPiece");
        }

        @Override
        public PIECE getPieceType() {
            return PIECE.ZPIECE;
        }
    }

    private static class TPiece extends AbstractPiece {
        public TPiece() {
            super(new int[][] {
                    {10, 11, 12, 11},
                    {1, 1, 1, 2},},
                    Color.yellow, "TPiece");
        }

        @Override
        public PIECE getPieceType() {
            return PIECE.TPIECE;
        }
    }

    private static class LongPiece extends AbstractPiece {
        public LongPiece() {
            super(new int[][] {
                    {10, 10, 10, 10},
                    {1, 2, 3, 4},},
                    Color.green, "LongPiece");
        }

        @Override
        public PIECE getPieceType() {
            return PIECE.LONGPIECE;
        }
    }

    private static class CubePiece extends AbstractPiece {
        public CubePiece() {
            super(new int[][] {
                    {10, 11, 10, 11},
                    {2, 2, 1, 1},},
                    Color.orange, "CubePiece");
        }

        @Override
        public PIECE getPieceType() {
            return PIECE.CUBEPIECE;
        }
    }

    private abstract static class AbstractPiece extends Shape {

        public AbstractPiece(int[][] cords, Color color, String name) {
            super(cords, color, name);
        }

        public void erase (JButton[][] cells) {
            for (int i = 0; i < cords[X_AXIS].length; i++)   {
                cells[cords[X_AXIS][i]][cords[Y_AXIS][i]].setBackground(Color.lightGray);
            }
        }

        public void draw(JButton[][] cells) {
            for(int i = 0; i < cords[X_AXIS].length; i++ ) {
                cells[cords[X_AXIS][i]][cords[Y_AXIS][i]].setBackground(color);
            }
        }

        private boolean testPosition(int x, int y, JButton[][] cells) {
            if((x < 0 || x > 19))
                return false;
            if((y < 0 || y > 19))
                return false;
            if(!cells[x][y].getBackground().equals(Color.lightGray))
                return false;
            return true;
        }

        private boolean moveDown(JButton[][] cells) {
            boolean down = tryDown(cells);
            if(down) {
                for(int i = 0; i < cords[X_AXIS].length; i++ ) {
                    cords[Y_AXIS][i]++;
                }
            }
            return down;
        }

        private boolean moveLeft(JButton[][] cells) {
            boolean left = tryLeft(cells);
            if(left) {
                for(int i = 0; i < 4; i++ ) {
                    cords[X_AXIS][i]--;
                }
            }
            return left;
        }

        private boolean moveRight(JButton[][] cells) {
            boolean right = tryRight(cells);
            if(right) {
                for(int i = 0; i < 4; i++ ) {
                    cords[X_AXIS][i]++;
                }
            }
            return right;
        }

        private boolean rotate(JButton[][] cells) {
            boolean rotate = tryRotate(cells);
            if(rotate) {
                for (int i = 0; i < 4; i++) {
                    int dx = cords[X_AXIS][i] - cords[0][1];
                    int dy = cords[Y_AXIS][i] - cords[1][1];
                    cords[Y_AXIS][i] = cords[1][1] + dx;
                    cords[X_AXIS][i] = cords[0][1] - dy;
                }
            }
            return rotate;
        }

        private boolean tryDown(JButton[][] cells) {
            for(int i = 0; i < cords[X_AXIS].length; i++ ) {
                if(!testPosition(cords[X_AXIS][i], cords[Y_AXIS][i] + 1, cells))
                    return false;
            }
            return true;
        }

        private boolean tryRight(JButton[][] cells) {
            for(int i = 0; i < cords[X_AXIS].length; i++ ) {
                if(!testPosition(cords[X_AXIS][i] + 1, cords[Y_AXIS][i], cells))
                    return false;
            }
            return true;
        }

        private boolean tryLeft(JButton[][] cells) {
            for(int i = 0; i < cords[X_AXIS].length; i++ ) {
                if(!testPosition(cords[X_AXIS][i] - 1, cords[Y_AXIS][i], cells))
                    return false;
            }
            return true;
        }

        private boolean tryRotate(JButton[][] cells) {
            for (int i = 0; i < 4; i++) {
                int dx = cords[X_AXIS][i] - cords[0][1];
                int dy = cords[Y_AXIS][i] - cords[1][1];
                int nx = cords[1][1] + dx;
                int ny = cords[0][1] - dy;
                if(!testPosition(nx, ny, cells))
                    return false;
            }
            return true;
        }

        public boolean move(DIRECTION direction, JButton[][] cells) {
            boolean moved = false;
            erase(cells);
            switch (direction) {
                case DOWN:
                    moved = moveDown(cells);
                    break;
                case RIGHT:
                    moved = moveRight(cells);
                    break;
                case LEFT:
                    moved = moveLeft(cells);
                    break;
                case ROTATE:
                    moved = rotate(cells);
                    break;
            }
            draw(cells);
            return moved;
        }
    }
}
