
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimonGame extends JFrame {
    List<Color> gamePattern = new ArrayList<>();
    Color userPattern;
    int i;
    Color[] colors = { Color.GREEN, Color.RED, Color.YELLOW, Color.BLUE };
    Random random = new Random();
    int level = 1;
    boolean started = false;
    JLabel startLabel;

    public SimonGame() {
        setTitle("Simon Game");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize(screenSize.width, screenSize.height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Create the colored buttons
        JButton greenButton = createButton(Color.GREEN);
        JButton redButton = createButton(Color.RED);
        JButton yellowButton = createButton(Color.YELLOW);
        JButton blueButton = createButton(Color.BLUE);

        // Add buttons to the grid
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(greenButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(redButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(yellowButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(blueButton, gbc);

        startLabel = new JLabel("Click a Button to Start");
        startLabel.setForeground(Color.WHITE);
        startLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(startLabel, gbc);

        getContentPane().setBackground(Color.BLACK);

        greenButton.addActionListener(e -> handleButtonClick(Color.GREEN));
        redButton.addActionListener(e -> handleButtonClick(Color.RED));
        yellowButton.addActionListener(e -> handleButtonClick(Color.YELLOW));
        blueButton.addActionListener(e -> handleButtonClick(Color.BLUE));
    }

    private JButton createButton(Color color) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(150, 150));
        button.setBackground(color);
        return button;
    }

    private void handleButtonClick(Color color) {

        if (!started) {
            started = true;
            nextSequence();
        } else {
            i++;
            if (color != gamePattern.get(i)) {
                getContentPane().setBackground(Color.RED);
                new Timer(300, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        getContentPane().setBackground(Color.BLACK);
                    }
                }).start();
                startLabel.setText("Game Over! Your Score: " + level);
                startOver();
            }
            if (i == gamePattern.size()-1) {
                level++;
                nextSequence();
            }
        }
    }

    private void nextSequence() {
        i = -1;
        Color randomColor = colors[random.nextInt(colors.length)];
        gamePattern.add(randomColor);
        animatePress(randomColor);
        startLabel.setText("Level " + level);
    }

    private void animatePress(Color color) {
        JButton button = getButtonByColor(color);
        if (button != null) {
            Timer timer = new Timer(300, null);
            timer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Animate button press effect (darken and lighten)
                    button.setBackground(color.darker());
                    Timer innerTimer = new Timer(300, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            button.setBackground(color);
                            timer.stop(); // Stop the outer timer
                        }
                    });
                    innerTimer.start();
                }
            });
            timer.start();
        }
    }

    private JButton getButtonByColor(Color color) {
        for (Component comp : getContentPane().getComponents()) {
            if (comp instanceof JButton && comp.getBackground().equals(color)) {
                return (JButton) comp;
            }
        }
        return null;
    }

    private void startOver() {
        level = 1;
        gamePattern.clear();
        started = false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimonGame game = new SimonGame();
            game.setVisible(true);
        });
    }
}