package step5;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import common.Debugger;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	Integer capacity;
	Queue<Message> queue;

	Lock lock;
	Condition bufferNotFull;
	Condition bufferNotEmpty;

	private int msgNumber = 0;

	public ProdCons(Integer capacity) {
		super();
		this.capacity = capacity;
		this.queue = new LinkedList<Message>();
		lock = new ReentrantLock();
		bufferNotEmpty = lock.newCondition();
		bufferNotFull = lock.newCondition();
	}

	@Override
	public int enAttente() {
		try {
			lock.lock();
			return queue.size();
		} finally {
			lock.unlock();
		}

	}

	public void setMessageId(MessageX msg) {

		msg.setId(msgNumber);
		msgNumber++;

	}

	@Override
	public Message get(_Consommateur arg0) throws Exception, InterruptedException {
		lock.lock();
		try {
			Message resultingMessage;
			while (enAttente() == 0) {
				bufferNotEmpty.await();
			}
			resultingMessage = queue.poll();
			Debugger.log("get by " + arg0.toString() + " of " + resultingMessage.toString() + " with " + enAttente()
					+ " messages remaining in buffer");
			if (resultingMessage == null) {
				throw new Exception("Couldn't poll message");
			}

			bufferNotFull.signal();
			return resultingMessage;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception, InterruptedException {
		lock.lock();
		try {
			while (enAttente() == taille()) {
				bufferNotFull.await();
			}
			setMessageId((MessageX) arg1);
			queue.add(arg1);
			Debugger.log("put by " + arg0.toString() + " of " + arg1.toString() + " with remaining capacity = "
					+ (taille() - enAttente()));
			bufferNotEmpty.signal();
		} finally {
			lock.unlock();
		}

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
