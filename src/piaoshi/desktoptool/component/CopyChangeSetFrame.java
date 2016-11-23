package piaoshi.desktoptool.component;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import piaoshi.desktoptool.Utils.StringUtils;

import java.awt.GridLayout;
import java.awt.BorderLayout;

/**
 * 
 * @author Piaoshi(jiayalei8@gmail.com)
 *
 */
public class CopyChangeSetFrame extends JFrame {

	ArrayList<Integer> removeIndex = new ArrayList<Integer>();
	ArrayList<JTextField> iniArray = new ArrayList<>();
	ArrayList<JTextField> finArray = new ArrayList<>();
	ArrayList<JLabel> indArray = new ArrayList<>();
	int itemNumber;
	int addNumber = 0;

	public CopyChangeSetFrame(CopyChange copyChange, String title) {

		setTitle(title);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Container contain = getContentPane();

		JPanel panel_1 = new JPanel();
		JPanel panel_2 = new JPanel();
		JPanel panel_12 = new JPanel();
		JLabel initLabel = new JLabel("Find(char sequence):");
		JLabel finlLabel = new JLabel("Replace with (char):");
		JLabel indexLabel = new JLabel("Index  ");
		itemNumber = Integer.parseInt(copyChange.getItemsProperties());

		panel_1.setLayout(new GridLayout(0, 1));
		panel_2.setLayout(new GridLayout(0, 1));
		panel_12.setLayout(new GridLayout(0, 1));
		panel_1.add(initLabel);
		panel_2.add(finlLabel);
		panel_12.add(indexLabel);

		JTextField initTextField[] = new JTextField[itemNumber];
		JTextField finlTextField[] = new JTextField[itemNumber];
		JLabel indexLabel_1[] = new JLabel[itemNumber];
		for (int i = 0; i < itemNumber; i++) {

			String[] temp = copyChange.getItemProperties(i).split(CopyChange.seperator);
			initTextField[i] = new JTextField("Current: " + StringUtils.escapeSpecial(temp[0]), 20);

			if (temp.length == 2) {
				finlTextField[i] = new JTextField("Current: " + StringUtils.escapeSpecial(temp[1]), 20);
			} else if (temp.length == 1) {
				finlTextField[i] = new JTextField("Current: " + "", 20);
			}

			indexLabel_1[i] = new JLabel(" " + i);

			panel_1.add(initTextField[i]);
			panel_2.add(finlTextField[i]);
			panel_12.add(indexLabel_1[i]);
		}

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				for (int i = 0; i < addNumber; i++) {
					if (iniArray.get(i).getText().equals("")) {
						JOptionPane.showConfirmDialog(okButton,
								"Find Field of " + (i + itemNumber) + " Can Not Be Empty");
						return;
					}
				}

				for (int i = 0; i < itemNumber; i++) {
					if (!removeIndex.contains(i)) {
						boolean itemModify = false;
						String inputString_1 = initTextField[i].getText();
						if (!inputString_1.startsWith("Current")) {
							itemModify = true;
						} else {
							inputString_1 = copyChange.getIniialText(i);
						}

						String inputString_2 = finlTextField[i].getText();
						if (!inputString_2.startsWith("Current")) {
							itemModify = true;
						} else {
							inputString_2 = copyChange.getFinalText(i);
						}
						if (itemModify) {
							copyChange.changItem(i, inputString_1, inputString_2);
						}
					}
				}

				String add[] = new String[addNumber];
				for (int i = 0; i < addNumber; i++) {
					add[i] = iniArray.get(i).getText() + CopyChange.seperator + finArray.get(i).getText();
				}

				int remove[] = new int[removeIndex.size()];
				for (int i = 0; i < removeIndex.size(); i++) {
					remove[i] = (int) removeIndex.get(i);
				}

				if (remove.length > 0) {
					copyChange.removeItem(remove);
				}
				if (add.length > 0) {
					copyChange.addItem(add);
				}

				dispose();
			}
		});
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JButton removeButton = new JButton("Remove(Index):");
		JTextField removeTextField = new JTextField(3);
		JButton addButton = new JButton("Add");

		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String temp = removeTextField.getText();
				int tempInt = Integer.parseInt(temp);
				if (tempInt >= itemNumber && tempInt < (addNumber + itemNumber)) {

					int index = tempInt - itemNumber;
					panel_1.remove(iniArray.get(index));
					;
					panel_2.remove(finArray.get(index));
					panel_12.remove(indArray.get(index));

					iniArray.remove(index);
					finArray.remove(index);
					indArray.remove(index);

					addNumber--;
					for (int i = 0; i < iniArray.size(); i++) {
						indArray.get(i).setText((i + itemNumber) + "");
					}

					repaintItem(addNumber + itemNumber);
				} else if (tempInt < itemNumber && tempInt != Integer.parseInt(copyChange.getButtonProperties())) {

					removeIndex.add(tempInt);

					panel_1.remove(initTextField[tempInt]);
					;
					panel_2.remove(finlTextField[tempInt]);
					panel_12.remove(indexLabel_1[tempInt]);
					repaintItem(addNumber + itemNumber);
				} else if (tempInt == Integer.parseInt(copyChange.getButtonProperties())) {
					JOptionPane.showConfirmDialog(okButton, "Can Not Remove Which Is The Main One");
				}

			}
		});

		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField ini = new JTextField();
				JTextField fin = new JTextField();
				JLabel ind = new JLabel((addNumber + itemNumber) + "");

				iniArray.add(addNumber, ini);
				finArray.add(addNumber, fin);
				indArray.add(addNumber, ind);

				panel_1.add(ini);
				panel_2.add(fin);
				panel_12.add(ind);
				addNumber++;

				repaintItem(addNumber + itemNumber);
			}
		});

		JPanel panel_3 = new JPanel();
		panel_3.add(removeButton);
		panel_3.add(removeTextField);
		panel_3.add(new JLabel("     "));
		panel_3.add(addButton);
		panel_3.add(new JLabel("          "));
		panel_3.add(okButton);
		panel_3.add(cancelButton);

		contain.add(panel_12, BorderLayout.WEST);
		contain.add(panel_1, BorderLayout.CENTER);
		contain.add(panel_2, BorderLayout.EAST);
		contain.add(panel_3, BorderLayout.SOUTH);

		setSize(500, 100 + itemNumber * 30);
		setLocation(100, 100);
		setResizable(false);
	}

	private void repaintItem(int height) {

		setResizable(true);
		setSize(500, 100 + height * 30);
		setResizable(false);
		repaint();
	}
}