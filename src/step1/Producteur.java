package step1;

import common.Debugger;
import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {

	// Number of messages already produced by Producteur
	private Integer alreadyProduced;
	private int nbMessageToProduce;
	private ProdCons buffer;
	private Aleatoire productionDurationRandomVariable;
	private TestProdCons simulator;

	/**
	 * 
	 * @param simulator
	 *            The Prod/Cons simulation
	 * @param moyenneTempsDeTraitement
	 *            The Average duration used to produce a message
	 * @param deviationTempsDeTraitement
	 *            The production's time variance
	 * @param nbMessageToProduce
	 *            The message number the Producteur will make
	 * @param buffer
	 *            The ProdCons instantiation used to get message
	 * @throws ControlException
	 */
	protected Producteur(TestProdCons simulator, int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
			int nbMessageToProduce, ProdCons buffer) throws ControlException {
		super(typeProducteur, new Observateur(), moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.nbMessageToProduce = nbMessageToProduce;
		this.buffer = buffer;
		productionDurationRandomVariable = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		alreadyProduced = 0;
		this.simulator = simulator;
	}

	/**
	 * the main loop of a Producteur Thread. Loop until all message are created.
	 */
	@Override
	public void run() {
		while (alreadyProduced < nbMessageToProduce) {
			Message newMessage = new MessageX(this);
			int timeToProduce = randomProductionDuration();
			try {
				sleep(timeToProduce);
				this.getBuffer().put(this, newMessage);
				oneNewMessageCreated();
			} catch (Exception e) {
				e.getMessage();
			}
		}
		simulator.removeProducer(this);
		Debugger.log(this.toString() + " dies");
	}

	private void oneNewMessageCreated() {
		alreadyProduced++;
	}

	private int randomProductionDuration() {
		return productionDurationRandomVariable.next();
	}

	@Override
	public int nombreDeMessages() {
		return nbMessageToProduce;
	}

	public Integer getAlreadyProduced() {
		return alreadyProduced;
	}

	@Override
	public String toString() {
		return "[Producteur " + this.identification() + "]";
	}

	private ProdCons getBuffer() {
		return this.buffer;
	}

}
