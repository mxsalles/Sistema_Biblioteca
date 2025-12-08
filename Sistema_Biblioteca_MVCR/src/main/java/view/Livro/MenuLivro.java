package view.Livro;

import controller.LivroController;
import model.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Tela de gerenciamento de livros.
 * Permite listar, buscar, cadastrar, editar e excluir livros.
 */
public class MenuLivro extends JFrame {
    private JPanel mainPanel;
    private JTextField txtBusca;
    private JButton btnBuscar;
    private JTable tblLivros;
    private JButton btnNovo;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnAtualizar;
    private JButton btnFechar;

    private LivroController livroController;
    private DefaultTableModel tableModel;

    public MenuLivro() {
        setTitle("Gerenciamento de Livros");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        this.livroController = new LivroController();

        configurarTabela();
        configurarEventos();
        carregarLivros();
    }

    /**
     * Configura a tabela de livros.
     */
    private void configurarTabela() {
        String[] colunas = {"ID", "Título", "Autor", "Tema", "ISBN", "Data Publicação", "Qtd. Disponível"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabela não editável
            }
        };
        tblLivros.setModel(tableModel);
        tblLivros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Configura os eventos dos botões.
     */
    private void configurarEventos() {
        btnNovo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novoLivro();
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarLivro();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirLivro();
            }
        });

        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarLivros();
            }
        });

        btnAtualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                carregarLivros();
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
     * Carrega todos os livros na tabela.
     */
    private void carregarLivros() {
        try {
            List<Livro> livros = livroController.listarTodos();
            atualizarTabela(livros);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar livros: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Busca livros por título.
     */
    private void buscarLivros() {
        try {
            String busca = txtBusca.getText().trim();
            List<Livro> livros;

            if (busca.isEmpty()) {
                livros = livroController.listarTodos();
            } else {
                livros = livroController.buscarPorTitulo(busca);
            }

            atualizarTabela(livros);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao buscar livros: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Atualiza a tabela com a lista de livros.
     */
    private void atualizarTabela(List<Livro> livros) {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Livro livro : livros) {
            Object[] row = {
                livro.getId(),
                livro.getTitulo(),
                livro.getAutor(),
                livro.getTema(),
                livro.getIsbn(),
                livro.getDataPublicacao().format(formatter),
                livro.getQuantidadeDisponivel()
            };
            tableModel.addRow(row);
        }
    }

    /**
     * Abre a tela de cadastro de novo livro.
     */
    private void novoLivro() {
        CadastroLivro cadastro = new CadastroLivro(this);
        cadastro.setVisible(true);

        if (cadastro.isSalvou()) {
            carregarLivros();
        }
    }

    /**
     * Abre a tela de edição do livro selecionado.
     */
    private void editarLivro() {
        int selectedRow = tblLivros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um livro para editar",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            Livro livro = livroController.buscarPorId(id);

            if (livro != null) {
                CadastroLivro cadastro = new CadastroLivro(this, livro);
                cadastro.setVisible(true);

                if (cadastro.isSalvou()) {
                    carregarLivros();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao editar livro: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Exclui o livro selecionado.
     */
    private void excluirLivro() {
        int selectedRow = tblLivros.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Selecione um livro para excluir",
                "Aviso",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcao = JOptionPane.showConfirmDialog(this,
            "Deseja realmente excluir este livro?",
            "Confirmar Exclusão",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (opcao == JOptionPane.YES_OPTION) {
            try {
                Long id = (Long) tableModel.getValueAt(selectedRow, 0);
                livroController.removerLivro(id);

                JOptionPane.showMessageDialog(this,
                    "Livro excluído com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

                carregarLivros();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erro ao excluir livro: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}
