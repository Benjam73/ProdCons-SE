package step3;

public enum Type {
	typeProducteur(1), typeConsommateur(2);

	private int value;

	Type(int value) {
		this.value = value;
	}

	protected int getValue() {
		return value;
	}
}
