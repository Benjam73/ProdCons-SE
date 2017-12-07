package step1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {

	// Number of messages already produced by Producteur
	private Integer alreadyProduced;

	public Producteur(int moyenneTempsTraitement, int deviationTempsTraitement) throws ControlException {
		// Producer type = int 1
		super(1, new Observateur(), moyenneTempsTraitement, deviationTempsTraitement);
		alreadyProduced = 0;
	}

	@Override
	public int nombreDeMessages() {
		return 0;
	}

	public Integer alreadyProduced() {
		return alreadyProduced;
	}

	@Override
	public String toString() {
		return "[Producteur " + this.identification() + "]";
	}

}
