package cz.cvut.fit.niccc;

import javax.swing.*;
import java.util.logging.Logger;

public class Main {

    public static final int FRAME_WIDTH = 1600;
    public static final int FRAME_HEIGHT = 900;

    static Logger log = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        //log.info("Hello this is a debug message");              //todo finalize log4j

        JFrame frame = new JFrame("Moving Dots");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        MovingDotsPanel movingDotsPanel = new MovingDotsPanel();
        frame.add(movingDotsPanel);
        frame.setVisible(true);

        MusicPlayer player = new MusicPlayer();
        player.setFileName("highway_sound.WAV");
        player.setLooping(true);
        player.start();

        //todo load game state
    }
}
