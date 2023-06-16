package client;
import java.time.Instant;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Duration;

import raytracer.Disp;
import raytracer.Scene;
import noeud_calcul.ServiceCalcul;
import raytracer.Image;

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
        String fichier_description = "../resources/simple.txt";

        // largeur et hauteur par défaut de l'image à reconstruire
        int largeur = 512, hauteur = 512;

        // Nombre de decoupage de l'image
        int nbDecoup = 1;

        if (args.length > 2) {
            try{
                nbDecoup = Integer.parseInt(args[2]);
            }catch(Exception e){
                System.out.println("Les données saisies sont erronées, valeur par défaut sélectionnée (nbDecoup 1)");
            }
            if(args.length > 3){
                try{
                    fichier_description = "../resources/" + args[3];
                }catch(Exception e){
                    System.out.println("Les données saisies sont erronées, valeur par défaut sélectionnée (fichier simple.txt)");
                }
                if(args.length > 4){
                    try{
                        largeur = Integer.parseInt(args[4]);
                    }catch(Exception e){
                        System.out.println("Les données saisies sont erronées, valeur par défaut sélectionnée (largeur 512)");
                    }
                    if(args.length > 5){
                        try{
                            hauteur = Integer.parseInt(args[5]);
                        }catch(Exception e){
                            System.out.println("Les données saisies sont erronées, valeur par défaut sélectionnée (hauteur 512)");
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

        int x = 0, y = 0;

        // Mode Normal
        int l = largeur/nbDecoup, h = hauteur/nbDecoup;

        // Question 3
        //On coupe l'image en 2 sur les 2 axes pour pouvoir afficher qu'un quart de l'image
        //int l = largeur/2, h = hauteur/2;


        // Chronométrage du temps de calcul
        Instant debut = Instant.now();
        System.out.println("Calcul de l'image :"
                           +"\n - Taille "+ largeur + "x" + hauteur);

         // Récuperer annuaire
		Registry reg = null;
		try {
			reg = LocateRegistry.getRegistry(ip, port);
		} catch (NumberFormatException e) {
			System.out.println("Erreur lors de la récupération de l'annuaire : NumberFormatException");
		} catch (RemoteException e) {
			System.out.println("Erreur lors de la récupération de l'annuaire");
		}
		
		// Récupérer service central
        service_central.ServiceCentral distributeur = null;
		try {
            distributeur = (service_central.ServiceCentral) reg.lookup("serviceCentral");
		} catch (AccessException e) {
			System.out.println("Permission non accordée");
		} catch (RemoteException e) {
			System.out.println("Erreur lors de l'execution du service");
		} catch (NotBoundException e) {
			System.out.println("Le service est inaccessible");
		}


        // Pour chaque decoupage
        for (int i = 0; i <= hauteur-h; i+=h){
                for (int j = 0; j <= largeur-l; j+=l){
                /*final service_central.ServiceCentral tdistrib = distributeur;
                final int tx = x;
                final int ty = y;
                final int th = h;
                final int tl = l; 
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Envoyer le morceau de calcul correspondant
                        ServiceCalcul noeud = null;
                        try{
                            noeud = tdistrib.envoyerNoeud();
                            Image img = null;
                            try{
                                synchronized (disp){
                                    img = noeud.calculerImage(scene, tx, ty, tl, th);
                                    System.out.println("Je suis tout la x : " + tx + " et tout ici y : " + ty);
                                    disp.setImage(img, tx, ty);
                                }
                            }catch(RemoteException e){
                                try{
                                    tdistrib.supprimerNoeud(noeud);
                                }catch(RemoteException exception){
                                    System.out.println("Erreur lors de la demande de suppression du noeud de calcul au service central");
                                }
                            }catch(Exception e){}
                        }catch(RemoteException e ){
                            System.out.println("Erreur lors de la récupération du noeud de calcul");
                        }catch(Exception e){}
                    } 
                }).start();*/
                ThreadClients t = new ThreadClients(scene, x, y, h, l, disp, distributeur);
                t.start();
                x += l;
            }
            y += h;
            x = 0;
        }
        //Image image = scene.compute(x0, y0, l, h);


        // Question 3
        // Nous affichons le quart inférieur droit de l'image ici
        //Image image2 = scene.compute(l, h, l, h);

        Instant fin = Instant.now();

        long duree = Duration.between(debut, fin).toMillis();

        System.out.println("Image calculée en :"+duree+" ms");

        // Affichage de l'image calculée
        //disp.setImage(image, x0, y0);

        // Question 3

        // Affichage du quart inférieur droit de l'image calculée
        //System.out.println("Largeur : " + largeur + " - l : " + l);
        //System.out.println("Hauteur : " + hauteur + " - h : " + h);
        //System.out.println("Image 2 Width : " + image2.getWidth());
        //System.out.println("Image 2 Height : " + image2.getHeight());

        //disp.setImage(image2, h, l);
    }
}
