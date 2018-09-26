package Game;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Game extends JFrame {
    Random rand = new Random();

    int boardSize = 0;
    int turn = (rand.nextBoolean() ? -1 : 1);

    Clip backgroundMusic = getClip("Sounds\\menu background.wav");

    BoardButton[][] board;

    final Color WALL = Color.BLACK;
    final Color EMPTY = Color.DARK_GRAY;
    final Color P1OnePointColor = new Color(0, 115, 185);
    final Color P2OnePointColor = new Color(115, 0, 0);

    private BackgroundPanel background = new BackgroundPanel();
    private MenuSettingCommands menus = new MenuSettingCommands(this, background);

    final int locationX = ((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() - 1280) / 2;
    final int locationY = ((int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() - 720) / 2;

    boolean fullscreen = false;
    boolean sound = true;
    boolean playerPlayer = false;
    boolean backgroundMusicPlaying = false;

    Object lastClickedButton;
    float lasClickTime;

    int menuIndex = 0;
    boolean easy = false;

    public Game() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 720);
        setLocation(locationX, locationY);
        setResizable(false);
        try {
            background.setBackground(ImageIO.read(new File("Sprites\\background.png")));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public void start() {
        if (!backgroundMusicPlaying && sound) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusicPlaying = true;
        }
        if (menuIndex == 0) {
            menus.setMainMenu();
        } else if (menuIndex == 1) {
            menus.setSettingsMenu();
        } else if (menuIndex == 2) {
            menus.setPlayerModeMenu();
        } else if (menuIndex == 3) {
            menus.setDifficultyModeMenu();
        } else if (menuIndex == 4) {
            menus.setBoardSizeMenu();
        } else if (menuIndex == 5) {
            menus.setGameStart();
        }
        setVisible(true);
        menuIndex = -1;
    }

    void refreshBoard() {
        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j) {
                board[i][j].refresh();
            }
        }
    }

    void passTurnRefreshBoard() {
        for (int i = 0; i < boardSize; ++i) {
            for (int j = 0; j < boardSize; ++j) {
                board[i][j].setWaiting(false);
                board[i][j].setActive(false);
                board[i][j].refresh();
            }
        }
    }

    Clip getClip(String Sound) {
        try {
            File soundFile = new File(Sound);
            Clip clip;
            AudioInputStream stream = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat format = stream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.open(stream);
            clip = audioClip;
            return clip;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            System.exit(0);
        }
        return getClip("ERROR");
    }

    boolean doubleClick(Object obj) {
        return System.nanoTime() - lasClickTime < 300000000 && obj == lastClickedButton;
    }

    void passTime(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private class BackgroundPanel extends JPanel {
        Image image;

        void setBackground(Image image) {
            this.image = image;
        }

        @Override
        public void paintComponent(Graphics G) {
            super.paintComponent(G);
            G.drawImage(image, 0, 0, getWidth(), getHeight(), null);
            repaint();
        }
    }
}
