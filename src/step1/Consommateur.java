package step1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
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
		// TODO Auto-generated constructor stub
		this.buffer = buffer;
		consumptionDurationRandomVariable = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		alreadyConsume = 0;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (!(this.buffer.equals(null))) {
			int timeTowait = consumptionDurationRandomVariable.next();
			try {
				sleep(timeTowait);
				buffer.get(this);
				alreadyConsume++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.getMessage();
				e.printStackTrace();
			}

		}

	}

	public String toString() {
		return "[Consommateur " + this.identification() + "]";
	}

	@Override
	public int nombreDeMessages() {
		return alreadyConsume;
	}

}
