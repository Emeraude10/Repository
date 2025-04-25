package model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// Représente un cours avec ses informations horaires et pédagogiques (Matière, enseignant)
public class Cours {
    // Attributs de la classe
    private Integer id;
    private Matiere matiere;         // Matière enseignée
    private Enseignant enseignant;   // Enseignant responsable
    private Classe classe;           // Classe concernée
    private LocalTime heureDebut;    // Heure de début du cours
    private LocalTime heureFin;      // Heure de fin du cours
    private DayOfWeek jourDeLaSemaine; // Jour de la semaine
    private String salle;            // Salle de classe
    private Niveau niveau;           // Niveau académique

    // Enumération des niveaux académiques disponibles
    public enum Niveau {
        L1("Licence 1"),
        L2("Licence 2"),
        L3("Licence 3"),
        M1("Master 1"),
        M2("Master 2");

        private final String nomComplet; // Nom complet du niveau

        // Constructeur de l'énumération
        Niveau(String nomComplet) {
            this.nomComplet = nomComplet;
        }

        // Retourne le nom complet du niveau
        public String getNomComplet() {
            return nomComplet;
        }

        // Retourne la version abrégée du niveau
        @Override
        public String toString() {
            return name();
        }
    }

    // Constructeur principal
    public Cours(int id, Matiere matiere, Enseignant enseignant, Classe classe, 
            LocalTime heureDebut, LocalTime heureFin, DayOfWeek jourDeLaSemaine, 
            String salle, Niveau niveau) {
        this.id = id;
        this.matiere = matiere;
        this.enseignant = enseignant;
        this.classe = classe;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.jourDeLaSemaine = jourDeLaSemaine;
        this.salle = salle;
        this.niveau = niveau;
    }
    
    // Formatage des heures (HH:mm)
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    
    //======================= GETTERS/SETTERS =======================
    
    public Integer getId() { 
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Enseignant getEnseignant() {
        return enseignant;
    }

    public void setEnseignant(Enseignant enseignant) {
        this.enseignant = enseignant;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public DayOfWeek getJourDeLaSemaine() {
        return jourDeLaSemaine;
    }

    public void setJourDeLaSemaine(DayOfWeek jourDeLaSemaine) {
        this.jourDeLaSemaine = jourDeLaSemaine;
    }

    public String getSalle() {
        return salle;
    }

    public void setSalle(String salle) {
        this.salle = salle;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    // Affichage formaté des informations du cours
    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", matiere=" + matiere +
                ", enseignant=" + enseignant +
                ", classe=" + classe +
                ", jourDeLaSemaine=" + jourDeLaSemaine +
                ", heureDebut=" + heureDebut.format(formatter) +
                ", heureFin=" + heureFin.format(formatter) +
                ", salle='" + salle + '\'' +
                ", niveau=" + niveau +
                '}';
    }
}