package graph;

import model.Classe;
import model.Etudiant;
import service.ClasseService;
import service.EtudiantService;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class ClasseUI {

    // Services injectés pour la gestion des données
    private final ClasseService classeService; 
    private final EtudiantService etudiantService; 
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Constructeur initialisant les services nécessaires
     * @param classeService Service de gestion des classes/options
     * @param etudiantService Service de gestion des étudiants
     */
    public ClasseUI(ClasseService classeService, EtudiantService etudiantService) {
        this.classeService = classeService;
        this.etudiantService = etudiantService;
    }

    /**
     * Affiche le menu principal et gère les interactions utilisateur
     */
    public void gererClasses() {
        int choix;
        do {
            afficherMenu();
            System.out.print("Entrez votre choix : ");
            try {
                choix = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Choix invalide. Veuillez entrer un nombre.");
                choix = -1;
            }
            scanner.nextLine();
            switch (choix) {
                case 1:
                    ajouterClasse(); // Ajout d'une nouvelle classe
                    break;
                case 2:
                    modifierClasse(); // Modification d'une classe existante
                    break;
                case 3:
                    supprimerClasse(); // Suppression d'une classe
                    break;
                case 4:
                    listerClasses(); // Liste de toutes les classes
                    break;
                case 5:
                    obtenirClasse(); // Recherche par ID
                    break;
                case 6:
                    ajouterEtudiantAClasseUI(); // Ajout étudiant à classe
                    break;
                case 7:
                    retirerEtudiantDeClasseUI(); // Retrait étudiant de classe
                    break;
                case 0:
                    System.out.println("Retour au menu principal.");
                    break;
                default:
                    if (choix != -1) {
                        System.out.println("Choix invalide. Veuillez réessayer.");
                    }
            }
        } while (choix != 0); // Sortie de la boucle quand choix = 0
    }

    /**
     * Affiche le menu des opérations disponibles
     */
    private void afficherMenu() {
        System.out.println("\n===*GESTION DES CLASSES OU OPTIONS*=====");
        System.out.println("1. Ajouter une classe ou option           |");
        System.out.println("2. Modifier une classe ou option          |");
        System.out.println("3. Supprimer une classe ou option         |");
        System.out.println("4. Lister les classes ou options          |");
        System.out.println("5. Obtenir une classe ou option par ID    |");
        System.out.println("6. Ajouter étudiant à une classe ou option|");
        System.out.println("7. Retirer étudiant de la classe ou option|");
        System.out.println("0. Retour au menu principal               |");
        System.out.println("==========================================");
    }

    /**
     * Crée une nouvelle classe/option avec saisie utilisateur
     */
    private void ajouterClasse() {
        System.out.println("\n--- Ajouter une classe ou option ---");
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        classeService.ajouterClasse(nom);
        System.out.println("Classe ou Option ajoutée avec succès.");
    }

    /**
     * Modifie le nom d'une classe existante
     */
    private void modifierClasse() {
        System.out.println("\n--- Modifier une classe ou option ---");
        int id = getIdFromUser();
        if (id == -1) return;

        Classe classe = classeService.obtenirClasse(id);
        if (classe != null) {
            System.out.println("Classe ou Option trouvée : " + classe);
            System.out.print("Nouveau Nom (" + classe.getNom() + ") : ");
            String nom = scanner.nextLine();
            if (!nom.isEmpty()) {
                classe.setNom(nom);
            }

            try {
                classeService.modifierClasse(classe);
                System.out.println("Classe ou Option modifiée avec succès.");
            } catch (Exception e) {
                System.err.println("Erreur lors de la modification : " + e.getMessage());
            }
        } else {
            System.out.println("Classe ou Option non trouvée.");
        }
    }

    /**
     * Supprime une classe après confirmation de l'ID
     */
    private void supprimerClasse() {
        System.out.println("\n--- Supprimer une classe ou option ---");
        int id = getIdFromUser();
        if (id == -1) return;

        try {
            classeService.supprimerClasse(id);
            System.out.println("Classe ou Option supprimée avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    /**
     * Affiche la liste complète des classes/options
     */
    private void listerClasses() {
        System.out.println("\n--- Liste des classes ou options ---");
        List<Classe> classes = classeService.listerClasses();
        if (classes.isEmpty()) {
            System.out.println("Aucune classe ou option trouvée.");
        } else {
            for (Classe classe : classes) {
                System.out.println(classe);
            }
        }
    }

    /**
     * Affiche les détails d'une classe spécifique par ID
     */
    private void obtenirClasse() {
        System.out.println("\n--- Obtenir une classe ou option par ID ---");
        int id = getIdFromUser();
        if (id == -1) return;

        Classe classe = classeService.obtenirClasse(id);
        if (classe != null) {
            System.out.println(classe);
        } else {
            System.out.println("Classe ou Option non trouvée.");
        }
    }

    /**
     * Récupère un ID valide depuis la saisie utilisateur
     * @return ID saisi ou -1 en cas d'erreur
     */
    private int getIdFromUser() {
        int id;
        while (true) {
            System.out.print("Entrez l'ID de la classe ou option : ");
            try {
                id = scanner.nextInt();
                scanner.nextLine();
                return id;
            } catch (InputMismatchException e) {
                System.out.println("ID invalide. Veuillez entrer un nombre entier.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Ajoute un étudiant à une classe après vérifications
     */
    private void ajouterEtudiantAClasseUI() {
        System.out.println("\n--- Ajouter un étudiant à une classe ou option ---");
        listerClasses();
        int classeId = getIdFromUser();
        if (classeId == -1) return;

        etudiantService.listerEtudiants();
        System.out.print("Entrez l'ID de l'étudiant à ajouter : ");
        int etudiantId = scanner.nextInt();
        scanner.nextLine();

        Classe classe = classeService.obtenirClasse(classeId);
        Etudiant etudiant = etudiantService.obtenirEtudiant(etudiantId);

        if (classe == null) {
            System.out.println("Classe ou Option introuvable !");
            return;
        }

        if (etudiant == null) {
            System.out.println("Étudiant introuvable !");
            return;
        }

        classeService.ajouterEtudiantAClasse(classeId, etudiantId);
        System.out.println("Étudiant ajouté avec succès à la classe ou option.");
    }

    /**
     * Retire un étudiant d'une classe après vérifications
     */
    private void retirerEtudiantDeClasseUI() {
        System.out.println("\n--- Retirer un étudiant d'une classe ou option ---");
        listerClasses();
        int classeId = getIdFromUser();
        if (classeId == -1) return;

        etudiantService.listerEtudiants();
        System.out.print("Entrez l'ID de l'étudiant à retirer : ");
        int etudiantId = scanner.nextInt();
        scanner.nextLine();

        Classe classe = classeService.obtenirClasse(classeId);
        Etudiant etudiant = etudiantService.obtenirEtudiant(etudiantId);

        if (classe == null) {
            System.out.println("Classe ou Option introuvable !");
            return;
        }

        if (etudiant == null) {
            System.out.println("Étudiant introuvable !");
            return;
        }

        classeService.retirerEtudiantDeClasse(classeId, etudiantId);
        System.out.println("Étudiant retiré de la classe ou option.");
    }
}