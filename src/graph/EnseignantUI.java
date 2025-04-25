package graph;

// Importations nécessaires
import model.Enseignant;
import service.EnseignantService;
import java.util.List;
import java.util.Scanner;

public class EnseignantUI {

    // Service injecté pour la gestion des données
    private final EnseignantService enseignantService;
    // Scanner pour lire les entrées utilisateur
    private final Scanner scanner = new Scanner(System.in);

    // Constructeur pour l'injection de dépendance
    public EnseignantUI(EnseignantService enseignantService) {
        this.enseignantService = enseignantService;
    }

    // Méthode principale de gestion du menu
    public void gererEnseignants() {
        int choix;
        do {
            afficherMenu();
            System.out.print("Entrez votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine(); 

            switch (choix) {
                case 1:
                    ajouterEnseignant();
                    break;
                case 2:
                    modifierEnseignant();
                    break;
                case 3:
                    supprimerEnseignant();
                    break;
                case 4:
                    listerEnseignants();
                    break;
                case 5:
                    obtenirEnseignant();
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
    private void afficherMenu() {
        System.out.println("\n====*GESTION DES ENSEIGNANTS*======");
        System.out.println("1. Ajouter un enseignant           |");
        System.out.println("2. Modifier un enseignant          |");
        System.out.println("3. Supprimer un enseignant         |");
        System.out.println("4. Lister les enseignants          |");
        System.out.println("5. Obtenir un enseignant par ID    |");
        System.out.println("0. Retour au menu principal        |");
        System.out.println("===================================");
    }

    // Processus d'ajout d'un nouvel enseignant
    private void ajouterEnseignant() {
        System.out.println("\n--- Ajouter un enseignant ---");
        // Collecte des informations
        System.out.print("ID : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();
        System.out.print("Spécialité : ");
        String specialite = scanner.nextLine();

        // Création et sauvegarde
        Enseignant enseignant = new Enseignant(id, nom, prenom, specialite);
        enseignantService.ajouterEnseignant(enseignant);
        System.out.println("Enseignant ajouté avec succès.");
    }

    // Modification d'un enseignant existant
    private void modifierEnseignant() {
        System.out.println("\n--- Modifier un enseignant ---");
        System.out.print("ID de l'enseignant à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        // Récupération de l'enseignant
        Enseignant enseignant = enseignantService.obtenirEnseignant(id);
        if (enseignant != null) {
            // Mise à jour partielle des champs
            System.out.print("Nouveau Nom (" + enseignant.getNom() + ") : ");
            String nom = scanner.nextLine();
            if (!nom.isEmpty()) enseignant.setNom(nom);

            System.out.print("Nouveau Prénom (" + enseignant.getPrenom() + ") : ");
            String prenom = scanner.nextLine();
            if (!prenom.isEmpty()) enseignant.setPrenom(prenom);

            System.out.print("Nouvelle Spécialité (" + enseignant.getSpecialite() + ") : ");
            String specialite = scanner.nextLine();
            if (!specialite.isEmpty()) enseignant.setSpecialite(specialite);

            // Sauvegarde des modifications
            enseignantService.modifierEnseignant(enseignant);
            System.out.println("Enseignant modifié avec succès.");
        } else {
            System.out.println("Enseignant non trouvé.");
        }
    }

    // Suppression d'un enseignant par ID
    private void supprimerEnseignant() {
        System.out.println("\n--- Supprimer un enseignant ---");
        System.out.print("ID de l'enseignant à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        enseignantService.supprimerEnseignant(id);
        System.out.println("Enseignant supprimé avec succès.");
    }

    // Affichage de la liste complète
    private void listerEnseignants() {
        List<Enseignant> enseignants = enseignantService.listerEnseignants();
        afficherListeEnseignants(enseignants);
    }

    // Affichage formaté de la liste
    private void afficherListeEnseignants(List<Enseignant> enseignants) {
        System.out.println("\n--- Liste des enseignants ---");
        if (enseignants.isEmpty()) {
            System.out.println("Aucun enseignant trouvé.");
        } else {
            for (Enseignant enseignant : enseignants) {
                System.out.println(enseignant);
            }
        }
    }

    // Recherche d'un enseignant par ID
    private void obtenirEnseignant() {
        System.out.println("\n--- Obtenir un enseignant par ID ---");
        System.out.print("ID de l'enseignant : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Enseignant enseignant = enseignantService.obtenirEnseignant(id);
        if (enseignant != null) {
            System.out.println(enseignant);
        } else {
            System.out.println("Enseignant non trouvé.");
        }
    }
}