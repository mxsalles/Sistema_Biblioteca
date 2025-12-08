package view.Livro;

import controller.LivroController;
import model.Livro;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CadastroLivro extends JDialog {
    private JPanel mainPanel;
    private JTextField txtTitulo;
    private JTextField txtTema;
    private JTextField txtAutor;
    private JTextField txtIsbn;
    private JFormattedTextField txtDataPublicacao;
    private JSpinner spnQuantidade;
    private JButton btnSalvar;
    private JButton btnCancelar;

    private LivroController livroController;
    private Livro livroEditando;
    private boolean salvou = false;

    /**
     * Construtor para cadastro de novo livro.
     */
    public CadastroLivro(JFrame parent) {
        this(parent, null);
    }

    /**
     * Construtor para edição de livro existente.
     */
    public CadastroLivro(JFrame parent, Livro livro) {
        super(parent, livro == null ? "Cadastro de Livro" : "Editar Livro", true);
        this.livroController = new LivroController();
        this.livroEditando = livro;

        setContentPane(mainPanel);
        setSize(500, 450);
        setLocationRelativeTo(parent);
        setResizable(false);

        configurarComponentes();
        configurarEventos();

        if (livro != null) {
            preencherCampos(livro);
        }
    }

    /**
     * Configura os componentes da tela (máscaras, spinners, etc).
     */
    private void configurarComponentes() {
        // Configura máscara para data (dd/MM/yyyy)
        try {
            MaskFormatter maskData = new MaskFormatter("##/##/####");
            maskData.setPlaceholderCharacter('_');
            maskData.install(txtDataPublicacao);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Configura spinner para quantidade
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 0, 1000, 1);
        spnQuantidade.setModel(spinnerModel);
    }

    /**
     * Configura os eventos dos botões.
     */
    private void configurarEventos() {
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvar();
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
     * Preenche os campos com os dados do livro para edição.
     */
    private void preencherCampos(Livro livro) {
        txtTitulo.setText(livro.getTitulo());
        txtTema.setText(livro.getTema());
        txtAutor.setText(livro.getAutor());
        txtIsbn.setText(livro.getIsbn());
        
        if (livro.getDataPublicacao() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            txtDataPublicacao.setText(livro.getDataPublicacao().format(formatter));
        }
        
        spnQuantidade.setValue(livro.getQuantidadeDisponivel());
    }

    /**
     * Salva o livro (cadastro ou edição).
     */
    private void salvar() {
        try {
            // Obtém os dados dos campos
            String titulo = txtTitulo.getText().trim();
            String tema = txtTema.getText().trim();
            String autor = txtAutor.getText().trim();
            String isbn = txtIsbn.getText().trim();
            String dataTexto = txtDataPublicacao.getText().trim();
            Integer quantidade = (Integer) spnQuantidade.getValue();

            // Valida data
            LocalDate dataPublicacao = parseData(dataTexto);

            // Salva ou atualiza
            if (livroEditando == null) {
                // Cadastro novo
                livroController.cadastrarLivro(titulo, tema, autor, isbn, dataPublicacao, quantidade);
                JOptionPane.showMessageDialog(this, 
                    "Livro cadastrado com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Edição
                livroController.atualizarLivro(
                    livroEditando.getId(), 
                    titulo, tema, autor, isbn, dataPublicacao, quantidade
                );
                JOptionPane.showMessageDialog(this, 
                    "Livro atualizado com sucesso!", 
                    "Sucesso", 
                    JOptionPane.INFORMATION_MESSAGE);
            }

            salvou = true;
            dispose();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, 
                ex.getMessage(), 
                "Erro de Validação", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao salvar livro: " + ex.getMessage(), 
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
            throw new IllegalArgumentException("A data de publicação é obrigatória");
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return LocalDate.parse(dataTexto, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Data inválida. Use o formato dd/MM/yyyy");
        }
    }

    /**
     * Cancela a operação e fecha a janela.
     */
    private void cancelar() {
        int opcao = JOptionPane.showConfirmDialog(this,
            "Deseja realmente cancelar?",
            "Confirmar Cancelamento",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (opcao == JOptionPane.YES_OPTION) {
            dispose();
        }
    }

    /**
     * Verifica se o livro foi salvo.
     */
    public boolean isSalvou() {
        return salvou;
    }
}
