package step2;

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

	protected Consommateur(TestProdCons simulator, int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
			ProdCons buffer) throws ControlException {

		super(Type.typeConsommateur.getValue(), new Observateur(), moyenneTempsDeTraitement,
				deviationTempsDeTraitement);
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
				System.out.println(this.toString() + " received message " + this.getBuffer().get(this).toString());
				sleep(timeToConsume);
				newMessageConsumed();
			} catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
			}
		}
		System.out.println(this.toString() + " dies");
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
