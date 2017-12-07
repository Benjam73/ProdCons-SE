package step1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {

	private int nbMessagesConsommes;

	protected Consommateur(int moyenneTempsDeTraitement, int deviationTempsDeTraitement) throws ControlException {
		super(2, new Observateur(), moyenneTempsDeTraitement, deviationTempsDeTraitement);
		nbMessagesConsommes = 0;
	}

	public String toString() {
		return "[Consommateur " + this.identification() + "]";
	}

	@Override
	public int nombreDeMessages() {
		return nbMessagesConsommes;
	}

}
