package repository;

import model.Etudiant;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository pour la gestion des étudiants dans un fichier CSV
 * Format du fichier : id,nom,prenom,niveau
 */
public class EtudiantRepository implements Repository<Etudiant, Integer> {

    // Configuration du fichier de stockage
    private static final String FICHIER_CSV = "etudiants.csv";
    
    // Gestion des IDs auto-incrémentés
    private int prochainId = 1;

    /**
     * Constructeur initialisant le prochain ID disponible
     */
    public EtudiantRepository() {
        this.prochainId = obtenirProchainId();
    }

    /**
     * Détermine le prochain ID en lisant le fichier CSV
     * @return ID maximum existant + 1
     */
    private int obtenirProchainId() {
        int idMax = 0;
        try (java.io.BufferedReader lecteur = new java.io.BufferedReader(new java.io.FileReader(FICHIER_CSV))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                try {
                    // Extraction de l'ID depuis la première colonne
                    int id = Integer.parseInt(ligne.split(",")[0].trim());
                    idMax = Math.max(idMax, id);
                } catch (NumberFormatException e) {
                    System.err.println("Ligne corrompue : " + ligne);
                }
            }
        } catch (java.io.IOException e) {
            System.err.println("Erreur de lecture : " + e.getMessage());
        }
        return idMax + 1;
    }

    // Opérations CRUD

    /**
     * Crée un nouvel étudiant dans le fichier CSV
     * @param etudiant L'étudiant à persister
     */
    @Override
    public void create(Etudiant etudiant) {
        try (java.io.BufferedWriter ecrivain = new java.io.BufferedWriter(new java.io.FileWriter(FICHIER_CSV, true))) {
            etudiant.setId(prochainId++); // Attribution d'un nouvel ID
            ecrivain.write(etudiantToString(etudiant));
            ecrivain.newLine();
        } catch (java.io.IOException e) {
            System.err.println("Erreur lors de la création de l'étudiant : " + e.getMessage());
        }
    }

    /**
     * Recherche un étudiant par son ID
     * @param id L'identifiant de l'étudiant
     * @return L'étudiant trouvé ou null
     */
    @Override
    public Etudiant read(Integer id) {
        List<Etudiant> etudiants = chargerEtudiantsDepuisFichier();
        for (Etudiant etudiant : etudiants) {
            if (etudiant.getId() == id) {
                return etudiant;
            }
        }
        return null;
    }

    /**
     * Met à jour les informations d'un étudiant
     * @param etudiant L'étudiant modifié à persister
     */
    @Override
    public void update(Etudiant etudiant) {
        List<Etudiant> etudiants = chargerEtudiantsDepuisFichier();
        boolean misAJour = false;

        // Réécriture complète du fichier avec modification
        try (java.io.BufferedWriter ecrivain = new java.io.BufferedWriter(new java.io.FileWriter(FICHIER_CSV))) {
            for (Etudiant e : etudiants) {
                if (e.getId() == etudiant.getId()) {
                    ecrivain.write(etudiantToString(etudiant)); // Écriture de la version mise à jour
                    misAJour = true;
                } else {
                    ecrivain.write(etudiantToString(e)); // Réécriture des autres étudiants
                }
                ecrivain.newLine();
            }
        } catch (java.io.IOException e) {
            System.err.println("Erreur lors de la mise à jour de l'étudiant : " + e.getMessage());
        }

        if (!misAJour) {
            System.err.println("Aucun étudiant trouvé avec l'ID " + etudiant.getId());
        }
    }

    /**
     * Supprime un étudiant par son ID
     * @param id L'identifiant de l'étudiant à supprimer
     */
    @Override
    public void delete(Integer id) {
        List<Etudiant> etudiants = chargerEtudiantsDepuisFichier();
        boolean supprime = false;

        // Réécriture du fichier en excluant l'étudiant cible
        try (java.io.BufferedWriter ecrivain = new java.io.BufferedWriter(new java.io.FileWriter(FICHIER_CSV))) {
            for (Etudiant e : etudiants) {
                if (e.getId() == id) {
                    supprime = true; // Marque la suppression
                } else {
                    ecrivain.write(etudiantToString(e)); // Réécriture des autres étudiants
                    ecrivain.newLine();
                }
            }
        } catch (java.io.IOException e) {
            System.err.println("Erreur lors de la suppression de l'étudiant : " + e.getMessage());
        }

        if (!supprime) {
            System.err.println("Aucun étudiant trouvé avec l'ID " + id);
        }
    }

    /**
     * Retourne tous les étudiants du fichier
     * @return Liste complète des étudiants
     */
    @Override
    public List<Etudiant> findAll() {
        return chargerEtudiantsDepuisFichier();
    }


    /**
     * Charge les étudiants depuis le fichier CSV
     * @return Liste des étudiants parsés
     */
    private List<Etudiant> chargerEtudiantsDepuisFichier() {
        List<Etudiant> etudiants = new ArrayList<>();
        try (java.io.BufferedReader lecteur = new java.io.BufferedReader(new java.io.FileReader(FICHIER_CSV))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                String[] donnees = ligne.split(",");
                if (donnees.length == 4) { // Vérification du format
                    try {
                        // Conversion des données CSV en objet Etudiant
                        int id = Integer.parseInt(donnees[0].trim());
                        String nom = donnees[1].trim();
                        String prenom = donnees[2].trim();
                        String niveau = donnees[3].trim();
                        etudiants.add(new Etudiant(id, nom, prenom, niveau));
                    } catch (NumberFormatException e) {
                        System.err.println("Format invalide : " + ligne);
                    }
                } else {
                    System.err.println("Ligne invalide : " + ligne);
                }
            }
        } catch (java.io.IOException e) {
            System.err.println("Erreur de lecture : " + e.getMessage());
        }
        return etudiants;
    }

    /**
     * Convertit un étudiant en ligne CSV
     * @param etudiant L'étudiant à convertir
     * @return Chaîne formatée pour le CSV
     */
    private String etudiantToString(Etudiant etudiant) {
        return String.join(",",
            String.valueOf(etudiant.getId()),
            etudiant.getNom(),
            etudiant.getPrenom(),
            etudiant.getNiveau()
        );
    }
}