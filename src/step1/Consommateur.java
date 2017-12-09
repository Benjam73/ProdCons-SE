package step1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {

	private ProdCons buffer;
	private Aleatoire consumptionDurationRandomVariable;
	private Integer alreadyConsume;

	protected Consommateur(int moyenneTempsDeTraitement, int deviationTempsDeTraitement, ProdCons buffer)
			throws ControlException {

		super(Type.typeConsommateur.getValue(), new Observateur(), moyenneTempsDeTraitement,
				deviationTempsDeTraitement);
		this.buffer = buffer;
		consumptionDurationRandomVariable = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		alreadyConsume = 0;
	}

	@Override
	public void run() {
		while (this.getBuffer().size() != 0) {
			int timeToConsume = randomConsumptionDuration();
			try {
				sleep(timeToConsume);
				Message messGet = this.getBuffer().get(this);
				System.out.println(messGet);
				newMessageConsume();
			} catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
			}
		}
	}

	private int randomConsumptionDuration() {
		return consumptionDurationRandomVariable.next();
	}

	private void newMessageConsume() {
		alreadyConsume++;
	}

	public String toString() {
		return "[Consommateur " + this.identification() + "]";
	}

	@Override
	public int nombreDeMessages() {
		return alreadyConsume;
	}

	private ProdCons getBuffer() {
		return this.buffer;
	}

}
