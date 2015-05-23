package hr.djajcevic.aee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * @author djajcevic | 23.05.2015.
 */
public class AEUGUI extends JFrame implements KeyListener {

    private static final long serialVersionUID = 5982841315570917499L;
    boolean up, right, down, left;

    private JLabel upArrow;
    private JLabel rightArrow;
    private JLabel downArrow;
    private JLabel leftArrow;

    private AbsoluteEncoder horizontalEncoder, verticalEncoder;

    public AEUGUI() throws HeadlessException {
        super("Absolute Encoder Emulator");
        setSize(100, 120);

        Container contentPane = new Container();
        GridLayout layout = new GridLayout(3, 3);
        contentPane.setLayout(layout);
        setContentPane(contentPane);
        Font font = null;

        upArrow = new JLabel(Toolkit.getProperty("AWT.up", "Up"), SwingConstants.CENTER);

        font = upArrow.getFont();
        font = font.deriveFont(24f);
        upArrow.setFont(font);

        rightArrow = new JLabel(Toolkit.getProperty("AWT.right", "Right"), SwingConstants.CENTER);
        rightArrow.setFont(font);
        downArrow = new JLabel(Toolkit.getProperty("AWT.down", "Down"), SwingConstants.CENTER);
        downArrow.setFont(font);
        leftArrow = new JLabel(Toolkit.getProperty("AWT.left", "Left"), SwingConstants.CENTER);
        leftArrow.setFont(font);

        contentPane.add(new Label(""));
        contentPane.add(upArrow);
        contentPane.add(new Label(""));
        contentPane.add(leftArrow);
        contentPane.add(new Label(""));
        contentPane.add(rightArrow);
        contentPane.add(new Label(""));
        contentPane.add(downArrow);
        contentPane.add(new Label(""));

        horizontalEncoder = new AbsoluteEncoder("Horizontal", 0);
        verticalEncoder = new AbsoluteEncoder("Vertical", 0);

        addKeyListener(this);

    }

    public static void main(String[] args) {
        AEUGUI gui = new AEUGUI();
        gui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gui.setLocationRelativeTo(null);
//        gui.pack();
        gui.setVisible(true);
    }

    @Override
    public void keyTyped(final KeyEvent e) {

    }

    @Override
    public void keyPressed(final KeyEvent e) {
        int keyCode = e.getKeyCode();
        up = keyCode == KeyEvent.VK_UP || up;
        right = keyCode == KeyEvent.VK_RIGHT || right;
        down = keyCode == KeyEvent.VK_DOWN || down;
        left = keyCode == KeyEvent.VK_LEFT || left;

        if (up) {
            verticalEncoder.rotate(Encoder.Direction.FORWARD);
        } else if (down) {
            verticalEncoder.rotate(Encoder.Direction.BACKWARD);
        }

        if (right) {
            horizontalEncoder.rotate(Encoder.Direction.FORWARD);
        } else if (left) {
            horizontalEncoder.rotate(Encoder.Direction.BACKWARD);
        }

        upArrow.setForeground(resolveColor(up));
        rightArrow.setForeground(resolveColor(right));
        downArrow.setForeground(resolveColor(down));
        leftArrow.setForeground(resolveColor(left));

    }

    @Override
    public void keyReleased(final KeyEvent e) {
        int keyCode = e.getKeyCode();
        up = keyCode == KeyEvent.VK_UP ? !up : up;
        right = keyCode == KeyEvent.VK_RIGHT ? !right : right;
        down = keyCode == KeyEvent.VK_DOWN ? !down : down;
        left = keyCode == KeyEvent.VK_LEFT ? !left : left;

        upArrow.setForeground(resolveColor(up));
        rightArrow.setForeground(resolveColor(right));
        downArrow.setForeground(resolveColor(down));
        leftArrow.setForeground(resolveColor(left));
    }

    public static Color resolveColor(boolean selected) {
        return selected ? Color.GREEN : Color.BLACK;
    }
}
