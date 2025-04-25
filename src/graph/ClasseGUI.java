package graph;

import model.Classe;
import model.Etudiant;
import service.ClasseService;
import service.EtudiantService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ClasseGUI extends JFrame implements ActionListener {

    private final ClasseService classeService;
    private final EtudiantService etudiantService;
    private final DefaultTableModel tableModel;
    private final JTable classeTable;
    private final JButton ajouterButton;
    private final JButton modifierButton;
    private final JButton supprimerButton;
    private final JButton obtenirButton;
    private final JButton ajouterEtudiantButton;
    private final JButton retirerEtudiantButton;
    private final JButton retourButton; // Nouveau bouton pour le menu principal
    private static final Font MAIN_FONT = new Font("Arial", Font.PLAIN, 14); // Utilisation de la même police que CoursGUI

    public ClasseGUI(ClasseService classeService, EtudiantService etudiantService) {
        this.classeService = classeService;
        this.etudiantService = etudiantService;
        setTitle("Gestion des Classes/Options");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Création du modèle de la table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom"}, 0);
        classeTable = new JTable(tableModel);
        classeTable.setFont(MAIN_FONT); // Appliquer la police de CoursGUI à la table
        JScrollPane scrollPane = new JScrollPane(classeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panneau pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        ajouterButton = new JButton("Ajouter");
        modifierButton = new JButton("Modifier");
        supprimerButton = new JButton("Supprimer");
        obtenirButton = new JButton("Obtenir par ID");
        ajouterEtudiantButton = new JButton("Ajouter Étudiant");
        retirerEtudiantButton = new JButton("Retirer Étudiant");
        retourButton = new JButton("Retour au Menu Principal"); // Création du bouton retour

        ajouterButton.addActionListener(this);
        modifierButton.addActionListener(this);
        supprimerButton.addActionListener(this);
        obtenirButton.addActionListener(this);
        ajouterEtudiantButton.addActionListener(this);
        retirerEtudiantButton.addActionListener(this);
        retourButton.addActionListener(this); // Ajout de l'écouteur pour le bouton retour

        // Appliquer la police de CoursGUI aux boutons
        ajouterButton.setFont(MAIN_FONT);
        modifierButton.setFont(MAIN_FONT);
        supprimerButton.setFont(MAIN_FONT);
        obtenirButton.setFont(MAIN_FONT);
        ajouterEtudiantButton.setFont(MAIN_FONT);
        retirerEtudiantButton.setFont(MAIN_FONT);
        retourButton.setFont(MAIN_FONT);

        buttonPanel.add(ajouterButton);
        buttonPanel.add(modifierButton);
        buttonPanel.add(supprimerButton);
        buttonPanel.add(obtenirButton);
        buttonPanel.add(ajouterEtudiantButton);
        buttonPanel.add(retirerEtudiantButton);
        buttonPanel.add(retourButton); // Ajout du bouton retour au panneau

        add(buttonPanel, BorderLayout.SOUTH);

        // Initialisation de la table
        listerClasses();
    }

    public void gererClasses() {
        setVisible(true);
    }

    private void ajouterClasse() {
        String nom = JOptionPane.showInputDialog(this, "Nom de la classe/option:");
        if (nom != null && !nom.trim().isEmpty()) {
            classeService.ajouterClasse(nom);
            listerClasses();
            JOptionPane.showMessageDialog(this, "Classe ou Option ajoutée avec succès.");
        }
    }

    private void modifierClasse() {
        int selectedRow = classeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une classe à modifier.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String nouveauNom = JOptionPane.showInputDialog(this, "Nouveau nom de la classe/option:");
        if (nouveauNom != null && !nouveauNom.trim().isEmpty()) {
            Classe classe = classeService.obtenirClasse(id);
            if (classe != null) {
                classe.setNom(nouveauNom);
                classeService.modifierClasse(classe);
                listerClasses();
                JOptionPane.showMessageDialog(this, "Classe ou Option modifiée avec succès.");
            } else {
                JOptionPane.showMessageDialog(this, "Classe ou Option non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void supprimerClasse() {
        int selectedRow = classeTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner une classe à supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer cette classe/option ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            try {
                classeService.supprimerClasse(id);
                listerClasses();
                JOptionPane.showMessageDialog(this, "Classe ou Option supprimée avec succès.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la suppression: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void obtenirClasse() {
        String idStr = JOptionPane.showInputDialog(this, "Entrez l'ID de la classe/option:");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Classe classe = classeService.obtenirClasse(id);
                if (classe != null) {
                    JOptionPane.showMessageDialog(this, classe, "Informations de la Classe/Option", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Classe ou Option non trouvée.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID invalide. Veuillez entrer un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ajouterEtudiantAClasseUI() {
        String classeIdStr = JOptionPane.showInputDialog(this, "Entrez l'ID de la classe/option:");
        if (classeIdStr != null && !classeIdStr.trim().isEmpty()) {
            try {
                int classeId = Integer.parseInt(classeIdStr);
                String etudiantIdStr = JOptionPane.showInputDialog(this, "Entrez l'ID de l'étudiant à ajouter:");
                if (etudiantIdStr != null && !etudiantIdStr.trim().isEmpty()) {
                    try {
                        int etudiantId = Integer.parseInt(etudiantIdStr);
                        Classe classe = classeService.obtenirClasse(classeId);
                        Etudiant etudiant = etudiantService.obtenirEtudiant(etudiantId);
                        if (classe == null) {
                            JOptionPane.showMessageDialog(this, "Classe ou Option introuvable !", "Erreur", JOptionPane.ERROR_MESSAGE);
                        } else if (etudiant == null) {
                            JOptionPane.showMessageDialog(this, "Étudiant introuvable !", "Erreur", JOptionPane.ERROR_MESSAGE);
                        } else {
                            classeService.ajouterEtudiantAClasse(classeId, etudiantId);
                            JOptionPane.showMessageDialog(this, "Étudiant ajouté avec succès à la classe ou option.");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "ID de l'étudiant invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID de la classe/option invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void retirerEtudiantDeClasseUI() {
        String classeIdStr = JOptionPane.showInputDialog(this, "Entrez l'ID de la classe/option:");
        if (classeIdStr != null && !classeIdStr.trim().isEmpty()) {
            try {
                int classeId = Integer.parseInt(classeIdStr);
                String etudiantIdStr = JOptionPane.showInputDialog(this, "Entrez l'ID de l'étudiant à retirer:");
                if (etudiantIdStr != null && !etudiantIdStr.trim().isEmpty()) {
                    try {
                        int etudiantId = Integer.parseInt(etudiantIdStr);
                        Classe classe = classeService.obtenirClasse(classeId);
                        Etudiant etudiant = etudiantService.obtenirEtudiant(etudiantId);
                        if (classe == null) {
                            JOptionPane.showMessageDialog(this, "Classe ou Option introuvable !", "Erreur", JOptionPane.ERROR_MESSAGE);
                        } else if (etudiant == null) {
                            JOptionPane.showMessageDialog(this, "Étudiant introuvable !", "Erreur", JOptionPane.ERROR_MESSAGE);
                        } else {
                            classeService.retirerEtudiantDeClasse(classeId, etudiantId);
                            JOptionPane.showMessageDialog(this, "Étudiant retiré de la classe ou option.");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(this, "ID de l'étudiant invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID de la classe/option invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listerClasses() {
        tableModel.setRowCount(0);
        List<Classe> classes = classeService.listerClasses();
        for (Classe classe : classes) {
            tableModel.addRow(new Object[]{classe.getId(), classe.getNom()});
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ajouterButton) {
            ajouterClasse();
        } else if (e.getSource() == modifierButton) {
            modifierClasse();
        } else if (e.getSource() == supprimerButton) {
            supprimerClasse();
        } else if (e.getSource() == obtenirButton) {
            obtenirClasse();
        } else if (e.getSource() == ajouterEtudiantButton) {
            ajouterEtudiantAClasseUI();
        } else if (e.getSource() == retirerEtudiantButton) {
            retirerEtudiantDeClasseUI();
        } else if (e.getSource() == retourButton) {
            this.dispose(); // Ferme la fenêtre actuelle
            // Ici, tu dois ajouter le code pour afficher ou réactiver
            // ta fenêtre de menu principal si elle existe.
            // Par exemple :
            // new MainPrincipalGUI().setVisible(true);
        }
    }
}