package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.Sozu;
import the_gatherer.GathererMod;
import the_gatherer.potions.LesserExplosivePotion;
import the_gatherer.powers.BomberFormPower;
import the_gatherer.powers.ExplodingPower;


public class ObtainLesserPotionAction extends AbstractGameAction {
	private AbstractPotion potion;

	public ObtainLesserPotionAction(AbstractPotion potion) {
		this.actionType = ActionType.SPECIAL;
		this.duration = Settings.ACTION_DUR_XFAST;
		this.potion = potion;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_XFAST) {
			if (AbstractDungeon.player.hasRelic(Sozu.ID)) {
				AbstractDungeon.player.getRelic(Sozu.ID).flash();
			} else if (AbstractDungeon.player.hasPower(BomberFormPower.POWER_ID)) {
				if (GathererMod.potionSack.addPotion(new LesserExplosivePotion())) {
					AbstractDungeon.player.getPower(BomberFormPower.POWER_ID).flash();
					BomberFormPower bfp = (BomberFormPower) AbstractDungeon.player.getPower(BomberFormPower.POWER_ID);

					AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ExplodingPower(AbstractDungeon.player, bfp.amount), bfp.amount));
				}
			} else {
				GathererMod.potionSack.addPotion(this.potion);
			}
		}

		this.tickDuration();
	}
}
