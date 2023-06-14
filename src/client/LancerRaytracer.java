package client;
import java.time.Instant;
import java.time.Duration;

import client.raytracer.Disp;
import client.raytracer.Scene;
import client.raytracer.Image;

public class LancerRaytracer {

    public static String aide = "Raytracer : synthèse d'image par lancé de rayons (https://en.wikipedia.org/wiki/Ray_tracing_(graphics))\n\nUsage : java LancerRaytracer [fichier-scène] [largeur] [hauteur]\n\tfichier-scène : la description de la scène (par défaut simple.txt)\n\tlargeur : largeur de l'image calculée (par défaut 512)\n\thauteur : hauteur de l'image calculée (par défaut 512)\n";

    public static void main(String args[]){

        // Vérification que l'adress ip et le port de l'annuaire du service central
        if (args.length < 2){
            throw new Error("Adresse IP et Port requis ! (dans cet ordre)");
        }

        String ip = args[0];
        int port = Integer.parseInt(args[1]);

        // Le fichier de description de la scène si pas fournie
        String fichier_description = "resources/simple.txt";

        // largeur et hauteur par défaut de l'image à reconstruire
        int largeur = 512, hauteur = 512;

        // Nombre de decoupage de l'image
        int nbDecoup = 1;

        if(args.length > 2){
            try{
                fichier_description = "./resource/" + args[2];
            }catch(Exception e){
                System.out.println("Les données saisies sont erronées, valeur par défaut sélectionnée (fichier simple.txt)");
            }
            if(args.length > 3){
                try{
                    largeur = Integer.parseInt(args[3]);
                }catch(Exception e){
                    System.out.println("Les données saisies sont erronées, valeur par défaut sélectionnée (largeur 512)");
                }
                if(args.length > 4){
                    try{
                        hauteur = Integer.parseInt(args[4]);
                    }catch(Exception e){
                        System.out.println("Les données saisies sont erronées, valeur par défaut sélectionnée (hauteur 512)");
                    }
                    if (args.length > 5) {
                        try{
                            nbDecoup = Integer.parseInt(args[5]);
                        }catch(Exception e){
                            System.out.println("Les données saisies sont erronées, valeur par défaut sélectionnée (largeur 512)");
                        }
                    }
                }
            }
        }


        // création d'une fenêtre
        Disp disp = new Disp("Raytracer", largeur, hauteur);

        // Initialisation d'une scène depuis le modèle
        Scene scene = new Scene(fichier_description, largeur, hauteur);

        // Calcul de l'image de la scène les paramètres :
        // - x0 et y0 : correspondant au coin haut à gauche
        // - l et h : hauteur et largeur de l'image calculée
        // Ici on calcule toute l'image (0,0) -> (largeur, hauteur)

        int x0 = 0, y0 = 0;

        // Mode Normal
        int l = largeur/nbDecoup, h = hauteur/nbDecoup;

        // Question 3
        //On coupe l'image en 2 sur les 2 axes pour pouvoir afficher qu'un quart de l'image
        //int l = largeur/2, h = hauteur/2;


        // Chronométrage du temps de calcul
        Instant debut = Instant.now();
        System.out.println("Calcul de l'image :\n - Coordonnées : "+x0+","+y0
                           +"\n - Taille "+ largeur + "x" + hauteur);
        Image image = scene.compute(x0, y0, l, h);


        // Question 3
        // Nous affichons le quart inférieur droit de l'image ici
        //Image image2 = scene.compute(l, h, l, h);

        Instant fin = Instant.now();

        long duree = Duration.between(debut, fin).toMillis();

        System.out.println("Image calculée en :"+duree+" ms");

        // Affichage de l'image calculée
        disp.setImage(image, x0, y0);

        // Question 3

        // Affichage du quart inférieur droit de l'image calculée
        //System.out.println("Largeur : " + largeur + " - l : " + l);
        //System.out.println("Hauteur : " + hauteur + " - h : " + h);
        //System.out.println("Image 2 Width : " + image2.getWidth());
        //System.out.println("Image 2 Height : " + image2.getHeight());

        //disp.setImage(image2, h, l);
    }
}
