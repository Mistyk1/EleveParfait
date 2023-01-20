import extensions.CSVFile;
import extensions.File;

class EleveParfait extends Program {
  final String CHEMIN_LISTE_SAUVEGARDE = "../ressources/sauvegardes/liste-sauvegardes.csv";
  final String CHEMIN_DOSSIER_ASCII_CHARGEMENT = "../ressources/global/ascii-art-chargement";
  final String CHEMIN_DOSSIER_ASCII_QUITTER = "../ressources/global/ascii-art-quitter";
  final int DELAI_CHARGEMENT = 15;
  final int HAUTEUR_ZONE_PRINCIPALE = 21;
  final int LARGEUR_ZONE_PRINCIPALE = 61;
  final int HAUTEUR_ZONE_REPONSE = 3;
  final int LARGEUR_ZONE_REPONSE = 61;
  final int HAUTEUR_ZONE_INFO = 4;
  final int LARGEUR_ZONE_INFO = 61;
  final char CONTOUR_ZONE_PRINCIPALE = '#';
  final char CONTOUR_ZONE_REPONSE = '*';
  ZoneDeTexte zonePrincipale = newZoneDeTexte(1, 1, LARGEUR_ZONE_PRINCIPALE, HAUTEUR_ZONE_PRINCIPALE);
  ZoneDeTexte zoneReponse = newZoneDeTexte(HAUTEUR_ZONE_PRINCIPALE + 4, 1, LARGEUR_ZONE_REPONSE, HAUTEUR_ZONE_REPONSE);
  ZoneDeTexte zoneInfo = newZoneDeTexte(HAUTEUR_ZONE_PRINCIPALE + 1, 1, LARGEUR_ZONE_INFO, HAUTEUR_ZONE_INFO);
  ZoneDeTexte[] listeZones = new ZoneDeTexte[]{zonePrincipale, zoneReponse, zoneInfo};
  String[] contenuZonePrincipale;

  ///////////////////////////////////////////
  //Fonctions relatives au type Difficulte//
  /////////////////////////////////////////

  Difficulte intToDifficulte(int difficulte) { //Permet de convertir un entier en type Diffculte
    Difficulte result = null;
    if (difficulte == 0) {
      result = Difficulte.FACILE;
    } else if (difficulte == 1) {
      result = Difficulte.MOYENNE;
    } else if (difficulte == 2) {
      result = Difficulte.DIFFICILE;
    }
    return result;
  }

  int difficulteToInt(Difficulte difficulte) { //Permet de convertir un type Diffculte en int
    int result = -1;
    if (difficulte == Difficulte.FACILE) {
      result = 0;
    } else if (difficulte == Difficulte.MOYENNE) {
      result = 1;
    } else if (difficulte == Difficulte.DIFFICILE) {
      result = 2;
    }
    return result;
  }




  ////////////////////////////////////////
  //Fonctions relatives au type Matiere//
  //////////////////////////////////////

  Matiere intToMatiere(int matiere) { //Permet de convertir un entier en type matière
    Matiere result = null;
    if (matiere == 0) {
      result = Matiere.FRANCAIS;
    } else if (matiere == 1) {
      result = Matiere.HISTOIRE;
    } else if (matiere == 2) {
      result = Matiere.GEOGRAPHIE;
    } else if (matiere == 3) {
      result = Matiere.MATHEMATIQUES;
    } else if (matiere == 4) {
      result = Matiere.CULTUREGENERALE;
    } else if (matiere == 5) {
      result = Matiere.ANGLAIS;
    } else if (matiere == 6) {
      result = Matiere.INFORMATIQUE;
    }
    return result;
  }

  int matiereToInt(Matiere matiere) { //Permet de convertir un type matière en int
    int result = -1;
    if (matiere == Matiere.FRANCAIS) {
      result = 0;
    } else if (matiere == Matiere.HISTOIRE) {
      result = 1;
    } else if (matiere == Matiere.GEOGRAPHIE) {
      result = 2;
    } else if (matiere == Matiere.MATHEMATIQUES) {
      result = 3;
    } else if (matiere == Matiere.CULTUREGENERALE) {
      result = 4;
    } else if (matiere == Matiere.ANGLAIS) {
      result = 5;
    } else if (matiere == Matiere.INFORMATIQUE) {
      result = 6;
    }
    return result;
  }




  ///////////////////////////////////////
  //Fonctions relatives au type Joueur//
  /////////////////////////////////////

  Joueur newJoueur(int id, String nom, String prenom, int xpPopularite, int moyenneGenerale) { //Création d'un objet Joueur
    Joueur j = new Joueur();
    j.id = id;
    j.nom = nom;
    j.prenom = prenom;
    j.xpPopularite = xpPopularite;
    j.moyenneGenerale = moyenneGenerale;
    return j;
  }

  Joueur chargerJoueur(int id){ //Cette fonction permet de charger le joueur principal d'une partie
    CSVFile joueurs = loadCSV("../ressources/sauvegardes/joueurs-" + id + ".csv");
    String nom = getCell(joueurs, 1, 1);
    String prenom = getCell(joueurs, 1, 2);
    int xpPopularite = stringToInt(getCell(joueurs, 1, 3));
    int moyenneGenerale = stringToInt(getCell(joueurs, 1, 4));
    return newJoueur(id, nom, prenom, xpPopularite, moyenneGenerale);
  }

  Joueur[] chargerLesEleves(int id){ //Cette fonction permet de charger l'ensemble des joueurs fictifs d'une partie
    CSVFile eleves = loadCSV("../ressources/sauvegardes/joueurs-" + id + ".csv");
    int nbEleves = rowCount(eleves);
    Joueur[] tabEleves = new Joueur[nbEleves-2];
    for (int i = 2; i < nbEleves; i += 1){
      String nom = getCell(eleves, i, 1);
      String prenom = getCell(eleves, i, 2);
      int xpPopularite = stringToInt(getCell(eleves, i, 3));
      int moyenneGenerale = stringToInt(getCell(eleves, i, 4));
      tabEleves[i-2] = newJoueur(id, nom, prenom, xpPopularite, moyenneGenerale);
    }
    return tabEleves;
  }

  void calculerResultatsEleves(Quete quete, Joueur[] listeEleves){ //Permet de simuler une quêtes pour les autres joueurs
    //5% de chance d'avoir 100 %EXP
    double chance100 = 0.5;
    //15% de chance d'avoir 75 %EXP
    double chance75 = 0.15;
    //30% de chance d'avoir 50 %EXP
    double chance50 = 0.30;
    //25% de chance d'avoir 25 %EXP
    //Pour chaque élève
    for (int i = 0; i < length(listeEleves); i++) {
      double alea = random();
      if (alea <= chance100) {
        listeEleves[i].xpPopularite += quete.recompenseXP;
      } else if (alea <= chance100 + chance75) {
        listeEleves[i].xpPopularite += quete.recompenseXP * 0.75;
      } else if (alea <= chance100 + chance75 + chance50) {
        listeEleves[i].xpPopularite += quete.recompenseXP * 0.50;
      } else {
        listeEleves[i].xpPopularite += quete.recompenseXP * 0.25;
      }
    }
  }

  void afficherResultat(Joueur joueurPrincipal, Joueur[] listeEleves){ //Permet d'afficher les résultats de chaque joueur
    println("Résultats");
    for (int i = 0; i <= length(listeEleves); i += 1){
      if (i == 0){
        println(joueurPrincipal.prenom + " " + joueurPrincipal.nom + ": " + joueurPrincipal.moyenneGenerale);
      } else {
        println(listeEleves[i - 1].prenom + " " + listeEleves[i - 1].nom + ": " + listeEleves[i - 1].moyenneGenerale);
      }
    }
  }




  /////////////////////////////////////
  //Fonctions relatives au type Lieu//
  ///////////////////////////////////

  Lieu newLieu(int id, String nom, String description, int[] idsLieuxVoisins, int[] idsQuetes) { //Création d'un objet Lieu
    Lieu l = new Lieu();
    l.id = id;
    l.nom = nom;
    l.description = description;
    l.idsLieuxVoisins = idsLieuxVoisins;
    l.idsQuetes = idsQuetes;
    return l;
  }

  Lieu[] chargerLesLieux(Quete[] listeQuetes){ //Cette fonction permet de récupérer tous les lieux via un fichier .csv
    //On récupère le fichier csv
    CSVFile csvFile = loadCSV("../ressources/global/lieux.csv");
    int totalLignes = rowCount(csvFile);
    Lieu[] result = new Lieu[totalLignes - 1];
    for (int i = 1; i < totalLignes; i++) {
      int idLieu = stringToInt(getCell(csvFile, i, 0));
      String nom = getCell(csvFile, i, 1);
      String description = getCell(csvFile, i, 2);
      int[] idsVoisins = calculerVoisins(idLieu);
      int[] idsQuetes = quetesDisponibles(idLieu, listeQuetes);
      result[i - 1] = newLieu(idLieu, nom, description, idsVoisins, idsQuetes);
    }
    return result;
  }

  int[] calculerVoisins(int idLieu) { //Cette fonction renvoi une liste d'id de lieu voisin avec le lieu donné en paramètre
    //On récupère le fichier eleves.csv
    CSVFile csvFile = loadCSV("../ressources/global/voisins.csv");
    int totalLignes = rowCount(csvFile);
    //On compte le nombre de voisins
    int total = 0;
    for (int i = 1; i < totalLignes; i++) {
      if (stringToInt(getCell(csvFile, i, 0)) == idLieu || stringToInt(getCell(csvFile, i, 1)) == idLieu) {
        total += 1;
      }
    }
    //On constitue désormais la liste
    int[] result = new int[total];
    int i = 1;
    int j = 0;
    while (i < totalLignes && j < total) {
      if (stringToInt(getCell(csvFile, i, 0)) == idLieu) {
        result[j] = stringToInt(getCell(csvFile, i, 1));
        j += 1;
      } else if (stringToInt(getCell(csvFile, i, 1)) == idLieu) {
        result[j] = stringToInt(getCell(csvFile, i, 0));
        j += 1;
      }
      i += 1;
    }
    return result;
  }




  /////////////////////////////////////////
  //Fonctions relatives au type Question//
  ///////////////////////////////////////

  Question newQuestion(int id, String intitule, String reponse, Difficulte difficulte, Matiere matiere) { //Création d'un objet Question
    Question q = new Question();
    q.id = id;
    q.intitule = intitule;
    q.reponse = reponse;
    q.difficulte = difficulte;
    q.matiere = matiere;
    return q;
  }

  Question[] chargerLesQuestions(){ //Cette fonction permet de charger toutes les questions via un fichier .csv
    //Ouverture du fichier csv
    CSVFile csvFile = loadCSV("../ressources/global/questions.csv");
    int totalQuestions = rowCount(csvFile);
    Question[] result = new Question[totalQuestions - 1];
    for (int i = 1; i < totalQuestions; i++) {
      //Récupération des éléments de la ligne
      int id = stringToInt(getCell(csvFile, i, 0));
      String intitule = getCell(csvFile, i, 1);
      String reponse = getCell(csvFile, i, 2);
      Difficulte difficulte = intToDifficulte(stringToInt(getCell(csvFile, i, 3)));
      Matiere matiere = intToMatiere(stringToInt(getCell(csvFile, i, 4)));
      //Création de la question
      result[i - 1] = newQuestion(id, intitule, reponse, difficulte, matiere);
    }
    return result;
  }




  //////////////////////////////////////
  //Fonctions relatives au type Quete//
  ////////////////////////////////////

  Quete newQuete(int id, String nom, String description, int recompenseXP, int nbQuest, Difficulte difficulte, Matiere matiere, int idLieu) { //Création d'un objet Quete
    Quete result = new Quete();
    result.id = id;
    result.nom = nom;
    result.description = description;
    result.recompenseXP = recompenseXP;
    result.nbQuest = nbQuest;
    result.difficulte = difficulte;
    result.matiere = matiere;
    result.idLieu = idLieu;
    return result;
  }

  Quete[] chargerLesQuetes() { //Permet de charger l'ensemble des quêtes depuis un fichier csv
    //Ouverture du fichier csv
    CSVFile csvFile = loadCSV("../ressources/global/quetes.csv");
    int totalQuetes = rowCount(csvFile) - 1;
    Quete[] result = new Quete[totalQuetes];
    for (int i = 1; i <= totalQuetes; i++) {
      //Récupération des éléments de la ligne
      int id = stringToInt(getCell(csvFile, i, 0));
      String nom = getCell(csvFile, i, 1);
      String description = getCell(csvFile, i, 2);
      int recompenseXP = stringToInt(getCell(csvFile, i, 3));
      int nbQuest = stringToInt(getCell(csvFile, i, 4));
      Difficulte difficulte = intToDifficulte(stringToInt(getCell(csvFile, i, 5)));
      Matiere matiere = intToMatiere(stringToInt(getCell(csvFile, i, 6)));
      int idLieu = stringToInt(getCell(csvFile, i, 7));
      //Création de la quête
      result[i - 1] = newQuete(id, nom, description, recompenseXP, nbQuest, difficulte, matiere, idLieu);
    }
    return result;
  }

  int[] quetesDisponibles(int idLieu, Quete[] listeQuetes) { //Cette fonction renvoi une liste d'id de quêtes disponibles à partir du lieu donné en paramètre
    //On compte le nombre de quêtes accessibles
    int total = 0;
    for (int i = 0; i < length(listeQuetes); i++) {
      if (listeQuetes[i].idLieu == idLieu) {
        total += 1;
      }
    }
    //On constitue désormais la liste
    int[] result = new int[total];
    int i = 0;
    int j = 0;
    while (i < length(listeQuetes) && j < total) {
      if (listeQuetes[i].idLieu == idLieu) {
        result[j] = listeQuetes[i].id;
        j += 1;
      }
      i += 1;
    }
    return result;
  }

  void entrerDansLieu(Lieu lieu, Lieu[] listeLieux, Quete[] listeQuetes) { //Permet de présenter un lieu et d'afficher la liste de choix possibles depuis un lieu
    String presentationLieu = ajouterTitreZone(lieu.nom);
    presentationLieu += lieu.description;
    //On affiche la liste des choix
    presentationLieu += "\n\n Liste des lieux accessibles : ";
    //Tout d'abord on affiche les lieux accessibles
    int totalVoisins = length(lieu.idsLieuxVoisins);
    for (int i = 0; i < totalVoisins; i++) {
      presentationLieu += "\n[" + ((int)(i + 1)) + "] " + listeLieux[lieu.idsLieuxVoisins[i]].nom;
    }
    //Puis les quêtes
    presentationLieu += "\n\n Liste des quêtes : ";
    for (int i = 0; i < length(lieu.idsQuetes); i++) {
      presentationLieu += "\n[" + ((int)(i + totalVoisins + 1)) + "] " + listeQuetes[lieu.idsQuetes[i]].nom;
    }
    //Enfin le choix de quitter
    presentationLieu += "\n\n[0] Rentrer chez soi";
    print(TypeZone.PRINCIPALE, presentationLieu);
  }

  //nécessite un array de Lieu de taille 1 afin de modifier cette variable aisément par référence
  Quete choisirQuete(Lieu[] lieuActuelle, Lieu[] listeLieux, Quete[] listeQuetes) { //Cette fonction permet de choisir une quête
    //Présenter le lieu actuel
    entrerDansLieu(lieuActuelle[0], listeLieux, listeQuetes);
    //Récupération du choix du joueur
    int totalQuetes = length(lieuActuelle[0].idsQuetes);
    int totalVoisins = length(lieuActuelle[0].idsLieuxVoisins);
    int choix = choixJoueur(0, totalQuetes + totalVoisins, null, "\nQue souhaitez vous faire ?", "\nCe choix n'est pas disponible.");
    //Si le joueur souhaite quitter
    if (choix == 0) {
      return null;
    }
    //On décrémente le choix pour correspondre aux indexs
    choix -= 1;
    //Si le joueur choisi un lieu
    if (choix < totalVoisins) {
      //Appel récursif pour entrer dans ce nouveau lieu
      //On modifie le lieu actuelle
      lieuActuelle[0] = listeLieux[lieuActuelle[0].idsLieuxVoisins[choix]];
      return choisirQuete(lieuActuelle, listeLieux, listeQuetes);
    } else {
      //Forcément une quête...
      //On décrémente le choix pour correspondre aux indexs
      choix -= totalVoisins;
      return listeQuetes[lieuActuelle[0].idsQuetes[choix]];
    }
  }

  int random(int limit){ //Renvoi un entier au hasard avec une valeur maximale
    return (int)(random() * limit);
  }

  Boolean questionPotentielle(Quete quete, Question question) {//Permet de savoir si une question correspond bien à une quête
    return quete.difficulte == question.difficulte && quete.matiere == question.matiere;
  }

  int[] extraireIdsQuestions(Quete quete, Question[] listeQuestions) { //Retourne une liste d'ids de questions qui correspondent à une quête
    int[] result = new int[quete.nbQuest];
    //On compte le nombre de questions potentielles
    int totalQuestionsPotentielles = 0;
    for (int i = 0; i < length(listeQuestions); i++) {
      if (questionPotentielle(quete, listeQuestions[i])) {
        totalQuestionsPotentielles += 1;
      }
    }
    int[] idsQuestions = new int[totalQuestionsPotentielles];
    //On récupère tous les ids de questions ratachés
    int i = 0;
    int j = 0;
    while (i < length(listeQuestions) && j < length(idsQuestions)) {
      if (questionPotentielle(quete, listeQuestions[i])) {
        idsQuestions[j] = listeQuestions[i].id;
        j++;
      }
      i++;
    }
    //Si le nombre de questions potentielles est identique au nombre souhaité
    if (totalQuestionsPotentielles == quete.nbQuest) {
      //On renvoi simplement les ids potentiels
      return idsQuestions;
    }
    //Si le nombre de questions potentielles vaut zéro, impossible de poser des questions
    if (totalQuestionsPotentielles == 0) {
      return null;
    }
    //Si le nombre de questions potentielles est inférieur au nombre souhaité
    if (totalQuestionsPotentielles < quete.nbQuest) {
      //On rajoute d'abord tous les ids que l'on a
      for (int k = 0; k < totalQuestionsPotentielles; k++) {
        result[k] = idsQuestions[k];
      }
      //Puis on tire au hasard pour les restantes (Il y aura des doublons !)
      for (int k = totalQuestionsPotentielles; k < quete.nbQuest; k++) {
        result[k] = idsQuestions[random(length(idsQuestions))];
      }
    } else {
      //Autrement, on a forcément plus de questions que nécessaire, on va donc éviter les doublons
      i = 0;
      while (i < quete.nbQuest) {
        int dernierIdx = length(idsQuestions) - i - 1;
        int idxRandom = random(dernierIdx);
        result[i] = idsQuestions[idxRandom];
        //On passe l'id de la question tirée au sort à la fin du tableau pour ne plus la tirer
        int temp = idsQuestions[idxRandom];
        idsQuestions[idxRandom] = idsQuestions[dernierIdx];
        idsQuestions[dernierIdx] = temp;
        i++;
      }
    }
    return result;
  }

  boolean poserQuestion(Question question, Quete quete) { //Permet de poser une question au joueur. indique si la réponse est correcte ou non
    String texteQuestion = ajouterTitreZone("Quete : " + quete.nom);
    texteQuestion += question.intitule;
    print(TypeZone.PRINCIPALE, texteQuestion);
    boolean reponse = equals(toUpperCase(choixJoueur("\nQuelle est votre réponse ?")), toUpperCase(question.reponse)); //Ajouter fonction pour mettre en majuscule
    if (reponse) {
      //Si la réponse est correcte
      infoJoueur("\nRéponse correcte !");
      delay(1000);
    } else {
      alertJoueur("Réponse incorrecte\nLa réponse était : " + question.reponse);
      delay(2000);
    }
    return reponse;
  }

  boolean demarrerQuete(Quete quete, Joueur[] joueurPrincipal, Question[] listeQuestions) { //Permet de démarrer une quête, renvoi faux si la quête n'est pas faisable
    //On extrait des ids de questions aléatoires
    int[] idsQuestions = extraireIdsQuestions(quete, listeQuestions);
    //S'il n'y a pas de questions on ne peut pas faire de quête
    if (idsQuestions == null) {
      String texteResultat = ajouterTitreZone("Quete : " + quete.nom);
      texteResultat += "\n\n La maîtraisse n'a pas eu le temps de préparer des questions... :(";
      print(TypeZone.PRINCIPALE, texteResultat);
      choixJoueur("\nCliquez sur n'importe quelle touche pour continuer.");
      return false;
    }
    //On pose chacune des questions
    int score = 0;
    for (int i = 0; i < length(idsQuestions); i++) {
      if (poserQuestion(listeQuestions[idsQuestions[i]], quete)) {
        //Si la réponse est correcte
        score++;
      }
    }
    int xp = (int)(quete.recompenseXP * ((double)score / quete.nbQuest));
    String texteResultat = ajouterTitreZone("Quete : " + quete.nom);
    texteResultat += "Vous avez répondu bon à " + score + " questions sur " + quete.nbQuest + " et vous avez gagné " + xp + " point(s) d'expérience.";
    print(TypeZone.PRINCIPALE, texteResultat);
    joueurPrincipal[0].xpPopularite += xp;
    choixJoueur("\nCliquez sur n'importe quelle touche pour continuer.");
    return true;
  }

  String toString(Quete quete) { //Permet d'afficher une quête pour debug
    String result = "";
    result += "id : " + quete.id + " ";
    result += "nom : " + quete.nom + " ";
    result += "description : " + quete.description + " ";
    result += "recompense XP : " + quete.recompenseXP + " ";
    result += "nombre de question : " + quete.nbQuest + " ";
    result += "difficulte : " + difficulteToInt(quete.difficulte) + " ";
    result += "matière : " + matiereToInt(quete.matiere) + " ";
    result += "Lieu : " + quete.idLieu + " ";
    return result;
  }




  ///////////////////////////////////////////
  //Fonctions relatives au type Sauvegarde//
  /////////////////////////////////////////

  //Cette fonction nécessite l'id de la sauvegarde
  Sauvegarde newSauvegarde(int id) { //Création d'un objet Sauvegarde à partir du fichier .csv
    //Récupération du fichier .csv
    CSVFile csvFile = loadCSV(CHEMIN_LISTE_SAUVEGARDE);
    //On ne continu que si l'ID est correct
    if (id > rowCount(csvFile) - 1 || id != stringToInt(getCell(csvFile, id, 0))) {
      return null;
    }
    Sauvegarde result = new Sauvegarde();
    result.id = id;
    result.nomJoueur = getCell(csvFile, id, 1);
    result.prenomJoueur = getCell(csvFile, id, 2);
    result.dateCreation = getCell(csvFile, id, 3);
    result.dateDerniereSauvegarde = getCell(csvFile, id, 4);
    result.nbTour = stringToInt(getCell(csvFile, id, 5));
    result.idLieu = stringToInt(getCell(csvFile, id, 6));
    return result;
  }

  int ajouterSauvegarde(String nom, String prenom) { //Cette fonction ajoute une ligne sur le fichier .csv des sauvegardes
    //Récupération du fichier .csv
    CSVFile csvFile = loadCSV(CHEMIN_LISTE_SAUVEGARDE);
    //On calcule l'id
    int id = rowCount(csvFile);
    //On ajoute cette ligne au csv
    String[][] nouvelleLigne = new String[][]{{"" + id, nom, prenom, dateActuelle(), dateActuelle(), "0", "0"}};
    ajouterLigneCSV(CHEMIN_LISTE_SAUVEGARDE, nouvelleLigne);
    return id;
  }

  Sauvegarde creerNouvellePartie() { //Cette fonction permet de configurer une nouvelle sauvegarde
    print(TypeZone.PRINCIPALE, ajouterTitreZone("CREATION NOUVELLE PARTIE"));
    //On demande à l'utilisateur de saisir nom et prénom
    String nom = choixJoueur("\nVeuillez saisir votre nom");
    String prenom = choixJoueur("\nVeuillez saisir votre prénom");
    //On met à jour le fichier csv de sauvegarde
    int id = ajouterSauvegarde(nom, prenom);
    //Et création du fichier .csv des joueurs
    String cheminFichier = "../ressources/sauvegardes/joueurs-" + id + ".csv";
    saveCSV(constituerFichierJoueur(nom, prenom), cheminFichier);
    //On peut souhaiter la bienvenu au nouvel élève !
    String texteBienvenu = ajouterTitreZone("BIENVENU !");
    texteBienvenu += "\n\nBienvenu à toi " + prenom + " " + nom;
    texteBienvenu += ".\n\nLe but de ce jeu est de devenir l'élève PAR-FAIT de l'école LEBENCARBASCQ.";
    texteBienvenu += "\nPour cela, libre à toi de te promener dans l\'établissement afin de réussir toutes les quêtes disponibles et devenir le plus populaire, mais aussi celui avec les meilleurs notes.";
    texteBienvenu += "\n\n\nBonne chance !";
    print(TypeZone.PRINCIPALE, texteBienvenu);
    choixJoueur("\nCliquez sur n'importe quelle touche pour commencer.");
    return newSauvegarde(id);
  }

  String[][] constituerFichierJoueur(String nom, String prenom) { //Cette fonction retourne le contenu par défaut d'un fichier joueurs-xxx.csv
    //On récupère le fichier eleves.csv
    CSVFile csvFile = loadCSV("../ressources/global/eleves.csv");
    String[][] result = new String[rowCount(csvFile) + 1][5];
    //On ajoute l'en-tête du fichier
    result[0][0] = "id";
    result[0][1] = "nom";
    result[0][2] = "prenom";
    result[0][3] = "xp-popularite";
    result[0][4] = "moyenne-generale";
    //On ajoute le véritable joueur
    result[1][0] = "0";
    result[1][1] = nom;
    result[1][2] = prenom;
    result[1][3] = "100"; //XP par défaut
    result[1][4] = "10"; //Moyenne par défaut
    //On ajoute tous les élèves
    for (int i = 2; i < length(result, 1); i++) {
      result[i][0] = "" + (int)(i - 1); //On commence à l'ID 1, le n°0 étant réservé au véritable joueur
      result[i][1] = getCell(csvFile, i - 1, 0);
      result[i][2] = getCell(csvFile, i - 1, 1);
      result[i][3] = "100"; //XP par défaut
      result[i][4] = "10"; //Moyenne par défaut
    }
    return result;
  }

  //Cette fonction permet de rajouter une ligne à un fichier .csv
  void ajouterLigneCSV(String cheminFichier, String[][] lignes) {
    //Récupération du fichier .csv
    CSVFile csvFile = loadCSV(cheminFichier);
    int totalLignes = rowCount(csvFile);
    //Si le nombre de colonnes des lignes à rajouter ne convient pas, on ne les rajoute pas
    int totalColonnes = columnCount(csvFile);
    if (length(lignes, 2) != totalColonnes) {
      return;
    }
    //On récupère toutes les lignes du fichier actuel
    String[][] contenu = csvToTab(csvFile, length(lignes, 1));
    //On ajoute les ligne voulues
    for (int i = 0; i < length(lignes, 1); i++) {
      contenu[totalLignes + i] = lignes[i];
    }
    //On peut sauvegarder le nouveau fichier
    saveCSV(contenu, cheminFichier);
  }

  void afficherSauvegardes(CSVFile saves, int premiereSauvegarde, int derniereSauvegarde, int nbPages, int pageActuelle) {
    String texteMenu = ajouterTitreZone("CHARGER UNE PARTIE (" + pageActuelle + "/" + nbPages + ")");
    texteMenu += "\n[0] Quitter\n";

    for (int i = premiereSauvegarde; i <= derniereSauvegarde; i++) {
      texteMenu += "[" + i + "] " + getCell(saves, i, 2) + " " + getCell(saves, i, 1) + " (" + getCell(saves, i, 3) + ")\n";
    }
    print(TypeZone.PRINCIPALE, texteMenu);
  }

  Sauvegarde chargerPartie(){ //Cette fonction permet de charger une sauvegarde et renvoyer les données de celle ci
    CSVFile saves = loadCSV("../ressources/sauvegardes/liste-sauvegardes.csv");
    int nbSaves = rowCount(saves)-1;
    //Est-ce qu'il y a assez d'espace sur l'écran pour afficher toutes les sauvegardes ?
    int lignesDisponibles = HAUTEUR_ZONE_PRINCIPALE - 2 - 5; //-2 pour les bordures, -5 pour le titre, retour à la ligne et l'option de quitter
    //S'il n'y a pas assez de place pour afficher toutes les sauvegardes
    int choix = 2;
    if (nbSaves > lignesDisponibles) {
      //On calcule le nombre de page nécessaires
      int pagesNecessaires = nbSaves / lignesDisponibles;
      if (nbSaves % lignesDisponibles > 0) {
        pagesNecessaires += 1;
      }
      //On affiche la première page
      int pageActuelle = 1;
      int choixMin = 0;
      int choixMax = 1;
      while (choix > choixMax) {
        //On détermine le choix minimum et maximum
        choixMin = ((pageActuelle - 1) * lignesDisponibles) + 1;
        choixMax = choixMin + lignesDisponibles - 1;
        //Si le choix max dépasse le nombre de sauvegarde, on corrige
        if (choixMax > nbSaves) {
          choixMax = nbSaves;
        }
        //On affiche la liste des sauvegardes
        afficherSauvegardes(saves, choixMin, choixMax, pagesNecessaires, pageActuelle);
        //On détermine si le joueur peut passer à une page suivante ou précédente
        if (pageActuelle == 1) {
          //On ne peut que faire page suivante ou quitter
          choix = choixJoueur(choixMin, choixMax, new String[]{"+", "0"}, "Veuillez saisir le numéro de votre choix.\n\"+\" pour aller à la page suivante", "\nCe choix n'est pas disponible");
          //Si il y a un changement de page
          if (choix == choixMax + 1) {
            pageActuelle += 1;
          } else if (choix == choixMax + 2) {
            choix = 0;
          }
        } else if (pageActuelle == pagesNecessaires) {
          //Si c'est la dernière page
          choix = choixJoueur(choixMin, choixMax, new String[]{"-", "0"}, "Veuillez saisir le numéro de votre choix.\n\"-\" pour aller à la page précédente", "\nCe choix n'est pas disponible");
          //Si il y a un changement de page
          if (choix == choixMax + 1) {
            pageActuelle -= 1;
          } else if (choix == choixMax + 2) {
            choix = 0;
          }
        } else {
          choix = choixJoueur(choixMin, choixMax, new String[]{"-", "+", "0"}, "Veuillez saisir le numéro de votre choix.\n\"-\" ou \"+\" pour aller à la page précédente ou suivante", "\nCe choix n'est pas disponible");
          //Si il y a un changement de page
          if (choix == choixMax + 1) {
            pageActuelle -= 1;
          } else if (choix == choixMax + 2) {
            pageActuelle += 1;
          } else if (choix == choixMax + 3) {
            choix = 0;
          }
        }
      }
    } else {
      //La liste tient sur une seule page
      afficherSauvegardes(saves, 1, nbSaves, 1, 1);
      choix = choixJoueur(0, nbSaves, null, "\nVeuillez saisir le numéro de votre choix.", "\nCe choix n'est pas disponible");
    }
    //Si le joueur souhaite quitter
    if (choix == 0) {
      return null;
    }
    return newSauvegarde(choix);
  }

  void quitterJeu(Sauvegarde partieActuelle, Joueur[] joueurPrincipal, Joueur[] listeEleves, int nbTour){
    //On récupère les fichiers CSV
    CSVFile sauvegardesCSV = loadCSV("../ressources/sauvegardes/liste-sauvegardes.csv");
    CSVFile joueursCSV = loadCSV("../ressources/sauvegardes/joueurs-" + partieActuelle.id + ".csv");
    //Conversion en tableaux de String
    String[][] sauvegardes = csvToTab(sauvegardesCSV, 0);
    String[][] joueurs = csvToTab(joueursCSV, 0);
    //Sauvegarde de l'état des joueurs
    for (int i = 1; i < length(joueurs, 1); i += 1){
      if (i == 1){
        joueurs[i][3] = "" + joueurPrincipal[0].xpPopularite;
        joueurs[i][4] = "" + joueurPrincipal[0].moyenneGenerale;
      } else {
        joueurs[i][3] = "" + listeEleves[i-2].xpPopularite;
        joueurs[i][4] = "" + listeEleves[i-2].moyenneGenerale;
      }
    }
    //Sauvegarde de l'état de la partie actuelle
    sauvegardes[partieActuelle.id][4] = dateActuelle();
    sauvegardes[partieActuelle.id][5] = "" + partieActuelle.nbTour;
    sauvegardes[partieActuelle.id][6] = "" + partieActuelle.idLieu;
    //On enregistre les CSV
    saveCSV(sauvegardes, "../ressources/sauvegardes/liste-sauvegardes.csv");
    saveCSV(joueurs, "../ressources/sauvegardes/joueurs-" + partieActuelle.id + ".csv");
    ecranChargement(true);
  }

  String[][] csvToTab(CSVFile file, int ajouterLigne) { //Cette fonction converti un type CSVFile en String[][] en y ajoutant un certain nombre de ligne
    int totalLignes = rowCount(file);
    int totalColonnes = columnCount(file);
    String[][] result = new String[totalLignes + ajouterLigne][totalColonnes];
    for (int i = 0; i < totalLignes; i++) {
      for (int j = 0; j < totalColonnes; j++) {
        result[i][j] = getCell(file, i, j);
      }
    }
    return result;
  }

  String toString(Sauvegarde sauvegarde) { //Permet d'afficher une sauvegarde pour debug
    String result = "";
    result += sauvegarde.id + " - ";
    result += sauvegarde.nomJoueur + " - ";
    result += sauvegarde.prenomJoueur + " - ";
    result += sauvegarde.dateCreation + " - ";
    result += sauvegarde.dateDerniereSauvegarde + " - ";
    result += sauvegarde.nbTour + " - ";
    result += sauvegarde.idLieu;
    return result;
  }




  ///////////////////////
  // Fonctions annexes//
  /////////////////////

  int nombreJoursMois(int mois, int annee) { //Cette fonction retourne le nombre de jours dans un mois donné
    // Si le mois ou l'année n'est pas valide, on retourne 0
    if (!(mois > 0 && mois <= 12) || annee < 1) {
      return 0;
    }
    //Par défaut, on considère qu'un mois a 31 jours
    int jours = 31;
    //On calcule selon si le mois est février
    if (mois == 2) {
      if (estBissextile(annee)) {
        jours = 29;
      } else {
        jours = 28;
      }
      //On vérifie si c'est un mois à 30 jours
    } else if (mois == 4 || mois == 6 || mois == 9 || mois == 11) {
      jours = 30;
    }
    return jours;
  }

  boolean estBissextile(int annee) { //Cette fonction détermine si une année donnée est bissextile ou non
    if (annee % 400 == 0) {
      return true;
    }
    if (annee % 100 == 0) {
      return false;
    }
    if (annee % 4 == 0) {
      return true;
    }
    return false;
  }

  String dateActuelle() { //Cette fonction retourne la date actuelle au format JJ/MM/AAAA
    //On calcule les éléments de temps
    long ms = getTime();
    long secondes = ms / 1000;
    long jours = (secondes / 86400) + 1;
    //Pour calculer, on démarre du 01/01/1970 00:00:00
    int annee = 1970;
    int mois = 1;
    //Tant que le nombre de jour est supérieur à 1 année
    while ((!estBissextile(annee) && jours > 365) || (estBissextile(annee) && jours > 366)) {
      //Si l'année et bissextile
      if (estBissextile(annee)) {
        //On retire les 366 jours s'il en reste ensuite
        if (jours > 366) {
          jours -= 366;
          //Cela veut dire que l'on a passé une année
          annee += 1;
        }
      } else {
        //Lorsque l'année n'est pas bissextile, on ajoute une année
        jours -= 365;
        annee += 1;
      }
    }
    //A ce stade, il nous reste le nombre de jour de l'année actuelle
    //On calcule mois après mois le nombre de jour d'un mois pour les déduire
    while (jours > nombreJoursMois(mois, annee)) {
      jours -= nombreJoursMois(mois, annee);
      mois += 1;
    }
    return "" + jours + "/" + mois + "/" + annee;
  }

  int length(int nombre) { //Cette fonction retourne la taille d'un entier
    int length = 0;
    while (nombre != 0) {
        nombre = nombre / 10;
        length++;
    }
    return length;
}

  int readInt(int min, int max, String[] choixSupplementaires) { //Cette fonction permet de lire un nombre sans faire planter le jeu
    String input = readString();
    if (equals(input, "")) {
      return -1;
    }
    //On vérifie tout d'abord si c'est un choix supplémentaire
    if (choixSupplementaires != null) {
      for (int i = 0; i < length(choixSupplementaires); i++) {
        if (equals(choixSupplementaires[i], input)) {
          //Dans ce cas, on retourne le max + l'indice du tableau
          return i + 1 + max;
        }
      }
    }
    int tailleMin = length(min);
    int tailleMax = length(max);
    int tailleInput = length(input);
    //On vérifie d'abord que la taille de l'input soit cohérente
    if (tailleInput < tailleMin || tailleInput > tailleMax) {
      return -1;
    }

    //On vérifie que l'input soit constitué uniquement de chiffre
    for (int i = 0; i < tailleInput; i++) {
      if (!(charAt(input, i) >= '0' && charAt(input, i) <= '9')) {
        return -1;
      }
    }
    //Nous sommes maintenant sûr que que l'input soit un nombre
    int result = stringToInt(input);
    //On vérifie qu'il soit dans l'intervalle
    if (result < min || result > max) {
      return -1;
    }
    return result;
  }

  void clearTerminal() { //Simule le resultat de la fonction bash "clear"
    print("\033c");
  }

  int nombreOccurences(String texte, char c) {
    int result = 0;
    for (int i = 0; i < length(texte); i++) {
      char lettreCourante = charAt(texte, i);
      if (lettreCourante == c) {
        result++;
      }
    }
    return result;
  }

  String[] split(String texte, char c) { //Sépare du texte selon un caractère
    int totalOccurences = nombreOccurences(texte, c) + 1;
    String[] result = new String[totalOccurences];
    int tailleTexte = length(texte);
    int j = 0;
    //Pour chaque occurences attendus
    for (int i = 0; i < length(result); i++) {
      String ligne = "";
      //On assigne un catactère forcément différent
      char lettreCourante = (char)(c + 1);
      //Tant que la lettre est différente de celle recherchée
      while (lettreCourante != c && j < tailleTexte) {
        //On récupère chaque caractère
        lettreCourante = charAt(texte, j);
        //On l'ajoute au résultat
        if (lettreCourante != c) {
            ligne += lettreCourante;
        }
        j++;
      }
      result[i] = ligne;
      //Tant que le caractère n'est pas celui recherché
      //Si ce n'est pas le caractère recherché, on continu la recherche
    }
    return result;
  }

  void changerTailleTerminal(int hauteur, int largeur) {
    print("\033[8;" + hauteur + ";" + largeur + "t");
  }



  //////////////////////////////////////////////
  //Fonctions relatives au type zone de texte//
  ////////////////////////////////////////////

  Coordonnees newCoordonnees(int ligne, int colonne) {
    Coordonnees result = new Coordonnees();
    result.ligne = ligne;
    result.colonne = colonne;
    return result;
  }

  LigneHorizontal newLigneHorizontal(int ligne, int colonne) {
    LigneHorizontal result = new LigneHorizontal();
    result.coordDebut = newCoordonnees(ligne, colonne);
    return result;
  }

  LigneVertical newLigneVertical(int ligne, int colonne) {
    LigneVertical result = new LigneVertical();
    result.coordDebut = newCoordonnees(ligne, colonne);
    return result;
  }

  ZoneDeTexte newZoneDeTexte(int ligneDepart, int colonneDepart, int largeur, int hauteur) {
    ZoneDeTexte result = new ZoneDeTexte();
    result.largeur = largeur;
    result.hauteur = hauteur;
    result.coordDebut = newCoordonnees(ligneDepart, colonneDepart);
    result.ligneHaut = newLigneHorizontal(ligneDepart, colonneDepart);
    result.ligneBas = newLigneHorizontal(ligneDepart + result.hauteur - 1, colonneDepart);
    result.ligneGauche = newLigneVertical(ligneDepart, colonneDepart);
    result.ligneDroite = newLigneVertical(ligneDepart, colonneDepart + result.largeur - 1);
    return result;
  }

  void dessiner(LigneHorizontal ligne, ZoneDeTexte zone, char c) { //Permet de dessiner une ligne horizontal
    String dessin = "";

    //On déplace le curseur au bon endroit
    cursor(ligne.coordDebut.ligne, ligne.coordDebut.colonne);

    //Si la largeur est pair, on n'ajoute pas d'espace
    if (zone.largeur % 2 == 0) {
      for (int i = 0; i < zone.largeur; i++) {
        dessin += c;
      }
    //Si non, on ajoute un espace
    } else {
      dessin += c;
      for (int i = 0; i < (zone.largeur / 2); i++) {
        dessin += " " + c;
      }
    }
    print(dessin);
  }

  void dessiner(LigneVertical ligne, ZoneDeTexte zone, char c) { //Permet de dessiner une ligne vertical
    for (int i = 0; i < zone.hauteur; i++) {
      //On déplace le curseur au bon endroit
      cursor(ligne.coordDebut.ligne + i, ligne.coordDebut.colonne);
      print(c);
    }
  }

  void dessiner(ZoneDeTexte zone, char c) { //Permet de dessiner les bordures d'une zone de texte
    dessiner(zone.ligneHaut, zone, c);
    dessiner(zone.ligneBas, zone, c);
    dessiner(zone.ligneGauche, zone, c);
    dessiner(zone.ligneDroite, zone, c);
  }

  void effacerContenu(TypeZone typeZone) { //Permet d'effacer le contenu d'une zone de texte
    for (int i = 0; i < listeZones[typeZoneToInt(typeZone)].hauteur - 2; i++) {
      //On déplace le curseur
      int ligne = listeZones[typeZoneToInt(typeZone)].coordDebut.ligne + i + 1;
      int colonne = listeZones[typeZoneToInt(typeZone)].coordDebut.colonne + 2;
      cursor(ligne, colonne);
      //On ajoute autant d'espace que nécessaire
      for (int j = 0; j < listeZones[typeZoneToInt(typeZone)].largeur - 4; j++) {
        print(" ");
      }
    }
  }

  String[] contenuZoneTexte(ZoneDeTexte zone, String texte) { //Cette fonction permet de mettre en forme un texte pour l'afficher dans une zone de texte
    //Par défaut, chaque ligne est vide
    String[] result = new String[zone.hauteur - 2];
    for (int i = 0; i < length(result); i++) {
      result[i] = "";
    }

    //On sépare le texte en plusieurs lignes
    String[] lignesTexte = split(texte, '\n');
    //Tant que l'on peut imprimer dans la zone de texte
    int compteurResultat = 0;
    int compteurLigne = 0;
    while(compteurResultat < length(result)) {
      //On ajoute uniquement une ligne au résultat s'il y en une !
      if (compteurLigne < lignesTexte.length) {
        //S'il y a assez d'espace sur la ligne de zone de texte
        if (length(result[compteurResultat]) + length(lignesTexte[compteurLigne]) <= zone.largeur - 4) {
          //On ajoute la ligne entière
          result[compteurResultat] = lignesTexte[compteurLigne];
        } else {
          //S'il n'y en a pas, on tente d'ajouter mot par mot
          //On sépare la ligne en cours de traitement
          String[] mots = split(lignesTexte[compteurLigne], ' ');
          //Pour chaque mots
          for (int i = 0; i < length(mots) && compteurResultat < length(result); i++) {
            //On rajoute un espace au mot
            //S'il y a assez d'espace sur la ligne de zone de texte avec l'espace
            if (length(result[compteurResultat]) + length(mots[i]) + 1 <= zone.largeur - 4) {
              //On ajoute le mot
              result[compteurResultat] = result[compteurResultat] + mots[i] + " ";
            } else if (length(result[compteurResultat]) + length(mots[i]) <= zone.largeur - 4) {
              //Il y a de la place si on ne rajoute pas d'espace
              result[compteurResultat] = result[compteurResultat] + mots[i];
            } else if (length(mots[i]) > zone.largeur - 4) {
              //Si le mot est plus long que la place restante
              //On ajoute le maximum de lettres
              //Tant qu'il reste de la place
              while (length(result[compteurResultat]) + 1 <=  zone.largeur - 4) {
                //On ajoute la 1ere lettre
                result[compteurResultat] += charAt(mots[i], 0);
                //On retire cette premiere lettre au mot
                mots[i] = substring(mots[i], 1, length(mots[i]));
              }
              //Il n'y a plus de place sur cette ligne
              compteurResultat++;
              i--;
            } else {
              //Il n'y a plus de place sur cette ligne, on passe à la suivante
              compteurResultat++;
              i--;
            }
          }
        }
      }
      compteurResultat++;
      compteurLigne++;
    }
    return result;
  }

  void print(TypeZone typeZone, String texte) { //Permet d'imprimer du texte dans une zone de texte
    //Si le texte est vide, on se contente d'effacer le contenu
    effacerContenu(typeZone);
    if (equals("", texte)) {
      return;
    }
    listeZones[typeZoneToInt(typeZone)].contenuSepare = contenuZoneTexte(listeZones[typeZoneToInt(typeZone)], texte);
    //On peut désormais imprimer le contenu
    print(typeZone);
  }

  void print(TypeZone typeZone) { //Se contente de réafficher le dernier contenu enregistré
    //On imprime ligne après ligne
    for (int i = 0; i < length(listeZones[typeZoneToInt(typeZone)].contenuSepare); i++) {
      //Calcul nouvelle position
      int ligne = listeZones[typeZoneToInt(typeZone)].coordDebut.ligne + i + 1;
      int colonne = listeZones[typeZoneToInt(typeZone)].coordDebut.colonne + 2;
      cursor(ligne, colonne);
      print(listeZones[typeZoneToInt(typeZone)].contenuSepare[i]);
    }
  }

  int typeZoneToInt(TypeZone type) { //Permet de convertir un type TypeZone en int
    int result = -1;
    if (type == TypeZone.PRINCIPALE) {
      result = 0;
    } else if (type == TypeZone.REPONSE) {
      result = 1;
    } else if (type == TypeZone.INFO) {
      result = 2;
    }
    return result;
  }

  void reparerZones() {
    clearTerminal();
    changerTailleTerminal(HAUTEUR_ZONE_PRINCIPALE + HAUTEUR_ZONE_REPONSE + HAUTEUR_ZONE_INFO - 1, LARGEUR_ZONE_PRINCIPALE);
    dessiner(zonePrincipale, CONTOUR_ZONE_PRINCIPALE);
    dessiner(zoneReponse, CONTOUR_ZONE_REPONSE);
    print(TypeZone.PRINCIPALE);
    print(TypeZone.INFO);
    print(TypeZone.REPONSE);
  }

  String ajouterTitreZone(String titre) {//Renvoi du texte pour l'afficher comme titre de la zone pricipale
    String result = "";
    //Calculer espace disponibles
    int espaceDispo = zonePrincipale.largeur - 4;
    //On dessine le premier trait
    for (int i = 0; i < espaceDispo; i++) {
      result += "-";
    }
    result += "\n";
      //Si le titre peut être centré
    if (length(titre) < espaceDispo) {
      //Calculer espace restants
      int espaceRestant = espaceDispo - length(titre);
      //On ajoute la moitié en espace pour centrer le titre
      for (int i = 0; i < espaceRestant / 2; i++) {
        result += " ";
      }
    }
    //Ajout du titre et du trait
    result += titre + "\n";
    for (int i = 0; i < espaceDispo; i++) {
      result += "-";
    }
    return result + "\n";
  }

  String lireAsciiArt(String chemin) { //Renvoi une chaine de caractère ascii art depuis un fichier
    //On récupère le fichier
    File fichier = newFile(chemin);
    String result = "";
    //On récupère chaque ligne du fichier
  	while(ready(fichier)){
  	    //affichage du contenu de la ligne suivante
  	    result += readLine(fichier) + "\n";
  	}
    return result;
  }

  void ecranChargement(boolean quitter) { //Cette fonction va piocher dans la liste des ascii art pour en afficher un au hasard
    //On ne sélectionne pas le même dossier pour les ascii art de fermeture
    String[] listeFichiers;
    String cheminAleatoire;
    if (quitter) {
      listeFichiers = getAllFilesFromDirectory(CHEMIN_DOSSIER_ASCII_QUITTER);
      cheminAleatoire = CHEMIN_DOSSIER_ASCII_QUITTER + "/" + listeFichiers[random(length(listeFichiers))];
    } else {
      listeFichiers = getAllFilesFromDirectory(CHEMIN_DOSSIER_ASCII_CHARGEMENT);
      cheminAleatoire = CHEMIN_DOSSIER_ASCII_CHARGEMENT + "/" + listeFichiers[random(length(listeFichiers))];
    }

    String asciiArt = lireAsciiArt(cheminAleatoire);
    //On imprime l'ascii art
    print(TypeZone.PRINCIPALE, asciiArt);
    //Barre de chargement
    String barreChargement = "";
    for (int i = 1; i < LARGEUR_ZONE_REPONSE - 2; i++) {
      barreChargement += "■";
      print(TypeZone.REPONSE, barreChargement);
      delay(DELAI_CHARGEMENT);
    }
  }



  //////////////////////////////////////
  //*!*!* FONCTIONS PRINCIPALES *!*!*//
  ////////////////////////////////////
  void afficherMenu() {
    effacerContenu(TypeZone.PRINCIPALE);
    String texteMenu = ajouterTitreZone("MENU PRINCIPAL");

    texteMenu += "\n\n\n\n\n\n[0] Quitter\n\n";
    texteMenu += "[1] Nouvelle partie\n\n";
    texteMenu += "[2] Charger partie";
    print(TypeZone.PRINCIPALE, texteMenu);
  }

  void alertJoueur(String info) {
    effacerContenu(TypeZone.INFO);
    print(ANSI_RED);
    print(TypeZone.INFO, info);
    print(ANSI_RESET);
  }

  void infoJoueur(String info) {
    effacerContenu(TypeZone.INFO);
    print(ANSI_BLUE);
    print(TypeZone.INFO, info);
    print(ANSI_RESET);
  }

  int choixJoueur(int min, int max, String[] choixSupplementaire, String info, String erreur) { //Cette fonction renvoi le choix du joueur entre min et max inclus
    int result = -1;
    infoJoueur(info);
    do {
      effacerContenu(TypeZone.REPONSE);
      print(TypeZone.REPONSE, "> ");
      result = readInt(min, max, choixSupplementaire);
      //Erreur de saisie
      if (result == -1) {
        //On réinitialise la zone de choix pour prévenir des erreurs du joueur
        reparerZones();
        alertJoueur(erreur);
      }
    } while (result == -1);
    //On réinitialise la zone de choix pour prévenir des erreurs du joueur
    reparerZones();
    effacerContenu(TypeZone.REPONSE);
    effacerContenu(TypeZone.INFO);
    return result;
  }

  String choixJoueur(String info) { //Cette fonction retourne l'entrée clavier d'un joueur à l'emplacement prévu
    effacerContenu(TypeZone.REPONSE);
    infoJoueur(info);
    print(TypeZone.REPONSE, "> ");
    String result = readString();
    //On réinitialise la zone de choix pour prévenir des erreurs du joueur
    reparerZones();
    return result;
  }

  ChoixMenuPrincipal menuPrincipal(){ //Cette fonction affiche le menu principal du jeu et renvoi le choix du joueur
    ChoixMenuPrincipal menu = ChoixMenuPrincipal.QUITTER;
    CSVFile saves = loadCSV(CHEMIN_LISTE_SAUVEGARDE);
    int nbSaves = rowCount(saves)-1;
    boolean verif = false;
    int choix;
    afficherMenu();
    while (!verif){
      choix = choixJoueur(0, 2, null, "\nVeuillez saisir le numéro de votre choix", "\nCe choix n'est pas disponible.");
      if (choix == 0){
        menu = ChoixMenuPrincipal.QUITTER;
        verif = true;
      } else if (choix == 1){
        menu = ChoixMenuPrincipal.NOUVELLE_PARTIE;
        verif = true;
      } else if (choix == 2){
        if (nbSaves == 0) {
          alertJoueur("\nIl n'y a aucune partie à charger.");
          delay(1000);
        } else {
          menu = ChoixMenuPrincipal.CHARGER_PARTIE;
          verif = true;
        }
      }
    }
    return menu;
  }



  void algorithm() { //Ici, void algorithm gère uniquement le menu principal du jeu
    clearTerminal();
    changerTailleTerminal(HAUTEUR_ZONE_PRINCIPALE + HAUTEUR_ZONE_REPONSE + HAUTEUR_ZONE_INFO - 1, LARGEUR_ZONE_PRINCIPALE);
    dessiner(zonePrincipale, CONTOUR_ZONE_PRINCIPALE);
    dessiner(zoneReponse, CONTOUR_ZONE_REPONSE);
    Sauvegarde partieActuelle;
    //On affiche le menu principal tant que le joueur ne veut pas quitter
    while(true) {
      //On affiche le menu principal et on renvoi le choix de l'utilisateur
      ChoixMenuPrincipal choixMenu = menuPrincipal();
      //Création d'une nouvelle partie
      if (choixMenu == ChoixMenuPrincipal.NOUVELLE_PARTIE) {
        partieActuelle = creerNouvellePartie();
        ecranChargement(false);
        jouer(partieActuelle);
      //Chargement d'une partie existante
      } else if (choixMenu == ChoixMenuPrincipal.CHARGER_PARTIE) {
        partieActuelle = chargerPartie();
        if (partieActuelle != null) {
          ecranChargement(false);
          jouer(partieActuelle);
        }
      //On quitte complètement le jeu :(
      } else {
        ecranChargement(true);
        clearTerminal();
        return;
      }
    }
  }

  void jouer(Sauvegarde partieActuelle) { //Cette procédure gère la partie visible du jeu
    //Chargement de tous les éléments nécessaires
    Quete[] listeQuetes = chargerLesQuetes();
    Lieu[] listeLieux = chargerLesLieux(listeQuetes);
    Question[] listeQuestions = chargerLesQuestions();
    Joueur[] listeEleves = chargerLesEleves(partieActuelle.id); //--> joueurs.csv SAUF 1ere ligne
    Joueur[] joueurPrincipal = new Joueur[]{chargerJoueur(partieActuelle.id)}; //--> Lire la première ligne du joueurs.csv
    int nbTour = partieActuelle.nbTour;
    Lieu[] lieuActuelle = new Lieu[1]; //Le lieu est stocké dans un tableau de taille 1 pour le passer par référence aux fonctions
    lieuActuelle[0] = listeLieux[partieActuelle.idLieu];

    //Tant que le joueur souhaite continuer le jeu
    while(true) {
      //
      //
      //
      //AJOUTER UN CONTROLE SURPRISE ALEATOIRE ? OU PERIODIQUE ?
      //QUETE = EXP | CONTROLE = MOYENNE ?
      //
      //
      //Choix de la quête + changement de salle si désiré par le joueur
      Quete quete = choisirQuete(lieuActuelle, listeLieux, listeQuetes);
      partieActuelle.idLieu = lieuActuelle[0].id;
      //Si aucune quête n'est choisi, cela veut dire que l'on veut quitter le jeu
      if (quete == null) {
        //On quitte le jeu en sauvegardant automatiquement !
        quitterJeu(partieActuelle, joueurPrincipal, listeEleves, nbTour);
        return;
      }
      /*
      On peut démarrer la quete :
        On extrait des questions selon matiere et difficulte
        On pose toutes ces questions
        On calcule l'XP (et la moyenne ?)
      */
      demarrerQuete(quete, joueurPrincipal, listeQuestions);
      //On simule la quete pour les autres élèves pour màj de leur XP et moyenne
      ecranChargement(false);
      calculerResultatsEleves(quete, listeEleves);
      //On affiche les résultats de la classe après cette journée
      //afficherResultat(joueurPrincipal, listeEleves);
      //Animation pour le jour suivant ?
      partieActuelle.nbTour += 1;
    }
  }

  ///////////////////////
  //FONCTIONS DE TESTS//
  /////////////////////
  void testCalculerResultatsEleves() {
    Quete q = new Quete();
    q.recompenseXP = 100;
    Joueur j1 = new Joueur();
    j1.xpPopularite = 100;
    Joueur[] js = new Joueur[] {j1};
    //Tant que toutes les augmentations n'ont pas eu lieu
    int i = 0;
    boolean[] augmentation = new boolean[]{false, false, false, false};
    while ((augmentation[0] == false || augmentation[1] == false || augmentation[2] == false || augmentation[3] == false) && i < 1000) {
      int xpAvant = js[0].xpPopularite;
      calculerResultatsEleves(q, js);
      int xpApres = js[0].xpPopularite;
      //100%
      if (xpApres - q.recompenseXP == xpAvant) {
        augmentation[0] = true;
        //75%
      } else if(xpApres - (q.recompenseXP * 0.75) == xpAvant) {
        augmentation[1] = true;
        //50%
      } else if(xpApres - (q.recompenseXP * 0.50) == xpAvant) {
        augmentation[2] = true;
        //25%
      } else if(xpApres - (q.recompenseXP * 0.25) == xpAvant) {
        augmentation[3] = true;
      } else {
        //Résultat incohérent !
        assertEquals(true, false);
        return;
      }
      i++;
    }
    //On valide la fonction de test :)
    if (i < 1000) {
      assertEquals(true, true);
    } else {
      for (int j = 0; j < length(augmentation); j++) {
        println("\n" + augmentation[j]);
      }
      assertEquals(true, false);
    }
    println(i);

  }

  void testNombreJoursMois() {
    assertEquals(31, nombreJoursMois(1, 2022));
    assertEquals(30, nombreJoursMois(4, 2022));
    assertEquals(28, nombreJoursMois(2, 2022));
    assertEquals(29, nombreJoursMois(2, 2020));
  }

  void testEstBissextile() {
    assertTrue(estBissextile(2020));
    assertTrue(estBissextile(2016));
    assertTrue(estBissextile(2012));
    assertFalse(estBissextile(2022));
    assertFalse(estBissextile(2022));
    assertFalse(estBissextile(2021));
  }

  void testDateActuelle() {
    print("\nVeuillez saisir la date du jour (format JJ/MM/AAAA sans padding de 0): ");
    String auj = readString();
    String hier = "8/1/2023"; //format JJ/MM/AAAA sans padding de 0
    assertEquals(auj, dateActuelle());
    assertNotEquals(hier, dateActuelle());
  }

  void testQuetesDisponibles(){
    Lieu l1 = new Lieu();
    l1.id = 1;
    Lieu l2 = new Lieu();
    l2.id = 2;
    Quete q1 = new Quete();
    q1.id = 1;
    q1.idLieu = 1;
    Quete q2 = new Quete();
    q2.id = 2;
    q2.idLieu = 1;
    Quete q3 = new Quete();
    q3.id = 3;
    q3.idLieu = 3;
    Quete q4 = new Quete();
    q4.id = 4;
    q4.idLieu = 1;
    Quete q5 = new Quete();
    q5.id = 5;
    q5.idLieu = 3;
    Quete[] listeQuetes = new Quete[]{q1, q2, q3, q4, q5};
    assertArrayEquals(new int[]{1, 2, 4}, quetesDisponibles(l1.id, listeQuetes));
    if (length(quetesDisponibles(l2.id, listeQuetes)) == 0){
      assertTrue(true);
    } else {
      assertTrue(false);
    }
  }

  void testSplit() {
    assertArrayEquals(new String[]{"Hello", "le", "monde"}, split("Hello le monde", ' '));
    assertArrayEquals(new String[]{"Hello", "le", "monde", ""}, split("Hello le monde ", ' '));
    assertArrayEquals(new String[]{"", "Hello", "le", "monde", ""}, split(" Hello le monde ", ' '));
    assertArrayEquals(new String[]{"Hello", "le", "monde"}, split("Hello\nle\nmonde", '\n'));
    assertArrayEquals(new String[]{"Hello", "le", "monde", ""}, split("Hello\nle\nmonde\n", '\n'));
    assertArrayEquals(new String[]{"", "Hello", "le", "monde", ""}, split("\nHello\nle\nmonde\n", '\n'));
    assertArrayEquals(new String[]{"", "Hello", "le", "monde", ""}, split("\nHello\nle\nmonde\n", '\n'));
    assertArrayEquals(new String[]{"Veuillez saisir le numéro de votre choix. \"+\" pour changer de page"}, split("Veuillez saisir le numéro de votre choix. \"+\" pour changer de page", '\n'));
  }

  void testRandom(){
    int rand = random(10);
    assertTrue(rand >= 0 && rand < 10);
    rand = random(20);
    assertTrue(rand >= 0 && rand < 20);
    rand = random(0);
    assertTrue(rand == 0);
  }

  void testLength(){
    assertEquals(3, length(100));
    assertEquals(2, length(12));
    assertEquals(5, length(45213));
  }

  void testContenuZoneTexte() {
    //Zone de 61 de large et 4 de hauteur (donc 2 lignes) et 57 de largeur interne pour du texte
    ZoneDeTexte zoneTest = newZoneDeTexte(1, 1, 61, 4);
    //Une seule ligne, taille OK
    String txt1 = "Hello world";
    String[] res1 = new String[]{"Hello world", ""};
    assertArrayEquals(res1, contenuZoneTexte(zoneTest, txt1));

    //Une seule ligne, taille trop grande (espace à la fin) :
    String txt2 = "Hello world comment vous allez la famille hein oui alors? ";
    String[] res2 = new String[]{"Hello world comment vous allez la famille hein oui alors?", ""};
    assertArrayEquals(res2, contenuZoneTexte(zoneTest, txt2));

    //Une seule ligne, taille trop grande (trop de mots, mais on déborde sur 2e ligne) :
    String txt3 = "Hello world comment vous allez la famille hein oui alors? moi ça va bien";
    String[] res3 = new String[]{"Hello world comment vous allez la famille hein oui alors?", "moi ça va bien "};
    assertArrayEquals(res3, contenuZoneTexte(zoneTest, txt3));

    //Une seule ligne, taille trop grande (trop de mots, même pour la deuxième ligne à cause espace) :
    String txt4 = "Hello world comment vous allez la famille hein oui alors? Hello world comment vous allez la famille hein oui alors? ";
    String[] res4 = new String[]{"Hello world comment vous allez la famille hein oui alors?", "Hello world comment vous allez la famille hein oui alors?"};
    assertArrayEquals(res4, contenuZoneTexte(zoneTest, txt4));

    //Une seule ligne, taille trop grande (trop de mots, même pour la deuxième ligne même sans espace) :
    String txt5 = "Hello world comment vous allez la famille hein oui alors? Hello world comment vous allez la famille hein oui alors? haha bah oui...";
    String[] res5 = new String[]{"Hello world comment vous allez la famille hein oui alors?", "Hello world comment vous allez la famille hein oui alors?"};
    assertArrayEquals(res5, contenuZoneTexte(zoneTest, txt5));

    //2 lignes, tailles OK.
    String txt6 = "Hello\nworld";
    String[] res6 = new String[]{"Hello", "world"};
    assertArrayEquals(res6, contenuZoneTexte(zoneTest, txt6));

    //3 lignes, tailles OK.
    String txt7 = "Hello\nworld\nça va ?";
    String[] res7 = new String[]{"Hello", "world"};
    assertArrayEquals(res7, contenuZoneTexte(zoneTest, txt7));

    //Zone de 1 ligne, mot sans espace trop long
    ZoneDeTexte zoneTest2 = newZoneDeTexte(1, 1, 10, 3);
    String txt8 = "123456789";
    String[] res8 = new String[]{"123456"};
    assertArrayEquals(res8, contenuZoneTexte(zoneTest2, txt8));
  }
}
