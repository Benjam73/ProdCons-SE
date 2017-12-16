package step4;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {

	private ProdCons buffer;
	private Aleatoire consumptionDurationRandomVariable;
	private Integer alreadyConsumed;
	private TestProdCons simulator;

	protected Consommateur(TestProdCons simulator, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement, ProdCons buffer) throws ControlException {

		super(typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.buffer = buffer;
		consumptionDurationRandomVariable = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		alreadyConsumed = 0;
		this.simulator = simulator;
	}

	@Override
	public void run() {
		while ((this.getBuffer().enAttente() != 0) || (simulator.hasProducer())) {
			int timeToConsume = randomConsumptionDuration();
			try {
				try {
					Message removedMessage = this.getBuffer().get(this);
					Debugger.log(this.toString() + " received message " + removedMessage.toString());
					observateur.retraitMessage(this, removedMessage);

					sleep(timeToConsume);
					observateur.consommationMessage(this, removedMessage, timeToConsume);
					newMessageConsumed();
				} catch (InterruptedException e) {
					currentThread().interrupt();
				}

			} catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
			}
		}
	}

	private int randomConsumptionDuration() {
		return consumptionDurationRandomVariable.next();
	}

	private void newMessageConsumed() {
		alreadyConsumed++;
	}

	public String toString() {
		return "[Consommateur " + this.identification() + "]";
	}

	@Override
	public int nombreDeMessages() {
		return alreadyConsumed;
	}

	private ProdCons getBuffer() {
		return this.buffer;
	}

}
