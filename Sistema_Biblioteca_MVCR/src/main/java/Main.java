import view.Principal;

import javax.swing.*;


public class Main {

    public static void main(String[] args) {
        configurarLookAndFeel();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Cria e exibe a tela principal
                    Principal principal = new Principal();
                    principal.setVisible(true);
                } catch (Exception e) {
                    System.err.println("Erro ao iniciar a aplicação: " + e.getMessage());
                    e.printStackTrace();
                    
                    JOptionPane.showMessageDialog(null,
                        "Erro ao iniciar a aplicação:\n" + e.getMessage() +
                        "\n\nVerifique se o banco de dados está configurado corretamente.",
                        "Erro de Inicialização",
                        JOptionPane.ERROR_MESSAGE);
                    
                    System.exit(1);
                }
            }
        });
    }

    private static void configurarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | 
                 IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("Não foi possível definir o Look and Feel do sistema: " + e.getMessage());
        }
    }
}
