package service;

import model.Etudiant;
import repository.EtudiantRepository;
import java.util.List;

/**
 * Service de gestion des étudiants.
 * Fournit les opérations CRUD (Création, Lecture, Mise à jour, Suppression) pour les étudiants
 * en interagissant avec le repository dédié.
 */
public class EtudiantService {

    // Repository pour la persistance des données des étudiants
    private final EtudiantRepository etudiantRepository = new EtudiantRepository();

    /**
     * Constructeur avec injection de dépendance optionnelle pour le service des classes.
     * @param classeService Service de gestion des classes
     */
    public EtudiantService(ClasseService classeService) {
    }

    /**
     * Constructeur par défaut sans dépendances obligatoires
     */
    public EtudiantService() {
    }

    /**
     * Ajoute un nouvel étudiant dans le système
     * @param etudiant L'objet étudiant à créer
     */
    public void ajouterEtudiant(Etudiant etudiant) {
        etudiantRepository.create(etudiant);
    }

    /**
     * Récupère un étudiant par son identifiant
     * @param id Identifiant unique de l'étudiant
     * @return L'étudiant correspondant ou null si non trouvé
     */
    public Etudiant obtenirEtudiant(Integer id) {
        return etudiantRepository.read(id);
    }

    /**
     * Met à jour les informations d'un étudiant existant
     * @param etudiant L'objet étudiant modifié avec les nouvelles valeurs
     */
    public void modifierEtudiant(Etudiant etudiant) {
        etudiantRepository.update(etudiant);
    }

    /**
     * Supprime un étudiant du système
     * @param id Identifiant unique de l'étudiant à supprimer
     */
    public void supprimerEtudiant(Integer id) {
        etudiantRepository.delete(id);
    }

    /**
     * Liste tous les étudiants enregistrés dans le système
     * @return Liste complète des étudiants
     */
    public List<Etudiant> listerEtudiants() {
        return etudiantRepository.findAll();
    }
}