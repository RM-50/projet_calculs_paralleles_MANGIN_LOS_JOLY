package service_central;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import noeud_calcul.ServiceCalcul;

public class Distributeur implements ServiceCentral{

    private List<ServiceCalcul> noeuds;
    private int noeudCourant;
    private ServiceCalcul n;
    private Lock verrou;

    public Distributeur(){
        this.noeuds = new ArrayList<ServiceCalcul>();
        this.noeudCourant = 0;
        this.verrou = new ReentrantLock();
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
                verrou.lock();
                if (noeuds.size() > 0){
                    if (noeudCourant >= noeuds.size())
                        noeudCourant = 0;
                    n = noeuds.get(noeudCourant); 
                    noeudCourant++;
                    verrou.unlock();
                }else{
                    verrou.unlock();
                }
            }
        });
        t.start();
        while(t.isAlive()){
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return n;
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
