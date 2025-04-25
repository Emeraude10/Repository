package model;

/**
 * Interface marquant les entités possédant un identifiant unique
 */
public interface Identifiable {
    
    /**
     * Retourne l'identifiant unique de l'entité
     * @return l'ID numérique de l'entité
     */
    int getId();
}