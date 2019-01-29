package the_gatherer.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.Sozu;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import the_gatherer.GathererMod;
import the_gatherer.modules.PotionSackPopUp;
import the_gatherer.potions.LesserExplosivePotion;
import the_gatherer.potions.SackPotion;
import the_gatherer.powers.BomberFormPower;
import the_gatherer.powers.RecipeChangePower;

import java.util.ArrayList;


public class ObtainLesserPotionAction extends AbstractGameAction {
	private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("Gatherer:ObtainLesserPotionAction");
	public static final String[] TEXT = uiStrings.TEXT;
	private SackPotion potion;
	private boolean allowRecipeChange;

	public ObtainLesserPotionAction(SackPotion potion, boolean allowRecipeChange) {
		this.actionType = ActionType.SPECIAL;
		this.duration = Settings.ACTION_DUR_XFAST;
		this.potion = potion;
		this.allowRecipeChange = allowRecipeChange;
	}

	public void update() {
		if (this.duration == Settings.ACTION_DUR_XFAST) {
			if (AbstractDungeon.player.hasRelic(Sozu.ID)) {
				AbstractDungeon.player.getRelic(Sozu.ID).flash();
			} else {
				BomberFormPower bfp = (BomberFormPower) AbstractDungeon.player.getPower(BomberFormPower.POWER_ID);
				if (bfp != null) {
					potion = new LesserExplosivePotion();
				} else {
					RecipeChangePower rcp = (RecipeChangePower) AbstractDungeon.player.getPower(RecipeChangePower.POWER_ID);
					if (rcp != null && allowRecipeChange && AbstractDungeon.cardRandomRng.random(99) < rcp.ratio) {
						this.potion = (SackPotion) rcp.potion.makeCopy();
						rcp.flash();
					}
				}
				if (!GathererMod.potionSack.addPotion(this.potion)) {
					AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0f,
							TEXT[0] + this.potion.name + TEXT[1], true));

					ArrayList<AbstractMonster> tmp = new ArrayList<>();
					for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
						if (!m.halfDead && !m.isDying && !m.isEscaping) {
							tmp.add(m);
						}
					}
					if (tmp.size() > 0) {
						AbstractCreature target = tmp.get(AbstractDungeon.cardRandomRng.random(tmp.size() - 1));
						PotionSackPopUp.usePotionInSack(target, potion);
					}
				}
			}
		}
		this.tickDuration();
	}
}
