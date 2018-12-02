package the_gatherer.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import the_gatherer.GathererMod;
import the_gatherer.actions.ObtainLesserPotionAction;

public class AlchemyBag extends CustomRelic {
	private static final String RelicID = "AlchemyBag";
	public static final String ID = GathererMod.makeID(RelicID);

	public AlchemyBag() {
		super(ID, new Texture(GathererMod.GetRelicPath(RelicID)),
				RelicTier.STARTER, LandingSound.FLAT);
	}

	@Override
	public void atBattleStart() {
		this.flash();
		AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(GathererMod.returnRandomLesserPotion(), true));
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new AlchemyBag();
	}
}
