package the_gatherer.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.BaseModCardTags;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.blue.Defend_Blue;
import com.megacrit.cardcrawl.cards.green.Defend_Green;
import com.megacrit.cardcrawl.cards.red.Defend_Red;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class ColorfulStone extends CustomRelic {
	public static final String ID = "ColorfulStone";

	public ColorfulStone() {
		super(ID, new Texture("img/relics/" + ID + ".png"),
				RelicTier.COMMON, LandingSound.SOLID);
	}

	@Override
	public void onEquip() {
		CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
		for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
			if (c.hasTag(BaseModCardTags.BASIC_DEFEND)) {
				group.addToRandomSpot(c);
			}
		}
		CardGroup newGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
		for (int i = 0; i < 3; i++) {
			if (group.size() <= i) break;
			AbstractCard c = group.group.get(i);
			AbstractDungeon.player.masterDeck.removeCard(c);
			AbstractCard newcard =
					i == 0 ? new Defend_Red() :
							i == 1 ? new Defend_Green() : new Defend_Blue();
			if (c.upgraded)
				newcard.upgrade();
			newcard.baseBlock = c.baseBlock + 1;
			newGroup.addToBottom(newcard);
		}
		float dx = 0;
		for (AbstractCard c : newGroup.group) {
			AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(c, (float) Settings.WIDTH / 3.0F + dx, (float) Settings.HEIGHT / 2.0F, false));
			dx += (float) Settings.WIDTH / 6.0F;
		}
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new ColorfulStone();
	}
}
