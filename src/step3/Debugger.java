package step3;

public class Debugger {

	static boolean enabled = false;

	public static boolean isEnabled() {
		return enabled;
	}

	public static void log(Object o) {
		if (Debugger.isEnabled()) {
			System.out.println(o.toString());
		}
	}
}