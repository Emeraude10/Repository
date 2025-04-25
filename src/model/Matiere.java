package model;

// Représente une matière enseignée avec ses caractéristiques ou description
public class Matiere implements Identifiable {
    
    // Propriétés de la matière
    private int id;             // Identifiant unique
    private String nom;         // Nom de la matière
    private String description; // Description détaillée

    // Constructeur pour initialiser une matière
    public Matiere(int id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }

    //======================= GETTERS/SETTERS =======================
    
    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    // Formatage de l'affichage des informations
    @Override
    public String toString() {
        return "Matiere{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}