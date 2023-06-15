package service_central;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import noeud_calcul.ServiceCalcul;

public class Distributeur implements ServiceCentral{

    private List<ServiceCalcul> noeuds;
    private int noeudCourant;
    private ServiceCalcul noeud;

    public Distributeur(){
        this.noeuds = new ArrayList<ServiceCalcul>();
        this.noeudCourant = 0;
    }

    @Override
    public void enregistrerNoeud(ServiceCalcul noeud) throws RemoteException {
        new Thread(new Runnable(){
            @Override
            public void run() {
                synchronized (noeuds){
                    noeuds.add(noeud);
                }
            }
        }).start();
    }

    @Override
    public ServiceCalcul envoyerNoeud() {
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                synchronized (noeuds){
                    if (noeuds.size() > 0){
                        if (noeudCourant >= noeuds.size())
                            noeudCourant = 0;
                        noeud = noeuds.get(noeudCourant); 
                        noeudCourant++;
                    }else{
                        throw new Error("Aucun noeud de calcul connecté");
                    }
                }
            }
        });
        t.start();
        while(t.isAlive()){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return noeud;
    }

    @Override
    public void supprimerNoeud(ServiceCalcul noeud) throws RemoteException {
        new Thread(new Runnable(){
            @Override
            public void run() {
                synchronized (noeuds) {
                    noeuds.remove(noeud);
                }
            }
        }).start();
    }
    
}
