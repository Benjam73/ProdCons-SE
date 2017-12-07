package step1;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	private String msg;

	public MessageX(String msg) {
		this.msg = msg;
	}

	public String toString() {
		try {
			throw new Exception("Not implemented yet");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
