package step6;

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
	private MyObserver myObserver;

	protected Producteur(TestProdCons simulator, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement, int nbMessageToProduce, ProdCons buffer, MyObserver myObserver)
			throws ControlException {
		super(typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		this.nbMessageToProduce = nbMessageToProduce;
		this.buffer = buffer;
		productionDurationRandomVariable = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		alreadyProduced = 0;
		this.simulator = simulator;
		this.myObserver = myObserver;
	}

	@Override
	public void run() {
		while (alreadyProduced < nbMessageToProduce) {
			Message newMessage = new MessageX(this);
			int timeToProduce = randomProductionDuration();
			try {
				observateur.productionMessage(this, newMessage, timeToProduce);
				myObserver.messageProduction(this, newMessage, timeToProduce);
				sleep(timeToProduce);
				this.getBuffer().put(this, newMessage);
				observateur.depotMessage(this, newMessage);
				myObserver.messageDeposition(this, newMessage);
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
		return nbMessageToProduce;
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
