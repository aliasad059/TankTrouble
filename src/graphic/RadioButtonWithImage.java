package graphic;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Radio button with image
 *
 * @author Ali asd & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */

public class RadioButtonWithImage extends JPanel {

    private JRadioButton radio;
    private JLabel image;

    /**
     * Creates a JPanel that acts as Radio button with image
     *
     * @param icon icon of the JButton
     */
    public RadioButtonWithImage(Icon icon) {
        image = new JLabel(icon);
        radio = new JRadioButton();
        add(radio);
        add(image);
    }

    /**
     * add button to button group
     *
     * @param group group to be added
     */
    public void addToButtonGroup(ButtonGroup group) {
        group.add(radio);
    }

    /**
     * add an action listener
     *
     * @param listener Radio button listener
     */
    public void addActionListener(ActionListener listener) {
        image.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                radio.setSelected(true);
            }
        });
        radio.addActionListener(listener);
    }

    /**
     * set the radio button selection
     *
     * @param value value to be selected
     */
    public void setSelected(Boolean value) {
        radio.setSelected(value);
    }
}