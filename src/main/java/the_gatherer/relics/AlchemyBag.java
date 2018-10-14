package the_gatherer.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import the_gatherer.actions.ObtainLesserPotionAction;
import the_gatherer.patches.PotionRarityEnum;

public class AlchemyBag extends CustomRelic {
	public static final String ID = "AlchemyBag";

	public AlchemyBag() {
		super(ID, new Texture("img/relics/" + ID + ".png"),
				RelicTier.STARTER, LandingSound.FLAT);
	}

	@Override
	public void atBattleStart() {
		this.flash();
		AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(AbstractDungeon.returnRandomPotion(PotionRarityEnum.LESSER, true)));
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
