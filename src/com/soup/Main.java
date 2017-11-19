package com.soup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;

public class Main {

    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        SoupTimer timer = new SoupTimer(30, screenSize);
    }
}

class SoupTimer extends JFrame implements Runnable{

    private Thread thread;

    int seconds;
    int elapsedSeconds;
    long startTime;
    Dimension size;
    boolean started = false;

    JLabel timerLabel;

    public SoupTimer(int seconds, Dimension size) {
        this.seconds = seconds;
        this.size = size;
        getContentPane().setBackground(Color.BLACK);

        setTitle("Soup Timer");
        setSize(size);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        timerLabel = new JLabel(String.valueOf(seconds) + ".000");
        timerLabel.setFont(new Font("Consolas", Font.BOLD, 500));
        timerLabel.setForeground(new Color(200, 20, 20));
        //timerLabel.setHorizontalAlignment(JLabel.CENTER);

        add(timerLabel, BorderLayout.CENTER);
        pack();

        //setUndecorated(true);
        setVisible(true);
        start();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    int x = JOptionPane.showConfirmDialog(null, "Do you want to shutdown?", "Shutdown", JOptionPane.YES_NO_OPTION);
                    if (x == 0) {
                        System.exit(0);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if (started) {
                        startTime = System.currentTimeMillis();
                        started = false;
                        resetTimer();
                    } else {
                        started = true;
                        startTime = System.currentTimeMillis();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    timerLabel.setFont(new Font("Consolas", Font.BOLD, timerLabel.getFont().getSize() + 10));
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    timerLabel.setFont(new Font("Consolas", Font.BOLD, timerLabel.getFont().getSize() - 10));
                } else if (e.getKeyCode() == KeyEvent.VK_T) {
                    showSecondsDialog();
                    startTime = System.currentTimeMillis();
                    started = false;
                    resetTimer();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }



    @Override
    public void run() {
        while(true) {
            //System.out.println("test"); //for some reason this needs to be here
            if(started) {
                int elapsedMillis = (int) Math.ceil(getElapsedTimeMillis(startTime));
                elapsedSeconds = (int) Math.ceil(elapsedMillis / 1000);
                //System.out.println("RUNNING");
                timerLabel.setText((seconds - 1 - elapsedSeconds) + "." + (1000 - (elapsedMillis % 1000)));

                //System.out.println(seconds - (elapsedSeconds + 1));
            }

            if (seconds - (elapsedSeconds + 1) < 0 && started) {
                started = false;
                timerLabel.setText("Done!");
            }

            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start () {
        System.out.println(new Timestamp(System.currentTimeMillis()) + " Starting " +  this.getClass().getName() + "!");
        if (thread == null) {
            thread = new Thread (this, "SoupTimer");
            thread.start();
        }
    }

    public long getElapsedTimeMillis(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

    public void showSecondsDialog() {
        seconds = Integer.parseInt(JOptionPane.showInputDialog("Input the number of seconds."));
    }

    public JLabel getTimerLabel() {
        return timerLabel;
    }

    public void setTimerLabel(JLabel timerLabel) {
        this.timerLabel = timerLabel;
    }

    public void resetTimer() {
        timerLabel.setText(seconds + ".000");
    }
}
