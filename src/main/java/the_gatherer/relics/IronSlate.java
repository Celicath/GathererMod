package the_gatherer.relics;

import basemod.abstracts.CustomRelic;
import basemod.helpers.BaseModCardTags;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import the_gatherer.GathererMod;

public class IronSlate extends CustomRelic {
	private static final String RelicID = "IronSlate";
	public static final String ID = GathererMod.makeID(RelicID);

	public IronSlate() {
		super(ID, new Texture(GathererMod.GetRelicPath(RelicID)),
				RelicTier.COMMON, LandingSound.SOLID);
	}

	@Override
	public void onEquip() {
		CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
		for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
			if (c.hasTag(BaseModCardTags.BASIC_STRIKE)) {
				group.addToRandomSpot(c);
			}
		}
		CardGroup newGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
		for (int i = 0; i < 2; i++) {
			if (group.size() <= i) break;
			AbstractCard c = group.group.get(i);
			AbstractDungeon.player.masterDeck.removeCard(c);
			AbstractCard newcard = new Strike_Red();
			if (c.upgraded)
				newcard.upgrade();
			newcard.misc = 2;
			newcard.baseDamage = c.baseDamage + 2;
			newGroup.addToBottom(newcard);
		}
		float dx = 0;
		for (AbstractCard c : newGroup.group) {
			AbstractDungeon.topLevelEffectsQueue.add(new ShowCardAndObtainEffect(c, (float) Settings.WIDTH * 0.4F + dx, (float) Settings.HEIGHT / 2.0F, false));
			dx += (float) Settings.WIDTH * 0.2F;
		}
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new IronSlate();
	}
}
