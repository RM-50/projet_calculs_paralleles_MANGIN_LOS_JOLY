package noeud_calcul;

import java.rmi.Remote;
import java.rmi.RemoteException;

import raytracer.Image;
import raytracer.Scene;

public interface ServiceCalcul extends Remote{
    
    public Image calculerImage(Scene s, int x, int y, int l, int h) throws RemoteException;
}
