package the_gatherer.interfaces;

public interface OnceEffect {
	// Called when you already played it at least once
	void notSingleEffect();
	// Called when you first use this unique card
	void singleEffect();
}
