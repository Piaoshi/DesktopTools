package piaoshi.desktoptool.component;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import piaoshi.desktoptool.interfaces.MainButton;

/**
 * Change the text in clip board: 1)replace some characters(initial chars) to a
 * particular character(final char) 2)one kind of initial/final chars correspond
 * to a JMenuItem 3)function of MainButton correspond to a JMenuItem
 * 
 * @author Piaoshi(jiayalei8@gmail.com)
 *
 */
public class CopyChange implements MainButton {

	/**
	 * CopyChange singleton object
	 */
	public static CopyChange copyChange = new CopyChange();
	/**
	 * Properties to save properties of CopyChange
	 */
	private Properties properties = null;
	/**
	 * MainButton have a properties to set: buttonIndex out of the
	 * ArrayLilst(JMenuItem) whose function is set to MainButton
	 */
	private boolean haveButtonProperties = true;
	/**
	 * Properties key of buttonProperties
	 */
	private String buttonPropertiesName = "copyChange_button";
	/**
	 * Hint info when to set as MainButton
	 */
	private String buttonPropertiesHint = "int(buttonIndex of itemsNumber)";
	/**
	 * button Label
	 */
	private String buttonText;
	/**
	 * prefix of JMenuItem label and prefix of Properties key
	 */
	private String itemTextIni = "CopyChange";
	/**
	 * buttonIndex out of the ArrayLilst(JMenuItem) whose function is set to
	 * MainButton
	 */
	private int buttonIndex;
	/**
	 * PopupMenuItem have properties to set: 1)how many items are there 2)the
	 * initial chars and final char of each JMenuItem
	 */
	private boolean haveItemProperties = true;
	/**
	 * how many doItems are there
	 */
	private int doItemsNumber;
	/**
	 * Properties key of itemNumber
	 */
	private String itemsPropertiesName = "copyChange_items";
	/**
	 * seperator of itemProrperties String between initial chars and final char
	 */
	public static String seperator = ",..,";
	/**
	 * JMenuItem with different initial chars and final char
	 */
	private ArrayList<JMenuItem> doItems = null;
	/**
	 * JMenuItem for the set
	 */
	private JMenuItem setItem = null;
	/**
	 * doItems+setItem
	 */
	private ArrayList<JMenuItem> items = null;
	/**
	 * the text read from the clip board initially
	 */
	private String clipInputText;
	/**
	 * the text write to the clip board finally
	 */
	private String clipOutputText;
	/**
	 * default initial chars
	 */
	private String defaultInitial = "\n|\t|:| {1,}";
	/**
	 * default final char
	 */
	private String defaultFinal = " ";
	/**
	 * initial chars of each doItems
	 */
	private ArrayList<String> iniialTexts = null;
	/**
	 * final char of each doItems
	 */
	private ArrayList<String> finalTexts = null;

	/**
	 * return singleton object of CopyChange
	 * 
	 * @return
	 */
	public static CopyChange getInstance() {
		return copyChange;
	}

	private CopyChange() {

		doItemsNumber = 1;
		buttonIndex = 0;

		setButtonText();

		doItems = new ArrayList<>();
		iniialTexts = new ArrayList<>();
		finalTexts = new ArrayList<>();

		JMenuItem temp = new JMenuItem(itemTextIni + "_" + buttonIndex);
		doItems.add(0, temp);
		iniialTexts.add(0, defaultInitial);
		finalTexts.add(0, defaultFinal);

		setItem = new JMenuItem("set");

		doItems.get(0).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dealClipText(0);
			}
		});

		setItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame setFrame = new CopyChangeSetFrame(CopyChange.this, "Find/Replace Set");
				setFrame.setVisible(true);
			}
		});
	}

	/**
	 * for the function of setItem: add several JMenuItems with initial chars
	 * and final char in itemProperties to doItems
	 * 
	 * @param itemProperties
	 */
	public void addItem(String[] itemProperties) {

		for (int i = 0; i < itemProperties.length; i++) {
			int itemsNumberTemp = doItemsNumber;
			JMenuItem temp = new JMenuItem(itemTextIni + "_" + (itemsNumberTemp));

			iniialTexts.add(itemsNumberTemp, defaultInitial);
			finalTexts.add(itemsNumberTemp, defaultFinal);
			doItems.add(itemsNumberTemp, temp);
			doItems.get(itemsNumberTemp).addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					dealClipText(itemsNumberTemp);
				}
			});

			write_setItemProperties(properties, itemsNumberTemp, itemProperties[i]);
			write_setItemsProperties(properties, Integer.toString(itemsNumberTemp + 1));
		}
	}

	/**
	 * for the function of setItem: change the initial chars or final char of
	 * JMenuItem
	 * 
	 * @param itemsIndex
	 * @param init
	 * @param finl
	 */
	public void changItem(int itemIndex, String init, String finl) {
		String initTemp = "";
		String finlTemp = "";
		if (init != null) {
			initTemp = init;
		}
		if (finl != null) {
			finlTemp = finl;
		}

		write_setItemProperties(properties, itemIndex, initTemp + seperator + finlTemp);
	}

	/**
	 * for the function of setItem: remove several JMenuItem
	 * 
	 * @param itemsIndex
	 */
	public void removeItem(int[] itemsIndex) {

		for (int i = 0; i < itemsIndex.length; i++) {
			if (itemsIndex[i] != buttonIndex) {
				if (itemsIndex[i] < buttonIndex) {
					write_setButtonProperties(properties, Integer.toString(buttonIndex - 1));
				}
				doItems.remove(itemsIndex[i]);
				iniialTexts.remove(itemsIndex[i]);
				finalTexts.remove(itemsIndex[i]);
				write_setItemsProperties(properties, Integer.toString(doItemsNumber - 1));
			}
		}
	}

	/**
	 * replace the initial chars with final char
	 */
	private void dealClipText(int itemIndex) {
		// read the clip board
		Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable clipContont = sysClip.getContents(null);
		if (clipContont != null) {
			if (clipContont.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				try {
					clipInputText = (String) clipContont.getTransferData(DataFlavor.stringFlavor);
				} catch (UnsupportedFlavorException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// replace
		System.out.println("Replace: (" + getIniialText(itemIndex) + ") to (" + getFinalText(itemIndex) + ")");
		clipOutputText = clipInputText.replaceAll(getIniialText(itemIndex), getFinalText(itemIndex))
				.replaceAll(getCenterText(itemIndex), getFinalText(itemIndex)).trim();
		// write the clip board
		Transferable writeText = new StringSelection(clipOutputText);
		sysClip.setContents(writeText, null);
	}

	public String getIniialText(int itemIndex) {
		return iniialTexts.get(itemIndex);
	}

	public void setIniialText(int itemIndex, String iniialText) {
		this.iniialTexts.set(itemIndex, iniialText);
	}

	public String getFinalText(int itemIndex) {
		return finalTexts.get(itemIndex);
	}

	public void setFinalText(int itemIndex, String finalText) {
		this.finalTexts.set(itemIndex, finalText);
	}

	public String getCenterText(int itemIndex) {
		return finalTexts.get(itemIndex) + "{1,}";
	}

	/**
	 * set the Button Label of MainButton
	 */
	private void setButtonText() {
		buttonText = "<html>&nbsp;&nbsp;Copy<br>Change<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + buttonIndex + "</html>";
	}

	// Override from ManiButton

	@Override
	public String getButtonText() {
		return buttonText;
	}

	@Override
	public void doButton() {
		dealClipText(buttonIndex);
	}

	@Override
	public boolean haveButtonProperties() {
		return haveButtonProperties;
	}

	@Override
	public String getButtonHint() {
		return buttonPropertiesHint;
	}

	@Override
	public String getButtonProperties() {
		return Integer.toString(buttonIndex);
	}

	@Override
	public void read_setButtonProperties(Properties properties) {
		buttonIndex = Integer.parseInt(properties.getProperty(buttonPropertiesName));
		setButtonText();
		return;
	}

	@Override
	public void write_setButtonProperties(Properties properties, String string) {
		buttonIndex = Integer.parseInt(string);
		setButtonText();
		properties.setProperty(buttonPropertiesName, Integer.toString(buttonIndex));
		return;
	}

	@Override
	public void read_setButtonPopupMenuItemProperties(Properties properties) {
		read_setPopupMenuItemProperties(properties);
		read_setButtonProperties(properties);
	}

	@Override
	public void writeButtonPopupMenuItemProperties(Properties properties) {
		properties.setProperty(buttonPropertiesName, Integer.toString(buttonIndex));
		writePopupMenuItemProperties(properties);
	}

	// Override from PopupMenuItem

	@Override
	public void setPropertiesObj(Properties properties) {
		if (properties != this.properties) {
			this.properties = properties;
		}
	}

	@Override
	public ArrayList<JMenuItem> getMenuItems() {
		items = new ArrayList<>();
		items.addAll(doItems);
		items.add(setItem);
		return items;
	}

	@Override
	public boolean haveItemsProperties() {
		return haveItemProperties;
	}

	@Override
	public String getItemsPropertiesName() {
		return itemsPropertiesName;
	}

	@Override
	public String getItemsProperties() {
		return Integer.toString(doItemsNumber);
	}

	@Override
	public void read_setPopupMenuItemProperties(Properties properties) {
		int tempDoItemsNumber = Integer.parseInt(properties.getProperty(itemsPropertiesName));

		if (tempDoItemsNumber < doItemsNumber) {
			for (int i = tempDoItemsNumber; i < doItemsNumber; i++) {
				doItems.remove(i);
				iniialTexts.remove(i);
				finalTexts.remove(i);
			}
		}

		for (int i = 0; i < tempDoItemsNumber; i++) {
			if (i >= doItemsNumber) {
				addItem(new String[] { properties.getProperty(itemsPropertiesName + "_" + i) });
			} else {
				read_setItemProperties(properties, i);
			}
		}
		// write_setItemsProperties(properties,
		// Integer.toString(tempItemsNumber));
	}

	@Override
	public void writePopupMenuItemProperties(Properties properties) {
		properties.setProperty(itemsPropertiesName, Integer.toString(doItemsNumber));
		for (int i = 0; i < doItemsNumber; i++) {
			properties.setProperty(getItemPropertiesName(i), getItemProperties(i));
		}
	}

	@Override
	public void read_setItemsProperties(Properties properties) {
		doItemsNumber = Integer.parseInt(properties.getProperty(itemsPropertiesName));
		return;
	}

	@Override
	public void write_setItemsProperties(Properties properties, String string) {
		doItemsNumber = Integer.parseInt(string);
		properties.setProperty(itemsPropertiesName, string);
	}

	@Override
	public boolean haveItemProperties() {
		return true;
	}

	@Override
	public String getItemPropertiesName(int itemIndex) {
		return itemsPropertiesName + "_" + itemIndex;
	}

	@Override
	public String getItemProperties(int itemIndex) {
		return getIniialText(itemIndex) + seperator + getFinalText(itemIndex);
	}

	@Override
	public void read_setItemProperties(Properties properties, int itemIndex) {
		String temp = properties.getProperty(getItemPropertiesName(itemIndex));
		setItemProperties(itemIndex, temp);
		return;
	}

	@Override
	public void write_setItemProperties(Properties properties, int itemIndex, String string) {
		setItemProperties(itemIndex, string);
		properties.setProperty(getItemPropertiesName(itemIndex), getItemProperties(itemIndex));
		return;
	}

	/**
	 * set the properties of JMenuItem[itemIndex]
	 * 
	 * @param itemIndex
	 * @param string
	 */
	private void setItemProperties(int itemIndex, String string) {
		String[] tempArray = string.split(seperator);

		if (tempArray.length == 2) {
			setIniialText(itemIndex, tempArray[0]);
			setFinalText(itemIndex, tempArray[1]);
		} else if (tempArray.length == 1) {
			setIniialText(itemIndex, "");
			setFinalText(itemIndex, defaultFinal);
		} else if (tempArray.length == 0) {
			setIniialText(itemIndex, defaultInitial);
			setFinalText(itemIndex, defaultFinal);
		} else {
			setIniialText(itemIndex, tempArray[0]);
			setFinalText(itemIndex, tempArray[1]);
			JOptionPane.showConfirmDialog(null, "You properties string may be wrong");
		}
	}
}
