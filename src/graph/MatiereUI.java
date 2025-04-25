package graph;

// Importations nécessaires
import model.Matiere;
import service.MatiereService;
import java.util.List;
import java.util.Scanner;

public class MatiereUI {

    // Service injecté pour la gestion des données
    private final MatiereService matiereService;
    // Scanner pour lire les entrées utilisateur
    private final Scanner scanner = new Scanner(System.in);

    // Constructeur pour l'injection de dépendance
    public MatiereUI(MatiereService matiereService) {
        this.matiereService = matiereService;
    }

    // Méthode principale de gestion du menu
    public void gererMatieres() {
        int choix;
        do {
            afficherMenu();
            System.out.print("Entrez votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    ajouterMatiere();
                    break;
                case 2:
                    modifierMatiere();
                    break;
                case 3:
                    supprimerMatiere();
                    break;
                case 4:
                    listerMatieres();
                    break;
                case 5:
                    obtenirMatiere();
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
        System.out.println("\n===*GESTION DES MATIERES*======");
        System.out.println("1. Ajouter une matière           |");
        System.out.println("2. Modifier une matière          |");
        System.out.println("3. Supprimer une matière         |");
        System.out.println("4. Lister les matières           |");
        System.out.println("5. Obtenir une matière par ID    |");
        System.out.println("0. Retour au menu principal      |");
        System.out.println("================================");
    }

    // Processus d'ajout d'une nouvelle matière
    private void ajouterMatiere() {
        System.out.println("\n--- Ajouter une matière ---");
        
        // Collecte des informations
        System.out.print("ID : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        System.out.print("Description : ");
        String description = scanner.nextLine();

        // Création et sauvegarde
        Matiere matiere = new Matiere(id, nom, description);
        matiereService.ajouterMatiere(matiere);
        System.out.println("Matière ajoutée avec succès.");
    }

    // Modification d'une matière existante
    private void modifierMatiere() {
        System.out.println("\n--- Modifier une matière ---");
        System.out.print("ID de la matière à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        // Récupération de la matière
        Matiere matiere = matiereService.obtenirMatiere(id);
        if (matiere != null) {
            // Mise à jour partielle des champs
            System.out.print("Nouveau Nom (" + matiere.getNom() + ") : ");
            String nom = scanner.nextLine();
            if (!nom.isEmpty()) matiere.setNom(nom);

            System.out.print("Nouvelle Description (" + matiere.getDescription() + ") : ");
            String description = scanner.nextLine();
            if (!description.isEmpty()) matiere.setDescription(description);

            // Sauvegarde des modifications
            matiereService.modifierMatiere(matiere);
            System.out.println("Matière modifiée avec succès.");
        } else {
            System.out.println("Matière non trouvée.");
        }
    }

    // Suppression d'une matière par ID
    private void supprimerMatiere() {
        System.out.println("\n--- Supprimer une matière ---");
        System.out.print("ID de la matière à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        matiereService.supprimerMatiere(id);
        System.out.println("Matière supprimée avec succès.");
    }

    // Affichage de la liste complète
    private void listerMatieres() {
        System.out.println("\n--- Liste des matières ---");
        List<Matiere> matieres = matiereService.listerMatieres();
        if (matieres.isEmpty()) {
            System.out.println("Aucune matière trouvée.");
        } else {
            for (Matiere matiere : matieres) {
                System.out.println(matiere);
            }
        }
    }

    // Recherche d'une matière par ID
    private void obtenirMatiere() {
        System.out.println("\n--- Obtenir une matière par ID ---");
        System.out.print("ID de la matière : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Matiere matiere = matiereService.obtenirMatiere(id);
        if (matiere != null) {
            System.out.println(matiere);
        } else {
            System.out.println("Matière non trouvée.");
        }
    }
}