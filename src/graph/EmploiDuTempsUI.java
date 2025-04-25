package graph;

// Importations nécessaires
import model.Cours;
import model.Enseignant;
import model.Classe;
import model.Matiere;
import service.EmploiDuTempsService;
import service.EnseignantService;
import service.MatiereService;
import service.ClasseService;
import service.EmploiDuTempsService.ConflitHoraireException;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.List;

public class EmploiDuTempsUI {

    // Services injectés pour la gestion des données
    private EmploiDuTempsService emploiDuTempsService;
    private EnseignantService enseignantService;
    private MatiereService matiereService;
    private ClasseService classeService;
    
    // Pour la lecture des entrées utilisateur
    private Scanner scanner;

    // Constructeur pour l'injection des dépendances
    public EmploiDuTempsUI(EmploiDuTempsService emploiDuTempsService, EnseignantService enseignantService, MatiereService matiereService, ClasseService classeService) {
        this.emploiDuTempsService = emploiDuTempsService;
        this.enseignantService = enseignantService;
        this.matiereService = matiereService;
        this.classeService = classeService;
        this.scanner = new Scanner(System.in);
    }

    // Méthode principale de gestion du menu
    public void gererEmploiDuTemps() {
        int choix;
        do {
            afficherMenuEmploiDuTemps();
            System.out.print("Entrez votre choix : ");
            choix = lireEntier();
            scanner.nextLine(); // Nettoyage du buffer

            switch (choix) {
                case 1:
                    planifierCoursUI();
                    break;
                case 2:
                    supprimerCoursUI();
                    break;
                case 3:
                    consulterEmploiDuTempsEnseignantUI();
                    break;
                case 4:
                    consulterEmploiDuTempsClasseUI();
                    break;
                case 5:
                    genererEmploiDuTempsCSVUI();
                    break;
                case 0:
                    System.out.println("Retour au menu principal.");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        } while (choix != 0);
    }

    // Affichage du menu principal
    private void afficherMenuEmploiDuTemps() {
        System.out.println("\n======*Gestion de l'Emploi du Temps*=========");
        System.out.println("1. Planifier un cours                          |");
        System.out.println("2. Supprimer un cours                          |");
        System.out.println("3. Consulter l'emploi du temps d'un enseignant |");
        System.out.println("4. Consulter l'emploi du temps d'une option    |");
        System.out.println("5. Générer l'emploi du temps au format CSV     |");
        System.out.println("0. Retour au menu principal                    |");
        System.out.println("===============================================");
    }

    // Processus complet de planification d'un nouveau cours
    private void planifierCoursUI() {
        System.out.println("\n--- Planifier un cours ---");

        // Collecte des informations nécessaires
        Matiere matiere = choisirMatiere();
        if (matiere == null) return;
        Enseignant enseignant = choisirEnseignant();
        if (enseignant == null) return;
        Classe classe = choisirClasse();
        if (classe == null) return;
        DayOfWeek jour = choisirJourDeLaSemaine();
        if (jour == null) return;
        LocalTime heureDebut = choisirHeure("début");
        if (heureDebut == null) return;
        LocalTime heureFin = choisirHeure("fin");
        if (heureFin == null) return;
        
        // Validation des heures
        if (heureFin.isBefore(heureDebut)) {
            System.out.println("Erreur : L'heure de fin doit être après l'heure de début.");
            return;
        }
        
        String salle = choisirSalle();
        if (salle == null) return;
        Cours.Niveau niveau = choisirNiveau();
        if (niveau == null) return;
        
        // Génération automatique de l'ID
        int id = emploiDuTempsService.getEmploiDuTemps().getCours().size() + 1;

        // Création de l'objet Cours
        Cours cours = new Cours(id, matiere, enseignant, classe, 
                              heureDebut, heureFin, jour, salle, niveau);

        try {
            // Tentative de planification
            emploiDuTempsService.planifierNouveauCours(cours);
            System.out.println("Cours planifié avec succès.");
        } catch (ConflitHoraireException e) {
            System.out.println(e.getMessage());
        }
    }

    // Suppression d'un cours existant
    private void supprimerCoursUI() {
        System.out.println("\n--- Supprimer un cours ---");
        List<Cours> coursList = emploiDuTempsService.getEmploiDuTemps().getCours();
        if (coursList.isEmpty()) {
            System.out.println("Aucun cours à supprimer.");
            return;
        }
        afficherListeCours(coursList);

        Cours coursASupprimer = selectionnerCoursParId(coursList);
        if (coursASupprimer != null) {
            emploiDuTempsService.supprimerCours(coursASupprimer);
            System.out.println("Cours supprimé.");
        } else {
            System.out.println("Cours non trouvé.");
        }
    }

    // Consultation de l'emploi du temps par enseignant
    private void consulterEmploiDuTempsEnseignantUI() {
        System.out.println("\n--- Consulter l'emploi du temps d'un enseignant ---");
        Enseignant enseignant = choisirEnseignant();
        if (enseignant == null) return;
        List<Cours> cours = emploiDuTempsService.consulterEmploiDuTempsParEnseignant(enseignant);
        afficherEmploiDuTemps(cours);
    }

    // Consultation de l'emploi du temps par classe
    private void consulterEmploiDuTempsClasseUI() {
        System.out.println("\n--- Consulter l'emploi du temps d'une option ---");
        Classe classe = choisirClasse();
        if (classe == null) return;
        List<Cours> cours = emploiDuTempsService.consulterEmploiDuTempsParClasse(classe);
        afficherEmploiDuTemps(cours);
    }

    // Génération d'un fichier CSV
    private void genererEmploiDuTempsCSVUI() {
        System.out.println("\n--- Générer l'emploi du temps au format CSV ---");
        System.out.print("Entrez le nom du fichier CSV (sans extension) : ");
        String nomFichier = scanner.nextLine();
        emploiDuTempsService.genererEmploiDuTempsCSV(emploiDuTempsService.getEmploiDuTemps(), nomFichier);
        System.out.println("Emploi du temps généré dans " + nomFichier + ".csv");
    }

    // Méthodes utilitaires de sélection et de validation
    private Matiere choisirMatiere() {
        System.out.println("\n--- Choisir une matière ---");
        List<Matiere> matieres = matiereService.listerMatieres();
        if (matieres.isEmpty()) {
            System.out.println("Aucune matière disponible.");
            return null;
        }
        afficherListeMatieres(matieres);
        return selectionnerElementParId(matieres, "matière");
    }

    private Enseignant choisirEnseignant() {
        System.out.println("\n--- Choisir un enseignant ---");
        List<Enseignant> enseignants = enseignantService.listerEnseignants();
        if (enseignants.isEmpty()) {
            System.out.println("Aucun enseignant disponible.");
            return null;
        }
        afficherListeEnseignants(enseignants);
        return selectionnerElementParId(enseignants, "enseignant");
    }

    private Classe choisirClasse() {
        System.out.println("\n--- Choisir une option ---");
        List<Classe> classes = classeService.listerClasses();
        if (classes.isEmpty()) {
            System.out.println("Aucune classe disponible.");
            return null;
        }
        afficherListeClasses(classes);
        return selectionnerElementParId(classes, "option");
    }

    // Sélection d'un jour de la semaine
    private DayOfWeek choisirJourDeLaSemaine() {
        System.out.println("\n--- Choisir le jour de la semaine ---");
        DayOfWeek[] jours = DayOfWeek.values();
        String[] joursEnFrancais = {"LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI", "DIMANCHE"};

        for (int i = 0; i < jours.length; i++) {
            System.out.println((i + 1) + ". " + joursEnFrancais[i]);
        }

        int choixJour;
        do {
            System.out.print("Entrez le numéro du jour : ");
            choixJour = lireEntier();
            if (choixJour >= 1 && choixJour <= jours.length) {
                return jours[choixJour - 1];
            } else {
                System.out.println("Choix invalide.");
            }
        } while (true);
    }

    // Validation du format horaire
    private LocalTime choisirHeure(String moment) {
        LocalTime heure = null;
        boolean heureValide = false;
        do {
            System.out.print("Entrez l'heure de " + moment + " (HH:mm) : ");
            String heureStr = scanner.nextLine();
            try {
                heure = LocalTime.parse(heureStr, DateTimeFormatter.ofPattern("HH:mm"));
                heureValide = true;
            } catch (DateTimeParseException e) {
                System.out.println("Format d'heure invalide. Veuillez utiliser le format HH:mm.");
            }
        } while (!heureValide);
        return heure;
    }

    // Saisie simple du nom de la salle
    private String choisirSalle() {
        System.out.print("Entrez le nom de la salle : ");
        return scanner.nextLine();
    }

    // Sélection du niveau parmi les valeurs possibles
    private Cours.Niveau choisirNiveau() {
        System.out.println("\n--- Choisir le niveau ---");
        Cours.Niveau[] niveaux = Cours.Niveau.values();
        for (int i = 0; i < niveaux.length; i++) {
            System.out.println((i + 1) + ". " + niveaux[i].getNomComplet() + " (" + niveaux[i] + ")");
        }

        int choixNiveau;
        do {
            System.out.print("Entrez le numéro du niveau : ");
            choixNiveau = lireEntier();
            if (choixNiveau >= 1 && choixNiveau <= niveaux.length) {
                return niveaux[choixNiveau - 1];
            } else {
                System.out.println("Choix invalide.");
            }
        } while (true);
    }

    // Méthodes d'affichage des différentes listes
    private void afficherListeCours(List<Cours> cours) {
        System.out.println("\n--- Liste des Cours ---");
        if (cours.isEmpty()) {
            System.out.println("Aucun cours à afficher.");
            return;
        }
        for (Cours c : cours) {
            System.out.println(c.getId() + ". " + c);
        }
    }

    private void afficherListeEnseignants(List<Enseignant> enseignants) {
        System.out.println("\n--- Liste des Enseignants ---");
        if (enseignants.isEmpty()) {
            System.out.println("Aucun enseignant à afficher.");
            return;
        }
        for (Enseignant e : enseignants) {
            System.out.println(e.getId() + ". " + e);
        }
    }

    private void afficherListeMatieres(List<Matiere> matieres) {
        System.out.println("\n--- Liste des Matières ---");
        if (matieres.isEmpty()) {
            System.out.println("Aucune matière à afficher.");
            return;
        }
        for (Matiere m : matieres) {
            System.out.println(m.getId() + ". " + m);
        }
    }

    private void afficherListeClasses(List<Classe> classes) {
        System.out.println("\n--- Liste des Options ---");
        if (classes.isEmpty()) {
            System.out.println("Aucune option à afficher.");
            return;
        }
        for (Classe cl : classes) {
            System.out.println(cl.getId() + ". " + cl);
        }
    }

    // Méthode générique de sélection par ID
    private <T extends model.Identifiable> T selectionnerElementParId(List<T> elements, String nomElement) {
        if (elements.isEmpty()) {
            return null;
        }

        int idSelectionne;
        do {
            System.out.print("Entrez l'ID de la " + nomElement + " : ");
            idSelectionne = lireEntier();
            if (idSelectionne == -1) {
                return null;
            }
            for (T element : elements) {
                if (element.getId() == idSelectionne) {
                    return element;
                }
            }
            System.out.println("Aucune " + nomElement + " trouvée avec cet ID.");
        } while (true);
    }

    // Sélection spécifique pour les cours
    private Cours selectionnerCoursParId(List<Cours> cours) {
        if (cours.isEmpty()) {
            return null;
        }

        int idSelectionne;
        do {
            System.out.print("Entrez l'ID du cours : ");
            idSelectionne = lireEntier();
            if (idSelectionne == -1) {
                return null;
            }
            for (Cours c : cours) {
                if (c.getId() == idSelectionne) {
                    return c;
                }
            }
            System.out.println("Aucun cours trouvé avec cet ID.");
        } while (true);
    }

    // Affichage formaté de l'emploi du temps
    private void afficherEmploiDuTemps(List<Cours> cours) {
        if (cours.isEmpty()) {
            System.out.println("Aucun cours à afficher.");
            return;
        }
        System.out.println("\n--- Emploi du Temps ---");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        for (Cours c : cours) {
            System.out.println("ID: " + c.getId() +
                    ", Matière: " + c.getMatiere().getNom() +
                    ", Enseignant: " + c.getEnseignant().getNom() +
                    ", Option: " + c.getClasse().getNom() +
                    ", Jour: " + c.getJourDeLaSemaine() +
                    ", Heure Début: " + (c.getHeureDebut() != null ? c.getHeureDebut().format(formatter) : "N/A") +
                    ", Heure Fin: " + (c.getHeureFin() != null ? c.getHeureFin().format(formatter) : "N/A") +
                    ", Salle: " + c.getSalle() +
                    ", Niveau: " + c.getNiveau());
        }
    }

    // Lecture sécurisée d'un entier
    private int lireEntier() {
        try {
            return scanner.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.out.println("Entrée invalide. Veuillez entrer un nombre entier.");
            scanner.next(); // Nettoyage de l'entrée invalide
            return -1; // Valeur d'erreur
        }
    }
}