package service_central;

import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerService {
    public static void main(String[] args) {
        if (args.length < 0){
            throw new Error("Il faut spécifier le numéro de port");
        }
        // Création des instances
		Distributeur d = new Distributeur();

        // Création de la référence
        ServiceCentral ref = null;
        try{
           ref = (ServiceCentral) UnicastRemoteObject.exportObject(d, 0);
        }catch(Exception e){
            System.out.println("Erreur lors de la création de la référence");
        }
		
		// Récupération de l'annuaire
		Registry reg1 = null;
        try {
            reg1 = LocateRegistry.createRegistry(Integer.parseInt(args[0]));
        } catch (RemoteException e) {
            System.out.println("Erreur lors de la création de l'annuaire");
        }catch (Exception e2){
            System.out.println("Le numéro de port doit être un entier");
        }

		// Enregistrement des références dans l'annuaire
		try {
            reg1.rebind("serviceCentral", ref);
        } catch (Exception e) {
            System.out.println("Erreur de l'enregistrement de la référence dans l'annuaire");
        }
    }
}
