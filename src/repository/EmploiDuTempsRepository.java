package repository;

import model.EmploiDuTemps;
import model.Cours;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmploiDuTempsRepository {

    private final String FICHIER_COURS = "cours.csv"; // Nom du fichier pour les cours
    public EmploiDuTempsRepository(MatiereRepository matiereRepository, EnseignantRepository enseignantRepository, ClasseRepository classeRepository) {
    }

 // Implémentation de la logique pour lire les fichiers CSV en retournant un nouvel emploi du temps vide.
    public EmploiDuTemps chargerEmploiDuTemps() {
        return new EmploiDuTemps();
    }

    public void sauvegarderEmploiDuTemps(EmploiDuTemps emploiDuTemps) {
        sauvegarderCours(emploiDuTemps.getCours());
        System.out.println("Emploi du temps sauvegardé dans les fichiers CSV.");
    }

    private void sauvegarderCours(List<Cours> cours) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        try (FileWriter writer = new FileWriter(FICHIER_COURS)) {
            writer.write("ID,MatiereID,EnseignantID,ClasseID,Jour,HeureDebut,HeureFin,Salle,Niveau\n");
            for (Cours coursItem : cours) {
                writer.write(String.format("%d,%d,%d,%d,%s,%s,%s,%s,%s\n",
                        coursItem.getId(),
                        coursItem.getMatiere().getId(),
                        coursItem.getEnseignant().getId(),
                        coursItem.getClasse().getId(),
                        coursItem.getJourDeLaSemaine(),
                        coursItem.getHeureDebut() != null ? coursItem.getHeureDebut().format(timeFormatter) : "",
                        coursItem.getHeureFin() != null ? coursItem.getHeureFin().format(timeFormatter) : "",
                        coursItem.getSalle(),
                        coursItem.getNiveau()));
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des cours dans le fichier CSV : " + e.getMessage());
        }
    }
}