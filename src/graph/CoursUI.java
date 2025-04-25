package graph;

// Importations des classes nécessaires
import model.Cours;
import model.Matiere;
import model.Enseignant;
import model.Classe;
import model.Cours.Niveau;
import service.CoursService;
import service.MatiereService;
import service.EnseignantService;
import service.ClasseService;

import java.util.List;
import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.DayOfWeek;

public class CoursUI {

    // Services injectés pour accéder aux données
    private final CoursService coursService;
    private final MatiereService matiereService;
    private final EnseignantService enseignantService;
    private final ClasseService classeService;
    
    // Scanner pour lire les entrées utilisateur
    private final Scanner scanner = new Scanner(System.in);
    
    // Formatage des heures (HH:mm)
    private static final DateTimeFormatter HEURE_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    // Constructeur pour l'injection des dépendances
    public CoursUI(CoursService coursService, MatiereService matiereService, EnseignantService enseignantService, ClasseService classeService) {
        this.coursService = coursService;
        this.matiereService = matiereService;
        this.enseignantService = enseignantService;
        this.classeService = classeService;
    }

    // Méthode principale pour gérer le menu des cours
    public void gererCours() {
        int choix;
        do {
            afficherMenu();
            choix = lireEntier("Entrez votre choix : ");

            switch (choix) {
                case 1:
                    ajouterCours();
                    break;
                case 2:
                    modifierCours();
                    break;
                case 3:
                    supprimerCours();
                    break;
                case 4:
                    listerCours();
                    break;
                case 5:
                    obtenirCours();
                    break;
                case 0:
                    System.out.println("Retour au menu principal.");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        } while (choix != 0);
    }

    // Affiche le menu de gestion des cours
    private void afficherMenu() {
        System.out.println("\n===*GESTION DES COURS*=======");
        System.out.println("1. Ajouter un cours          |");
        System.out.println("2. Modifier un cours         |");
        System.out.println("3. Supprimer un cours        |");
        System.out.println("4. Lister les cours          |");
        System.out.println("5. Obtenir un cours par ID   |");
        System.out.println("0. Retour au menu principal  |");
        System.out.println("=============================");
    }

    // Ajoute un nouveau cours après avoir collecté toutes les informations
    private void ajouterCours() {
        System.out.println("\n--- Ajouter un cours ---");
        Integer id = lireInteger("ID : ");
        if (id == null) return;

        // Collecte des informations nécessaires
        Matiere matiere = choisirMatiere();
        if (matiere == null) return;
        Enseignant enseignant = choisirEnseignant();
        if (enseignant == null) return;
        Classe classe = choisirClasse();
        if (classe == null) return;
        LocalTime heureDebut = choisirHeure("début");
        if (heureDebut == null) return;
        LocalTime heureFin = choisirHeure("fin");
        if (heureFin == null) return;
        DayOfWeek jourDeLaSemaine = choisirJourDeLaSemaine();
        if (jourDeLaSemaine == null) return;
        String salle = choisirSalle();
        if (salle == null) return;
        Niveau niveau = choisirNiveau();
        if (niveau == null) return;

        // Création et sauvegarde du cours
        Cours cours = new Cours(id, matiere, enseignant, classe, heureDebut, heureFin, 
                              jourDeLaSemaine, salle, niveau);
        
        coursService.ajouterCours(cours);
        System.out.println("Cours ajouté avec succès.");
    }

    // Modifie un cours existant après vérification de l'ID
    private void modifierCours() {
        System.out.println("\n--- Modifier un cours ---");
        Integer id = lireInteger("ID du cours à modifier : ");
        if (id == null) return;

        // Récupération et vérification du cours existant
        Cours cours = coursService.obtenirCours(id);
        if (cours != null) {
            System.out.println("Entrez les nouvelles informations du cours (laissez vide pour ne pas modifier) :");

            // Mise à jour des champs modifiés
            Matiere matiere = choisirMatiere();
            if (matiere != null) cours.setMatiere(matiere);
            Enseignant enseignant = choisirEnseignant();
            if (enseignant != null) cours.setEnseignant(enseignant);
            Classe classe = choisirClasse();
            if (classe != null) cours.setClasse(classe);
            LocalTime heureDebut = choisirHeure("début");
            if (heureDebut != null) cours.setHeureDebut(heureDebut);
            LocalTime heureFin = choisirHeure("fin");
            if (heureFin != null) cours.setHeureFin(heureFin);
            DayOfWeek jourDeLaSemaine = choisirJourDeLaSemaine();
            if (jourDeLaSemaine != null) cours.setJourDeLaSemaine(jourDeLaSemaine);
            String salle = choisirSalle();
            if (salle != null) cours.setSalle(salle);
            Niveau niveau = choisirNiveau();
            if (niveau != null) cours.setNiveau(niveau);

            coursService.modifierCours(cours);
            System.out.println("Cours modifié avec succès.");
        } else {
            System.out.println("Cours non trouvé.");
        }
    }

    // Supprime un cours après vérification de l'ID
    private void supprimerCours() {
        System.out.println("\n--- Supprimer un cours ---");
        Integer id = lireInteger("ID du cours à supprimer : ");
        if (id == null) return;

        coursService.supprimerCours(id);
        System.out.println("Cours supprimé avec succès.");
    }

    // Affiche la liste de tous les cours
    private void listerCours() {
        System.out.println("\n--- Liste des cours ---");
        List<Cours> cours = coursService.listerCours();
        if (cours.isEmpty()) {
            System.out.println("Aucun cours trouvé.");
        } else {
            for (Cours c : cours) {
                System.out.println(c);
            }
        }
    }

    // Affiche un cours spécifique via son ID
    private void obtenirCours() {
        System.out.println("\n--- Obtenir un cours par ID ---");
        Integer id = lireInteger("ID du cours : ");
        if (id == null) return;

        Cours cours = coursService.obtenirCours(id);
        if (cours != null) {
            System.out.println(cours);
        } else {
            System.out.println("Cours non trouvé.");
        }
    }

    // Méthodes utilitaires pour la saisie utilisateur
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
        System.out.println("\n--- Choisir une classe ---");
        List<Classe> classes = classeService.listerClasses();
        if (classes.isEmpty()) {
            System.out.println("Aucune classe disponible.");
            return null;
        }
        afficherListeClasses(classes);
        return selectionnerElementParId(classes, "classe");
    }

    // Convertit une saisie utilisateur en heure valide
    private LocalTime choisirHeure(String moment) {
        return lireLocalTime("Entrez l'heure de " + moment + " (HH:mm) : ", HEURE_FORMATTER);
    }

    // Permet de choisir un jour dans la semaine
    private DayOfWeek choisirJourDeLaSemaine() {
        System.out.println("\n--- Choisir le jour de la semaine ---");
        DayOfWeek[] jours = DayOfWeek.values();
        String[] joursEnFrancais = {"LUNDI", "MARDI", "MERCREDI", "JEUDI", "VENDREDI", "SAMEDI", "DIMANCHE"};

        for (int i = 0; i < jours.length; i++) {
            System.out.println((i + 1) + ". " + joursEnFrancais[i]);
        }

        Integer choixJour = lireInteger("Entrez le numéro du jour : ");
        if (choixJour != null && choixJour >= 1 && choixJour <= jours.length) {
            return jours[choixJour - 1];
        } else {
            System.out.println("Choix invalide.");
            return null;
        }
    }

    // Saisie simple du nom de la salle
    private String choisirSalle() {
        System.out.print("Salle : ");
        return scanner.nextLine();
    }

    // Sélection du niveau parmi une liste prédéfinie
    private Niveau choisirNiveau() {
        System.out.println("\n--- Choisir le niveau ---");
        Niveau[] niveaux = Niveau.values();
        for (int i = 0; i < niveaux.length; i++) {
            System.out.println((i + 1) + ". " + niveaux[i].getNomComplet() + " (" + niveaux[i] + ")");
        }

        Integer choixNiveau = lireInteger("Niveau : ");
        if (choixNiveau != null && choixNiveau >= 1 && choixNiveau <= niveaux.length) {
            return niveaux[choixNiveau - 1];
        } else {
            System.out.println("Choix invalide.");
            return null;
        }
    }

    // Méthode générique pour sélectionner un élément par ID
    private <T extends model.Identifiable> T selectionnerElementParId(List<T> elements, String nomElement) {
        if (elements.isEmpty()) {
            return null;
        }

        Integer idSelectionne;
        do {
            System.out.print("Entrez l'ID de la " + nomElement + " : ");
            idSelectionne = lireInteger("");

            if (idSelectionne != null) {
                for (T element : elements) {
                    if (element.getId() == idSelectionne) {
                        return element;
                    }
                }
                System.out.println("Aucune " + nomElement + " trouvée avec cet ID.");
            } else {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
            }
        } while (true);
    }

    // Méthodes d'affichage des listes
    private void afficherListeEnseignants(List<Enseignant> enseignants) {
        System.out.println("\n--- Liste des Enseignants ---");
        for (Enseignant e : enseignants) {
            System.out.println(e.getId() + ". " + e);
        }
    }

    private void afficherListeMatieres(List<Matiere> matieres) {
        System.out.println("\n--- Liste des Matières ---");
        for (Matiere m : matieres) {
            System.out.println(m.getId() + ". " + m);
        }
    }

    private void afficherListeClasses(List<Classe> classes) {
        System.out.println("\n--- Liste des Classes ---");
        for (Classe cl : classes) {
            System.out.println(cl.getId() + ". " + cl);
        }
    }

    // Méthodes de lecture sécurisée des entrées
    private Integer lireInteger(String prompt) {
        Integer valeur = null;
        boolean valide = false;
        do {
            System.out.print(prompt);
            try {
                valeur = scanner.nextInt();
                valide = true;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                scanner.nextLine(); 
            }
        } while (!valide);
        scanner.nextLine();
        return valeur;
    }

    private int lireEntier(String prompt) {
        int valeur = -1;
        boolean valide = false;
        do {
            System.out.print(prompt);
            try {
                valeur = scanner.nextInt();
                valide = true;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                scanner.nextLine(); 
            }
        } while (!valide);
        scanner.nextLine(); 
        return valeur;
    }

    // Lecture d'une heure au format HH:mm avec validation
    private LocalTime lireLocalTime(String prompt, DateTimeFormatter formatter) {
        LocalTime heure = null;
        boolean valide = false;
        do {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                heure = LocalTime.parse(input, formatter);
                valide = true;
            } catch (DateTimeParseException e) {
                System.out.println("Format invalide. Veuillez réessayer.");
            }
        } while (!valide);
        return heure;
    }
}