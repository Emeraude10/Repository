package graph;

import model.Etudiant;
import service.EtudiantService;
import service.ClasseService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.InputMismatchException;

public class EtudiantGUI extends JFrame implements ActionListener {

    private final EtudiantService etudiantService;
    private final ClasseService classeService; // Bien que non utilisé directement ici, il est injecté
    private final DefaultTableModel tableModel;
    private final JTable etudiantTable;
    private final JButton ajouterButton;
    private final JButton modifierButton;
    private final JButton supprimerButton;
    private final JButton obtenirButton;
    private final JButton retourButton; // Nouveau bouton pour le menu principal
    private static final Font MAIN_FONT = new Font("Arial", Font.PLAIN, 14); // Utilisation de la même police que CoursGUI

    public EtudiantGUI(EtudiantService etudiantService, ClasseService classeService) {
        this.etudiantService = etudiantService;
        this.classeService = classeService;
        setTitle("Gestion des Étudiants");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom", "Prénom", "Niveau"}, 0);
        etudiantTable = new JTable(tableModel);
        etudiantTable.setFont(MAIN_FONT); // Appliquer la police de CoursGUI à la table
        JScrollPane scrollPane = new JScrollPane(etudiantTable);
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

        listerEtudiants();
    }

    public void gererEtudiants() {
        setVisible(true);
    }

    private void ajouterEtudiant() {
        JDialog dialog = new JDialog(this, "Ajouter un Étudiant", true);
        dialog.setLayout(new GridLayout(5, 2, 5, 5));
        dialog.setPreferredSize(new Dimension(300, 180));

        JTextField idField = new JTextField();
        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField niveauField = new JTextField();
        JButton enregistrerButton = new JButton("Enregistrer");
        JButton annulerButton = new JButton("Annuler");

        JLabel idLabel = new JLabel("ID:");
        JLabel nomLabel = new JLabel("Nom:");
        JLabel prenomLabel = new JLabel("Prénom:");
        JLabel niveauLabel = new JLabel("Niveau:");

        idLabel.setFont(MAIN_FONT);
        nomLabel.setFont(MAIN_FONT);
        prenomLabel.setFont(MAIN_FONT);
        niveauLabel.setFont(MAIN_FONT);
        idField.setFont(MAIN_FONT);
        nomField.setFont(MAIN_FONT);
        prenomField.setFont(MAIN_FONT);
        niveauField.setFont(MAIN_FONT);
        enregistrerButton.setFont(MAIN_FONT);
        annulerButton.setFont(MAIN_FONT);

        dialog.add(idLabel);
        dialog.add(idField);
        dialog.add(nomLabel);
        dialog.add(nomField);
        dialog.add(prenomLabel);
        dialog.add(prenomField);
        dialog.add(niveauLabel);
        dialog.add(niveauField);
        dialog.add(enregistrerButton);
        dialog.add(annulerButton);

        enregistrerButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String niveau = niveauField.getText();

                if (nom.trim().isEmpty() || prenom.trim().isEmpty() || niveau.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Etudiant etudiant = new Etudiant(id, nom, prenom, niveau);
                etudiantService.ajouterEtudiant(etudiant);
                listerEtudiants();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Étudiant ajouté avec succès.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "L'ID doit être un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        annulerButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void modifierEtudiant() {
        int selectedRow = etudiantTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un étudiant à modifier.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Etudiant etudiantAModifier = etudiantService.obtenirEtudiant(id);

        if (etudiantAModifier != null) {
            JDialog dialog = new JDialog(this, "Modifier un Étudiant", true);
            dialog.setLayout(new GridLayout(5, 2, 5, 5));
            dialog.setPreferredSize(new Dimension(300, 180));

            JTextField nomField = new JTextField(etudiantAModifier.getNom());
            JTextField prenomField = new JTextField(etudiantAModifier.getPrenom());
            JTextField niveauField = new JTextField(etudiantAModifier.getNiveau());
            JButton enregistrerButton = new JButton("Enregistrer");
            JButton annulerButton = new JButton("Annuler");

            JLabel nomLabel = new JLabel("Nom:");
            JLabel prenomLabel = new JLabel("Prénom:");
            JLabel niveauLabel = new JLabel("Niveau:");

            nomLabel.setFont(MAIN_FONT);
            prenomLabel.setFont(MAIN_FONT);
            niveauLabel.setFont(MAIN_FONT);
            nomField.setFont(MAIN_FONT);
            prenomField.setFont(MAIN_FONT);
            niveauField.setFont(MAIN_FONT);
            enregistrerButton.setFont(MAIN_FONT);
            annulerButton.setFont(MAIN_FONT);

            dialog.add(nomLabel);
            dialog.add(nomField);
            dialog.add(prenomLabel);
            dialog.add(prenomField);
            dialog.add(niveauLabel);
            dialog.add(niveauField);
            dialog.add(enregistrerButton);
            dialog.add(annulerButton);

            enregistrerButton.addActionListener(e -> {
                String nom = nomField.getText();
                String prenom = prenomField.getText();
                String niveau = niveauField.getText();

                if (nom.trim().isEmpty() || prenom.trim().isEmpty() || niveau.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Tous les champs doivent être remplis.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                etudiantAModifier.setNom(nom);
                etudiantAModifier.setPrenom(prenom);
                etudiantAModifier.setNiveau(niveau);
                etudiantService.modifierEtudiant(etudiantAModifier);
                listerEtudiants();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Étudiant modifié avec succès.");
            });

            annulerButton.addActionListener(e -> dialog.dispose());

            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Étudiant non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerEtudiant() {
        int selectedRow = etudiantTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un étudiant à supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cet étudiant ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            etudiantService.supprimerEtudiant(id);
            listerEtudiants();
            JOptionPane.showMessageDialog(this, "Étudiant supprimé avec succès.");
        }
    }

    private void obtenirEtudiant() {
        String idStr = JOptionPane.showInputDialog(this, "Entrez l'ID de l'étudiant:");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Etudiant etudiant = etudiantService.obtenirEtudiant(id);
                if (etudiant != null) {
                    JOptionPane.showMessageDialog(this, etudiant, "Informations de l'Étudiant", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Étudiant non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID invalide. Veuillez entrer un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listerEtudiants() {
        tableModel.setRowCount(0);
        List<Etudiant> etudiants = etudiantService.listerEtudiants();
        for (Etudiant etudiant : etudiants) {
            tableModel.addRow(new Object[]{etudiant.getId(), etudiant.getNom(), etudiant.getPrenom(), etudiant.getNiveau()});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ajouterButton) {
            ajouterEtudiant();
        } else if (e.getSource() == modifierButton) {
            modifierEtudiant();
        } else if (e.getSource() == supprimerButton) {
            supprimerEtudiant();
        } else if (e.getSource() == obtenirButton) {
            obtenirEtudiant();
        } else if (e.getSource() == retourButton) {
            this.dispose(); // Ferme la fenêtre actuelle
            // Ici, ajoute le code pour afficher ta fenêtre de menu principal.
            // Exemple (si ta classe de menu principal est MenuPrincipalGUI) :
            // new MenuPrincipalGUI().setVisible(true);
        }
    }
}