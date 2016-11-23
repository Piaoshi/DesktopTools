package piaoshi.desktoptool.interfaces;

import java.util.Properties;

/**
 * To realize the function of JButton displayed on the main frame. and realize
 * the JMenuItem displayed on the JPopupMenu.
 * 
 * @author Piaoshi(jiayalei8@gmail.com)
 *
 */
public interface MainButton extends PopupMenuIterm {

	/**
	 * read the Properties and set the properties of mainButto and PopupMenuItem
	 * 
	 * @param properties
	 */
	public void read_setButtonPopupMenuItemProperties(Properties properties);

	/**
	 * write the Properties of MainButton and PopupMenuItem
	 * 
	 * @param properties
	 */
	public void writeButtonPopupMenuItemProperties(Properties properties);

	/**
	 * return whether the mainButton have properties to set
	 * 
	 * @return
	 */
	public boolean haveButtonProperties();

	/**
	 * return the properties of mainButton
	 * 
	 * @return
	 */
	public String getButtonProperties();

	/**
	 * read the Properties and set the properties of mainButton
	 * 
	 * @param properties
	 */
	public void read_setButtonProperties(Properties properties);

	/**
	 * write the Properties and set the properties of mainButton
	 * 
	 * @param properties
	 * @param string
	 */
	public void write_setButtonProperties(Properties properties, String string);

	/**
	 * return hint information when to set as MainButton
	 * 
	 * @return
	 */
	public String getButtonHint();

	/**
	 * return button Label when used as MainButton
	 * 
	 * @return
	 */
	public String getButtonText();

	/**
	 * function when used as MainButton
	 */
	public void doButton();

}
