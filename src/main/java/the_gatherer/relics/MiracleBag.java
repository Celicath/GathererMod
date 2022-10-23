package the_gatherer.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import the_gatherer.GathererMod;

public class MiracleBag extends CustomRelic {
	private static final String RelicID = "MiracleBag";
	public static final String ID = GathererMod.makeID(RelicID);

	public MiracleBag() {
		super(ID, new Texture(GathererMod.GetRelicPath(RelicID)),
			RelicTier.BOSS, LandingSound.FLAT);
	}

	@Override
	public void obtain() {
		for (int i = 0; i < AbstractDungeon.player.relics.size(); ++i) {
			if (AbstractDungeon.player.relics.get(i).relicId.equals(AlchemyBag.ID)) {
				instantObtain(AbstractDungeon.player, i, true);
				return;
			}
		}
		super.obtain();
	}

	@Override
	public void atBattleStart() {
		this.flash();
		AbstractPotion p = AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.RARE, false);
		AbstractDungeon.player.obtainPotion(p);
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public boolean canSpawn() {
		return AbstractDungeon.player.hasRelic(AlchemyBag.ID);
	}

	public void onEquip() {
		AbstractDungeon.player.potionSlots += 1;
		AbstractDungeon.player.potions.add(new PotionSlot(AbstractDungeon.player.potionSlots - 1));
	}

	@Override
	public AbstractRelic makeCopy() {
		return new MiracleBag();
	}
}
