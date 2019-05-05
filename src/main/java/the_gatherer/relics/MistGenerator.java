package the_gatherer.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import the_gatherer.GathererMod;

public class MistGenerator extends CustomRelic {
	private static final String RelicID = "MistGenerator";
	public static final String ID = GathererMod.makeID(RelicID);
	public static final int AMOUNT = 8;

	public MistGenerator() {
		super(ID, new Texture(GathererMod.GetRelicPath(RelicID)),
				RelicTier.COMMON, LandingSound.FLAT);
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new MistGenerator();
	}
}
