package com.mx85.main;

import javax.swing.*;
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

    public TetrisGame() {
        super("Tetris");
        this.setSize(400, 500);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        gameLooper = new GameLooper();
        timer = new Timer(normalSpeed, gameLooper);
        timer.start();
    }

    private class ResultPanel extends JPanel {

        private JLabel pointsTextLabel = new JLabel("Points: ");
        private JLabel pointsLabel = new JLabel("0");
        private JLabel nextLabel = new JLabel("Next: ");
        private NextPieceType nextType = new NextPieceType();

        public ResultPanel() {
            setPreferredSize(new Dimension(40, 60));
            pointsTextLabel.setName("pointsTextLabel");

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
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Image buffer = boardPanel.createImage(boardPanel.getWidth(), boardPanel.getHeight());
        Graphics sg = buffer.getGraphics();
        sg.clearRect(0, 0, boardPanel.getWidth(), boardPanel.getHeight());
        gameLooper.draw(sg);
        drawGrids(sg);
        g.drawImage(buffer, 0, 0, boardPanel);

    }

    private void drawGrids(Graphics g) {
        g.setColor(Color.white);
        for(int x = 0; x < gameLooper.cells.length; x++) {
            for(int y = 0; y < gameLooper.cells[x].length; y++) {
                g.drawRect(20 * y, 20 * x, 20, 20);
            }
        }
    }


    private class GameLooper implements ActionListener {

        private Color[][] cells = new Color[20][20];
        private com.mx85.main.Shape currentShape;
        private com.mx85.main.Shape nextShape;

        public GameLooper() {
            for (int i = 0; i  < 20; i++)
                for (int j = 0; j < 20; j++)
                    cells[j][i] = Color.lightGray;
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

        public void draw(Graphics g) {
            for(int x = 0; x < 20; x++) {
                for(int y = 0; y < 20; y++) {
                    g.setColor(cells[y][x]);
                    g.fillRect(20*y, 380-20*x, 20, 20);
                }
            }
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
            for(int x = 0; x < 20; x++) {
                boolean scored = true;
                for(int y = 0; y < 20; y++) {
                   if(cells[y][x] == Color.lightGray) {
                       scored = false;
                   }
                }
                if(scored) {
                    deleteRow(x);
                    --x;
                }
            }
        }

        private void deleteRow(int start) {
            for(int x = start; x < 20; x++) {
                for(int y = 0; y < 20; y++) {
                    if(x < 19)
                        cells[y][x] = cells[y][x + 1];
                    else
                        cells[y][x] = cells[y][x] = Color.lightGray;
                }
            }
        }
    }
}
