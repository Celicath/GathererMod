package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.common.RefundAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.GathererMod;

public class PoisonMasteryPower extends AbstractPower {
	private static final String RAW_ID = "PoisonMastery";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public PoisonMasteryPower(AbstractCreature owner, int amount) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = amount;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));
	}

	@Override
	public void onPlayCard(AbstractCard card, AbstractMonster m) {
		if (card.costForTurn > 0) {
			String lowered = card.rawDescription.toLowerCase();
			boolean containsPosion =
					card.keywords != null && (card.keywords.contains("poison")
							|| card.keywords.contains(GameDictionary.POISON.NAMES[0]))
							|| lowered.contains("poison");

			if (!containsPosion) {
				for (String word : GameDictionary.POISON.NAMES) {
					if (lowered.contains(word)) {
						containsPosion = true;
						break;
					}
				}
			}

			if (containsPosion) {
				this.flash();
				AbstractDungeon.actionManager.addToBottom(new RefundAction(card, this.amount));
			}
		}
	}

	public void updateDescription() {
		this.description = DESCRIPTIONS[0] + (this.amount * 100) + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
	}
}
