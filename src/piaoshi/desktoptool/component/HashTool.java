package piaoshi.desktoptool.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

import piaoshi.desktoptool.Utils.HashToolUtils;
import piaoshi.desktoptool.interfaces.MainButton;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.crypto.Data;

import java.awt.event.ActionListener;
import java.awt.event.TextListener;
import java.awt.event.ActionEvent;

/**
 * checksum of file and String
 * 
 * @author Piaoshi(jiayalei8@gmail.com)
 */
public class HashTool extends JFrame implements MainButton {

	/**
	 * HashTool singleton object
	 */
	private static HashTool hashTool = new HashTool();
	/**
	 * MainButton have no properties to set
	 */
	private boolean haveButtonProperties = false;
	/**
	 * button label
	 */
	private String buttonText;
	/**
	 * Properties key of buttonProperties
	 */
	private String buttonPropertiesName = null;
	/**
	 * Hint info when to set as MainButton
	 */
	private String buttonPropertiesHint = null;
	/**
	 * JMenuItem to show the JFrame
	 */
	private ArrayList<JMenuItem> items;
	/**
	 * number of hashType
	 */
	private int hashTypeNumber = 4;

	/**
	 * return singleton object of HashTool
	 * 
	 * @return
	 */
	public static HashTool getInstance() {
		return hashTool;
	}

	private HashTool() {

		items = new ArrayList<>();
		JMenuItem menuItem = new JMenuItem(getButtonText());
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doButton();
			}
		});
		items.add(menuItem);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Hash Tool");
		setSize(500, 400);
		setLocation(400, 100);
		setResizable(false);

		JLabel hashTypeLable = new JLabel("Hash Type");
		JCheckBox checkBoxs[] = new JCheckBox[hashTypeNumber];
		checkBoxs[0] = new JCheckBox("MD5");
		checkBoxs[1] = new JCheckBox("SHA-1");
		checkBoxs[2] = new JCheckBox("SHA256");
		checkBoxs[3] = new JCheckBox("SHA512");
		checkBoxs[0].setSelected(true);

		JPanel panel_11 = new JPanel();
		panel_11.setLayout(new GridLayout(0, 1));
		panel_11.add(hashTypeLable);
		// System.out.println(hashTypeNumber);
		for (int i = 0; i < hashTypeNumber; i++) {
			panel_11.add(checkBoxs[i]);
		}

		JPanel panel_12 = new JPanel();
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(new BorderLayout());
		panel_1.add(panel_11, BorderLayout.NORTH);
		panel_1.add(panel_12, BorderLayout.SOUTH);

		JLabel strLabel = new JLabel("String");
		JTextArea strArea = new JTextArea();
		strArea.setEditable(true);
		JScrollPane strPane = new JScrollPane(strArea);
		strPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		strPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		JPanel panel_21 = new JPanel();
		panel_21.setLayout(new BorderLayout());
		panel_21.add(strLabel, BorderLayout.NORTH);
		panel_21.add(strPane, BorderLayout.CENTER);

		JLabel fileLabel = new JLabel("File");
		JButton fileSelectButton = new JButton("Browse...");
		JTextField fileField = new JTextField("File Selected...");

		JTextArea resultArea = new JTextArea("Result", 5, 0);
		resultArea.setEditable(false);
		JScrollPane resultPane = new JScrollPane(resultArea);
		resultPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		resultPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		strArea.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				String src = strArea.getText();
				if (src == null) {
					return;
				}
				String result = checksum(src, checkBoxs);
				resultArea.setText(result.toUpperCase());
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				String src = strArea.getText();
				if (src == null) {
					return;
				}
				String result = checksum(src, checkBoxs);
				resultArea.setText(result.toUpperCase());

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}
		});

		fileSelectButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(fileSelectButton);
				File file = fileChooser.getSelectedFile();
				if (file == null) {
					return;
				}
				String filePath = file.getAbsolutePath();

				if (filePath == null) {
					return;
				}
				fileField.setText(filePath);

				String result = checksum(file, checkBoxs);

				resultArea.setText(result.toUpperCase());
			}
		});
		JPanel panel_221 = new JPanel();
		panel_221.setLayout(new BorderLayout());
		panel_221.add(fileLabel, BorderLayout.WEST);
		panel_221.add(fileSelectButton, BorderLayout.EAST);
		panel_221.add(fileField, BorderLayout.SOUTH);

		JPanel panel_22 = new JPanel();
		panel_22.setLayout(new BorderLayout());
		panel_22.add(panel_221, BorderLayout.NORTH);
		panel_22.add(resultPane, BorderLayout.CENTER);

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(new GridLayout(2, 1));
		panel_2.add(panel_21);
		panel_2.add(panel_22);

		JButton strButton = new JButton("String Hash");
		strButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String src = strArea.getText();
				if (src == null) {
					return;
				}
				String result = checksum(src, checkBoxs);
				resultArea.setText(result.toUpperCase());
			}
		});
		JButton fileButton = new JButton("File Hash");
		fileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				File file = new File(fileField.getText());

				if (!file.isFile()) {
					return;
				}
				String result = checksum(file, checkBoxs);

				resultArea.setText(result.toUpperCase());
			}
		});
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new GridLayout(1, 4));
		panel_3.add(new JLabel());
		panel_3.add(strButton);
		panel_3.add(fileButton);
		panel_3.add(exitButton);

		DropTarget dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetListener() {

			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) {
				// TODO Auto-generated method stub

			}

			@Override
			public void drop(DropTargetDropEvent dtde) {
				// TODO Auto-generated method stub

				Transferable tr = dtde.getTransferable();

				if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {

					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

					List<File> list = null;
					try {
						list = (List) (tr.getTransferData(DataFlavor.javaFileListFlavor));
					} catch (UnsupportedFlavorException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (list == null) {
						return;
					}
					// Iterator iterator = list.iterator();
					// while (iterator.hasNext()) {
					// File f = (File) iterator.next();
					// }

					File file = list.get(0);

					if (!file.isFile()) {
						return;
					}

					fileField.setText(file.getAbsolutePath());

					String result = checksum(file, checkBoxs);

					resultArea.setText(result.toUpperCase());

					dtde.dropComplete(true);
				} else if (dtde.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					String string = null;
					try {
						string = (String) tr.getTransferData(DataFlavor.stringFlavor);
					} catch (UnsupportedFlavorException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (string == null) {
						return;
					}
					strArea.setText(string);
					;

					String result = checksum(string, checkBoxs);
					resultArea.setText(result.toUpperCase());

					dtde.dropComplete(true);
				} else {
					System.out.println("Not Support");
					dtde.rejectDrop();
				}
			}

			@Override
			public void dragOver(DropTargetDragEvent dtde) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dragExit(DropTargetEvent dte) {
				// TODO Auto-generated method stub

			}

			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				// TODO Auto-generated method stub

			}
		});

		getContentPane().add(panel_1, BorderLayout.WEST);
		getContentPane().add(panel_2, BorderLayout.CENTER);
		getContentPane().add(panel_3, BorderLayout.SOUTH);

	}

	private void setButtonText() {
		buttonText = "Hash";
	}

	/**
	 * return the hash code of file with the method according to JCheckBox[]
	 * 
	 * @param file
	 * @param checkBoxs
	 * @return
	 */
	private String checksum(File file, JCheckBox[] checkBoxs) {
		if (file == null) {
			return null;
		}

		try {
			FileInputStream fi;

			String temp = file.getAbsolutePath() + "\n";

			if (checkBoxs[0].isSelected()) {
				fi = new FileInputStream(file);
				temp = temp + "MD5: " + HashToolUtils.md5Hex(fi) + "\n";
				HashToolUtils.closeInputStream(fi);
			}
			if (checkBoxs[1].isSelected()) {
				fi = new FileInputStream(file);
				temp = temp + "SHA-1: " + HashToolUtils.sha1Hex(fi) + "\n";
				HashToolUtils.closeInputStream(fi);
			}
			if (checkBoxs[2].isSelected()) {
				fi = new FileInputStream(file);
				temp = temp + "SHA256: " + HashToolUtils.sha256Hex(fi) + "\n";
				HashToolUtils.closeInputStream(fi);
			}
			if (checkBoxs[3].isSelected()) {
				fi = new FileInputStream(file);
				temp = temp + "SHA512 :" + HashToolUtils.sha512Hex(fi) + "\n";
				HashToolUtils.closeInputStream(fi);
			}

			return temp;

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * return the hash code of string with the method according to JCheckBox[]
	 * 
	 * @param string
	 * @param checkBoxs
	 * @return
	 */
	private String checksum(String string, JCheckBox[] checkBoxs) {
		if (string == null) {
			return null;
		}

		String temp = "";

		if (checkBoxs[0].isSelected()) {
			temp = temp + "MD5: " + HashToolUtils.md5Hex(string) + "\n";
		}
		if (checkBoxs[1].isSelected()) {
			temp = temp + "SHA-1: " + HashToolUtils.sha1Hex(string) + "\n";
		}
		if (checkBoxs[2].isSelected()) {
			temp = temp + "SHA256: " + HashToolUtils.sha256Hex(string) + "\n";
		}
		if (checkBoxs[3].isSelected()) {
			temp = temp + "SHA512: " + HashToolUtils.sha512Hex(string) + "\n";
		}

		return temp;
	}

	@Override
	public String getItemProperties(int arg0) {
		return null;
	}

	@Override
	public String getItemPropertiesName(int arg0) {
		return null;
	}

	@Override
	public String getItemsProperties() {
		return null;
	}

	@Override
	public String getItemsPropertiesName() {
		return null;
	}

	@Override
	public ArrayList<JMenuItem> getMenuItems() {
		return items;
	}

	@Override
	public boolean haveItemProperties() {
		return false;
	}

	@Override
	public boolean haveItemsProperties() {
		return false;
	}

	@Override
	public void read_setPopupMenuItemProperties(Properties arg0) {
		return;
	}

	@Override
	public void read_setItemProperties(Properties arg0, int arg1) {
		return;
	}

	@Override
	public void read_setItemsProperties(Properties arg0) {
		return;
	}

	@Override
	public void setPropertiesObj(Properties arg0) {
		return;
	}

	@Override
	public void writePopupMenuItemProperties(Properties arg0) {
		return;
	}

	@Override
	public void write_setItemProperties(Properties arg0, int arg1, String arg2) {
		return;
	}

	@Override
	public void write_setItemsProperties(Properties arg0, String arg1) {
		return;
	}

	@Override
	public void doButton() {
		setVisible(true);
	}

	@Override
	public String getButtonHint() {
		return buttonPropertiesHint;
	}

	@Override
	public String getButtonProperties() {
		return null;
	}

	@Override
	public String getButtonText() {
		setButtonText();
		return buttonText;
	}

	@Override
	public boolean haveButtonProperties() {
		return haveButtonProperties;
	}

	@Override
	public void read_setButtonPopupMenuItemProperties(Properties arg0) {
		return;
	}

	@Override
	public void read_setButtonProperties(Properties arg0) {
		return;
	}

	@Override
	public void writeButtonPopupMenuItemProperties(Properties arg0) {
		return;
	}

	@Override
	public void write_setButtonProperties(Properties arg0, String arg1) {
		return;
	}

}
