package client;


import noeud_calcul.ServiceCalcul;
import raytracer.Disp;
import raytracer.Image;
import raytracer.Scene;
import service_central.ServiceCentral;

public class ThreadClients extends Thread {

    private Scene scene;
    private int x, y, h, l;
    private Disp disp;
    private ServiceCentral distributeur;

    public ThreadClients(Scene scene, int x, int y, int h, int l, Disp d, ServiceCentral s){
        this.scene = scene;
        this.x = x;
        this.y = y;
        this.l = l;
        this.h = h;
        this.disp = d;
        this.distributeur = s;
    }

    @Override
    public void run(){
        // Envoyer le morceau de calcul correspondant
        ServiceCalcul noeud = null;
        try{
            noeud = distributeur.envoyerNoeud();
            Image img = null;
            try{
                    img = noeud.calculerImage(scene, x, y, l, h);
                    disp.setImage(img, x, y);
            }catch(Exception e){
                try{
                    distributeur.supprimerNoeud(noeud);
                    Thread.sleep(2500);
                    ThreadClients t = new ThreadClients(scene, x, y, h, l, disp, distributeur);
                    t.start();
                }catch(Exception exception){
                    System.out.println("Erreur lors de la demande de suppression du noeud de calcul au service central");
                    Thread.sleep(2500);
                    ThreadClients t = new ThreadClients(scene, x, y, h, l, disp, distributeur);
                    t.start();
                }
            }
        }catch(Exception e ){
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            ThreadClients t = new ThreadClients(scene, x, y, h, l, disp, distributeur);
            t.start();
        }
    }
    
}
