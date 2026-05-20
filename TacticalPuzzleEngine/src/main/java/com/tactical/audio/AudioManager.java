package com.tactical.audio;

import com.tactical.observer.GameObserver;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;

public class AudioManager implements GameObserver {

    private static AudioManager instance;
    private final AudioFormat audioFormat = new AudioFormat(8000f, 8, 1, true, false);

    private AudioManager() {}

    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void playCoinSound() {
        byte[] data = generateTone(1200, 150, 60);
        playSoundEffect(data);
    }

    public void playWinSound() {
        byte[] data = generateSequence(new double[]{400, 600, 800}, 200, 60);
        playSoundEffect(data);
    }

    public void playLoseSound() {
        byte[] data = generateSequence(new double[]{300, 200}, 200, 60);
        playSoundEffect(data);
    }

    private void playSoundEffect(byte[] data) {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(audioFormat, data, 0, data.length);
            
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-10.0f);
            
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private byte[] generateTone(double frequency, int durationMs, int amplitude) {
        int samples = (int) ((durationMs / 1000.0) * audioFormat.getSampleRate());
        byte[] buffer = new byte[samples];
        for (int i = 0; i < samples; i++) {
            double time = i / audioFormat.getSampleRate();
            buffer[i] = (byte) (Math.sin(2.0 * Math.PI * frequency * time) * amplitude);
        }
        return buffer;
    }

    private byte[] generateSequence(double[] frequencies, int durationMsPerTone, int amplitude) {
        int samplesPerTone = (int) ((durationMsPerTone / 1000.0) * audioFormat.getSampleRate());
        byte[] buffer = new byte[samplesPerTone * frequencies.length];
        int offset = 0;
        for (double freq : frequencies) {
            for (int i = 0; i < samplesPerTone; i++) {
                double time = i / audioFormat.getSampleRate();
                buffer[offset++] = (byte) (Math.sin(2.0 * Math.PI * freq * time) * amplitude);
            }
        }
        return buffer;
    }

    @Override
    public void onEvent(String eventType) {
        switch (eventType) {
            case "COIN_COLLECTED" -> playCoinSound();
            case "PLAYER_WIN" -> playWinSound();
            case "PLAYER_LOSE" -> playLoseSound();
        }
    }
}
