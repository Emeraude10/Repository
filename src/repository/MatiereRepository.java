package repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import model.Matiere;

/**
 * Repository pour la gestion des matières dans un fichier CSV
 * Format du fichier : id,nom,description
 */
public class MatiereRepository implements Repository<Matiere, Integer> {

    // Fichier CSV de stockage des matières
    private static final String FICHIER_MATIERES = "matieres.csv";

    
    /**
     * Crée une nouvelle matière dans le fichier CSV
     * @param entity La matière à créer
     */
    @Override
    public void create(Matiere entity) {
        ajouterMatiere(entity);
    }

    /**
     * Recherche une matière par son ID
     * @param id Identifiant de la matière
     * @return La matière trouvée ou null
     */
    @Override
    public Matiere read(Integer id) {
        return getMatiereParId(id);
    }

    /**
     * Met à jour une matière existante
     * @param entity La matière modifiée
     */
    @Override
    public void update(Matiere entity) {
        mettreAJourMatiere(entity);
    }

    /**
     * Supprime une matière par son ID
     * @param id Identifiant de la matière à supprimer
     */
    @Override
    public void delete(Integer id) {
        supprimerMatiere(id);
    }

    /**
     * Retourne toutes les matières
     * @return Liste complète des matières
     */
    @Override
    public List<Matiere> findAll() {
        return getAllMatieres();
    }

    
    /**
     * Récupère toutes les matières depuis le fichier CSV
     * @return Liste des matières parsées
     */
    public List<Matiere> getAllMatieres() {
        List<Matiere> matieres = new ArrayList<>();
        try (BufferedReader lecteur = new BufferedReader(new FileReader(FICHIER_MATIERES))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                String[] valeurs = ligne.split(",");
                if (valeurs.length == 3) { // Validation du format
                    try {
                        // Conversion des données CSV en objet Matiere
                        int id = Integer.parseInt(valeurs[0].trim());
                        String nom = valeurs[1].trim();
                        String description = valeurs[2].trim();
                        matieres.add(new Matiere(id, nom, description));
                    } catch (NumberFormatException e) {
                        System.err.println("Erreur de format dans la ligne CSV : " + ligne);
                    }
                } else {
                    System.err.println("Ligne CSV invalide : " + ligne);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier " + FICHIER_MATIERES + " : " + e.getMessage());
        }
        return matieres;
    }

    /**
     * Ajoute une matière dans le fichier CSV (mode append)
     * @param matiere La matière à ajouter
     */
    public void ajouterMatiere(Matiere matiere) {
        try (BufferedWriter ecrivain = new BufferedWriter(new FileWriter(FICHIER_MATIERES, true))) {
            ecrivain.write(String.format("%d,%s,%s\n", 
                matiere.getId(), 
                matiere.getNom(), 
                matiere.getDescription()));
        } catch (IOException e) {
            System.err.println("Erreur d'écriture dans le fichier " + FICHIER_MATIERES + " : " + e.getMessage());
        }
    }

    /**
     * Recherche une matière par son ID
     * @param id Identifiant à rechercher
     * @return Matière correspondante ou null
     */
    public Matiere getMatiereParId(int id) {
        List<Matiere> toutesLesMatieres = getAllMatieres();
        for (Matiere matiere : toutesLesMatieres) {
            if (matiere.getId() == id) {
                return matiere;
            }
        }
        return null;
    }

    /**
     * Met à jour une matière en réécrivant tout le fichier
     * @param matiere La matière mise à jour
     */
    public void mettreAJourMatiere(Matiere matiere) {
        List<Matiere> toutesLesMatieres = getAllMatieres();
        try (BufferedWriter ecrivain = new BufferedWriter(new FileWriter(FICHIER_MATIERES, false))) {
            for (Matiere m : toutesLesMatieres) {
                if (m.getId() == matiere.getId()) {
                    // Écrit la version mise à jour
                    ecrivain.write(String.format("%d,%s,%s\n", 
                        matiere.getId(), 
                        matiere.getNom(), 
                        matiere.getDescription()));
                } else {
                    // Réécrit les autres matières inchangées
                    ecrivain.write(String.format("%d,%s,%s\n", 
                        m.getId(), 
                        m.getNom(), 
                        m.getDescription()));
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de mise à jour du fichier " + FICHIER_MATIERES + " : " + e.getMessage());
        }
    }

    /**
     * Supprime une matière en réécrivant le fichier sans l'entrée
     * @param id Identifiant de la matière à supprimer
     */
    public void supprimerMatiere(int id) {
        List<Matiere> toutesLesMatieres = getAllMatieres();
        try (BufferedWriter ecrivain = new BufferedWriter(new FileWriter(FICHIER_MATIERES, false))) {
            for (Matiere matiere : toutesLesMatieres) {
                if (matiere.getId() != id) {
                    ecrivain.write(String.format("%d,%s,%s\n", 
                        matiere.getId(), 
                        matiere.getNom(), 
                        matiere.getDescription()));
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de suppression dans le fichier " + FICHIER_MATIERES + " : " + e.getMessage());
        }
    }
}