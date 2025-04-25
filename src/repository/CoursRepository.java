package repository;

// Importations nécessaires pour les opérations de persistance et de modélisation
import model.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.*;

/**
 * Implémentation du repository pour la gestion des cours dans un fichier CSV
 */
public class CoursRepository implements Repository<Cours, Integer> {

    // Configuration du fichier CSV
    private static final String CSV_FILE = "cours.csv";
    
    // Formateur pour le traitement des heures
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_TIME;
    
    // Logger pour le suivi des erreurs
    private static final Logger LOGGER = Logger.getLogger(CoursRepository.class.getName());

    // Caches pour les entités liées (optimisation des performances)
    private final Map<Integer, Matiere> matiereMap = new HashMap<>();
    private final Map<Integer, Enseignant> enseignantMap = new HashMap<>();
    private final Map<Integer, Classe> classeMap = new HashMap<>();

    // Références aux repositories des entités associées
    private final MatiereRepository matiereRepository;
    private final EnseignantRepository enseignantRepository;
    private final ClasseRepository classeRepository;

    /**
     * Constructeur avec injection des dépendances
     * @param matiereRepository Repository pour les matières
     * @param enseignantRepository Repository pour les enseignants
     * @param classeRepository Repository pour les classes
     */
    public CoursRepository(MatiereRepository matiereRepository, 
                          EnseignantRepository enseignantRepository,
                          ClasseRepository classeRepository) {
        this.matiereRepository = matiereRepository;
        this.enseignantRepository = enseignantRepository;
        this.classeRepository = classeRepository;
        loadEntities(); // Chargement initial des données
    }

    /**
     * Charge en mémoire les entités liées depuis leurs repositories respectifs
     */
    private void loadEntities() {
        // Remplissage du cache des matières
        List<Matiere> matieres = matiereRepository.findAll();
        for (Matiere matiere : matieres) {
            matiereMap.put(matiere.getId(), matiere);
        }
        
        // Remplissage du cache des enseignants
        List<Enseignant> enseignants = enseignantRepository.findAll();
        for (Enseignant enseignant : enseignants) {
            enseignantMap.put(enseignant.getId(), enseignant);
        }
        
        // Remplissage du cache des classes
        List<Classe> classes = classeRepository.findAll();
        for (Classe classe : classes) {
            classeMap.put(classe.getId(), classe);
        }
    }

    
    /**
     * Crée un nouveau cours dans le fichier CSV
     * @param cours L'entité Cours à persister
     */
    @Override
    public void create(Cours cours) {
        saveCoursToCSV(cours, true); // Ajout en mode append
    }

    /**
     * Recherche un cours par son identifiant
     * @param id L'identifiant du cours
     * @return Le cours trouvé ou null
     */
    @Override
    public Cours read(Integer id) {
        List<Cours> coursList = findAll();
        return coursList.stream()
                       .filter(c -> c.getId().equals(id))
                       .findFirst()
                       .orElse(null);
    }

    /**
     * Met à jour un cours existant dans le fichier
     * @param cours Le cours modifié à persister
     */
    @Override
    public void update(Cours cours) {
        List<Cours> coursList = findAll();
        boolean updated = false;
        
        // Réécriture complète du fichier avec modification
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, false))) {
            for (Cours c : coursList) {
                if (c.getId().equals(cours.getId())) {
                    bw.write(formatCoursToCSV(cours)); // Écriture de la version mise à jour
                    updated = true;
                } else {
                    bw.write(formatCoursToCSV(c)); // Écriture des autres cours inchangés
                }
                bw.newLine();
            }
        } catch (IOException e) {
            logError("Erreur lors de la mise à jour du fichier CSV : ", e);
            throw new RepositoryException("Erreur lors de la mise à jour du fichier CSV", e);
        }
        if (!updated) {
            LOGGER.warning("Cours avec l'ID " + cours.getId() + " non trouvé pour la mise à jour.");
        }
    }

    /**
     * Supprime un cours du fichier par son ID
     * @param id L'identifiant du cours à supprimer
     */
    @Override
    public void delete(Integer id) {
        List<Cours> coursList = findAll();
        boolean deleted = false;
        
        // Réécriture du fichier en excluant le cours cible
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, false))) {
            for (Cours c : coursList) {
                if (!c.getId().equals(id)) { 
                    bw.write(formatCoursToCSV(c)); // Réécriture des autres cours
                    bw.newLine();
                } else {
                    deleted = true; // Marque la suppression
                }
            }
        } catch (IOException e) {
            logError("Erreur lors de la suppression dans le fichier CSV : ", e);
            throw new RepositoryException("Erreur lors de la suppression dans le fichier CSV", e);
        }
        if (!deleted) {
            LOGGER.warning("Cours avec l'ID " + id + " non trouvé pour la suppression.");
        }
    }

    /**
     * Retourne tous les cours du fichier
     * @return Liste complète des cours
     */
    @Override
    public List<Cours> findAll() {
        loadEntities(); // Actualisation des données liées
        return chargerCoursDepuisCSV(); // Chargement depuis le CSV
    }
    
    /**
     * Convertit un objet Cours en ligne CSV
     * @param cours Le cours à convertir
     * @return String au format CSV
     */
    private String formatCoursToCSV(Cours cours) {
        // Formatage des propriétés avec gestion des null
        String heureDebutStr = (cours.getHeureDebut() != null) ? cours.getHeureDebut().format(TIME_FORMATTER) : "";
        String heureFinStr = (cours.getHeureFin() != null) ? cours.getHeureFin().format(TIME_FORMATTER) : "";
        String jourDeLaSemaineStr = (cours.getJourDeLaSemaine() != null) ? cours.getJourDeLaSemaine().toString() : "";
        
        return String.format(
            "%d,%d,%d,%d,%s,%s,%s,%s,%s", 
            cours.getId(), 
            cours.getMatiere().getId(),
            cours.getEnseignant().getId(), 
            cours.getClasse().getId(), 
            jourDeLaSemaineStr, 
            heureDebutStr, 
            heureFinStr,
            cours.getSalle(), 
            cours.getNiveau() != null ? cours.getNiveau().toString() : ""
        );
    }

    /**
     * Sauvegarde un cours dans le fichier CSV
     * @param cours Le cours à sauvegarder
     * @param append Mode d'ajout (true = ajout en fin de fichier)
     */
    private void saveCoursToCSV(Cours cours, boolean append) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE, append))) {
            bw.write(formatCoursToCSV(cours)); // Écriture de la ligne
            bw.newLine(); // Saut de ligne
        } catch (IOException e) {
            logError("Erreur d'écriture CSV : ", e);
            throw new RepositoryException("Erreur d'écriture CSV", e);
        }
    }

    /**
     * Charge tous les cours depuis le fichier CSV
     * @return Liste des cours 
     */
    private List<Cours> chargerCoursDepuisCSV() {
        List<Cours> coursList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            boolean isFirstLine = true; // Gestion de l'en-tête
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Ignore la première ligne
                    continue;
                }
                Cours cours = parseCoursFromCSV(line); // Conversion ligne -> objet
                if (cours != null) {
                    coursList.add(cours); // Ajout à la liste
                }
            }
        } catch (IOException e) {
            logError("Erreur lors de la lecture du fichier CSV : ", e);
            throw new RepositoryException("Erreur lors de la lecture du fichier CSV", e);
        }
        return coursList;
    }

    /**
     * Convertit une ligne CSV en objet Cours
     * @param line Ligne CSV à parser
     * @return Objet Cours ou null si erreur
     */
    private Cours parseCoursFromCSV(String line) {
        String[] data = line.split(",");
        if (data.length != 9) { // Vérification du nombre de colonnes
            logError("Ligne invalide dans le fichier CSV : ", new IllegalArgumentException("Invalid line format: " + line));
            return null;
        }
        try {
            // Extraction et conversion des données
            int id = parseInteger(data[0].trim(), "ID", line);
            int matiereId = parseInteger(data[1].trim(), "Matiere ID", line);
            int enseignantId = parseInteger(data[2].trim(), "Enseignant ID", line);
            int classeId = parseInteger(data[3].trim(), "Classe ID", line);
            
            // Conversion des valeurs complexes
            DayOfWeek jourDeLaSemaine = parseDayOfWeek(data[4].trim(), "Jour de la semaine", line);
            LocalTime heureDebut = parseLocalTime(data[5].trim(), TIME_FORMATTER, "Heure Debut", line);
            LocalTime heureFin = parseLocalTime(data[6].trim(), TIME_FORMATTER, "Heure Fin", line);
            
            // Récupération des entités liées
            Matiere matiere = matiereMap.get(matiereId);
            Enseignant enseignant = enseignantMap.get(enseignantId);
            Classe classe = classeMap.get(classeId);

            // Validation des relations
            if (matiere == null || enseignant == null || classe == null) {
                logError("Entité associée non trouvée dans le fichier CSV : ", new IllegalArgumentException("Invalid data: " + line));
                return null;
            }

            // Conversion du niveau
            String niveauStr = data[8].trim();
            Cours.Niveau niveau = Cours.Niveau.valueOf(niveauStr.toUpperCase());

            return new Cours(
                    id, 
                    matiere, 
                    enseignant, 
                    classe, 
                    heureDebut, 
                    heureFin, 
                    jourDeLaSemaine, 
                    data[7].trim(), // Salle
                    niveau
                );

        } catch (RepositoryException e) {
            logError("Erreur lors de l'analyse de la ligne CSV : ", e);
            return null;
        } catch (IllegalArgumentException e) {
            logError("Niveau invalide dans le fichier CSV : ", e);
            return null;
        }
    }

    
    /**
     * Convertit une chaîne en entier avec gestion d'erreur
     * @throws RepositoryException Si conversion impossible
     */
    private int parseInteger(String value, String fieldName, String line) throws RepositoryException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logError("Erreur de format pour " + fieldName + " dans le fichier CSV : ", e);
            throw new RepositoryException("Erreur de format pour " + fieldName, e);
        }
    }

    /**
     * Convertit une chaîne en LocalTime avec gestion d'erreur
     * @throws RepositoryException Si conversion impossible
     */
    private LocalTime parseLocalTime(String value, DateTimeFormatter formatter, String fieldName, String line) throws RepositoryException {
        if (value.isEmpty()) return null;
        try {
            return LocalTime.parse(value, formatter);
        } catch (DateTimeException e) {
            String errorMsg = String.format("Erreur de temps pour %s ('%s') ligne : %s",
                                          fieldName, value, line);
            logError(errorMsg, e);
            throw new RepositoryException(errorMsg, e);
        }
    }

    /**
     * Convertit une chaîne en DayOfWeek avec gestion d'erreur
     * @throws RepositoryException Si conversion impossible
     */
    private DayOfWeek parseDayOfWeek(String value, String fieldName, String line) throws RepositoryException {
        if (value.isEmpty()) return null;
        try {
            return DayOfWeek.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            logError("Jour invalide dans le fichier CSV : ", e);
            throw new RepositoryException("Erreur de format pour " + fieldName, e);
        }
    }


    
    /**
     * Journalise les erreurs avec niveau SEVERE
     */
    private void logError(String message, Exception e) {
        LOGGER.log(Level.SEVERE, message + e.getMessage(), e);
    }

    /**
     * Exception personnalisée pour les erreurs du repository
     */
    public static class RepositoryException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public RepositoryException(String message, Throwable cause) {
            super(message, cause);
        }

        public RepositoryException(String message) {
            super(message);
        }
    }
}