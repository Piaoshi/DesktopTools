package piaoshi.desktoptool.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JMenuItem;

import piaoshi.desktoptool.interfaces.MainButton;

/**
 * display a ruler on the screen Now: just horizontal and vertical ruler, and
 * can not used as a real ruler
 * 
 * @author Piaoshi(jiayalei8@gmail.com)
 *
 */
public class Ruler implements MainButton {

	/**
	 * Ruler singleton object
	 */
	public static Ruler ruler = new Ruler();
	/**
	 * MainButton have a properties to set: buttonDirection
	 */
	private boolean haveButtonProperties = true;
	/**
	 * Properties key of buttonProperties
	 */
	private String buttonPropertiesName = "ruler_button";
	/**
	 * Hint info when to set as MainButton
	 */
	private String buttonPropertiesHint = "0|1(0-Virtical,1-Horizonal) ruler";
	/**
	 * button label
	 */
	private String buttonText;
	/**
	 * which direction of ruler is shown when set as MainButton 0---vertical
	 * ruler 1 and others---horizontal ruler
	 */
	private int buttonDirection = 1;
	/**
	 * JMenuItem of horizontal ruler
	 */
	private JMenuItem horizontalItem = null;
	/**
	 * label of horizontal ruler JMenuItem
	 */
	private String horizontalText = "Ruller(-)";
	/**
	 * JMenuItem of vertical ruler
	 */
	private JMenuItem verticalItem = null;
	/**
	 * label of vertical ruler JMenuItem
	 */
	private String verticalText = "Ruler(|)";
	/**
	 * horizontalItem + verticalItem
	 */
	private ArrayList<JMenuItem> items = null;

	/**
	 * return singleton object of Ruler
	 * 
	 * @return
	 */
	public static Ruler getInstance() {
		return ruler;
	}

	private Ruler() {

		horizontalItem = new JMenuItem(horizontalText);
		verticalItem = new JMenuItem(verticalText);

		items = new ArrayList<JMenuItem>();
		items.add(0, horizontalItem);
		items.add(1, verticalItem);

		setButtonText();

		horizontalItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				showHorizontalRuler();
			}
		});
		verticalItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showVirticalRuler();
			}
		});
	}

	/**
	 * show horizontal ruler
	 */
	private void showHorizontalRuler() {
		RulerFrame ruler = new RulerFrame(1);
		ruler.setVisible(true);
	}

	/**
	 * show vertical ruler
	 */
	private void showVirticalRuler() {
		RulerFrame ruler = new RulerFrame(0);
		ruler.setVisible(true);
	}

	/**
	 * set button label according to buttonDirection
	 */
	private void setButtonText() {
		if (buttonDirection == 0) {
			buttonText = "<html>Ruler<br>&nbsp;&nbsp;&nbsp;(|)</html>";
		} else {
			buttonText = "<html>Ruler<br>&nbsp;(----)</html>";
		}
	}

	/**
	 * return buttonDirection
	 * 
	 * @return
	 */
	public int getButtonDirection() {
		return buttonDirection;
	}

	/**
	 * set buttonDirection
	 * 
	 * @param buttonDirection
	 */
	public void setButtonDirection(int buttonDirection) {
		this.buttonDirection = buttonDirection;
	}

	// Override from ManiButton

	@Override
	public String getButtonText() {
		return buttonText;
	}

	@Override
	public void doButton() {
		if (buttonDirection == 0) {
			showVirticalRuler();
		} else {
			showHorizontalRuler();
		}
	}

	@Override
	public boolean haveButtonProperties() {
		return haveButtonProperties;
	}

	@Override
	public String getButtonProperties() {
		return Integer.toString(buttonDirection);
	}

	@Override
	public void read_setButtonProperties(Properties properties) {
		buttonDirection = Integer.parseInt(properties.getProperty(buttonPropertiesName));
		setButtonText();
	}

	@Override
	public void write_setButtonProperties(Properties properties, String string) {
		buttonDirection = Integer.parseInt(string);
		setButtonText();
		properties.setProperty(buttonPropertiesName, Integer.toString(buttonDirection));
		return;
	}

	@Override
	public void read_setButtonPopupMenuItemProperties(Properties properties) {
		read_setButtonProperties(properties);
	}

	@Override
	public void writeButtonPopupMenuItemProperties(Properties properties) {
		properties.setProperty(buttonPropertiesName, Integer.toString(buttonDirection));
	}

	@Override
	public String getButtonHint() {
		return buttonPropertiesHint;
	}

	// Override from PopupMenuItem

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

}
