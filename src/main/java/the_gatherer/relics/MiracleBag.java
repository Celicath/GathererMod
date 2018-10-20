package the_gatherer.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import the_gatherer.GathererMod;

import static the_gatherer.GathererMod.logger;

public class MiracleBag extends CustomRelic {
	private static final String RelicID = "MiracleBag";
	public static final String ID = GathererMod.makeID(RelicID);

	boolean upgraded = false;  // If this relic replaced Alchemy Bag, it is upgraded.

	public MiracleBag() {
		super(ID, new Texture("img/relics/" + RelicID + ".png"),
				RelicTier.BOSS, LandingSound.FLAT);
	}

	@Override
	public void onEquip() {
		AbstractPlayer p = AbstractDungeon.player;
		if (p.hasRelic(AlchemyBag.ID)) {
			p.potions.add(new PotionSlot(p.potionSlots));
			p.potionSlots++;
			p.loseRelic(AlchemyBag.ID);
			upgraded = true;
		} else upgraded = false;
	}

	@Override
	public void atBattleStart() {
		AbstractPotion p;
		if (upgraded)
			p = AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.RARE, false);
		else p = AbstractDungeon.returnRandomPotion();
		AbstractDungeon.player.obtainPotion(p);
	}

	@Override
	public String getUpdatedDescription() {
		try {
			if (upgraded || AbstractDungeon.player.hasRelic(AlchemyBag.ID))
				return DESCRIPTIONS[1];
			else return DESCRIPTIONS[0];
		} catch (NullPointerException npe) {
			logger.info("Miracle Bag: " + npe.getMessage());
			return DESCRIPTIONS[1];
		}
	}

	@Override
	public AbstractRelic makeCopy() {
		return new MiracleBag();
	}
}