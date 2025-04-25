package model;

import java.util.ArrayList;
import java.util.List;

// Représente un emploi du temps contenant une liste de cours
public class EmploiDuTemps {

    // Liste des cours programmés
    private List<Cours> cours;

    // Constructeur initialisant une liste vide de cours
    public EmploiDuTemps() {
        this.cours = new ArrayList<>();
    }

    // Retourne la liste complète des cours
    public List<Cours> getCours() {
        return cours;
    }

    // Ajoute un nouveau cours à l'emploi du temps
    public void ajouterCours(Cours cours) {
        this.cours.add(cours);
    }

    // Supprime un cours existant par son ID
    public void supprimerCours(Cours coursASupprimer) {
        this.cours.removeIf(cours -> cours.getId().equals(coursASupprimer.getId()));
    }

    // Met à jour un cours existant en le remplaçant par une nouvelle version
    public void mettreAJourCours(Cours nouveauCours) {
        // Parcours de la liste pour trouver le cours correspondant
        for (int i = 0; i < cours.size(); i++) {
            if (cours.get(i).getId().equals(nouveauCours.getId())) {
                cours.set(i, nouveauCours); // Remplacement du cours
                return; // Sortie après la mise à jour (ID unique supposé)
            }
        }
        // Message d'avertissement si le cours n'est pas trouvé
        System.out.println("Avertissement : Cours avec l'ID " + nouveauCours.getId() 
            + " non trouvé dans l'emploi du temps pour la mise à jour.");
    }
}