package the_gatherer.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.FrozenEgg2;
import com.megacrit.cardcrawl.relics.MoltenEgg2;
import com.megacrit.cardcrawl.relics.ToxicEgg2;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import the_gatherer.GathererMod;
import the_gatherer.patches.CustomTags;

public class FloralEgg extends CustomRelic {
	private static final String RelicID = "FloralEgg";
	public static final String ID = GathererMod.makeID(RelicID);

	public FloralEgg() {
		super(ID, new Texture(GathererMod.GetRelicPath(RelicID)),
				RelicTier.UNCOMMON, LandingSound.FLAT);
	}

	@Override
	public void onEquip() {
		int effectCount = 0;
		for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
			if (c.hasTag(CustomTags.FLOWER) && c.canUpgrade()) {
				if (c.canUpgrade()) {
					if (effectCount < 20) {
						float x = MathUtils.random(0.1F, 0.9F) * Settings.WIDTH;
						float y = MathUtils.random(0.2F, 0.8F) * Settings.HEIGHT;

						AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c
								.makeStatEquivalentCopy(), x, y));
						AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineEffect(x, y));

						effectCount++;
					}
					c.upgrade();
				}
			}
		}
	}

	public void onObtainCard(AbstractCard c) {
		if (c.hasTag(CustomTags.FLOWER)) {
			int times = 1;
			if (c.type == AbstractCard.CardType.ATTACK && AbstractDungeon.player.hasRelic(MoltenEgg2.ID) ||
					c.type == AbstractCard.CardType.SKILL && AbstractDungeon.player.hasRelic(ToxicEgg2.ID) ||
					c.type == AbstractCard.CardType.POWER && AbstractDungeon.player.hasRelic(FrozenEgg2.ID)
			) {
				times = 2;
			}
			times -= c.timesUpgraded;
			if (times < 1) times = 1;
			for (int i = 0; i < times; i++) {
				c.upgrade();
			}
			this.flash();
		}
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new FloralEgg();
	}
}
