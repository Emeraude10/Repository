package service;

import model.Matiere;
import repository.MatiereRepository;
import repository.Repository;

import java.util.List;

/**
 * Service de gestion des matières académiques.
 * Fournit les opérations standard pour la gestion des matières.
 * Utilise un repository générique pour les opérations de persistance en CSV.
 */
public class MatiereService {

    // Repository générique pour les opérations des matières (clé primaire de type Integer)
    private final Repository<Matiere, Integer> matiereRepository = new MatiereRepository();

    /**
     * Ajoute une nouvelle matière dans le système
     * @param matiere Objet Matière à créer contenant toutes les informations nécessaires
     */
    public void ajouterMatiere(Matiere matiere) {
        matiereRepository.create(matiere);
    }

    /**
     * Récupère une matière par son identifiant unique
     * @param id Identifiant numérique de la matière à rechercher
     * @return La matière correspondante ou null si non trouvée
     */
    public Matiere obtenirMatiere(int id) {
        return matiereRepository.read(id);
    }

    /**
     * Met à jour les informations d'une matière existante
     * @param materie Objet Matière modifié contenant les nouvelles valeurs
     */
    public void modifierMatiere(Matiere matiere) {
        matiereRepository.update(matiere);
    }

    /**
     * Supprime définitivement une matière du système
     * @param id Identifiant numérique de la matière à supprimer
     */
    public void supprimerMatiere(int id) {
        matiereRepository.delete(id);
    }

    /**
     * Liste toutes les matières disponibles dans le système
     * @return Liste complète des matières enregistrées
     */
    public List<Matiere> listerMatieres() {
        return matiereRepository.findAll();
    }
}