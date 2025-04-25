package service;

import model.Cours;
import repository.Repository;
import java.util.List;

/**
 * Service pour la gestion des cours 
 */
public class CoursService {

    // Repository pour la persistance des cours
    private final Repository<Cours, Integer> coursRepository;

    /**
     * Constructeur avec injection de dépendance
     * @param coursRepository Repository des cours à utiliser
     */
    public CoursService(Repository<Cours, Integer> coursRepository) {
        this.coursRepository = coursRepository;
    }

    /**
     * Ajoute un nouveau cours dans le système
     * @param cours Le cours à créer
     */
    public void ajouterCours(Cours cours) {
        coursRepository.create(cours);
    }

    /**
     * Récupère un cours par son identifiant
     * @param id Identifiant du cours
     * @return Le cours trouvé ou null
     */
    public Cours obtenirCours(int id) {
        return coursRepository.read(id);
    }

    /**
     * Met à jour les informations d'un cours existant
     * @param cours Le cours avec les nouvelles données
     * @throws IllegalArgumentException Si le cours n'existe pas
     */
    public void modifierCours(Cours cours) {
        // Vérification de l'existence du cours
        Cours coursExistant = coursRepository.read(cours.getId());
        if (coursExistant == null) {
            throw new IllegalArgumentException("Cours non trouvé.");
        }
        
        // Mise à jour des propriétés modifiables
        coursExistant.setMatiere(cours.getMatiere());
        coursExistant.setEnseignant(cours.getEnseignant());
        coursExistant.setClasse(cours.getClasse());
        coursExistant.setHeureDebut(cours.getHeureDebut());
        coursExistant.setHeureFin(cours.getHeureFin());
        coursExistant.setJourDeLaSemaine(cours.getJourDeLaSemaine());
        coursExistant.setSalle(cours.getSalle());
        coursExistant.setNiveau(cours.getNiveau());
        
        // Persistance des modifications
        coursRepository.update(coursExistant);
    }

    /**
     * Supprime définitivement un cours
     * @param id Identifiant du cours à supprimer
     */
    public void supprimerCours(int id) {
        coursRepository.delete(id);
    }

    /**
     * Liste tous les cours disponibles
     * @return Liste complète des cours
     */
    public List<Cours> listerCours() {
        return coursRepository.findAll();
    }
}