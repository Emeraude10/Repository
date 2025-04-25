package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// Classe représentant une classe/option d'étudiants
public class Classe implements Identifiable {
    
    // Propriétés de la classe
    private int id;                 // Identifiant unique
    private String nom;              // Nom de la classe/option
    private List<Etudiant> etudiants; // Liste des étudiants inscrits

    // Constructeur pour initialiser une classe
    public Classe(int id, String nom) {
        this.id = id;
        this.nom = nom;
        this.etudiants = new ArrayList<>(); 
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

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }

    
    // Ajoute un étudiant à la classe
    public void ajouterEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
    }

    // Retire un étudiant de la classe
    public void retirerEtudiant(Etudiant etudiantARetirer) {
        this.etudiants.remove(etudiantARetirer);
    }

    
    // Compare les classes par leur ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Classe classe = (Classe) o;
        return id == classe.id;
    }

    // Génère un hashcode basé sur l'ID
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // Formatage de l'affichage
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Classe ou Option{id=").append(id)
          .append(", nom='").append(nom).append('\'')
          .append(", etudiants=[");

        // Ajoute les IDs des étudiants
        for (Etudiant e : etudiants) {
            sb.append(e.getId()).append(",");
        }

        // Enlève la dernière virgule si nécessaire
        if (!etudiants.isEmpty()) sb.deleteCharAt(sb.length() - 1);
        sb.append("]}");

        return sb.toString();
    }
}