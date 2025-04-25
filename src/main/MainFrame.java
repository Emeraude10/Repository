package main;

import graph.*;
import repository.*;
import service.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {

    private JButton enseignantButton;
    private JButton matiereButton;
    private JButton etudiantButton;
    private JButton classeButton;
    private JButton coursButton;
    private JButton emploiDuTempsButton;
    private JButton quitterButton;
    private JLabel titleLabel; // Nouveau label pour le titre
    private Font buttonFont; // Nouvelle police pour les boutons
    private static final Font MAIN_FONT = new Font("Arial", Font.PLAIN, 14); // Déclaration de la police principale

    private final EnseignantService enseignantService;
    private final MatiereService matiereService;
    private final EtudiantService etudiantService;
    private final ClasseService classeService;
    private final CoursService coursService;
    private final EmploiDuTempsService emploiDuTempsService;

    public MainFrame(
            EnseignantService enseignantService,
            MatiereService matiereService,
            EtudiantService etudiantService,
            ClasseService classeService,
            CoursService coursService,
            EmploiDuTempsService emploiDuTempsService
    ) {
        this.enseignantService = enseignantService;
        this.matiereService = matiereService;
        this.etudiantService = etudiantService;
        this.classeService = classeService;
        this.coursService = coursService;
        this.emploiDuTempsService = emploiDuTempsService;

        setTitle("Gestion Scolaire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(350, 500)); // Augmenter légèrement la taille
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 15, 5, 15); // Augmenter les marges
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // Création et ajout du label de titre
        titleLabel = new JLabel("Gestion des enseignements", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18)); // Augmenter la taille de la police du titre
        gbc.gridy = 0;
        gbc.insets = new Insets(15, 15, 20, 15); // Augmenter les marges du titre
        add(titleLabel, gbc);

        // Définir la police pour les boutons
        buttonFont = MAIN_FONT; // Utilisation de la police MAIN_FONT

        // Réinitialiser les contraintes pour les boutons
        gbc.insets = new Insets(8, 15, 8, 15); // Augmenter les marges des boutons
        gbc.gridy = 1;
        enseignantButton = new JButton("Gestion des enseignants");
        enseignantButton.setFont(buttonFont); // Appliquer la police au bouton
        enseignantButton.addActionListener(this);
        add(enseignantButton, gbc);

        gbc.gridy = 2;
        matiereButton = new JButton("Gestion des matières");
        matiereButton.setFont(buttonFont);
        matiereButton.addActionListener(this);
        add(matiereButton, gbc);

        gbc.gridy = 3;
        etudiantButton = new JButton("Gestion des étudiants");
        etudiantButton.setFont(buttonFont);
        etudiantButton.addActionListener(this);
        add(etudiantButton, gbc);

        gbc.gridy = 4;
        classeButton = new JButton("Gestion des classes/options");
        classeButton.setFont(buttonFont);
        classeButton.addActionListener(this);
        add(classeButton, gbc);

        gbc.gridy = 5;
        coursButton = new JButton("Gestion des cours");
        coursButton.setFont(buttonFont);
        coursButton.addActionListener(this);
        add(coursButton, gbc);

        gbc.gridy = 6;
        emploiDuTempsButton = new JButton("Gestion des Emplois du Temps");
        emploiDuTempsButton.setFont(buttonFont);
        emploiDuTempsButton.addActionListener(this);
        add(emploiDuTempsButton, gbc);

        gbc.gridy = 7;
        quitterButton = new JButton("Quitter");
        quitterButton.setFont(buttonFont);
        quitterButton.addActionListener(this);
        add(quitterButton, gbc);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enseignantButton) {
            new EnseignantGUI(enseignantService).setVisible(true);
        } else if (e.getSource() == matiereButton) {
            new MatiereGUI(matiereService).setVisible(true);
        } else if (e.getSource() == etudiantButton) {
            new EtudiantGUI(etudiantService, classeService).setVisible(true);
        } else if (e.getSource() == classeButton) {
            new ClasseGUI(classeService, etudiantService).setVisible(true);
        } else if (e.getSource() == coursButton) {
            new CoursGUI(coursService, matiereService, enseignantService, classeService).setVisible(true);
        } else if (e.getSource() == emploiDuTempsButton) {
            new EmploiDuTempsGUI(emploiDuTempsService, enseignantService, matiereService, classeService).setVisible(true);
        } else if (e.getSource() == quitterButton) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        // Création des couches d'accès aux données
        MatiereRepository matiereRepository = new MatiereRepository();
        EnseignantRepository enseignantRepositoryImpl = new EnseignantRepository();
        ClasseRepository classeRepositoryImpl = new ClasseRepository();
        CoursRepository coursRepository = new CoursRepository(matiereRepository, enseignantRepositoryImpl, classeRepositoryImpl);
        EmploiDuTempsRepository emploiDuTempsRepository = new EmploiDuTempsRepository(matiereRepository, enseignantRepositoryImpl, classeRepositoryImpl);

        // Initialisation des services métier
        MatiereService matiereService = new MatiereService();
        EnseignantService enseignantService = new EnseignantService();
        ClasseService classeService = new ClasseService();
        CoursService coursService = new CoursService(coursRepository);
        EtudiantService etudiantService = new EtudiantService(classeService);
        EmploiDuTempsService emploiDuTempsService = new EmploiDuTempsService(
                emploiDuTempsRepository,
                coursService,
                enseignantRepositoryImpl,
                classeRepositoryImpl,
                coursRepository
        );

        // Création et affichage de la fenêtre principale sur l'Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new MainFrame(
                enseignantService,
                matiereService,
                etudiantService,
                classeService,
                coursService,
                emploiDuTempsService
        ));
    }
}