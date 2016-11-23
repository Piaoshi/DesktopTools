package piaoshi.desktoptool.interfaces;

import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JMenuItem;

/**
 * To realize the JMenuItem displayed on the JPopupMenu.
 * 
 * @author Piaoshi(jiayalei8@gmail.com)
 *
 */
public interface PopupMenuIterm extends Singlet {

	/**
	 * set the Properties to be used
	 * 
	 * @return properties Properties object
	 */
	public void setPropertiesObj(Properties properties);

	/**
	 * read the Properties and set the properties of ArrayList<JMenuItem> and
	 * JMenuItem
	 * 
	 * @param Properties
	 */
	public void read_setPopupMenuItemProperties(Properties properties);

	/**
	 * write the Properties of ArrayList<JMenuItem> and JMenuItems
	 * 
	 * @param properties
	 */
	public void writePopupMenuItemProperties(Properties properties);

	// ArrayList<JMenuItem>

	/**
	 * return the ArrayList of JMenuItem to be displayed on the PopupMenu of the
	 * MainButton
	 * 
	 * @return
	 */
	public ArrayList<JMenuItem> getMenuItems();

	/**
	 * return whether the ArrayList<JMenuItem> have properties to be set
	 * 
	 * @return
	 */
	public boolean haveItemsProperties();

	/**
	 * return the Properties key of ArrayList<JMenuItem> if have.
	 * 
	 * @return
	 */
	public String getItemsPropertiesName();

	/**
	 * return the Properties value of ArrayList<JMenuItem>, usually the number
	 * of JMenuItem
	 * 
	 * @return
	 */
	public String getItemsProperties();

	/**
	 * read Properties and set the properties of ArrayList<JMenuItem>, usually
	 * the number of JMenuItem
	 * 
	 * @param
	 */
	public void read_setItemsProperties(Properties properties);

	/**
	 * write the Properties of ArrayList<JMenuItem>, usually the number of
	 * JMenuItem
	 * 
	 * @param properties
	 * @param string
	 */
	public void write_setItemsProperties(Properties properties, String string);

	// JMenuItem

	/**
	 * return whether JMenuItem has properties to set
	 * 
	 * @return
	 */
	public boolean haveItemProperties();

	/**
	 * return the Properties key of JMenuItem[itemIndex]
	 * 
	 * @param itemIndex
	 * @return
	 */
	public String getItemPropertiesName(int itemIndex);

	/**
	 * return the Properties value of JMenuItem[itemIndex]
	 * 
	 * @param itemIndex
	 * @return
	 */
	public String getItemProperties(int itemIndex);

	/**
	 * read the Properties and set the properties of JMenuItem[itemIndex]
	 * 
	 * @param properties
	 * @param itemIndex
	 */
	public void read_setItemProperties(Properties properties, int itemIndex);

	/**
	 * write the Properties and set the properties of JMenuItem[itemIndex]
	 * 
	 * @param properties
	 * @param itemIndex
	 * @param itemProperties
	 */
	public void write_setItemProperties(Properties properties, int itemIndex, String itemProperties);

}
