package repository;

// Importations nécessaires
import model.Classe;
import model.Etudiant;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implémentation du repository pour la gestion des classes/options dans un fichier CSV
 */
public class ClasseRepository implements Repository<Classe, Integer> {

    // Configuration du fichier de stockage
    private static final String FICHIER_CSV = "classes.csv";
    private int prochainId = 1;
    private final EtudiantRepository etudiantRepository = new EtudiantRepository();

    /**
     * Constructeur initialisant la gestion des IDs
     */
    public ClasseRepository() {
        this.prochainId = obtenirProchainId();
    }

    /**
     * Calcule le prochain ID disponible en lisant le fichier
     * @return ID maximum existant + 1
     */
    private int obtenirProchainId() {
        int idMax = 0;
        try (BufferedReader lecteur = new BufferedReader(new FileReader(FICHIER_CSV))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                try {
                    int id = Integer.parseInt(ligne.split(",")[0].trim());
                    idMax = Math.max(idMax, id);
                } catch (NumberFormatException e) {
                    System.err.println("Ligne corrompue : " + ligne);
                }
            }
        } catch (FileNotFoundException e) {
            return 1; // Fichier inexistant => premier ID
        } catch (IOException e) {
            System.err.println("Erreur de lecture (obtenirProchainId) : " + e.getMessage());
            return 1;
        }
        return idMax + 1;
    }


    /**
     * Crée une nouvelle classe dans le fichier
     * @param entity Classe à créer
     */
    @Override
    public void create(Classe entity) {
        try {
            entity.setId(prochainId++);
            enregistrerClasseDansFichier(entity);
        } catch (IOException e) {
            System.err.println("Erreur lors de la création de la classe ou option : " + e.getMessage());
        }
    }

    /**
     * Récupère une classe par son ID
     * @param id ID de la classe
     * @return Classe trouvée ou null
     */
    @Override
    public Classe read(Integer id) {
        List<Classe> classes = chargerClassesDepuisFichier();
        return classes.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Met à jour une classe existante
     * @param entity Classe modifiée
     */
    @Override
    public void update(Classe entity) {
        List<Classe> classes = chargerClassesDepuisFichier();
        boolean misAJour = false;
        
        try (BufferedWriter ecrivain = new BufferedWriter(new FileWriter(FICHIER_CSV))) {
            for (Classe c : classes) {
                if (c.getId() == entity.getId()) {
                    ecrivain.write(classeToString(entity));
                    misAJour = true;
                } else {
                    ecrivain.write(classeToString(c));
                }
                ecrivain.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur de mise à jour : " + e.getMessage());
        }
        
        if (!misAJour) {
            System.err.println("Aucune classe ou option trouvée avec l'ID " + entity.getId());
        }
    }

    /**
     * Supprime une classe par son ID
     * @param id ID de la classe à supprimer
     */
    @Override
    public void delete(Integer id) {
        List<Classe> classes = chargerClassesDepuisFichier();
        
        try (BufferedWriter ecrivain = new BufferedWriter(new FileWriter(FICHIER_CSV))) {
            for (Classe c : classes) {
                if (c.getId() != id) {
                    ecrivain.write(classeToString(c));
                    ecrivain.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de suppression : " + e.getMessage());
        }
    }

    /**
     * Récupère toutes les classes
     * @return Liste complète des classes
     */
    @Override
    public List<Classe> findAll() {
        return chargerClassesDepuisFichier();
    }

    // Méthodes auxiliaires

    /**
     * Sauvegarde une classe dans le fichier
     * @param classe Classe à sauvegarder
     */
    private void enregistrerClasseDansFichier(Classe classe) throws IOException {
        try (BufferedWriter ecrivain = new BufferedWriter(new FileWriter(FICHIER_CSV, true))) {
            ecrivain.write(classeToString(classe));
            ecrivain.newLine();
        }
    }

    /**
     * Charge toutes les classes depuis le fichier
     * @return Liste des classes avec leurs étudiants
     */
    private List<Classe> chargerClassesDepuisFichier() {
        List<Classe> classes = new ArrayList<>();
        
        try (BufferedReader lecteur = new BufferedReader(new FileReader(FICHIER_CSV))) {
            String ligne;
            while ((ligne = lecteur.readLine()) != null) {
                if (ligne.trim().isEmpty()) continue;
                
                String[] donnees = ligne.split(",");
                if (donnees.length >= 2) {
                    try {
                        int id = Integer.parseInt(donnees[0].trim());
                        String nom = donnees[1].trim();
                        Classe classe = new Classe(id, nom);
                        
                        // Ajout des étudiants
                        for (int i = 2; i < donnees.length; i++) {
                            try {
                                int etudiantId = Integer.parseInt(donnees[i].trim());
                                Etudiant etudiant = etudiantRepository.read(etudiantId);
                                if (etudiant != null) {
                                    classe.ajouterEtudiant(etudiant);
                                }
                            } catch (NumberFormatException e) {
                                System.err.println("ID étudiant invalide dans la ligne : " + ligne);
                            }
                        }
                        classes.add(classe);
                    } catch (NumberFormatException e) {
                        System.err.println("Format invalide : " + ligne);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur de lecture (chargerClassesDepuisFichier) : " + e.getMessage());
        }
        return classes;
    }

    /**
     * Convertit une classe en format CSV
     * @param classe Classe à convertir
     * @return Chaîne CSV formatée
     */
    private String classeToString(Classe classe) {
        StringBuilder sb = new StringBuilder();
        sb.append(classe.getId()).append(",").append(classe.getNom());
        
        if (classe.getEtudiants() != null) {
            for (Etudiant etudiant : classe.getEtudiants()) {
                if (etudiant != null) {
                    sb.append(",").append(etudiant.getId());
                }
            }
        }
        return sb.toString();
    }
}