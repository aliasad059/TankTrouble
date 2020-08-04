package graphic;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * This class extend "JPasswordField" class.
 * Constructor of this class get a string as prompt text.
 *
 * @author Ali Asad & Sayed Mohammad Ali Mirkazemi
 * @version 1.0.0
 * @since 2020-7-25
 */
public class JPPasswordField extends JPasswordField {
    /**
     * This is constructor of this class and get prompt text and set a focusListener to it.
     *
     * @param promptText is a string as prompt text
     */
    public JPPasswordField(final String promptText) {
        super(promptText);
        addFocusListener(new FocusListener() {

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(promptText);
                }
            }

            @Override
            public void focusGained(FocusEvent e) {
                if (getText().equals(promptText)) {
                    setText("");
                }
            }
        });

    }
}