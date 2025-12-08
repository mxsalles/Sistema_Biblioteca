package view.Emprestimo;

import controller.EmprestimoController;
import controller.LivroController;
import controller.UsuarioController;
import model.Livro;
import model.Usuario;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Tela para registrar novos empréstimos.
 * Permite selecionar usuário e livro, e registrar o empréstimo com data.
 */
public class RegistrarEmprestimo extends JDialog {
    private JPanel mainPanel;
    private JComboBox<ComboItem> cmbUsuario;
    private JComboBox<ComboItem> cmbLivro;
    private JFormattedTextField txtDataEmprestimo;
    private JButton btnRegistrar;
    private JButton btnCancelar;

    private EmprestimoController emprestimoController;
    private UsuarioController usuarioController;
    private LivroController livroController;
    private boolean registrou = false;

    public RegistrarEmprestimo(JFrame parent) {
        super(parent, "Registrar Empréstimo", true);

        this.emprestimoController = new EmprestimoController();
        this.usuarioController = new UsuarioController();
        this.livroController = new LivroController();

        setContentPane(mainPanel);
        setSize(500, 350);
        setLocationRelativeTo(parent);
        setResizable(false);

        configurarComponentes();
        configurarEventos();
        carregarDados();
    }

    /**
     * Configura os componentes da tela.
     */
    private void configurarComponentes() {
        // Configura máscara para data
        try {
            MaskFormatter maskData = new MaskFormatter("##/##/####");
            maskData.setPlaceholderCharacter('_');
            maskData.install(txtDataEmprestimo);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Define data atual como padrão
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        txtDataEmprestimo.setText(LocalDate.now().format(formatter));
    }

    /**
     * Configura os eventos dos botões.
     */
    private void configurarEventos() {
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarEmprestimo();
            }
        });

        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });
    }

    /**
     * Carrega usuários e livros disponíveis nos combos.
     */
    private void carregarDados() {
        try {
            // Carrega usuários
            List<Usuario> usuarios = usuarioController.listarTodos();
            cmbUsuario.removeAllItems();
            for (Usuario usuario : usuarios) {
                String nomeUsuario = usuario.getNome();
                if (usuario.isEmMulta()) {
                    nomeUsuario += String.format(" (MULTADO - %d dias)", usuario.getDiasRestantesMulta());
                }
                cmbUsuario.addItem(new ComboItem(usuario.getId(), nomeUsuario, usuario.isEmMulta()));
            }

            // Carrega apenas livros disponíveis
            List<Livro> livros = livroController.listarDisponiveis();
            cmbLivro.removeAllItems();
            for (Livro livro : livros) {
                String descricao = livro.getTitulo() + " - " + livro.getAutor() + 
                                  " (Disponível: " + livro.getQuantidadeDisponivel() + ")";
                cmbLivro.addItem(new ComboItem(livro.getId(), descricao));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar dados: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Registra o empréstimo.
     */
    private void registrarEmprestimo() {
        try {
            // Valida seleções
            ComboItem usuarioItem = (ComboItem) cmbUsuario.getSelectedItem();
            ComboItem livroItem = (ComboItem) cmbLivro.getSelectedItem();
            
            if (usuarioItem == null) {
                throw new IllegalArgumentException("Selecione um usuário");
            }

            if (livroItem == null) {
                throw new IllegalArgumentException("Selecione um livro");
            }
            
            // Verifica se o usuário está em multa (redundante, mas garante a validação)
            if (usuarioItem.isEmMulta()) {
                throw new IllegalArgumentException("O usuário selecionado está em período de multa e não pode realizar empréstimos.");
            }

            // Obtém IDs selecionados
            Long usuarioId = usuarioItem.getId();
            Long livroId = livroItem.getId();

            // Obtém e valida data
            String dataTexto = txtDataEmprestimo.getText().trim();
            LocalDate dataEmprestimo = parseData(dataTexto);

            // Registra o empréstimo
            emprestimoController.registrarEmprestimo(usuarioId, livroId, dataEmprestimo);

            JOptionPane.showMessageDialog(this,
                "Empréstimo registrado com sucesso!",
                "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);

            registrou = true;
            dispose();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Erro de Validação",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erro ao registrar empréstimo: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Converte texto de data para LocalDate.
     */
    private LocalDate parseData(String dataTexto) {
        if (dataTexto == null || dataTexto.replace("_", "").trim().isEmpty()) {
            return LocalDate.now();
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dataTexto, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data inválida. Use o formato dd/MM/yyyy");
        }
    }

    /**
     * Cancela a operação.
     */
    private void cancelar() {
        dispose();
    }

    /**
     * Verifica se o empréstimo foi registrado.
     */
    public boolean isRegistrou() {
        return registrou;
    }

    /**
     * Classe auxiliar para itens do ComboBox.
     */
    private static class ComboItem {
        private Long id;
        private String descricao;
        private boolean emMulta;

        public ComboItem(Long id, String descricao) {
            this(id, descricao, false);
        }
        
        public ComboItem(Long id, String descricao, boolean emMulta) {
            this.id = id;
            this.descricao = descricao;
            this.emMulta = emMulta;
        }

        public Long getId() {
            return id;
        }
        
        public boolean isEmMulta() {
            return emMulta;
        }

        @Override
        public String toString() {
            return descricao;
        }
    }
}
