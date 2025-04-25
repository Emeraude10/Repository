package service;

import model.*;
import repository.*;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service pour la gestion de l'emploi du temps et des conflits horaires
 */
public class EmploiDuTempsService {

    // Dépendances
    private final EmploiDuTempsRepository emploiDuTempsRepository;
    private final EmploiDuTemps emploiDuTemps;
    private final CoursRepository coursRepository;

    /**
     * Constructeur avec injection des dépendances
     * @param emploiDuTempsRepository Repository de persistance de l'emploi du temps
     * @param coursService Service de gestion des cours
     * @param enseignantRepository Repository des enseignants
     * @param classeRepository Repository des classes
     * @param coursRepository Repository des cours
     */
    public EmploiDuTempsService(EmploiDuTempsRepository emploiDuTempsRepository,
                                CoursService coursService,
                                Repository<Enseignant, Integer> enseignantRepository,
                                Repository<Classe, Integer> classeRepository,
                                CoursRepository coursRepository) {
        this.emploiDuTempsRepository = emploiDuTempsRepository;
        this.emploiDuTemps = this.emploiDuTempsRepository.chargerEmploiDuTemps();
        this.coursRepository = coursRepository;
        chargerTousLesCoursInitialement();
    }

    /**
     * Charge tous les cours existants dans l'emploi du temps au démarrage
     */
    private void chargerTousLesCoursInitialement() {
        List<Cours> tousLesCours = coursRepository.findAll();
        for (Cours cours : tousLesCours) {
            emploiDuTemps.ajouterCours(cours);
        }
    }

    /**
     * Récupère l'emploi du temps complet
     * @return L'objet EmploiDuTemps actuel
     */
    public EmploiDuTemps getEmploiDuTemps() {
        return emploiDuTemps;
    }

    /**
     * Planifie un nouveau cours avec vérification des conflits
     * @param cours Le cours à ajouter
     * @throws ConflitHoraireException Si un conflit est détecté
     */
    public void planifierNouveauCours(Cours cours) throws ConflitHoraireException {
        if (verifierConflitHoraire(cours)) {
            throw new ConflitHoraireException("Conflit horaire détecté");
        }
        emploiDuTemps.ajouterCours(cours);
    }

    /**
     * Supprime un cours de l'emploi du temps
     * @param coursASupprimer Le cours à supprimer
     */
    public void supprimerCours(Cours coursASupprimer) {
        emploiDuTemps.supprimerCours(coursASupprimer);
        emploiDuTempsRepository.sauvegarderEmploiDuTemps(emploiDuTemps);
    }

    /**
     * Consulte l'emploi du temps d'un enseignant
     * @param enseignant L'enseignant à filtrer
     * @return Liste des cours de l'enseignant
     */
    public List<Cours> consulterEmploiDuTempsParEnseignant(Enseignant enseignant) {
        return emploiDuTemps.getCours().stream()
                .filter(cours -> cours.getEnseignant().equals(enseignant))
                .collect(Collectors.toList());
    }

    /**
     * Consulte l'emploi du temps d'une classe
     * @param classe La classe à filtrer
     * @return Liste des cours de la classe
     */
    public List<Cours> consulterEmploiDuTempsParClasse(Classe classe) {
        return emploiDuTemps.getCours().stream()
                .filter(cours -> cours.getClasse().equals(classe))
                .collect(Collectors.toList());
    }

    /**
     * Génère un fichier CSV de l'emploi du temps
     * @param emploiDuTemps L'emploi du temps à exporter
     * @param nomFichier Le nom du fichier sans extension
     */
    public void genererEmploiDuTempsCSV(EmploiDuTemps emploiDuTemps, String nomFichier) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String filePath = nomFichier + ".csv";
        
        try (FileWriter writer = new FileWriter(filePath)) {
            // En-tête du fichier CSV
            writer.write("ID,Matière,Enseignant,Classe,Jour,Heure Début,Heure Fin,Salle,Niveau\n");
            
            // Écriture des données
            for (Cours cours : emploiDuTemps.getCours()) {
                writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        cours.getId(),
                        cours.getMatiere().getNom(),
                        cours.getEnseignant().getNom(),
                        cours.getClasse().getNom(),
                        cours.getJourDeLaSemaine(),
                        cours.getHeureDebut() != null ? cours.getHeureDebut().format(formatter) : "N/A",
                        cours.getHeureFin() != null ? cours.getHeureFin().format(formatter) : "N/A",
                        cours.getSalle(),
                        cours.getNiveau()));
            }
            System.out.println("Emploi du temps exporté vers " + filePath);
            
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier CSV : " + e.getMessage());
        }
    }

    /**
     * Vérifie les conflits horaires pour un nouveau cours
     * @param nouveauCours Le cours à vérifier
     * @return true si conflit détecté, false sinon
     */
    private boolean verifierConflitHoraire(Cours nouveauCours) {
        return emploiDuTemps.getCours().stream()
                .anyMatch(coursExistant ->
                    // Même jour et (même classe ou même salle)
                    coursExistant.getJourDeLaSemaine() == nouveauCours.getJourDeLaSemaine() &&
                    (coursExistant.getClasse().equals(nouveauCours.getClasse()) || 
                     coursExistant.getSalle().equals(nouveauCours.getSalle())) &&
                    
                    // Chevauchement horaire
                    (nouveauCours.getHeureDebut().isBefore(coursExistant.getHeureFin()) &&
                     nouveauCours.getHeureFin().isAfter(coursExistant.getHeureDebut()))
                );
    }

    /**
     * Exception pour les conflits horaires
     */
    public static class ConflitHoraireException extends Exception {
        private static final long serialVersionUID = 1L;
        
        public ConflitHoraireException(String message) {
            super(message);
        }
    }
}