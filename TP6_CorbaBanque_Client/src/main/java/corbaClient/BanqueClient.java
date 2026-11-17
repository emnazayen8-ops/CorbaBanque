package corbaClient;

import corbaBanque.Compte;
import corbaBanque.IBanqueRemote;
import corbaBanque.IBanqueRemoteHelper;

import javax.naming.Context;
import javax.naming.InitialContext;


public class BanqueClient {

    public static void main(String[] args) {
        try {
            Context ctx = new InitialContext();

            org.omg.CORBA.Object obj = (org.omg.CORBA.Object) ctx.lookup("banque");

            IBanqueRemote stub = IBanqueRemoteHelper.narrow(obj);
            System.out.println("Stub obtenu, connexion au serveur Banque OK.\n");


            System.out.println("=== 1. creerCompte ===");
            stub.creerCompte(new Compte(1, 1000f));
            stub.creerCompte(new Compte(2, 500f));
            stub.creerCompte(new Compte(3, 250.5f));
            System.out.println("3 comptes créés (codes 1, 2, 3).");

            System.out.println("\n=== 2. verser (300 sur le compte 1) ===");
            stub.verser(300f, 1);
            afficher(stub.getCompte(1));

            System.out.println("\n=== 3. retirer (200 du compte 2) ===");
            stub.retirer(200f, 2);
            afficher(stub.getCompte(2));

            System.out.println("\n=== 4. getCompte (code 3) ===");
            afficher(stub.getCompte(3));

            System.out.println("\n=== 5. getComptes ===");
            for (Compte c : stub.getComptes()) {
                afficher(c);
            }

            System.out.println("\n=== 6. conversion (100 EUR -> DT) ===");
            System.out.println("100 EUR = " + stub.conversion(100f) + " DT");

            System.out.println("\n=== 7. Cas d'erreur ===");
            try {
                stub.retirer(99999f, 1);
            } catch (org.omg.CORBA.BAD_PARAM e) {
                System.out.println("Retrait refusé : " + e.getMessage());
            }
            try {
                stub.getCompte(42);
            } catch (org.omg.CORBA.OBJECT_NOT_EXIST e) {
                System.out.println("Compte inexistant : " + e.getMessage());
            }
            try {
                stub.creerCompte(new Compte(1, 10f));
            } catch (org.omg.CORBA.BAD_PARAM e) {
                System.out.println("Doublon refusé : " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Erreur client : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void afficher(Compte c) {
        System.out.println("Compte [code=" + c.code + ", solde=" + c.solde + "]");
    }
}