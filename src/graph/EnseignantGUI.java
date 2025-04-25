package graph;

// Importations nécessaires
import model.Enseignant;
import service.EnseignantService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EnseignantGUI extends JFrame implements ActionListener {

    private final EnseignantService enseignantService;
    private final DefaultTableModel tableModel;
    private final JTable enseignantTable;
    private final JButton ajouterButton;
    private final JButton modifierButton;
    private final JButton supprimerButton;
    private final JButton obtenirButton;
    private final JButton retourButton; // Nouveau bouton pour le menu principal
    private static final Font MAIN_FONT = new Font("Arial", Font.PLAIN, 14); // Utilisation de la même police que CoursGUI

    public EnseignantGUI(EnseignantService enseignantService) {
        this.enseignantService = enseignantService;
        setTitle("Gestion des Enseignants");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Création du modèle de la table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom", "Prénom", "Spécialité"}, 0);
        enseignantTable = new JTable(tableModel);
        enseignantTable.setFont(MAIN_FONT); // Appliquer la police de CoursGUI à la table
        JScrollPane scrollPane = new JScrollPane(enseignantTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panneau pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ajouterButton = new JButton("Ajouter");
        modifierButton = new JButton("Modifier");
        supprimerButton = new JButton("Supprimer");
        obtenirButton = new JButton("Obtenir par ID");
        retourButton = new JButton("Retour au Menu Principal"); // Création du bouton retour

        ajouterButton.addActionListener(this);
        modifierButton.addActionListener(this);
        supprimerButton.addActionListener(this);
        obtenirButton.addActionListener(this);
        retourButton.addActionListener(this); // Ajout de l'écouteur pour le bouton retour

        // Appliquer la police de CoursGUI aux boutons
        ajouterButton.setFont(MAIN_FONT);
        modifierButton.setFont(MAIN_FONT);
        supprimerButton.setFont(MAIN_FONT);
        obtenirButton.setFont(MAIN_FONT);
        retourButton.setFont(MAIN_FONT);

        buttonPanel.add(ajouterButton);
        buttonPanel.add(modifierButton);
        buttonPanel.add(supprimerButton);
        buttonPanel.add(obtenirButton);
        buttonPanel.add(retourButton); // Ajout du bouton retour au panneau

        add(buttonPanel, BorderLayout.SOUTH);

        // Initialisation de la table
        listerEnseignants();
    }

    public void gererEnseignants() {
        setVisible(true);
    }

    private void ajouterEnseignant() {
        JDialog dialog = new JDialog(this, "Ajouter un Enseignant", true);
        dialog.setLayout(new GridLayout(5, 2, 5, 5));
        dialog.setPreferredSize(new Dimension(300, 200));

        JTextField idField = new JTextField();
        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField specialiteField = new JTextField();
        JButton enregistrerButton = new JButton("Enregistrer");
        JButton annulerButton = new JButton("Annuler");

        JLabel idLabel = new JLabel("ID:");
        JLabel nomLabel = new JLabel("Nom:");
        JLabel prenomLabel = new JLabel("Prénom:");
        JLabel specialiteLabel = new JLabel("Spécialité:");

        idLabel.setFont(MAIN_FONT);
        nomLabel.setFont(MAIN_FONT);
        prenomLabel.setFont(MAIN_FONT);
        specialiteLabel.setFont(MAIN_FONT);
        idField.setFont(MAIN_FONT);
        nomField.setFont(MAIN_FONT);
        prenomField.setFont(MAIN_FONT);
        specialiteField.setFont(MAIN_FONT);
        enregistrerButton.setFont(MAIN_FONT);
        annulerButton.setFont(MAIN_FONT);

        dialog.add(idLabel);
        dialog.add(idField);
        dialog.add(nomLabel);
        dialog.add(nomField);
        dialog.add(prenomLabel);
        dialog.add(prenomField);
        dialog.add(specialiteLabel);
        dialog.add(specialiteField);
        dialog.add(enregistrerButton);
        dialog.add(annulerButton);

        enregistrerButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String specialite = specialiteField.getText();
                Enseignant enseignant = new Enseignant(id, nom, prenom, specialite);
                enseignantService.ajouterEnseignant(enseignant);
                listerEnseignants(); // Rafraîchir la table
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Enseignant ajouté avec succès.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "L'ID doit être un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        annulerButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void modifierEnseignant() {
        int selectedRow = enseignantTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enseignant à modifier.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Enseignant enseignant = enseignantService.obtenirEnseignant(id);

        if (enseignant != null) {
            JDialog dialog = new JDialog(this, "Modifier un Enseignant", true);
            dialog.setLayout(new GridLayout(4, 2, 5, 5));
            dialog.setPreferredSize(new Dimension(300, 150));

            JTextField nomField = new JTextField(enseignant.getNom());
            JTextField prenomField = new JTextField(enseignant.getPrenom());
            JTextField specialiteField = new JTextField(enseignant.getSpecialite());
            JButton enregistrerButton = new JButton("Enregistrer");
            JButton annulerButton = new JButton("Annuler");

            JLabel nomLabel = new JLabel("Nom:");
            JLabel prenomLabel = new JLabel("Prénom:");
            JLabel specialiteLabel = new JLabel("Spécialité:");

            nomLabel.setFont(MAIN_FONT);
            prenomLabel.setFont(MAIN_FONT);
            specialiteLabel.setFont(MAIN_FONT);
            nomField.setFont(MAIN_FONT);
            prenomField.setFont(MAIN_FONT);
            specialiteField.setFont(MAIN_FONT);
            enregistrerButton.setFont(MAIN_FONT);
            annulerButton.setFont(MAIN_FONT);

            dialog.add(nomLabel);
            dialog.add(nomField);
            dialog.add(prenomLabel);
            dialog.add(prenomField);
            dialog.add(specialiteLabel);
            dialog.add(specialiteField);
            dialog.add(enregistrerButton);
            dialog.add(annulerButton);

            enregistrerButton.addActionListener(e -> {
                enseignant.setNom(nomField.getText());
                enseignant.setPrenom(prenomField.getText());
                enseignant.setSpecialite(specialiteField.getText());
                enseignantService.modifierEnseignant(enseignant);
                listerEnseignants(); // Rafraîchir la table
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Enseignant modifié avec succès.");
            });

            annulerButton.addActionListener(e -> dialog.dispose());

            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Enseignant non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerEnseignant() {
        int selectedRow = enseignantTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un enseignant à supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet enseignant ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            enseignantService.supprimerEnseignant(id);
            listerEnseignants(); // Rafraîchir la table
            JOptionPane.showMessageDialog(this, "Enseignant supprimé avec succès.");
        }
    }

    private void obtenirEnseignant() {
        String idStr = JOptionPane.showInputDialog(this, "Entrez l'ID de l'enseignant:", "Obtenir un Enseignant", JOptionPane.QUESTION_MESSAGE);
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Enseignant enseignant = enseignantService.obtenirEnseignant(id);
                if (enseignant != null) {
                    JOptionPane.showMessageDialog(this, enseignant, "Informations de l'Enseignant", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Enseignant non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "L'ID doit être un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listerEnseignants() {
        tableModel.setRowCount(0); // Effacer le contenu actuel de la table
        List<Enseignant> enseignants = enseignantService.listerEnseignants();
        for (Enseignant enseignant : enseignants) {
            tableModel.addRow(new Object[]{enseignant.getId(), enseignant.getNom(), enseignant.getPrenom(), enseignant.getSpecialite()});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ajouterButton) {
            ajouterEnseignant();
        } else if (e.getSource() == modifierButton) {
            modifierEnseignant();
        } else if (e.getSource() == supprimerButton) {
            supprimerEnseignant();
        } else if (e.getSource() == obtenirButton) {
            obtenirEnseignant();
        } else if (e.getSource() == retourButton) {
            this.dispose(); // Ferme la fenêtre actuelle
            // Ici, ajoute le code pour afficher ta fenêtre de menu principal.
            // Exemple (si ta classe de menu principal est MenuPrincipalGUI) :
            // new MenuPrincipalGUI().setVisible(true);
        }
    }
}