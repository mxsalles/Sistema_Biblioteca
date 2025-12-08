package view.Usuario;

import controller.EmprestimoController;
import controller.UsuarioController;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MenuUsuario extends JFrame {
    private JPanel mainPanel;
    private JTextField txtBusca;
    private JButton btnBuscar;
    private JTable tblUsuarios;
    private JButton btnNovo;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnAtualizar;
    private JButton btnFechar;

    private UsuarioController usuarioController;
    private EmprestimoController emprestimoController;
    private DefaultTableModel tableModel;

    public MenuUsuario() {
        setTitle("Gerenciamento de Usuários");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        this.usuarioController = new UsuarioController();
        this.emprestimoController = new EmprestimoController();

        configurarTabela();
        configurarEventos();
        carregarUsuarios();
    }

    private void configurarTabela() {
        String[] colunas = {"ID", "Nome", "Sexo", "Celular", "E-mail", "Status Multa"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblUsuarios.setModel(tableModel);
        tblUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void configurarEventos() {
        btnNovo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novoUsuario();
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarUsuario();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirUsuario();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarUsuarios();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarUsuarios();
            }
        });

        btnFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void carregarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioController.listarTodos();
            atualizarTabela(usuarios);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar usuários: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void buscarUsuarios() {
        try {
            String busca = txtBusca.getText().trim();
            List<Usuario> usuarios;

            if (busca.isEmpty()) {
                usuarios = usuarioController.listarTodos();
            } else {
                usuarios = usuarioController.buscarPorNome(busca);
            }

            atualizarTabela(usuarios);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao buscar usuários: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void atualizarTabela(List<Usuario> usuarios) {
        tableModel.setRowCount(0);

        for (Usuario usuario : usuarios) {

            String statusMulta = emprestimoController.verificarStatusTexto(usuario.getId());

            Object[] row = {
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getSexo(),
                    usuario.getCelular(),
                    usuario.getEmail(),
                    statusMulta
            };
            tableModel.addRow(row);
        }
    }

    private void novoUsuario() {
        CadastroUsuario cadastro = new CadastroUsuario(this);
        cadastro.setVisible(true);

        if (cadastro.isSalvou()) {
            carregarUsuarios();
        }
    }

    private void editarUsuario() {
        int selectedRow = tblUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um usuário para editar",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            Usuario usuario = usuarioController.buscarPorId(id);

            if (usuario != null) {
                CadastroUsuario cadastro = new CadastroUsuario(this, usuario);
                cadastro.setVisible(true);

                if (cadastro.isSalvou()) {
                    carregarUsuarios();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao editar usuário: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void excluirUsuario() {
        int selectedRow = tblUsuarios.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um usuário para excluir",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir este usuário?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcao == JOptionPane.YES_OPTION) {
            try {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                usuarioController.removerUsuario(id);

                JOptionPane.showMessageDialog(this,
                        "Usuário excluído com sucesso!",
                        "Sucesso",
                        JOptionPane.INFORMATION_MESSAGE);

                carregarUsuarios();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir usuário: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}