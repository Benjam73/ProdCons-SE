package step1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {

	private Aleatoire nbMessageToProduce;
	private ProdCons buffer;
	private Aleatoire treatmentTime;
	private int numberCurrentMessage;

	protected Producteur(int moyenneTempsDeTraitement, int deviationTempsDeTraitement, Aleatoire nbMessageToProduce,
			ProdCons buffer) throws ControlException {
		super(Type.typeProducteur.getValue(), new Observateur(), moyenneTempsDeTraitement, deviationTempsDeTraitement);
		// TODO Auto-generated constructor stub
		this.nbMessageToProduce = nbMessageToProduce;
		this.buffer = buffer;
		treatmentTime = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		numberCurrentMessage = 0;

	}

	@Override
	public void run() {
		// Put n in buffer
		int nbMessageMax = nbMessageToProduce.next();
		while (numberCurrentMessage < nbMessageMax) {
			MessageX newMessage = new MessageX("Teub");
			int timeToWait = treatmentTime.next();
			try {
				sleep(timeToWait);
				this.buffer.put(this, newMessage);

			} catch (Exception e) {
				e.getMessage();
			}
			numberCurrentMessage++;
		}
	}

	@Override
	public int nombreDeMessages() {
		// TODO Auto-generated method stub

		return 0;
	}

}
