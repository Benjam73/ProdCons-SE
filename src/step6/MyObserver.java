package step6;

import java.util.List;

import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import step5.Consommateur;
import step5.Producteur;

public class MyObserver {

	private boolean coherence;
	private int nbProducer;
	private int nbConsumer;
	private int bufferSize;
	private ProdCons messageList;
	private List<Consommateur> consumerList;
	private List<Producteur> producerList;

	public MyObserver() {
		coherence = true;
	}

	public boolean getCoherence() {
		return this.coherence;
	}

	public void init(int nbProducer, int nbConsumer, int bufferSize) throws ControlException {
		if (nbProducer < 0 || nbConsumer < 0 || bufferSize < 0) {
			coherence = false;
		}
		this.nbProducer = nbProducer;
		this.nbConsumer = nbConsumer;
		this.bufferSize = bufferSize;
		messageList = new ProdCons(bufferSize);

		if (!coherence) {
			throw new ControlException(this.getClass(), "init");
		}
	}

	public void newProducer(Producteur newProducer) throws ControlException {
		if (newProducer == null) {
			coherence = false;
		}
		if (producerList.contains(newProducer)) {
			coherence = false;
		}
		if (producerList.size() >= nbProducer) {
			coherence = false;
		}

		producerList.add(newProducer);

		if (coherence == false) {
			throw new ControlException(this.getClass(), "newProducer");
		}
	}

	public void newConsumer(Consommateur newConsumer) throws ControlException {
		if (newConsumer == null) {
			coherence = false;
		}
		if (consumerList.contains(newConsumer)) {
			coherence = false;
		}
		if (consumerList.size() >= nbConsumer) {
			coherence = false;
		}

		consumerList.add(newConsumer);

		if (coherence == false) {
			throw new ControlException(this.getClass(), "newConsumer");
		}
	}

	public void messageProduction(Producteur producer, Message message, int duration) {

	}

}
