package noeud_calcul;

import java.rmi.RemoteException;

import raytracer.Image;
import raytracer.Scene;

public class NoeudCalcul implements ServiceCalcul{

    public NoeudCalcul(){}

    private Image img;

    @Override
    public synchronized Image calculerImage(Scene s, int x, int y, int l, int h) throws RemoteException {
        /*Thread t = new Thread(new Runnable() {
            @Override
            public void run() {*/
                System.out.println("Je travaille...");
                return img = s.compute(x, y, l, h);
           /* }
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
        return img;*/
    }
    
}
