package model;

import java.util.Objects;

// Représente un étudiant avec ses informations personnelles et académiques
public class Etudiant {
    
    // Propriétés de l'étudiant
    private int id;         // Identifiant unique
    private String nom;      // Nom de famille
    private String prenom;   // Prénom
    private String niveau;   // Niveau d'études (ex: L1 ou M2)

    // Constructeur pour initialiser un étudiant
    public Etudiant(int id, String nom, String prenom, String niveau) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.niveau = niveau;
    }

    //======================= GETTERS/SETTERS =======================
    
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    
    // Formatage des informations de l'étudiant avec la méthode toString()
    @Override
    public String toString() {
        return "Etudiant{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", niveau='" + niveau + '\'' +
                '}';
    }

    // Comparer les étudiants par leur ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Même instance en mémoire
        if (o == null || getClass() != o.getClass()) return false; // Vérifie le type
        Etudiant etudiant = (Etudiant) o; // Cast sûr
        return Objects.equals(id, etudiant.id); // Compare les IDs
    }

    // Génère un hashcode basé sur l'ID
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}