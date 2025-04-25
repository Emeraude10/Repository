package model;

import java.util.Objects;

// Représente un enseignant avec ses informations personnelles
public class Enseignant implements Identifiable {
    
    // Propriétés de l'enseignant
    private int id;             // Identifiant unique
    private String nom;         // Nom de famille
    private String prenom;      // Prénom
    private String specialite;  // Domaine d'expertise

    // Constructeur pour initialiser un enseignant
    public Enseignant(int id, String nom, String prenom, String specialite) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.specialite = specialite;
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
    
    // Compare les enseignants par leur ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enseignant that = (Enseignant) o;
        return id == that.id;
    }

    // Génère un hashcode basé sur l'ID
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Formatage de l'affichage
    @Override
    public String toString() {
        return "Enseignant{" +
               "id=" + id +
               ", nom='" + nom + '\'' +
               ", prenom='" + prenom + '\'' +
               ", specialite='" + specialite + '\'' +
               '}';
    }
}