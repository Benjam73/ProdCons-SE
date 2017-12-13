package step4;

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
	private Aleatoire copyOfEachMessageRandomVariable;

	protected Producteur(TestProdCons simulator, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement, int nbMessageToProduce, ProdCons buffer,
			Aleatoire copyOfEachMessageRandomVariable) throws ControlException {
		super(typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.nbMessageToProduce = nbMessageToProduce;
		this.buffer = buffer;
		productionDurationRandomVariable = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		alreadyProduced = 0;
		this.simulator = simulator;
		this.copyOfEachMessageRandomVariable = copyOfEachMessageRandomVariable;
	}

	@Override
	public void run() {
		while (alreadyProduced < nbMessageToProduce) {
			int copyOfNewMessage = copyOfEachMessageRandomVariable.next();
			Message newMessage = new MessageX(this, copyOfNewMessage);
			int timeToProduce = randomProductionDuration();
			try {
				observateur.productionMessage(this, newMessage, timeToProduce);
				sleep(copyOfNewMessage * timeToProduce);
				this.getBuffer().put(this, newMessage);
				observateur.depotMessage(this, newMessage);
				oneNewMessageCreated();
			} catch (Exception e) {
				e.getMessage();
			}
		}
		simulator.removeProducer(this);
	}

	private void oneNewMessageCreated() {
		alreadyProduced++;
	}

	private int randomProductionDuration() {
		return productionDurationRandomVariable.next();
	}

	@Override
	public int nombreDeMessages() {

		// TODO Auto-generated method stub

		return 0;
	}

	public Integer alreadyProduced() {
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
