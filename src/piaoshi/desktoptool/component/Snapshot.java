package piaoshi.desktoptool.component;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JMenuItem;

import piaoshi.desktoptool.interfaces.MainButton;

/**
 * snapshot
 * 
 * @author Piaoshi(jiayalei8@gmail.com)
 *
 */
public class Snapshot implements MainButton, ClipboardOwner {

	/**
	 * Snapshot Singleton Mode
	 */
	private static Snapshot snapshot = new Snapshot();
	/**
	 * button label
	 */
	private String buttonText = "Snapshot";
	/**
	 * JMenuItem to do the snapshot
	 */
	private ArrayList<JMenuItem> items;

	/**
	 * return singleton object of Snapshot
	 * 
	 * @return
	 */
	public static Snapshot getInstance() {
		return snapshot;
	}

	private Snapshot() {
		items = new ArrayList<>();
		JMenuItem item = new JMenuItem(getButtonText());
		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doButton();
			}
		});
		items.add(item);
	}

	@Override
	public void setPropertiesObj(Properties properties) {
		return;
	}

	@Override
	public ArrayList<JMenuItem> getMenuItems() {
		return items;
	}

	@Override
	public boolean haveItemsProperties() {
		return false;
	}

	@Override
	public String getItemsPropertiesName() {
		return null;
	}

	@Override
	public String getItemsProperties() {
		return null;
	}

	@Override
	public void read_setItemsProperties(Properties properties) {
		return;
	}

	@Override
	public void write_setItemsProperties(Properties properties, String string) {
		return;
	}

	@Override
	public void read_setPopupMenuItemProperties(Properties properties) {
		return;
	}

	@Override
	public void writePopupMenuItemProperties(Properties properties) {
		return;
	}

	@Override
	public boolean haveItemProperties() {
		return false;
	}

	@Override
	public String getItemPropertiesName(int itemIndex) {
		return null;
	}

	@Override
	public String getItemProperties(int itemIndex) {
		return null;
	}

	@Override
	public void read_setItemProperties(Properties properties, int itemIndex) {
		return;
	}

	@Override
	public void write_setItemProperties(Properties properties, int itemIndex, String string) {
		return;
	}

	@Override
	public boolean haveButtonProperties() {
		return false;
	}

	@Override
	public String getButtonProperties() {
		return null;
	}

	@Override
	public void read_setButtonProperties(Properties properties) {
		return;
	}

	@Override
	public void read_setButtonPopupMenuItemProperties(Properties properties) {
		return;
	}

	@Override
	public void write_setButtonProperties(Properties properties, String string) {
		return;
	}

	@Override
	public void writeButtonPopupMenuItemProperties(Properties properties) {
		return;
	}

	@Override
	public String getButtonHint() {
		return null;
	}

	@Override
	public String getButtonText() {
		return buttonText;
	}

	@Override
	public void doButton() {
		doSnapshot();
	}

	public void doSnapshot() {
		try {

			Robot robot = new Robot();
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			Rectangle screen = new Rectangle(screenSize);
			BufferedImage i = robot.createScreenCapture(screen);
			TransferableImage trans = new TransferableImage(i);

			Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
			c.setContents(trans, this);

		} catch (AWTException x) {
			x.printStackTrace();
			System.exit(1);
		}
	}

	public void lostOwnership(Clipboard clip, Transferable trans) {
		System.out.println("Lost Clipboard Ownership");
	}

	private class TransferableImage implements Transferable {

		Image i;

		public TransferableImage(Image i) {
			this.i = i;
		}

		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
			if (flavor.equals(DataFlavor.imageFlavor) && i != null) {
				return i;
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}

		public DataFlavor[] getTransferDataFlavors() {
			DataFlavor[] flavors = new DataFlavor[1];
			flavors[0] = DataFlavor.imageFlavor;
			return flavors;
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			DataFlavor[] flavors = getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				if (flavor.equals(flavors[i])) {
					return true;
				}
			}

			return false;
		}
	}
}