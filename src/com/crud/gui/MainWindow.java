package com.crud.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import com.crud.dao.UsuarioDAO;
import com.crud.model.Usuario;

public class MainWindow extends JFrame {

	UsuarioDAO usuarioDAO;
	private JPanel pForm, pTable;
	private JLabel lName, lCpf, lPhone, lEmail;
	private JTextField tfName, tfCpf, tfPhone, tfEmail;
	private JButton bCreate, bDelete, bRead, bUpdate;
	private JScrollPane spTable;
	private JTable tTable;

	public MainWindow() {
		usuarioDAO = new UsuarioDAO();
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		createForm();
		pack();
		refreshTable();
	}

	public void createForm() {
		pForm = new JPanel();
		pForm.setLayout(new GridLayout(4, 3, 10, 5));

		getContentPane().setLayout(
				new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));

		pForm.setLayout(new java.awt.GridLayout(4, 3, 10, 5));

		lName = new JLabel("Nome: ");
		lName.setHorizontalAlignment(SwingConstants.RIGHT);
		tfName = new JTextField();
		pForm.add(lName);
		pForm.add(tfName);

		bCreate = new JButton("Criar");
		bCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				create();
			}
		});
		pForm.add(bCreate);

		lCpf = new JLabel("CPF: ");
		lCpf.setHorizontalAlignment(SwingConstants.RIGHT);
		tfCpf = new JTextField();
		pForm.add(lCpf);
		pForm.add(tfCpf);

		bRead = new JButton("Ler");
		bRead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				read();
			}
		});
		pForm.add(bRead);

		lPhone = new JLabel("Telefone: ");
		lPhone.setHorizontalAlignment(SwingConstants.RIGHT);
		tfPhone = new JTextField();
		pForm.add(lPhone);
		pForm.add(tfPhone);

		bUpdate = new JButton("Atualizar");
		bUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				update();
			}
		});
		pForm.add(bUpdate);

		lEmail = new JLabel("Email: ");
		lEmail.setHorizontalAlignment(SwingConstants.RIGHT);
		tfEmail = new JTextField();
		pForm.add(lEmail);
		pForm.add(tfEmail);

		bDelete = new JButton("Apagar");
		bDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				delete();
			}
		});
		pForm.add(bDelete);

		getContentPane().add(pForm);

		pTable = new JPanel();
		tTable = new JTable();
		pTable.setLayout(new BorderLayout());
		tTable.setModel(new DefaultTableModel(new Object[5][5], new String[] {
				"Id", "Nome", "CPF", "Telefone", "Email" }));
		spTable = new JScrollPane();
		spTable.setViewportView(tTable);
		pTable.add(spTable, BorderLayout.CENTER);
		tTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				read();
			}
		});

		getContentPane().add(pTable);
	}

	public void cleanFields() {
		tfName.setText("");
		tfCpf.setText("");
		tfPhone.setText("");
		tfEmail.setText("");
	}

	public boolean isValidData() {
		if (tfName.getText().isEmpty() || tfCpf.getText().isEmpty()
				|| tfPhone.getText().isEmpty() || tfEmail.getText().isEmpty())
			return false;
		else
			return true;
	}

	public void refreshTable() {
		DefaultTableModel tableModel = (DefaultTableModel) tTable.getModel();
		tableModel.setNumRows(0);

		List<Usuario> usuarios = usuarioDAO.readAll();

		for (int linha = 0; linha < usuarios.size(); linha++) {
			Usuario user = usuarios.get(linha);

			tableModel.addRow(new Object[] { 1 });

			tTable.setValueAt(user.getId(), linha, 0);
			tTable.setValueAt(user.getNome(), linha, 1);
			tTable.setValueAt(user.getCpf(), linha, 2);
			tTable.setValueAt(user.getTelefone(), linha, 3);
			tTable.setValueAt(user.getEmail(), linha, 4);

		}
	}

	public int getSelectedId() {
		int linhaSelecionada = tTable.getSelectedRow();
		if (linhaSelecionada >= 0) {
			int idSelecionado = (int) tTable.getValueAt(linhaSelecionada, 0);
			return idSelecionado;
		} else {
			System.err.println("Não existe linha selecionada");
			return -1;
		}
	}

	public void create() {
		Usuario user = new Usuario(tfName.getText(), tfCpf.getText(),
				tfPhone.getText(), tfEmail.getText());

		if (isValidData()) {
			usuarioDAO.create(user);
			refreshTable();
			JOptionPane.showMessageDialog(null, "Usuário " + tfName.getText()
					+ " inserido com sucesso!");
			cleanFields();
		} else {
			JOptionPane
			.showMessageDialog(null,
					"Usuário não pode ser inserido. Verifique os campos e tente novamente!");
		}

	}

	public void read() {
		if (getSelectedId() >= 0) {
			Usuario user = usuarioDAO.read(getSelectedId());
			cleanFields();
			tfName.setText(user.getNome());
			tfCpf.setText(user.getCpf());
			tfPhone.setText(user.getTelefone());
			tfEmail.setText(user.getEmail());
		}
	}

	public void update() {
		if (getSelectedId() >= 0) {
			Usuario user = new Usuario(getSelectedId(), tfName.getText(),
					tfCpf.getText(), tfPhone.getText(), tfEmail.getText());
			usuarioDAO.update(user);
			refreshTable();
		}

	}

	public void delete() {
		DefaultTableModel tableModel = (DefaultTableModel) tTable.getModel();
		int linhaSelecionada = tTable.getSelectedRow();
		if (linhaSelecionada >= 0) {
			System.out.println("Usuário de id=" + getSelectedId()
					+ " removido.");
			usuarioDAO.delete(getSelectedId());
			tableModel.removeRow(linhaSelecionada);
			refreshTable();
		} else {
			JOptionPane.showMessageDialog(null,
					"Nenhum usuário selecionado para remoção!");
		}
	}

}
