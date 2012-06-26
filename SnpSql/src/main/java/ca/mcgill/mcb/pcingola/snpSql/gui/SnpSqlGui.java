package ca.mcgill.mcb.pcingola.snpSql.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ca.mcgill.mcb.pcingola.util.Gpr;

public class SnpSqlGui {

	private JFrame frame;
	private JTextField textFieldVcfFile;
	private JTextField textFieldVal;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SnpSqlGui window = new SnpSqlGui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SnpSqlGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panelFile = new JPanel();
		frame.getContentPane().add(panelFile, BorderLayout.NORTH);

		JLabel lblVcfFile = new JLabel("Vcf file");
		panelFile.add(lblVcfFile);

		textFieldVcfFile = new JTextField();
		textFieldVcfFile.setText("/home/pcingola/snpSql/t2d2.10k.vcf");
		panelFile.add(textFieldVcfFile);
		textFieldVcfFile.setColumns(10);

		JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Open File
				Gpr.debug("Open file: " + textFieldVcfFile.getText());
			}
		});
		panelFile.add(btnOpen);

		JPanel queryPanel = new JPanel();
		frame.getContentPane().add(queryPanel, BorderLayout.CENTER);

		JComboBox comboBoxVar = new JComboBox();
		comboBoxVar.setModel(new DefaultComboBoxModel(new String[] { "", "CHROM", "POS", "ID", "REF", "ALT", "QUAL", "FILTER", "INFO" }));
		queryPanel.add(comboBoxVar);

		JComboBox comboBoxOp = new JComboBox();
		comboBoxOp.setModel(new DefaultComboBoxModel(new String[] { "", "=", "!=", "<", "<=", ">", ">=", "LIKE" }));
		queryPanel.add(comboBoxOp);

		textFieldVal = new JTextField();
		queryPanel.add(textFieldVal);
		textFieldVal.setColumns(10);
	}

}
