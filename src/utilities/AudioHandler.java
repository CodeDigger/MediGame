package utilities;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Tobias on 2016-02-07.
 * This class reads audio files in .wav format and start playback.
 * Use the play() function to play an audio file.
 * The file plays in a separate thread.
 */
public class AudioHandler{
    /**
     * this flag indicates whether the playback completes or not.
     */
    boolean playCompleted;

    /**
     * Play a given audio file.
     * @param audioFilePath Path of the audio file.
     */
    void play(String audioFilePath) {

        new AudioHandlerThread(audioFilePath).start();
    }

    private class AudioHandlerThread extends Thread implements LineListener{
        String audioFilePath;
        File audioFile;

        public AudioHandlerThread(String audioFilePath) {

            this.audioFilePath = audioFilePath;
            audioFile = new File(audioFilePath);
        }

        @Override
        public void run() {

            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

                AudioFormat format = audioStream.getFormat();

                DataLine.Info info = new DataLine.Info(Clip.class, format);

                Clip audioClip = (Clip) AudioSystem.getLine(info);

                audioClip.addLineListener(this);

                audioClip.open(audioStream);

                audioClip.start();

                while (!playCompleted) {
                    // wait for the playback completes
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                audioClip.close();

            } catch (UnsupportedAudioFileException ex) {
                System.out.println("The specified audio file is not supported.");
                ex.printStackTrace();
            } catch (LineUnavailableException ex) {
                System.out.println("Audio line for playing back is unavailable.");
                ex.printStackTrace();
            } catch (IOException e) {
                System.out.println("Error playing the audio file.");
                e.printStackTrace();
            }

        }

        /**
         * Listens to the START and STOP events of the audio line.
         */
        @Override
        public void update(LineEvent event) {
            LineEvent.Type type = event.getType();

            if (type == LineEvent.Type.START) {
                System.out.println("Playback started.");

            } else if (type == LineEvent.Type.STOP) {
                playCompleted = true;
                System.out.println("Playback completed.");
            }
        }
    }
}