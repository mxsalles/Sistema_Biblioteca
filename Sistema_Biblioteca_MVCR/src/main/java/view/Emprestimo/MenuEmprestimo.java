package view.Emprestimo;

import controller.EmprestimoController;
import model.Emprestimo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MenuEmprestimo extends JFrame {
    private JPanel mainPanel;
    private JTable tblEmprestimos;
    private JButton btnNovoEmprestimo;
    private JButton btnRegistrarDevolucao;
    private JButton btnListarAtrasados;
    private JButton btnAtualizar;
    private JButton btnFechar;

    private EmprestimoController emprestimoController;
    private DefaultTableModel tableModel;

    public MenuEmprestimo() {
        setTitle("Gerenciamento de Empréstimos");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);

        this.emprestimoController = new EmprestimoController();

        configurarTabela();
        configurarEventos();
        carregarEmprestimosAtivos();
    }

    /**
     * Configura a tabela de empréstimos.
     */
    private void configurarTabela() {
        String[] colunas = {"ID", "Usuário", "Livro", "Data Empréstimo", "Devolução Prevista", "Status"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblEmprestimos.setModel(tableModel);
        tblEmprestimos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Configura os eventos dos botões.
     */
    private void configurarEventos() {
        btnNovoEmprestimo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novoEmprestimo();
            }
        });

        btnRegistrarDevolucao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarDevolucao();
            }
        });

        btnListarAtrasados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarAtrasados();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarEmprestimosAtivos();
            }
        });

        btnFechar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    /**
     * Carrega empréstimos ativos na tabela.
     */
    private void carregarEmprestimosAtivos() {
        try {
            List<Emprestimo> emprestimos = emprestimoController.listarAtivos();
            atualizarTabela(emprestimos);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar empréstimos: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Lista apenas empréstimos atrasados.
     */
    private void listarAtrasados() {
        try {
            List<Emprestimo> emprestimos = emprestimoController.listarAtrasados();
            atualizarTabela(emprestimos);

            if (emprestimos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Não há empréstimos atrasados!",
                    "Informação",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao listar empréstimos atrasados: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Atualiza a tabela com a lista de empréstimos.
     */
    private void atualizarTabela(List<Emprestimo> emprestimos) {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Emprestimo emprestimo : emprestimos) {
            String status;
            if (emprestimo.isAtrasado()) {
                status = "ATRASADO (" + emprestimo.getDiasAtraso() + " dias)";
            } else {
                status = "Em dia";
            }

            Object[] row = {
                emprestimo.getId(),
                emprestimo.getUsuario().getNome(),
                emprestimo.getLivro().getTitulo(),
                emprestimo.getDataEmprestimo().format(formatter),
                emprestimo.getDataDevolucaoPrevista().format(formatter),
                status
            };
            tableModel.addRow(row);

            // Destaca linhas atrasadas em vermelho
            if (emprestimo.isAtrasado()) {
                int rowIndex = tableModel.getRowCount() - 1;
                // Nota: Para colorir a linha, seria necessário um TableCellRenderer customizado
            }
        }
    }

    /**
     * Abre a tela de registro de novo empréstimo.
     */
    private void novoEmprestimo() {
        RegistrarEmprestimo registrar = new RegistrarEmprestimo(this);
        registrar.setVisible(true);

        if (registrar.isRegistrou()) {
            carregarEmprestimosAtivos();
        }
    }

    /**
     * Registra a devolução do empréstimo selecionado.
     */
    private void registrarDevolucao() {
        int selectedRow = tblEmprestimos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um empréstimo para registrar a devolução",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcao = JOptionPane.showConfirmDialog(this,
            "Deseja registrar a devolução deste empréstimo?",
            "Confirmar Devolução",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (opcao == JOptionPane.YES_OPTION) {
            try {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                emprestimoController.registrarDevolucao(id);

                JOptionPane.showMessageDialog(this,
                    "Devolução registrada com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

                carregarEmprestimosAtivos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erro ao registrar devolução: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
