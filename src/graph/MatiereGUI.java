package graph;

import model.Matiere;
import service.MatiereService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MatiereGUI extends JFrame implements ActionListener {

    private final MatiereService matiereService;
    private final DefaultTableModel tableModel;
    private final JTable matiereTable;
    private final JButton ajouterButton;
    private final JButton modifierButton;
    private final JButton supprimerButton;
    private final JButton obtenirButton;
    private final JButton retourButton; // Nouveau bouton pour le menu principal
    private static final Font MAIN_FONT = new Font("Arial", Font.PLAIN, 14); // Utilisation de la même police que CoursGUI

    public MatiereGUI(MatiereService matiereService) {
        this.matiereService = matiereService;
        setTitle("Gestion des Matières");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom", "Description"}, 0);
        matiereTable = new JTable(tableModel);
        matiereTable.setFont(MAIN_FONT); // Appliquer la police de CoursGUI à la table
        JScrollPane scrollPane = new JScrollPane(matiereTable);
        add(scrollPane, BorderLayout.CENTER);

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

        listerMatieres();
    }

    public void gererMatieres() {
        setVisible(true);
    }

    private void ajouterMatiere() {
        JDialog dialog = new JDialog(this, "Ajouter une Matière", true);
        dialog.setLayout(new GridLayout(4, 2, 5, 5));
        dialog.setPreferredSize(new Dimension(300, 150));

        JTextField idField = new JTextField();
        JTextField nomField = new JTextField();
        JTextField descriptionField = new JTextField();
        JButton enregistrerButton = new JButton("Enregistrer");
        JButton annulerButton = new JButton("Annuler");

        JLabel idLabel = new JLabel("ID:");
        JLabel nomLabel = new JLabel("Nom:");
        JLabel descriptionLabel = new JLabel("Description:");

        idLabel.setFont(MAIN_FONT);
        nomLabel.setFont(MAIN_FONT);
        descriptionLabel.setFont(MAIN_FONT);
        idField.setFont(MAIN_FONT);
        nomField.setFont(MAIN_FONT);
        descriptionField.setFont(MAIN_FONT);
        enregistrerButton.setFont(MAIN_FONT);
        annulerButton.setFont(MAIN_FONT);

        dialog.add(idLabel);
        dialog.add(idField);
        dialog.add(nomLabel);
        dialog.add(nomField);
        dialog.add(descriptionLabel);
        dialog.add(descriptionField);
        dialog.add(enregistrerButton);
        dialog.add(annulerButton);

        enregistrerButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String nom = nomField.getText();
                String description = descriptionField.getText();

                if (nom.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Le nom de la matière ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Matiere matiere = new Matiere(id, nom, description);
                matiereService.ajouterMatiere(matiere);
                listerMatieres();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Matière ajoutée avec succès.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "L'ID doit être un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        annulerButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void modifierMatiere() {
        int selectedRow = matiereTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une matière à modifier.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Matiere matiereAModifier = matiereService.obtenirMatiere(id);

        if (matiereAModifier != null) {
            JDialog dialog = new JDialog(this, "Modifier une Matière", true);
            dialog.setLayout(new GridLayout(4, 2, 5, 5));
            dialog.setPreferredSize(new Dimension(300, 150));

            JTextField nomField = new JTextField(matiereAModifier.getNom());
            JTextField descriptionField = new JTextField(matiereAModifier.getDescription());
            JButton enregistrerButton = new JButton("Enregistrer");
            JButton annulerButton = new JButton("Annuler");

            JLabel nomLabel = new JLabel("Nom:");
            JLabel descriptionLabel = new JLabel("Description:");

            nomLabel.setFont(MAIN_FONT);
            descriptionLabel.setFont(MAIN_FONT);
            nomField.setFont(MAIN_FONT);
            descriptionField.setFont(MAIN_FONT);
            enregistrerButton.setFont(MAIN_FONT);
            annulerButton.setFont(MAIN_FONT);

            dialog.add(nomLabel);
            dialog.add(nomField);
            dialog.add(descriptionLabel);
            dialog.add(descriptionField);
            dialog.add(enregistrerButton);
            dialog.add(annulerButton);

            enregistrerButton.addActionListener(e -> {
                String nom = nomField.getText();
                String description = descriptionField.getText();

                if (nom.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Le nom de la matière ne peut pas être vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                matiereAModifier.setNom(nom);
                matiereAModifier.setDescription(description);
                matiereService.modifierMatiere(matiereAModifier);
                listerMatieres();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Matière modifiée avec succès.");
            });

            annulerButton.addActionListener(e -> dialog.dispose());

            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Matière non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerMatiere() {
        int selectedRow = matiereTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une matière à supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette matière ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            matiereService.supprimerMatiere(id);
            listerMatieres();
            JOptionPane.showMessageDialog(this, "Matière supprimée avec succès.");
        }
    }

    private void obtenirMatiere() {
        String idStr = JOptionPane.showInputDialog(this, "Entrez l'ID de la matière:");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Matiere matiere = matiereService.obtenirMatiere(id);
                if (matiere != null) {
                    JOptionPane.showMessageDialog(this, matiere, "Informations de la Matière", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Matière non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID invalide. Veuillez entrer un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listerMatieres() {
        tableModel.setRowCount(0);
        List<Matiere> matieres = matiereService.listerMatieres();
        for (Matiere matiere : matieres) {
            tableModel.addRow(new Object[]{matiere.getId(), matiere.getNom(), matiere.getDescription()});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ajouterButton) {
            ajouterMatiere();
        } else if (e.getSource() == modifierButton) {
            modifierMatiere();
        } else if (e.getSource() == supprimerButton) {
            supprimerMatiere();
        } else if (e.getSource() == obtenirButton) {
            obtenirMatiere();
        } else if (e.getSource() == retourButton) {
            this.dispose(); // Ferme la fenêtre actuelle
            // Ici, ajoute le code pour afficher ta fenêtre de menu principal.
            // Exemple (si ta classe de menu principal est MenuPrincipalGUI) :
            // new MenuPrincipalGUI().setVisible(true);
        }
    }
}