package view.Usuario;

import controller.UsuarioController;
import model.Usuario;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;


public class CadastroUsuario extends JDialog {
    private JPanel mainPanel;
    private JTextField txtNome;
    private JComboBox<String> cmbSexo;
    private JFormattedTextField txtCelular;
    private JTextField txtEmail;
    private JButton btnSalvar;
    private JButton btnCancelar;

    private UsuarioController usuarioController;
    private Usuario usuarioEditando;
    private boolean salvou = false;
    public CadastroUsuario(JFrame parent) {
        this(parent, null);
    }


    public CadastroUsuario(JFrame parent, Usuario usuario) {
        super(parent, usuario == null ? "Cadastro de Usuário" : "Editar Usuário", true);
        this.usuarioController = new UsuarioController();
        this.usuarioEditando = usuario;

        setContentPane(mainPanel);
        setSize(500, 400);
        setLocationRelativeTo(parent);
        setResizable(false);

        configurarComponentes();
        configurarEventos();

        if (usuario != null) {
            preencherCampos(usuario);
        }
    }

    private void configurarComponentes() {
        // máscara para celular (XX) XXXXX-XXXX
        try {
            MaskFormatter maskCelular = new MaskFormatter("(##) #####-####");
            maskCelular.setPlaceholderCharacter('_');
            maskCelular.install(txtCelular);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        cmbSexo.addItem("Masculino");
        cmbSexo.addItem("Feminino");
        cmbSexo.addItem("Outro");
    }
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

    private void preencherCampos(Usuario usuario) {
        txtNome.setText(usuario.getNome());
        cmbSexo.setSelectedItem(usuario.getSexo());
        txtCelular.setText(usuario.getCelular());
        txtEmail.setText(usuario.getEmail());
    }

    private void salvar() {
        try {
            // Obtém os dados dos campos
            String nome = txtNome.getText().trim();
            String sexo = (String) cmbSexo.getSelectedItem();
            String celular = txtCelular.getText().trim();
            String email = txtEmail.getText().trim();

            // Salva ou atualiza
            if (usuarioEditando == null) {
                // Cadastro novo
                usuarioController.cadastrarUsuario(nome, sexo, celular, email);
                JOptionPane.showMessageDialog(this,
                    "Usuário cadastrado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Edição
                usuarioController.atualizarUsuario(
                    usuarioEditando.getId(),
                    nome, sexo, celular, email
                );
                JOptionPane.showMessageDialog(this,
                    "Usuário atualizado com sucesso!",
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
                "Erro ao salvar usuário: " + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
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
     * Verifica se o usuário foi salvo.
     */
    public boolean isSalvou() {
        return salvou;
    }
}
