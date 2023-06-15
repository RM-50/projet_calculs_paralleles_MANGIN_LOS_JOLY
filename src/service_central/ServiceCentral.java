package service_central;

import java.rmi.Remote;
import java.rmi.RemoteException;

import noeud_calcul.ServiceCalcul;

public interface ServiceCentral extends Remote{

    public void enregistrerNoeud(ServiceCalcul noeud) throws RemoteException;

    public ServiceCalcul envoyerNoeud() throws RemoteException;

    public void supprimerNoeud(ServiceCalcul noeud) throws RemoteException;
}
