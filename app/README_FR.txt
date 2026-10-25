RAITRA — projet Android de démarrage
====================================

Compatibilité prévue : Android 8 à Android 13 (minSdk 26, targetSdk 33).
Nom de l'application : Raitra

IMPORTANT
Ce ZIP contient le code source d'un projet Android Studio, pas un APK compilé. L'environnement utilisé pour préparer ces fichiers ne contient pas Android SDK/Gradle, donc je ne peux pas compiler ni tester l'APK ici.

COMMENT COMPILER L'APK SUR UN ORDINATEUR
1. Installer Android Studio depuis le site officiel Android Developers.
2. Décompresser ce ZIP.
3. Dans Android Studio, choisir Open et sélectionner le dossier Raitra (celui contenant settings.gradle).
4. Attendre la synchronisation Gradle et installer les composants SDK demandés.
5. Choisir Build > Build Bundle(s) / APK(s) > Build APK(s).
6. L'APK de test sera normalement dans app/build/outputs/apk/debug/app-debug.apk.
7. Copier cet APK sur le téléphone Android et l'ouvrir. Si nécessaire, autoriser l'installation de cette source dans les paramètres Android.

FONCTIONS PRESENTES DANS CE PROTOTYPE
- Enregistrement microphone avec pause/reprise/arrêt.
- Bibliothèque locale IndexedDB, import audio/vidéo, lecture, suppression et ouverture dans le studio.
- Champ de paroles (lyrics) pendant l'enregistrement; les paroles sont sauvegardées avec l'audio et s'affichent quand on lance sa lecture ou avec le bouton dédié.
- Sauvegarde du projet complet dans la bibliothèque de la page B, avec les blobs média et les coupes.
- Bouton Lire toutes les pistes pour lancer simultanément les clips audio/vidéo de la chronologie, en respectant leur ordre et les espaces de chaque piste.
- Ajout d'un clip texte simple dans la chronologie.
- Interface de pistes audio/vidéo (maximum 5 de chaque), réglage de volume de préécoute, réglages de nom/symbole/couleur dans l'application et page À propos.
- Export simple du fichier source lorsqu'une seule piste est disponible.

LIMITES HONNÊTES
LIMITES D'EXPORT : l'export ne réalise pas encore le rendu final multipiste en MP3/MP4/MPEG. Pour le moment, il permet de télécharger un fichier source simple; les coupes et l'organisation du projet sont sauvegardées dans le projet Raitra. Le rendu audio/vidéo final, les transitions visuelles, les effets texte incrustés et la séparation de la voix humaine nécessitent un moteur tel que FFmpeg et des essais de performance sur l'appareil.

ICONE
L'icône Android de lancement est dans app/src/main/res/drawable/raitra_icon.xml. Pour changer l'icône qui apparaît sur l'écran d'accueil, modifier ce fichier (ou utiliser Image Asset dans Android Studio), puis reconstruire l'APK. Le réglage de symbole dans l'application ne change que le symbole interne à Raitra.
