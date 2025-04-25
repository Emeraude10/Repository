package service;

import model.Enseignant;
import repository.EnseignantRepository;
import java.util.List;

/**
 * Service pour la gestion des enseignants
 */
public class EnseignantService {

    // Répository pour la persistance des données
    private final EnseignantRepository enseignantRepository = new EnseignantRepository();

    /**
     * Ajoute un nouvel enseignant dans le système
     * @param enseignant L'enseignant à créer
     */
    public void ajouterEnseignant(Enseignant enseignant) {
        enseignantRepository.create(enseignant);
    }

    /**
     * Récupère un enseignant par son identifiant
     * @param id Identifiant unique de l'enseignant
     * @return L'enseignant trouvé ou null
     */
    public Enseignant obtenirEnseignant(Integer id) {
        return enseignantRepository.read(id);
    }

    /**
     * Met à jour les informations d'un enseignant existant
     * @param enseignant L'enseignant modifié avec les nouvelles données
     */
    public void modifierEnseignant(Enseignant enseignant) {
        enseignantRepository.update(enseignant);
    }

    /**
     * Supprime définitivement un enseignant
     * @param id Identifiant de l'enseignant à supprimer
     */
    public void supprimerEnseignant(Integer id) {
        enseignantRepository.delete(id);
    }

    /**
     * Liste tous les enseignants enregistrés
     * @return Liste complète des enseignants
     */
    public List<Enseignant> listerEnseignants() {
        return enseignantRepository.findAll();
    }
}