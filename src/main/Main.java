package main;

// Importations des bibliothèques
import graph.*;
import repository.*;
import service.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choix;

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

        // ========= BOUCLE PRINCIPALE =========
        do {
            afficherMenuPrincipal();
            System.out.print("Entrez votre choix : ");

            try {
                choix = scanner.nextInt();
                scanner.nextLine(); 
            } catch (InputMismatchException e) {
                System.out.println("Choix invalide. Veuillez entrer un nombre.");
                scanner.nextLine(); 
                choix = -1;
            }

            // Navigation vers les interfaces appropriées
            switch (choix) {
                case 1:
                    new EnseignantUI(enseignantService).gererEnseignants();
                    break;
                case 2:
                    new MatiereUI(matiereService).gererMatieres();
                    break;
                case 3:
                    new EtudiantUI(etudiantService, classeService).gererEtudiants();
                    break;
                case 4:
                    new ClasseUI(classeService, etudiantService).gererClasses();
                    break;
                case 5:
                    new CoursUI(coursService, matiereService, enseignantService, classeService).gererCours();
                    break;
                case 6:
                    new EmploiDuTempsUI(
                        emploiDuTempsService, 
                        enseignantService, 
                        matiereService, 
                        classeService
                    ).gererEmploiDuTemps();
                    break;
                case 0:
                    System.out.println("Fermeture de l'application.");
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        } while (choix != 0);

        scanner.close();
    }

    // Affichage du menu principal
    private static void afficherMenuPrincipal() {
        System.out.println("\n======*Menu Principal*===========");
        System.out.println("1. Gestion des enseignants       |");
        System.out.println("2. Gestion des matières          |");
        System.out.println("3. Gestion des étudiants         |");
        System.out.println("4. Gestion des classes ou options|");
        System.out.println("5. Gestion des cours             |");
        System.out.println("6. Gestion des Emplois du Temps  |");
        System.out.println("0. Quitter                       |");
        System.out.println("=================================");
    }
}