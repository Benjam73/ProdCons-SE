package step1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {

	// Number of messages already produced by Producteur
	private Integer alreadyProduced;
	private int nbMessageToProduce;
	private ProdCons buffer;
	private Aleatoire productionDurationRandomVariable;

	protected Producteur(int moyenneTempsDeTraitement, int deviationTempsDeTraitement, int nbMessageToProduce,
			ProdCons buffer) throws ControlException {
		super(Type.typeProducteur.getValue(), new Observateur(), moyenneTempsDeTraitement, deviationTempsDeTraitement);
		// TODO Auto-generated constructor stub
		this.nbMessageToProduce = nbMessageToProduce;
		this.buffer = buffer;
		productionDurationRandomVariable = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		alreadyProduced = 0;

	}

	@Override
	public void run() {
		// Put n in buffer
		while (alreadyProduced < nbMessageToProduce) {
			MessageX newMessage = new MessageX(this);
			int timeToWait = productionDurationRandomVariable.next();
			try {
				sleep(timeToWait);
				this.buffer.put(this, newMessage);
				alreadyProduced++;
			} catch (Exception e) {
				e.getMessage();
			}
		}

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

}
