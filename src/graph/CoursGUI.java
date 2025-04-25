package graph;

import model.Cours;
import model.Matiere;
import model.Enseignant;
import model.Classe;
import model.Cours.Niveau;
import service.CoursService;
import service.MatiereService;
import service.EnseignantService;
import service.ClasseService;

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

public class CoursGUI extends JFrame implements ActionListener {

    private final CoursService coursService;
    private final MatiereService matiereService;
    private final EnseignantService enseignantService;
    private final ClasseService classeService;
    private final DefaultTableModel tableModel;
    private final JTable coursTable;
    private final JButton ajouterButton;
    private final JButton modifierButton;
    private final JButton supprimerButton;
    private final JButton obtenirButton;
    private final JButton retourButton; // Nouveau bouton pour le menu principal

    private static final DateTimeFormatter HEURE_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final Font MAIN_FONT = new Font("Arial", Font.PLAIN, 14); // Utilisation de la même police que MainFrame

    public CoursGUI(CoursService coursService, MatiereService matiereService, EnseignantService enseignantService, ClasseService classeService) {
        this.coursService = coursService;
        this.matiereService = matiereService;
        this.enseignantService = enseignantService;
        this.classeService = classeService;
        setTitle("Gestion des Cours");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Matière", "Enseignant", "Classe", "Début", "Fin", "Jour", "Salle", "Niveau"}, 0);
        coursTable = new JTable(tableModel);
        coursTable.setFont(MAIN_FONT); // Appliquer la police à la table
        JScrollPane scrollPane = new JScrollPane(coursTable);
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

        listerCours();
    }

    public void gererCours() {
        setVisible(true);
    }

    private void ajouterCours() {
        JDialog dialog = new JDialog(this, "Ajouter un Cours", true);
        dialog.setLayout(new GridLayout(10, 2, 5, 5));
        dialog.setPreferredSize(new Dimension(400, 300));

        JTextField idField = new JTextField();
        JComboBox<Matiere> matiereComboBox = new JComboBox<>(matiereService.listerMatieres().toArray(new Matiere[0]));
        JComboBox<Enseignant> enseignantComboBox = new JComboBox<>(enseignantService.listerEnseignants().toArray(new Enseignant[0]));
        JComboBox<Classe> classeComboBox = new JComboBox<>(classeService.listerClasses().toArray(new Classe[0]));
        JTextField heureDebutField = new JTextField();
        JTextField heureFinField = new JTextField();
        JComboBox<DayOfWeek> jourComboBox = new JComboBox<>(DayOfWeek.values());
        JTextField salleField = new JTextField();
        JComboBox<Niveau> niveauComboBox = new JComboBox<>(Niveau.values());
        JButton enregistrerButton = new JButton("Enregistrer");
        JButton annulerButton = new JButton("Annuler");

        JLabel idLabel = new JLabel("ID:");
        JLabel matiereLabel = new JLabel("Matière:");
        JLabel enseignantLabel = new JLabel("Enseignant:");
        JLabel classeLabel = new JLabel("Classe:");
        JLabel heureDebutLabel = new JLabel("Heure Début (HH:mm):");
        JLabel heureFinLabel = new JLabel("Heure Fin (HH:mm):");
        JLabel jourLabel = new JLabel("Jour:");
        JLabel salleLabel = new JLabel("Salle:");
        JLabel niveauLabel = new JLabel("Niveau:");

        idLabel.setFont(MAIN_FONT);
        matiereLabel.setFont(MAIN_FONT);
        enseignantLabel.setFont(MAIN_FONT);
        classeLabel.setFont(MAIN_FONT);
        heureDebutLabel.setFont(MAIN_FONT);
        heureFinLabel.setFont(MAIN_FONT);
        jourLabel.setFont(MAIN_FONT);
        salleLabel.setFont(MAIN_FONT);
        niveauLabel.setFont(MAIN_FONT);
        idField.setFont(MAIN_FONT);
        matiereComboBox.setFont(MAIN_FONT);
        enseignantComboBox.setFont(MAIN_FONT);
        classeComboBox.setFont(MAIN_FONT);
        heureDebutField.setFont(MAIN_FONT);
        heureFinField.setFont(MAIN_FONT);
        jourComboBox.setFont(MAIN_FONT);
        salleField.setFont(MAIN_FONT);
        niveauComboBox.setFont(MAIN_FONT);
        enregistrerButton.setFont(MAIN_FONT);
        annulerButton.setFont(MAIN_FONT);

        dialog.add(idLabel);
        dialog.add(idField);
        dialog.add(matiereLabel);
        dialog.add(matiereComboBox);
        dialog.add(enseignantLabel);
        dialog.add(enseignantComboBox);
        dialog.add(classeLabel);
        dialog.add(classeComboBox);
        dialog.add(heureDebutLabel);
        dialog.add(heureDebutField);
        dialog.add(heureFinLabel);
        dialog.add(heureFinField);
        dialog.add(jourLabel);
        dialog.add(jourComboBox);
        dialog.add(salleLabel);
        dialog.add(salleField);
        dialog.add(niveauLabel);
        dialog.add(niveauComboBox);
        dialog.add(enregistrerButton);
        dialog.add(annulerButton);

        enregistrerButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                Matiere matiere = (Matiere) matiereComboBox.getSelectedItem();
                Enseignant enseignant = (Enseignant) enseignantComboBox.getSelectedItem();
                Classe classe = (Classe) classeComboBox.getSelectedItem();
                LocalTime heureDebut = LocalTime.parse(heureDebutField.getText(), HEURE_FORMATTER);
                LocalTime heureFin = LocalTime.parse(heureFinField.getText(), HEURE_FORMATTER);
                DayOfWeek jour = (DayOfWeek) jourComboBox.getSelectedItem();
                String salle = salleField.getText();
                Niveau niveau = (Niveau) niveauComboBox.getSelectedItem();

                Cours cours = new Cours(id, matiere, enseignant, classe, heureDebut, heureFin, jour, salle, niveau);
                coursService.ajouterCours(cours);
                listerCours();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Cours ajouté avec succès.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "L'ID doit être un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Format d'heure invalide (HH:mm).", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        annulerButton.addActionListener(e -> dialog.dispose());

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void modifierCours() {
        int selectedRow = coursTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un cours à modifier.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        Cours coursAModifier = coursService.obtenirCours(id);

        if (coursAModifier != null) {
            JDialog dialog = new JDialog(this, "Modifier un Cours", true);
            dialog.setLayout(new GridLayout(9, 2, 5, 5));
            dialog.setPreferredSize(new Dimension(400, 280));

            JComboBox<Matiere> matiereComboBox = new JComboBox<>(matiereService.listerMatieres().toArray(new Matiere[0]));
            matiereComboBox.setSelectedItem(coursAModifier.getMatiere());
            JComboBox<Enseignant> enseignantComboBox = new JComboBox<>(enseignantService.listerEnseignants().toArray(new Enseignant[0]));
            enseignantComboBox.setSelectedItem(coursAModifier.getEnseignant());
            JComboBox<Classe> classeComboBox = new JComboBox<>(classeService.listerClasses().toArray(new Classe[0]));
            classeComboBox.setSelectedItem(coursAModifier.getClasse());
            JTextField heureDebutField = new JTextField(coursAModifier.getHeureDebut().format(HEURE_FORMATTER));
            JTextField heureFinField = new JTextField(coursAModifier.getHeureFin().format(HEURE_FORMATTER));
            JComboBox<DayOfWeek> jourComboBox = new JComboBox<>(DayOfWeek.values());
            jourComboBox.setSelectedItem(coursAModifier.getJourDeLaSemaine());
            JTextField salleField = new JTextField(coursAModifier.getSalle());
            JComboBox<Niveau> niveauComboBox = new JComboBox<>(Niveau.values());
            niveauComboBox.setSelectedItem(coursAModifier.getNiveau());
            JButton enregistrerButton = new JButton("Enregistrer");
            JButton annulerButton = new JButton("Annuler");

            JLabel matiereLabel = new JLabel("Matière:");
            JLabel enseignantLabel = new JLabel("Enseignant:");
            JLabel classeLabel = new JLabel("Classe:");
            JLabel heureDebutLabel = new JLabel("Heure Début (HH:mm):");
            JLabel heureFinLabel = new JLabel("Heure Fin (HH:mm):");
            JLabel jourLabel = new JLabel("Jour:");
            JLabel salleLabel = new JLabel("Salle:");
            JLabel niveauLabel = new JLabel("Niveau:");

            matiereLabel.setFont(MAIN_FONT);
            enseignantLabel.setFont(MAIN_FONT);
            classeLabel.setFont(MAIN_FONT);
            heureDebutLabel.setFont(MAIN_FONT);
            heureFinLabel.setFont(MAIN_FONT);
            jourLabel.setFont(MAIN_FONT);
            salleLabel.setFont(MAIN_FONT);
            niveauLabel.setFont(MAIN_FONT);
            matiereComboBox.setFont(MAIN_FONT);
            enseignantComboBox.setFont(MAIN_FONT);
            classeComboBox.setFont(MAIN_FONT);
            heureDebutField.setFont(MAIN_FONT);
            heureFinField.setFont(MAIN_FONT);
            jourComboBox.setFont(MAIN_FONT);
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
            dialog.add(heureDebutLabel);
            dialog.add(heureDebutField);
            dialog.add(heureFinLabel);
            dialog.add(heureFinField);
            dialog.add(jourLabel);
            dialog.add(jourComboBox);
            dialog.add(salleLabel);
            dialog.add(salleField);
            dialog.add(niveauLabel);
            dialog.add(niveauComboBox);
            dialog.add(enregistrerButton);
            dialog.add(annulerButton);

            enregistrerButton.addActionListener(e -> {
                try {
                    coursAModifier.setMatiere((Matiere) matiereComboBox.getSelectedItem());
                    coursAModifier.setEnseignant((Enseignant) enseignantComboBox.getSelectedItem());
                    coursAModifier.setClasse((Classe) classeComboBox.getSelectedItem());
                    coursAModifier.setHeureDebut(LocalTime.parse(heureDebutField.getText(), HEURE_FORMATTER));
                    coursAModifier.setHeureFin(LocalTime.parse(heureFinField.getText(), HEURE_FORMATTER));
                    coursAModifier.setJourDeLaSemaine((DayOfWeek) jourComboBox.getSelectedItem());
                    coursAModifier.setSalle(salleField.getText());
                    coursAModifier.setNiveau((Niveau) niveauComboBox.getSelectedItem());

                    coursService.modifierCours(coursAModifier);
                    listerCours();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Cours modifié avec succès.");
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(dialog, "Format d'heure invalide (HH:mm).", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            });

            annulerButton.addActionListener(e -> dialog.dispose());

            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Cours non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerCours() {
        int selectedRow = coursTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un cours à supprimer.", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        int confirmation = JOptionPane.showConfirmDialog(this, "Êtes-vous sûr de vouloir supprimer ce cours ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            coursService.supprimerCours(id);
            listerCours();
            JOptionPane.showMessageDialog(this, "Cours supprimé avec succès.");
        }
    }

    private void obtenirCours() {
        String idStr = JOptionPane.showInputDialog(this, "Entrez l'ID du cours:");
        if (idStr != null && !idStr.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Cours cours = coursService.obtenirCours(id);
                if (cours != null) {
                    JTextArea textArea = new JTextArea(coursToString(cours));
                    textArea.setFont(MAIN_FONT);
                    textArea.setEditable(false);
                    JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Informations du Cours", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Cours non trouvé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID invalide. Veuillez entrer un nombre entier.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void listerCours() {
        tableModel.setRowCount(0);
        List<Cours> coursList = coursService.listerCours();
        for (Cours cours : coursList) {
            tableModel.addRow(new Object[]{
                    cours.getId(),
                    cours.getMatiere(),
                    cours.getEnseignant(),
                    cours.getClasse(),
                    cours.getHeureDebut().format(HEURE_FORMATTER),
                    cours.getHeureFin().format(HEURE_FORMATTER),
                    cours.getJourDeLaSemaine(),
                    cours.getSalle(),
                    cours.getNiveau()
            });
        }
    }

    private String coursToString(Cours cours) {
        return "ID: " + cours.getId() + "\n" +
                "Matière: " + cours.getMatiere() + "\n" +
                "Enseignant: " + cours.getEnseignant() + "\n" +
                "Classe: " + cours.getClasse() + "\n" +
                "Heure Début: " + cours.getHeureDebut().format(HEURE_FORMATTER) + "\n" +
                "Heure Fin: " + cours.getHeureFin().format(HEURE_FORMATTER) + "\n" +
                "Jour: " +cours.getJourDeLaSemaine() + "\n" +
                "Salle: " + cours.getSalle() + "\n" +
                "Niveau: " + cours.getNiveau();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ajouterButton) {
            ajouterCours();
        } else if (e.getSource() == modifierButton) {
            modifierCours();
        } else if (e.getSource() == supprimerButton) {
            supprimerCours();
        } else if (e.getSource() == obtenirButton) {
            obtenirCours();
        } else if (e.getSource() == retourButton) {
            this.dispose(); // Ferme la fenêtre actuelle
            // Ici, ajoute le code pour afficher ta fenêtre de menu principal.
            // Exemple (si ta classe de menu principal est MainFrame) :
            // new MainFrame(/* Passer les services nécessaires ici */).setVisible(true);
        }
    }
}