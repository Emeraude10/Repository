package repository;

import model.Enseignant;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository pour la gestion des enseignants dans un fichier CSV
 * Format du fichier : id,nom,prenom,specialite
 */
public class EnseignantRepository implements Repository<Enseignant, Integer> {

    // Fichier CSV pour le stockage des données
    private static final String CSV_FILE = "enseignants.csv";

    /**
     * Crée un nouvel enseignant dans le fichier CSV
     * @param enseignant L'enseignant à persister
     */
    @Override
    public void create(Enseignant enseignant) {
        saveEnseignantToCSV(enseignant);
    }

    /**
     * Recherche un enseignant par son ID
     * @param id L'identifiant de l'enseignant
     * @return L'enseignant trouvé ou null
     */
    @Override
    public Enseignant read(Integer id) {
        List<Enseignant> enseignants = findAll();
        for (Enseignant enseignant : enseignants) {
            if (enseignant.getId() == id) {
                return enseignant;
            }
        }
        return null;
    }

    /**
     * Met à jour un enseignant dans le fichier CSV
     * @param enseignant L'enseignant modifié à persister
     */
    @Override
    public void update(Enseignant enseignant) {
        List<Enseignant> enseignants = findAll();
        boolean updated = false;
        
        // Réécriture complète du fichier avec modification
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, false))) {
            for (Enseignant e : enseignants) {
                if (e.getId() == enseignant.getId()) { 
                    // Écriture de la version mise à jour
                    bw.write(String.format("%d,%s,%s,%s\n",
                            enseignant.getId(),
                            enseignant.getNom(),
                            enseignant.getPrenom(),
                            enseignant.getSpecialite()));
                    updated = true;
                } else {
                    // Réécriture des autres entrées inchangées
                    bw.write(String.format("%d,%s,%s,%s\n",
                            e.getId(),
                            e.getNom(),
                            e.getPrenom(),
                            e.getSpecialite()));
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la mise à jour du fichier CSV : " + e.getMessage());
            return;
        }
        if (!updated) {
            System.err.println("Enseignant avec l'ID " + enseignant.getId() + " non trouvé pour la mise à jour.");
        }
    }

    /**
     * Supprime un enseignant par son ID
     * @param id L'identifiant de l'enseignant à supprimer
     */
    @Override
    public void delete(Integer id) {
        List<Enseignant> enseignants = findAll();
        boolean deleted = false;
        
        // Réécriture du fichier en excluant l'entrée cible
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, false))) {
            for (Enseignant e : enseignants) {
                if (e.getId() != id) {
                    bw.write(String.format("%d,%s,%s,%s\n",
                            e.getId(),
                            e.getNom(),
                            e.getPrenom(),
                            e.getSpecialite()));
                } else {
                    deleted = true; // Marqueur de suppression
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la suppression dans le fichier CSV : " + e.getMessage());
            return;
        }
        if (!deleted) {
            System.err.println("Enseignant avec l'ID " + id + " non trouvé pour la suppression.");
        }
    }

    /**
     * Retourne tous les enseignants du fichier
     * @return Liste complète des enseignants
     */
    @Override
    public List<Enseignant> findAll() {
        return chargerEnseignantsDepuisCSV();
    }

    /**
     * Sauvegarde un enseignant dans le fichier CSV (mode append)
     * @param enseignant L'enseignant à sauvegarder
     */
    private void saveEnseignantToCSV(Enseignant enseignant) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, true))) {
            bw.write(String.format("%d,%s,%s,%s\n",
                    enseignant.getId(),
                    enseignant.getNom(),
                    enseignant.getPrenom(),
                    enseignant.getSpecialite()));
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier CSV : " + e.getMessage());
        }
    }

    /**
     * Charge tous les enseignants depuis le fichier CSV
     * @return Liste des enseignants parsés
     */
    private List<Enseignant> chargerEnseignantsDepuisCSV() {
        List<Enseignant> enseignants = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) { // Validation du format
                    try {
                        // Conversion des données CSV en objet
                        int id = Integer.parseInt(data[0].trim());
                        String nom = data[1].trim();
                        String prenom = data[2].trim();
                        String specialite = data[3].trim();
                        enseignants.add(new Enseignant(id, nom, prenom, specialite));
                    } catch (NumberFormatException e) {
                        System.err.println("Erreur de format numérique dans le fichier CSV : " + line);
                    }
                } else {
                    System.err.println("Ligne invalide dans le fichier CSV : " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
        }
        return enseignants;
    }
}