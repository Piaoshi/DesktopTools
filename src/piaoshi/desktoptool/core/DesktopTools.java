package piaoshi.desktoptool.core;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import piaoshi.desktoptool.interfaces.MainButton;
import piaoshi.desktoptool.interfaces.PopupMenuIterm;
import piaoshi.desktoptool.interfaces.Singlet;

/**
 * main method and main frame to display. This tool is for daily use, which can
 * include many functions, and you can add your function yourself.
 * 
 * @author Piaoshi(jiayalei8@gmail.com)
 *
 */
public class DesktopTools extends JFrame {

	/**
	 * container to place the Main Button
	 */
	private JPanel contentPane;
	/**
	 * Button connected to Main Button
	 */
	private JButton mainButton;
	/**
	 * Popup Menu to setting and use
	 */
	private JPopupMenu popupMenu;
	/**
	 * Properties object
	 */
	private Properties properties;
	/**
	 * object of Main Button
	 */
	private MainButton mainButtonObj;
	/**
	 * class name of Main Button
	 */
	private String mainButtonClassName;
	/**
	 * point of where mouseEvernt takes place.
	 */
	private Point offset = new Point(0, 0);
	/**
	 * Properties key for Main Button
	 */
	private String mainButtonProName = "mainButton";
	/**
	 * Properties key for using items
	 */
	private String useItemsProName = "useitems";
	/**
	 * Properties key for all items available
	 */
	private String allItemsProName = "items";
	/**
	 * To make the log file clear
	 */
	private String outputWarn = "########";
	/**
	 * Properties file
	 */
	private File proFile;
	/**
	 * log file
	 */
	private File logFile;

	/**
	 * main function to start the program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new DesktopTools();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * JFrame to be shown
	 */
	public DesktopTools() {
		// load properties file
		try {
			proFile = new File(System.getProperty("user.dir") + "/DesktopTool.xml");
			logFile = new File(System.getProperty("user.dir") + "/DesktopTool.log");
			// System.setOut(new PrintStream(new FileOutputStream(logFile),
			// true));

			System.out.println(outputWarn + "Begin Initialising: ");

			if (proFile.exists() && proFile.isFile()) {
				properties = new Properties();
				properties.loadFromXML(new FileInputStream(proFile));
				;
			} else {
				properties = new Properties();
				properties.setProperty(mainButtonProName, "CopyChange");
				properties.setProperty(allItemsProName, "CopyChange,Ruler,HashTool,Snapshot");
				properties.setProperty(useItemsProName, "CopyChange,Ruler,HashTool,Snapshot");
				Class clazz = loadClass("CopyChange");
				((MainButton) clazz.getMethod("getInstance").invoke(clazz, null))
						.writeButtonPopupMenuItemProperties(properties);

				clazz = loadClass("Ruler");
				((MainButton) clazz.getMethod("getInstance").invoke(clazz, null))
						.writeButtonPopupMenuItemProperties(properties);

				clazz = loadClass("HashTool");
				((MainButton) clazz.getMethod("getInstance").invoke(clazz, null))
						.writeButtonPopupMenuItemProperties(properties);

				clazz = loadClass("Snapshot");
				((MainButton) clazz.getMethod("getInstance").invoke(clazz, null))
						.writeButtonPopupMenuItemProperties(properties);

				writeProperties(); // write Properties to file

			}
			properties.store(System.out, null);
			System.out.println(outputWarn + "End Initialising");

		} catch (IOException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e1) {
			e1.printStackTrace();
		}

		setAlwaysOnTop(true);
		setUndecorated(true);
		setShape(new Ellipse2D.Double(0, 0, 55, 55));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(1200, -25, 55, 55);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		composeMainButton();

		Font mainButtonFont = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
		mainButton.setFont(mainButtonFont);
		mainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainButton.setMargin(new Insets(0, 0, 0, 0));
		mainButton.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				setLocation(getX() + e.getPoint().x - offset.x, getY() + e.getPoint().y - offset.y);
			}
		});
		mainButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1 && SwingUtilities.isLeftMouseButton(e)) {
					mainButtonObj.doButton();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					offset = e.getPoint();
				} else {
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					composePopupMenu();
					popupMenu.show(mainButton, e.getX(), e.getY());
				}
			}
		});
		mainButton.setComponentPopupMenu(popupMenu);
		contentPane.add(mainButton, BorderLayout.CENTER);
	}

	/**
	 * compose MainButton
	 */
	private void composeMainButton() {
		try {
			String className = properties.getProperty(mainButtonProName);
			mainButtonClassName = className;
			Class<Singlet> clazz = loadClass(className);

			// if (!clazz.isAssignableFrom(MainButton.class)) {
			// System.err.println("mainButton setted in db.properties is
			// error");
			// return;
			// }
			mainButtonObj = (MainButton) clazz.getMethod("getInstance", null).invoke(clazz, null);
			mainButtonObj.setPropertiesObj(properties);
			mainButtonObj.read_setButtonPopupMenuItemProperties(properties);

			String mainButtonText = mainButtonObj.getButtonText();
			mainButton = new JButton(mainButtonText);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * compose PopupMenu
	 */
	private void composePopupMenu() {
		popupMenu = new JPopupMenu();

		// get items info from Properties
		String useItems = properties.getProperty(useItemsProName);
		String[] useItemsArray = useItems.split(",");

		String allItems = properties.getProperty(allItemsProName);
		String[] allItemsArray = allItems.split(",");

		// generate MenuItem of "SET"
		setMenuItem(allItemsArray, useItemsArray);

		// generate MenuItem of useItems for PopupMenu
		for (int i = 0; i < useItemsArray.length; i++) {

			Class clazz = null;
			try {
				clazz = loadClass(useItemsArray[i]);
				// if (!clazz.isAssignableFrom(PopupMenuIterm.class)) {
				// System.err.println("12345");
				// return;
				// }

				PopupMenuIterm popupMenuItermObj = (PopupMenuIterm) clazz.getMethod("getInstance", null).invoke(clazz,
						null);
				popupMenuItermObj.setPropertiesObj(properties);
				popupMenuItermObj.read_setItemsProperties(properties);
				if (popupMenuItermObj.haveItemsProperties()) {
					popupMenuItermObj.read_setPopupMenuItemProperties(properties);
				}
				ArrayList<JMenuItem> items = popupMenuItermObj.getMenuItems();

				popupMenu.addSeparator();
				for (int j = 0; j < items.size(); j++) {
					popupMenu.add(items.get(j));
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}

		}

		// generate MenuItem of "EXIT"
		exitMenuItem();
	}

	/**
	 * generate MenuItem of "SET"
	 * 
	 * @param allItemsArray
	 *            all functions can be used
	 * @param useItemsArray
	 *            functions to use now
	 */
	private void setMenuItem(String[] allItemsArray, String[] useItemsArray) {
		JMenuItem setItem = new JMenuItem("SET");
		setItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFrame setFrame = new JFrame("Set");
				setFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				Container contain = setFrame.getContentPane();

				JLabel mainLabel = new JLabel("Chose Main Button:");
				JComboBox<String> mainList = new JComboBox<String>(useItemsArray);
				mainList.setSelectedItem(properties.getProperty(mainButtonProName));
				JLabel hintLable = new JLabel();
				JTextField propertyTextField = new JTextField();
				JPanel optionPanel = new JPanel();
				optionPanel.setLayout(new BorderLayout());
				optionPanel.add(hintLable, BorderLayout.NORTH);
				optionPanel.add(propertyTextField, BorderLayout.SOUTH);
				hintLable.setHorizontalAlignment(SwingConstants.RIGHT);
				Class clazz = DesktopTools.this.loadClass(properties.getProperty(mainButtonProName));
				boolean haveProperty = false;
				try {
					haveProperty = (boolean) ((MainButton) clazz.getMethod("getInstance").invoke(clazz, null))
							.haveButtonProperties();
					if (haveProperty) {
						hintLable.setText("Set: "
								+ ((MainButton) clazz.getMethod("getInstance").invoke(clazz, null)).getButtonHint());
						propertyTextField.setText(((MainButton) clazz.getMethod("getInstance").invoke(clazz, null))
								.getButtonProperties());

						hintLable.setVisible(true);
						propertyTextField.setVisible(true);
					} else {
						hintLable.setVisible(false);
						propertyTextField.setVisible(false);
					}

				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e1) {
					e1.printStackTrace();
				}

				mainList.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							optionalDisplay(hintLable, propertyTextField, (String) e.getItem());
							try {
								Class clazz = DesktopTools.this.loadClass((String) e.getItem());
								propertyTextField
										.setText(((MainButton) clazz.getMethod("getInstance").invoke(clazz, null))
												.getButtonProperties());
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
									| NoSuchMethodException | SecurityException e1) {
								e1.printStackTrace();
							}
						}
					}
				});

				JPanel mainPanel = new JPanel();
				mainPanel.setLayout(new BorderLayout());
				mainPanel.add(mainLabel, BorderLayout.NORTH);
				mainPanel.add(mainList, BorderLayout.SOUTH);

				JLabel allLabel = new JLabel("All Components:");
				JList<String> allList = new JList<String>();
				JLabel useLabel = new JLabel("Using Components:");
				JList<String> useList = new JList<String>();
				DefaultListModel<String> useListModel = new DefaultListModel<String>();
				for (int i = 0; i < useItemsArray.length; i++) {
					useListModel.addElement(useItemsArray[i]);
				}
				DefaultListModel<String> allListModel = new DefaultListModel<String>();
				for (int i = 0; i < allItemsArray.length; i++) {
					allListModel.addElement(allItemsArray[i]);
				}
				useList.setModel(useListModel);
				allList.setModel(allListModel);

				JScrollPane allScrollPane = new JScrollPane(allList);
				JScrollPane useScrollPane = new JScrollPane(useList);
				allScrollPane.setPreferredSize(new Dimension(200, 200));
				useScrollPane.setPreferredSize(new Dimension(200, 200));

				allList.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2) {
							if (!useListModel.contains(allList.getSelectedValue())) {
								useListModel.addElement(allList.getSelectedValue());
								useList.setModel(useListModel);

								StringBuilder tempUseList = new StringBuilder();
								for (int i = 0; i < useListModel.size(); i++) {
									tempUseList.append(useListModel.get(i));
									tempUseList.append(",");
								}
								tempUseList.deleteCharAt(tempUseList.length() - 1);
								properties.setProperty("useitems", tempUseList.toString());
							}
						}
					}

				});

				useList.addMouseListener(new MouseAdapter() {

					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getClickCount() == 2 && !useList.getSelectedValue().equals(mainButtonClassName)) {
							useListModel.removeElement(useList.getSelectedValue());
							useList.setModel(useListModel);

							StringBuilder tempUseList = new StringBuilder();
							for (int i = 0; i < useListModel.size(); i++) {
								tempUseList.append(useListModel.get(i));
								tempUseList.append(",");
							}
							tempUseList.deleteCharAt(tempUseList.length() - 1);
							properties.setProperty("useitems", tempUseList.toString());
						}
					}

				});

				JButton addButton = new JButton("Add");
				addButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
						fileChooser.setFileFilter(new FileFilter() {

							@Override
							public String getDescription() {
								return ".jar(.zip)(java package)";
							}

							@Override
							public boolean accept(File f) {

								if (f.getName().toLowerCase().endsWith(".zip")
										|| f.getName().toLowerCase().endsWith(".jar") || f.isDirectory()) {
									return true;
								}

								return false;
							}
						});
						int returnVal = fileChooser.showOpenDialog(setFrame);

						if (returnVal == JFileChooser.APPROVE_OPTION) {
							StringBuilder tempProperties = new StringBuilder();
							String classPackageInfo = null;
							String tempPackageExtra = "";

							File srcFile = fileChooser.getSelectedFile();
							String fileName = srcFile.getName();
							fileName = fileName.substring(0, fileName.length() - 4);

							JarFile jarFile;
							try {
								jarFile = new JarFile(srcFile);

								Enumeration<JarEntry> en = jarFile.entries();
								int tempInt = 0;
								while (en.hasMoreElements()) {
									String tempClassPackageInfo = en.nextElement().getName();
									System.out.println(tempClassPackageInfo);
									if (tempClassPackageInfo.endsWith(fileName + ".class")
											|| tempClassPackageInfo.endsWith(fileName + ".Class")
											|| tempClassPackageInfo.endsWith(fileName + ".CLASS")) {
										tempInt = tempInt + 1;
										if (tempInt == 1) {
											classPackageInfo = tempClassPackageInfo.replace("/", ".").substring(0,
													tempClassPackageInfo.length() - 6);
										}
									}
								}
								if (tempInt != 1) {
									System.out.println("Error: package name and class name not equal");
									return;
								}

							} catch (IOException e1) {
								e1.printStackTrace();
							}
							int reutenExtra = JOptionPane.showConfirmDialog(setItem,
									"Does This Jar need other library?", "Yes/NO", JOptionPane.YES_NO_OPTION);

							if (reutenExtra == JOptionPane.YES_OPTION) {
								fileChooser.setMultiSelectionEnabled(true);
								returnVal = fileChooser.showSaveDialog(setItem);
								if (returnVal == JFileChooser.APPROVE_OPTION) {
									File extraFiles[] = fileChooser.getSelectedFiles();
									if (extraFiles.length != 0) {
										for (int i = 0; i < extraFiles.length; i++) {
											tempPackageExtra = tempPackageExtra + "," + extraFiles[i].getAbsolutePath();
											System.out.println(tempPackageExtra);
										}
									}
								}
							}
							System.out.println(classPackageInfo);

							tempProperties.append(classPackageInfo + "," + srcFile.getAbsolutePath());
							if (!tempPackageExtra.equals("")) {
								tempProperties.append(tempPackageExtra);
							}

							properties.setProperty(fileName, tempProperties.toString());

							allListModel.addElement(fileName);
							allList.setModel(allListModel);

							StringBuilder tempAllList = new StringBuilder();
							for (int i = 0; i < allListModel.size(); i++) {
								tempAllList.append(allListModel.get(i));
								tempAllList.append(",");
							}
							tempAllList.deleteCharAt(tempAllList.length() - 1);
							properties.setProperty(allItemsProName, tempAllList.toString());

						}

						System.out.println("Not Tested");
						;

					}

				});

				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						String temp = (String) mainList.getSelectedItem();
						if (temp != mainButtonClassName) {
							Class clazz = DesktopTools.this.loadClass(temp);
							try {
								mainButtonClassName = temp;

								mainButtonObj = (MainButton) clazz.getMethod("getInstance").invoke(clazz, null);
								properties.setProperty(mainButtonProName, temp);
							} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
									| NoSuchMethodException | SecurityException e1) {
								e1.printStackTrace();
							}
						}

						if (mainButtonObj.haveButtonProperties()) {
							mainButtonObj.write_setButtonProperties(properties, propertyTextField.getText());
						}

						mainButton.setText(mainButtonObj.getButtonText());

						writeProperties();

						setFrame.dispose();
					}
				});
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						setFrame.dispose();
					}
				});

				JPanel panel_1 = new JPanel();
				JPanel panel_2 = new JPanel();
				JPanel panel_3 = new JPanel();
				JPanel panel_4 = new JPanel();

				panel_1.setLayout(new BorderLayout());
				panel_1.add(mainPanel, BorderLayout.WEST);
				panel_1.add(optionPanel, BorderLayout.CENTER);

				panel_2.setLayout(new BorderLayout());
				panel_2.add(allLabel, BorderLayout.NORTH);
				panel_2.add(allScrollPane, BorderLayout.SOUTH);
				panel_3.setLayout(new BorderLayout());

				panel_3.add(useLabel, BorderLayout.NORTH);
				panel_3.add(useScrollPane, BorderLayout.SOUTH);
				panel_4.add(addButton);
				panel_4.add(okButton);
				panel_4.add(cancelButton);
				contain.add(panel_1, BorderLayout.NORTH);
				contain.add(panel_2, BorderLayout.WEST);
				contain.add(panel_3, BorderLayout.EAST);
				contain.add(panel_4, BorderLayout.SOUTH);

				setFrame.setSize(440, 350);
				setFrame.setLocation(400, 100);
				// setFrame.setResizable(false);
				setFrame.setVisible(true);
			}
		});
		popupMenu.add(setItem);
	}

	/**
	 * generate MenuItem of "EXIT"
	 */
	private void exitMenuItem() {
		popupMenu.addSeparator();
		JMenuItem exitMenuItem = new JMenuItem("EXIT");
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				writeProperties();
				System.exit(0);
			}
		});
		popupMenu.add(exitMenuItem);
	}

	/**
	 * Hint for change mainButton in JFrame generate by "SET" MenuItem
	 * 
	 * @param hintLabel
	 *            hint information to displayed
	 * @param propertyTextField
	 *            TextField to display hint info
	 * @param className
	 *            class name of the Class
	 */
	private void optionalDisplay(JLabel hintLabel, JTextField propertyTextField, String className) {
		Class<MainButton> clazz = loadClass(className);
		boolean haveProperty = false;
		try {
			haveProperty = (boolean) ((MainButton) clazz.getMethod("getInstance").invoke(clazz, null))
					.haveButtonProperties();
			if (haveProperty) {
				hintLabel.setText(
						"Set: " + ((MainButton) clazz.getMethod("getInstance").invoke(clazz, null)).getButtonHint());
				hintLabel.setVisible(true);
				propertyTextField.setVisible(true);
			} else {
				hintLabel.setVisible(false);
				propertyTextField.setVisible(false);
			}

		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * write Properties to file
	 */
	private void writeProperties() {
		try {
			properties.storeToXML(new FileOutputStream(proFile), "DesktopTool Settings");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * return Class from class information
	 * 
	 * @param className
	 * @return Class
	 */
	private Class loadClass(String className) {
		String classPath = null;
		try {
			if (properties.containsKey(className)) {
				String temp = properties.getProperty(className);
				String tempArray[] = temp.split(",");
				File srcFiles[] = new File[tempArray.length - 1];
				URL urls[] = new URL[tempArray.length - 1];
				System.out.println(temp);
				System.out.println(tempArray[0]);
				try {
					for (int i = 1; i < tempArray.length; i++) {
						srcFiles[i - 1] = new File(tempArray[i]);
						urls[i - 1] = srcFiles[i - 1].toURI().toURL();
						System.out.println(className + ":url:" + urls[i - 1].toString());
					}
					URLClassLoader classLoader = new URLClassLoader(urls, this.getClass().getClassLoader());
					// URLClassLoader classLoader =
					// (URLClassLoader)ClassLoader.getSystemClassLoader();

					return Class.forName(tempArray[0], true, classLoader);
				} catch (Exception ex) {
					ex.printStackTrace();
					return null;
				}

			} else {
				classPath = "piaoshi.desktoptool.component";
				return Class.forName(classPath + "." + className);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

}
