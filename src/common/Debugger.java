package common;

public class Debugger {

	static boolean enabled = true;

	public static boolean isEnabled() {
		return enabled;
	}

	public static void log(Object o) {
		if (Debugger.isEnabled()) {
			System.out.println(o.toString());
		}
	}
}