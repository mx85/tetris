package com.mx85.main;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class TetrisGame extends JFrame {

    private GameLooper gameLooper;
    private Timer timer;

    private int normalSpeed = 200;
    private int highSpeed = 50;

    private JPanel boardPanel = new JPanel();
    private ResultPanel resultPanel = new ResultPanel();

    private JButton[][] cells = new JButton[20][20];

    public TetrisGame() {
        super("Tetris");
        this.setSize(400, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        }
        catch(Exception e) {}

        this.setLayout(new GridLayout());


        this.setLayout(new BorderLayout());
        this.add(boardPanel, BorderLayout.CENTER);
        this.add(resultPanel, BorderLayout.SOUTH);

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyEventDispatcher() {
                    @Override
                    public boolean dispatchKeyEvent(KeyEvent e) {
                        if(e.getID() == KeyEvent.KEY_LAST) {
                            int key = e.getKeyCode();
                            if(!timer.isRunning())
                                timer.start();
                            switch (key) {
                                case KeyEvent.VK_LEFT:
                                    gameLooper.move(com.mx85.main.Shape.DIRECTION.LEFT);
                                    break;
                                case KeyEvent.VK_RIGHT:
                                    gameLooper.move(com.mx85.main.Shape.DIRECTION.RIGHT);
                                    break;
                                case KeyEvent.VK_UP:
                                    gameLooper.move(com.mx85.main.Shape.DIRECTION.ROTATE);
                                    break;
                                case KeyEvent.VK_DOWN:
                                    timer.setDelay(highSpeed);
                                    break;
                                case KeyEvent.VK_P:
                                    timer.stop();
                                    break;
                            }
                        }
                        return false;
                    }
                });

        boardPanel.setLayout(new GridLayout(20,20));
        for(int i = 0; i < cells.length; i++) {
            for(int j = 0; j < cells[i].length; j++) {
                cells[j][i] = new JButton();
                cells[j][i].setEnabled(false);
                cells[j][i].setBackground(Color.lightGray);
                cells[j][i].setOpaque(true);
                boardPanel.add(cells[j][i]);
            }
        }

       gameLooper = new GameLooper();
       timer = new Timer(normalSpeed, gameLooper);
       timer.start();
    }

    private class ResultPanel extends JPanel {

        private JLabel pointsTextLabel = new JLabel("Points: ");
        private JLabel pointsLabel = new JLabel("0");
        private JLabel nextLabel = new JLabel("Next: ");
        private NextPieceType nextType = new NextPieceType();
        private int points = 0;

        public ResultPanel() {
            setPreferredSize(new Dimension(40, 60));

            this.setLayout(new GridLayout(2, 2));
            this.add(pointsTextLabel);
            this.add(pointsLabel);
            this.add(nextLabel);
            this.add(nextType);
        }

        public void setPieceType(com.mx85.main.Shape.PIECE piece) {
            nextType.setPieceType(piece);
        }

        private class NextPieceType extends JComponent {

            private com.mx85.main.Shape.PIECE nextPiece;

            public void setPieceType(com.mx85.main.Shape.PIECE piece) {
                this.nextPiece = piece;
            }

            @Override
            protected void paintComponent(Graphics g) {
                switch (nextPiece) {
                    case CUBEPIECE:
                        paintCubePiece(g);
                        break;
                    case LONGPIECE:
                        paintLongPiece(g);
                        break;
                    case LPIECE:
                        paintLPiece(g);
                        break;
                    case TPIECE:
                        paintTPiece(g);
                        break;
                    case ZPIECE:
                        paintZPiece(g);
                        break;
                }
            }

            private void paintLPiece(Graphics g) {
                g.setColor(Color.red);
                g.fillRect(0,0,10,10);
                g.fillRect(0,10,10,10);
                g.fillRect(0,20,10,10);
                g.fillRect(10,20,10,10);
            }

            private void paintTPiece(Graphics g) {
                g.setColor(Color.yellow);
                g.fillRect(0,0,10,10);
                g.fillRect(10,0,10,10);
                g.fillRect(20,0,10,10);
                g.fillRect(10,10,10,10);

            }

            private void paintZPiece(Graphics g) {
                g.setColor(Color.blue);
                g.fillRect(0,0,10,10);
                g.fillRect(10,0,10,10);
                g.fillRect(10,10,10,10);
                g.fillRect(20,10,10,10);
            }

            private void paintCubePiece(Graphics g) {
                g.setColor(Color.orange);
                g.fillRect(0,0,10,10);
                g.fillRect(10,0,10,10);
                g.fillRect(0,10,10,10);
                g.fillRect(10,10,10,10);
            }

            private void paintLongPiece(Graphics g) {
                g.setColor(Color.green);
                g.fillRect(0,0,10,10);
                g.fillRect(0,10,10,10);
                g.fillRect(0,20,10,10);
                g.fillRect(0,30,10,10);
            }
        }

        public void addPoints(int points) {
            this.points += points;
            pointsLabel.setText(Integer.toString(this.points));
        }
    }

    private class GameLooper implements ActionListener {

        private com.mx85.main.Shape currentShape;
        private com.mx85.main.Shape nextShape;

        public GameLooper() {
            currentShape = PieceFactory.createRandomPiece();
            nextShape = PieceFactory.createRandomPiece();
            resultPanel.setPieceType(nextShape.getPieceType());
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            dropPiece();
        }

        public void dropPiece() {
            move(com.mx85.main.Shape.DIRECTION.DOWN);
            repaint();
        }

        public void move(com.mx85.main.Shape.DIRECTION direction) {
            switch (direction) {
                case DOWN:
                    if(!currentShape.move(com.mx85.main.Shape.DIRECTION.DOWN, cells)) {
                        currentShape = nextShape;
                        nextShape = PieceFactory.createRandomPiece();
                        resultPanel.setPieceType(nextShape.getPieceType());
                        timer.setDelay(normalSpeed);
                        checkRows();
                    }
                    break;
                case RIGHT:
                    currentShape.move(com.mx85.main.Shape.DIRECTION.RIGHT, cells);
                    break;
                case LEFT:
                    currentShape.move(com.mx85.main.Shape.DIRECTION.LEFT, cells);
                    break;
                case ROTATE:
                    currentShape.move(com.mx85.main.Shape.DIRECTION.ROTATE, cells);
                    break;
                default:
            }
        }

        private void checkRows() {
            for(int y = 0; y < 20; y++) {
                boolean scored = true;
                for(int x = 0; x < 20; x++) {
                   if(cells[x][y].getBackground().equals(Color.lightGray)) {
                       scored = false;
                   }
                }
                if(scored) {
                    deleteRow(y);
                    y = y - 1;
                    resultPanel.addPoints(10);
                }
            }
        }

        private void deleteRow(int start) {
            for(int y = start; y > 0; y--) {
                for(int x = 0; x < 20; x++) {
                    if(y > 0)
                        cells[x][y].setBackground(cells[x][y-1].getBackground());
                    else
                        cells[x][y].setBackground(Color.lightGray);
                }
            }
        }
    }
}
