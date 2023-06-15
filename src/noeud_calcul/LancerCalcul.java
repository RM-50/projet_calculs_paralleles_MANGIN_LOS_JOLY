package noeud_calcul;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class LancerCalcul {
    public static void main(String[] args) {
        if (args.length < 1){
            throw new Error("Adresse IP et Port requis ! (dans cet ordre)");
        }
        // Récuperer adresse ip et numero de port
        String ip = args[0];
        int port = Integer.parseInt(args[1]);

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

        // Exporte serviceCalcul
        NoeudCalcul noeud = new NoeudCalcul();
		try {
			ServiceCalcul serviceCalcul = (ServiceCalcul) UnicastRemoteObject.exportObject(noeud, 0);
            // Utilisation de la methode enregistrerNoeud
            distributeur.enregistrerNoeud(serviceCalcul);
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de l'enregistrement du service");
		}
    }
}
