package the_gatherer.cards.Helper;

public class GathererCardHelper {
	public static String FlowerSuffix(int upgrade_count) {
		switch (upgrade_count) {
			case 0:
				return " NL Can be upgraded 3 times.";
			case 1:
				return " NL Can be upgraded 2 more times.";
			case 2:
				return " NL Can be upgraded 1 more time.";
			case 3:
				return "";
			default:
				return "";
		}
	}
}
