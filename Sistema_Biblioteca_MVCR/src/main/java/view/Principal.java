package view;

import view.Livro.MenuLivro;
import view.Usuario.MenuUsuario;
import view.Emprestimo.MenuEmprestimo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tela principal do Sistema de Gestão de Biblioteca.
 * Fornece acesso aos módulos de gerenciamento de Livros, Usuários e Empréstimos.
 */
public class Principal extends JFrame {
    private JPanel mainPanel;
    private JButton btnLivros;
    private JButton btnUsuarios;
    private JButton btnEmprestimos;
    private JButton btnSair;

    public Principal() {
        setTitle("Sistema de Gestão de Biblioteca");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        configurarEventos();
    }

    /**
     * Configura os eventos dos botões da tela principal.
     */
    private void configurarEventos() {
        
        // Botão Gerenciar Livros
        btnLivros.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirMenuLivros();
            }
        });

        // Botão Gerenciar Usuários
        btnUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirMenuUsuarios();
            }
        });

        // Botão Gerenciar Empréstimos
        btnEmprestimos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirMenuEmprestimos();
            }
        });

        // Botão Sair
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sair();
            }
        });
    }

    /**
     * Abre o menu de gerenciamento de livros.
     */
    private void abrirMenuLivros() {
        MenuLivro menuLivro = new MenuLivro();
        menuLivro.setVisible(true);
    }

    /**
     * Abre o menu de gerenciamento de usuários.
     */
    private void abrirMenuUsuarios() {
        MenuUsuario menuUsuario = new MenuUsuario();
        menuUsuario.setVisible(true);
    }

    /**
     * Abre o menu de gerenciamento de empréstimos.
     */
    private void abrirMenuEmprestimos() {
        MenuEmprestimo menuEmprestimo = new MenuEmprestimo();
        menuEmprestimo.setVisible(true);
    }

    /**
     * Encerra a aplicação.
     */
    private void sair() {
        int opcao = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente sair do sistema?",
            "Confirmar Saída",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (opcao == JOptionPane.YES_OPTION) {
            // Fecha o EntityManagerFactory antes de sair
            repository.JPAUtil.closeEntityManagerFactory();
            System.exit(0);
        }
    }

    /**
     * Método main para executar a aplicação.
     */
    public static void main(String[] args) {
        try {
            // Define o Look and Feel do sistema operacional
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Principal principal = new Principal();
                principal.setVisible(true);
            }
        });
    }
}
