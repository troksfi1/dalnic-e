package cz.cvut.fit.niccc;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class MusicPlayer extends Thread {

    private static final int BUFFER_SIZE = 4096;

    String fileName = "";

    boolean isLooping = false;

    public void setLooping(boolean looping) {
        this.isLooping = looping;
    }

    @Override
    public void run() {
        play(isLooping);
    }

    public void play(boolean isLooping) {
        do {
            try {
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
                assert inputStream != null;
                InputStream bufferedIn = new BufferedInputStream(inputStream);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedIn);

                AudioFormat audioFormat = audioStream.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
                sourceDataLine.open(audioFormat);
                sourceDataLine.start();

                System.out.println("Playback Started.");


                byte[] bufferBytes = new byte[BUFFER_SIZE];
                int readBytes = -1;
                while ((readBytes = audioStream.read(bufferBytes)) != -1) {
                    sourceDataLine.write(bufferBytes, 0, readBytes);
                }
                sourceDataLine.drain();
                sourceDataLine.close();
                audioStream.close();

                System.out.println("Playback played.");

            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
                System.out.println("Error occured during playback process:" + ex.getMessage());
            }
        } while (isLooping);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}