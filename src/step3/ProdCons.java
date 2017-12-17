package step3;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import common.Debugger;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	Integer capacity;
	Queue<Message> queue;

	Semaphore fifoProducer;
	Semaphore fifoConsumer;
	Semaphore mutex;

	private int msgNumber = 0;

	public ProdCons(Integer capacity) {
		super();
		this.capacity = capacity;
		this.queue = new LinkedList<Message>();
		fifoProducer = new Semaphore(capacity, true);
		fifoConsumer = new Semaphore(0, true);
		mutex = new Semaphore(1, true);
	}

	@Override
	public int enAttente() {
		return queue.size();
	}

	public void setMessageId(MessageX msg) {
		msg.setId(msgNumber);
		msgNumber++;
	}

	@Override
	public Message get(_Consommateur arg0) throws Exception, InterruptedException {
		Message resultingMessage;
		fifoConsumer.acquire();
		mutex.acquire();

		resultingMessage = queue.poll();
		if (resultingMessage == null) {
			throw new Exception("Couldn't poll message");
		}
		Debugger.log("get by " + arg0.toString() + " of " + resultingMessage.toString() + " with " + enAttente()
				+ " messages remaining in buffer");
		mutex.release();
		fifoProducer.release();
		return resultingMessage;
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		fifoProducer.acquire();
		mutex.acquire();
		setMessageId((MessageX) arg1);
		queue.add(arg1);
		Debugger.log("put by " + arg0.toString() + " of " + arg1.toString() + " with remaining capacity = "
				+ (taille() - enAttente()));
		mutex.release();
		fifoConsumer.release();
	}

	@Override
	public int taille() {
		return getCapacity();
	}

	// Getters
	private Integer getCapacity() {
		return capacity;
	}

}
