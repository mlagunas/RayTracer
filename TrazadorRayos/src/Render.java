import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Render extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Render() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public Render(BufferedImage image) {
		this();
		Dimension d = new Dimension(image.getWidth(), image.getHeight());
		setSize(d);
		setResizable(false);
		ImageIcon i = new ImageIcon(image);
		add(new JLabel(i));
		setVisible(true);
	}

}
