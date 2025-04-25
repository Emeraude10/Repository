package graph;

import model.Cours;
import model.Enseignant;
import model.Classe;
import model.Matiere;
import service.EmploiDuTempsService;
import service.EnseignantService;
import service.MatiereService;
import service.ClasseService;
import service.EmploiDuTempsService.ConflitHoraireException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EmploiDuTempsGUI extends JFrame implements ActionListener {

    private final EmploiDuTempsService emploiDuTempsService;
    private final EnseignantService enseignantService;
    private final MatiereService matiereService;
    private final ClasseService classeService;
    private final DefaultTableModel tableModel;
    private final JTable emploiDuTempsTable;
    private final JButton planifierButton;
    private final JButton supprimerButton;
    private final JButton consulterEnseignantButton;
    private final JButton consulterClasseButton;
    private final JButton genererCSVButton;
    private final JButton retourButton; // Nouveau bouton pour le menu principal

    private static final DateTimeFormatter HEURE_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final Font MAIN_FONT = new Font("Arial", Font.PLAIN, 14); // Utilisation de la même police que CoursGUI

    public EmploiDuTempsGUI(EmploiDuTempsService emploiDuTempsService, EnseignantService enseignantService, MatiereService matiereService, ClasseService classeService) {
        this.emploiDuTempsService = emploiDuTempsService;
        this.enseignantService = enseignantService;
        this.matiereService = matiereService;
        this.classeService = classeService;
        setTitle("Gestion de l'Emploi du Temps");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Matière", "Enseignant", "Classe", "Jour", "Début", "Fin", "Salle", "Niveau"}, 0);
        emploiDuTempsTable = new JTable(tableModel);
        emploiDuTempsTable.setFont(MAIN_FONT); // Appliquer la police de CoursGUI à la table
        JScrollPane scrollPane = new JScrollPane(emploiDuTempsTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        planifierButton = new JButton("Planifier Cours");
        supprimerButton = new JButton("Supprimer Cours");
        consulterEnseignantButton = new JButton("Emploi Enseignant");
        consulterClasseButton = new JButton("Emploi Classe");
        genererCSVButton = new JButton("Générer CSV");
        retourButton = new JButton("Retour au Menu Principal"); // Création du bouton retour

        planifierButton.addActionListener(this);
        supprimerButton.addActionListener(this);
        consulterEnseignantButton.addActionListener(this);
        consulterClasseButton.addActionListener(this);
        genererCSVButton.addActionListener(this);
        retourButton.addActionListener(this); // Ajout de l'écouteur pour le bouton retour

        // Appliquer la police de CoursGUI aux boutons
        planifierButton.setFont(MAIN_FONT);
        supprimerButton.setFont(MAIN_FONT);
        consulterEnseignantButton.setFont(MAIN_FONT);
        consulterClasseButton.setFont(MAIN_FONT);
        genererCSVButton.setFont(MAIN_FONT);
        retourButton.setFont(MAIN_FONT);

        buttonPanel.add(planifierButton);
        buttonPanel.add(supprimerButton);
        buttonPanel.add(consulterEnseignantButton);
        buttonPanel.add(consulterClasseButton);
        buttonPanel.add(genererCSVButton);
        buttonPanel.add(retourButton); // Ajout du bouton retour au panneau

        add(buttonPanel, BorderLayout.SOUTH);

        mettreAJourTable();
    }

    public void gererEmploiDuTemps() {
        setVisible(true);
    }

    private void planifierCoursUI() {
        JDialog dialog = new JDialog(this, "Planifier un Cours", true);
        dialog.setLayout(new GridLayout(10, 2, 5, 5));
        dialog.setPreferredSize(new Dimension(400, 300));

        JComboBox<Matiere> matiereComboBox = new JComboBox<>(matiereService.listerMatieres().toArray(new Matiere[0]));
        JComboBox<Enseignant> enseignantComboBox = new JComboBox<>(enseignantService.listerEnseignants().toArray(new Enseignant[0]));
        JComboBox<Classe> classeComboBox = new JComboBox<>(classeService.listerClasses().toArray(new Classe[0]));
        JComboBox<DayOfWeek> jourComboBox = new JComboBox<>(DayOfWeek.values());
        JTextField heureDebutField = new JTextField();
        JTextField heureFinField = new JTextField();
        JTextField salleField = new JTextField();
        JComboBox<Cours.Niveau> niveauComboBox = new JComboBox<>(Cours.Niveau.values());
        JButton enregistrerButton = new JButton("Planifier");
        JButton annulerButton = new JButton("Annuler");

        JLabel matiereLabel = new JLabel("Matière:");
        JLabel enseignantLabel = new JLabel("Enseignant:");
        JLabel classeLabel = new JLabel("Classe:");
        JLabel jourLabel = new JLabel("Jour:");
        JLabel heureDebutLabel = new JLabel("Heure Début (HH:mm):");
        JLabel heureFinLabel = new JLabel("Heure Fin (HH:mm):");
        JLabel salleLabel = new JLabel("Salle:");
        JLabel niveauLabel = new JLabel("Niveau:");

        matiereLabel.setFont(MAIN_FONT);
        enseignantLabel.setFont(MAIN_FONT);
        classeLabel.setFont(MAIN_FONT);
        jourLabel.setFont(MAIN_FONT);
        heureDebutLabel.setFont(MAIN_FONT);
        heureFinLabel.setFont(MAIN_FONT);
        salleLabel.setFont(MAIN_FONT);
        niveauLabel.setFont(MAIN_FONT);
        matiereComboBox.setFont(MAIN_FONT);
        enseignantComboBox.setFont(MAIN_FONT);
        classeComboBox.setFont(MAIN_FONT);
        jourComboBox.setFont(MAIN_FONT);
        heureDebutField.setFont(MAIN_FONT);
        heureFinField.setFont(MAIN_FONT);
        salleField.setFont(MAIN_FONT);
        niveauComboBox.setFont(MAIN_FONT);
        enregistrerButton.setFont(MAIN_FONT);
        annulerButton.setFont(MAIN_FONT);

        dialog.add(matiereLabel);
        dialog.add(matiereComboBox);
        dialog.add(enseignantLabel);
        dialog.add(enseignantComboBox);
        dialog.add(classeLabel);
        dialog.add(classeComboBox);
        dialog.add(jourLabel);
        dialog.add(jourComboBox);
        dialog.add(heureDebutLabel);
        dialog.add(heureDebutField);
        dialog.add(heureFinLabel);
        dialog.add(heureFinField);
        dialog.add(salleLabel);
        dialog.add(salleField);
        dialog.add(niveauLabel);
        dialog.add(niveauComboBox);
        dialog.add(enregistrerButton);
        dialog.add(annulerButton);

        enregistrerButton.addActionListener(e -> {
            Matiere matiere = (Matiere) matiereComboBox.getSelectedItem();
            Enseignant enseignant = (Enseignant) enseignantComboBox.getSelectedItem();
            Classe classe = (Classe) classeComboBox.getSelectedItem();
            DayOfWeek jour = (DayOfWeek) jourComboBox.getSelectedItem();
            LocalTime heureDebut = null;
            LocalTime heureFin = null;
            try {
                heureDebut = LocalTime.parse(heureDebutField.getText(), HEURE_FORMATTER);
                heureFin = LocalTime.parse(heureFinField.getText(), HEURE_FORMATTER);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Format d'heure invalide (HH:mm).", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String salle = salleField.getText();
            Cours.Niveau niveau = (Cours.Niveau) niveauComboBox.getSelectedItem();
            int id = emploiDuTempsService.getEmploiDuTemps().getCours().size() + 1;
            Cours cours = new Cours(id, matiere, enseignant, classe, heureDebut, heureFin, jour, salle, niveau);

            try {
                emploiDuTempsService.planifierNouveauCours(cours);
                mettreAJourTable();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Cours planifié avec succès.");
            } catch (ConflitHoraireException ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Conflit Horaire", JOptionPane.ERROR_MESSAGE);
            }
        });

        annulerButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void supprimerCoursUI() {
        int selectedRow = emploiDuTempsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un cours à supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Cours coursASupprimer = null;
        for (Cours cours : emploiDuTempsService.getEmploiDuTemps().getCours()) {
            if (cours.getId() == id) {
                coursASupprimer = cours;
                break;
            }
        }

        if (coursASupprimer != null) {
            int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce cours ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                emploiDuTempsService.supprimerCours(coursASupprimer);
                mettreAJourTable();
                JOptionPane.showMessageDialog(this, "Cours supprimé.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Cours non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void consulterEmploiDuTempsEnseignantUI() {
        JComboBox<Enseignant> enseignantComboBox = new JComboBox<>(enseignantService.listerEnseignants().toArray(new Enseignant[0]));
        enseignantComboBox.setFont(MAIN_FONT);
        int result = JOptionPane.showConfirmDialog(this, enseignantComboBox, "Choisir un enseignant", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Enseignant enseignantChoisi = (Enseignant) enseignantComboBox.getSelectedItem();
            List<Cours> cours = emploiDuTempsService.consulterEmploiDuTempsParEnseignant(enseignantChoisi);
            afficherEmploiDuTemps(cours, "Emploi du Temps de " + enseignantChoisi.getNom());
        }
    }

    private void consulterEmploiDuTempsClasseUI() {
        JComboBox<Classe> classeComboBox = new JComboBox<>(classeService.listerClasses().toArray(new Classe[0]));
        classeComboBox.setFont(MAIN_FONT);
        int result = JOptionPane.showConfirmDialog(this, classeComboBox, "Choisir une option", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Classe classeChoisie = (Classe) classeComboBox.getSelectedItem();
            List<Cours> cours = emploiDuTempsService.consulterEmploiDuTempsParClasse(classeChoisie);
            afficherEmploiDuTemps(cours, "Emploi du Temps de " + classeChoisie.getNom());
        }
    }

    private void genererEmploiDuTempsCSVUI() {
        String nomFichier = JOptionPane.showInputDialog(this, "Entrez le nom du fichier CSV (sans extension):");
        if (nomFichier != null && !nomFichier.trim().isEmpty()) {
            emploiDuTempsService.genererEmploiDuTempsCSV(emploiDuTempsService.getEmploiDuTemps(), nomFichier);
            JOptionPane.showMessageDialog(this, "Emploi du temps généré dans " + nomFichier + ".csv");
        }
    }

    private void mettreAJourTable() {
        tableModel.setRowCount(0);
        List<Cours> coursList = emploiDuTempsService.getEmploiDuTemps().getCours();
        for (Cours cours : coursList) {
            tableModel.addRow(new Object[]{
                    cours.getId(),
                    cours.getMatiere().getNom(),
                    cours.getEnseignant().getNom(),
                    cours.getClasse().getNom(),
                    cours.getJourDeLaSemaine(),
                    cours.getHeureDebut().format(HEURE_FORMATTER),
                    cours.getHeureFin().format(HEURE_FORMATTER),
                    cours.getSalle(),
                    cours.getNiveau()
            });
        }
    }

    private void afficherEmploiDuTemps(List<Cours> cours, String titre) {
        if (cours.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aucun cours à afficher pour " + titre, "Emploi du Temps", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        DefaultTableModel emploiDuTempsTableModel = new DefaultTableModel(new Object[]{"ID", "Matière", "Enseignant", "Option", "Jour", "Début", "Fin", "Salle", "Niveau"}, 0);
        JTable emploiDuTempsTableAffichage = new JTable(emploiDuTempsTableModel);
        emploiDuTempsTableAffichage.setFont(MAIN_FONT); // Appliquer la police
        for (Cours c : cours) {
            emploiDuTempsTableModel.addRow(new Object[]{
                    c.getId(),
                    c.getMatiere().getNom(),
                    c.getEnseignant().getNom(),
                    c.getClasse().getNom(),
                    c.getJourDeLaSemaine(),
                    c.getHeureDebut().format(HEURE_FORMATTER),
                    c.getHeureFin().format(HEURE_FORMATTER),
                    c.getSalle(),
                    c.getNiveau()
            });
        }
        JScrollPane scrollPane = new JScrollPane(emploiDuTempsTableAffichage);
        JDialog dialog = new JDialog(this, titre, true);
        dialog.add(scrollPane);
        dialog.setSize(800, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == planifierButton) {
            planifierCoursUI();
        } else if (e.getSource() == supprimerButton) {
            supprimerCoursUI();
        } else if (e.getSource() == consulterEnseignantButton) {
            consulterEmploiDuTempsEnseignantUI();
        } else if (e.getSource() == consulterClasseButton) {
            consulterEmploiDuTempsClasseUI();
        } else if (e.getSource() == genererCSVButton) {
            genererEmploiDuTempsCSVUI();
        } else if (e.getSource() == retourButton) {
            this.dispose(); // Ferme la fenêtre actuelle
            // Ici, ajoute le code pour afficher ta fenêtre de menu principal.
            // Exemple (si ta classe de menu principal est MenuPrincipalGUI) :
            // new MenuPrincipalGUI().setVisible(true);
        }
    }
}