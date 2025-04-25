package graph;

// Importations nécessaires
import model.Etudiant;
import service.EtudiantService;
import service.ClasseService;
import java.util.List;
import java.util.Scanner;

public class EtudiantUI {

    // Services injectés pour la gestion des données
    private final EtudiantService etudiantService;
    private final Scanner scanner = new Scanner(System.in);

    // Constructeur avec injection de dépendances
    public EtudiantUI(EtudiantService etudiantService, ClasseService classeService) {
        this.etudiantService = etudiantService;
        // ClasseService pourrait être utilisé pour des fonctionnalités futures
    }

    // Méthode principale de gestion du menu
    public void gererEtudiants() {
        int choix;
        do {
            afficherMenu();
            System.out.print("Entrez votre choix : ");
            choix = scanner.nextInt();
            scanner.nextLine(); 

            switch (choix) {
                case 1:
                    ajouterEtudiant();
                    break;
                case 2:
                    modifierEtudiant();
                    break;
                case 3:
                    supprimerEtudiant();
                    break;
                case 4:
                    listerEtudiants();
                    break;
                case 5:
                    obtenirEtudiant();
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
        System.out.println("\n===*GESTION DES ETUDIANTS*=======");
        System.out.println("1. Ajouter un étudiant            |");
        System.out.println("2. Modifier un étudiant           |");
        System.out.println("3. Supprimer un étudiant          |");
        System.out.println("4. Lister les étudiants           |");
        System.out.println("5. Obtenir un étudiant par ID     |");
        System.out.println("0. Retour au menu principal       |");
        System.out.println("=================================");
    }

    // Processus d'ajout d'un nouvel étudiant
    private void ajouterEtudiant() {
        System.out.println("\n--- Ajouter un étudiant ---");
        
        // Collecte des informations
        System.out.print("ID : ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Nom : ");
        String nom = scanner.nextLine();
        System.out.print("Prénom : ");
        String prenom = scanner.nextLine();
        System.out.print("Niveau : ");
        String niveau = scanner.nextLine();

        // Création et sauvegarde de l'étudiant
        Etudiant etudiant = new Etudiant(id, nom, prenom, niveau);
        etudiantService.ajouterEtudiant(etudiant);
        System.out.println("Étudiant ajouté avec succès.");
    }

    // Modification d'un étudiant existant
    private void modifierEtudiant() {
        System.out.println("\n--- Modifier un étudiant ---");
        System.out.print("ID de l'étudiant à modifier : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        // Récupération de l'étudiant
        Etudiant etudiant = etudiantService.obtenirEtudiant(id);
        if (etudiant != null) {
            // Mise à jour partielle des champs
            System.out.print("Nouveau Nom (" + etudiant.getNom() + ") : ");
            String nom = scanner.nextLine();
            if (!nom.isEmpty()) etudiant.setNom(nom);

            System.out.print("Nouveau Prénom (" + etudiant.getPrenom() + ") : ");
            String prenom = scanner.nextLine();
            if (!prenom.isEmpty()) etudiant.setPrenom(prenom);

            System.out.print("Nouveau Niveau (" + etudiant.getNiveau() + ") : ");
            String niveau = scanner.nextLine();
            if (!niveau.isEmpty()) etudiant.setNiveau(niveau);

            // Sauvegarde des modifications
            etudiantService.modifierEtudiant(etudiant);
            System.out.println("Étudiant modifié avec succès.");
        } else {
            System.out.println("Étudiant non trouvé.");
        }
    }

    // Suppression d'un étudiant par ID
    private void supprimerEtudiant() {
        System.out.println("\n--- Supprimer un étudiant ---");
        System.out.print("ID de l'étudiant à supprimer : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        etudiantService.supprimerEtudiant(id);
        System.out.println("Étudiant supprimé avec succès.");
    }

    // Affichage de la liste complète des étudiants
    private void listerEtudiants() {
        System.out.println("\n--- Liste des étudiants ---");
        List<Etudiant> etudiants = etudiantService.listerEtudiants();
        if (etudiants.isEmpty()) {
            System.out.println("Aucun étudiant trouvé.");
        } else {
            for (Etudiant etudiant : etudiants) {
                System.out.println(etudiant);
            }
        }
    }

    // Recherche d'un étudiant par ID
    private void obtenirEtudiant() {
        System.out.println("\n--- Obtenir un étudiant par ID ---");
        System.out.print("ID de l'étudiant : ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Etudiant etudiant = etudiantService.obtenirEtudiant(id);
        if (etudiant != null) {
            System.out.println(etudiant);
        } else {
            System.out.println("Étudiant non trouvé.");
        }
    }
}