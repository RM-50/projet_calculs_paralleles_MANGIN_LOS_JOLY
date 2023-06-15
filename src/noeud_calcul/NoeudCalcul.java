package noeud_calcul;

import java.rmi.RemoteException;

import raytracer.Image;
import raytracer.Scene;

public class NoeudCalcul implements ServiceCalcul{

    public NoeudCalcul(){}

    @Override
    public Image calculerImage(Scene s, int x, int y, int l, int h) throws RemoteException {
        System.out.println("Je travaille...");
        return s.compute(x, y, l, h);
    }
    
}
