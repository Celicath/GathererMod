package the_gatherer.interfaces;

public interface OnceEffect {
	// Called when this it not the first time you use this unique card.
	void notFirstTimeEffect();

	// Called when this is the first time you use this unique card.
	void firstTimeEffect();

	// Update Once effect text.
	void updateOnceText();
}
