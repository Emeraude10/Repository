package service;

import model.Classe;
import model.Etudiant;
import repository.ClasseRepository;
import repository.Repository;
import java.util.List;

/**
 * Service pour la gestion des classes/options et leurs étudiants
 */
public class ClasseService {

    // Dépendances
    private final Repository<Classe, Integer> classeRepository = new ClasseRepository();
    private final EtudiantService etudiantService = new EtudiantService();

    /**
     * Crée une nouvelle classe/option
     * @param nom Le nom de la classe/option (ex: "Maths Expertes")
     * Note : L'ID est généré automatiquement par le repository
     */
    public void ajouterClasse(String nom) {
        Classe classe = new Classe(0, nom);
        classeRepository.create(classe);
    }

    /**
     * Récupère une classe par son ID
     * @param id L'identifiant de la classe
     * @return La classe trouvée ou null
     */
    public Classe obtenirClasse(int id) {
        return classeRepository.read(id);
    }

    /**
     * Met à jour les informations d'une classe
     * @param classe La classe modifiée à persister
     */
    public void modifierClasse(Classe classe) {
        classeRepository.update(classe);
    }

    /**
     * Supprime une classe par son ID
     * @param id L'identifiant de la classe à supprimer
     */
    public void supprimerClasse(int id) {
        classeRepository.delete(id);
    }

    /**
     * Liste toutes les classes existantes
     * @return Liste complète des classes/options
     */
    public List<Classe> listerClasses() {
        return classeRepository.findAll();
    }

    /**
     * Ajoute un étudiant à une classe
     * @param classeId ID de la classe cible
     * @param etudiantId ID de l'étudiant à ajouter
     */
    public void ajouterEtudiantAClasse(int classeId, int etudiantId) {
        Classe classe = obtenirClasse(classeId);
        Etudiant etudiant = etudiantService.obtenirEtudiant(etudiantId);
        
        // Validation des entités avant modification
        if (classe != null && etudiant != null) {
            classe.ajouterEtudiant(etudiant);
            modifierClasse(classe);
        }
    }

    /**
     * Retire un étudiant d'une classe
     * @param classeId ID de la classe concernée
     * @param etudiantId ID de l'étudiant à retirer
     */
    public void retirerEtudiantDeClasse(int classeId, int etudiantId) {
        Classe classe = obtenirClasse(classeId);
        Etudiant etudiant = etudiantService.obtenirEtudiant(etudiantId);
        
        // Vérification de l'existence des entités
        if (classe != null && etudiant != null) {
            classe.retirerEtudiant(etudiant);
            modifierClasse(classe);
        }
    }
}