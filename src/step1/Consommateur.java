package step1;

import common.Debugger;
import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {

	private ProdCons buffer;
	private Aleatoire consumptionDurationRandomVariable;
	private Integer alreadyConsumed;
	private TestProdCons simulator;

	/**
	 * 
	 * @param simulator
	 *            The Prod/Cons simulation
	 * @param moyenneTempsDeTraitement
	 *            The Average duration used to consume a message
	 * @param deviationTempsDeTraitement
	 *            The consumtpion's time variance
	 * @param buffer
	 *            The ProdCons instantiation used to get message
	 * @throws ControlException
	 */
	protected Consommateur(TestProdCons simulator, int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
			ProdCons buffer) throws ControlException {

		super(typeConsommateur, new Observateur(), moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.buffer = buffer;
		consumptionDurationRandomVariable = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		alreadyConsumed = 0;
		this.simulator = simulator;
	}

	/**
	 * The main loop of a Consummateur Thread. Loop until the ProdCons buffer
	 * isn't empty or, some producer are waiting to produce their message
	 */
	@Override
	public void run() {
		while ((this.getBuffer().enAttente() != 0) || (simulator.hasProducer())) {
			int timeToConsume = randomConsumptionDuration();
			try {
				try {
					Debugger.log(this.toString() + " received message " + this.getBuffer().get(this).toString());
					sleep(timeToConsume);
					newMessageConsumed();
				} catch (InterruptedException e) {
					currentThread().interrupt();
				}
			} catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
			}
		}
		Debugger.log(this.toString() + " dies");
	}

	/**
	 * 
	 * @return A new consumption duration time
	 */
	private int randomConsumptionDuration() {
		return consumptionDurationRandomVariable.next();
	}

	/**
	 * Increase by 1 the number of message consumed
	 */
	private void newMessageConsumed() {
		alreadyConsumed++;
	}

	/**
	 * @return A string conversion of a Consommateur instance
	 */
	public String toString() {
		return "[Consommateur " + this.identification() + "]";
	}

	/**
	 * @return the number of message already consumed
	 */
	@Override
	public int nombreDeMessages() {
		return alreadyConsumed;
	}

	/**
	 * 
	 * @return the ProdCons buffer
	 */
	private ProdCons getBuffer() {
		return this.buffer;
	}

}
