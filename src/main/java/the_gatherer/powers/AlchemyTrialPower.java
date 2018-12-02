package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.GathererMod;
import the_gatherer.actions.ObtainLesserPotionAction;
import the_gatherer.potions.*;

public class AlchemyTrialPower extends AbstractPower {
	private static final String RAW_ID = "AlchemyTrial";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	private static final int CARD_AMT = 10;
	private int numPotion;

	public AlchemyTrialPower(AbstractCreature owner, int power) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = CARD_AMT;
		this.numPotion = power;
		updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));
	}

	@Override
	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.numPotion += stackAmount;
		this.updateDescription();
	}

	@Override
	public void onPlayCard(AbstractCard card, AbstractMonster m) {
		this.amount--;
		if (this.amount == 0) {
			this.flash();
			this.amount = CARD_AMT;
			for (int i = 0; i < numPotion; i++) {
				boolean rng = AbstractDungeon.cardRandomRng.randomBoolean();

				AbstractPotion p;
				switch (card.type) {
					case ATTACK:
						p = rng ? new LesserFirePotion() : new LesserExplosivePotion();
						break;
					case SKILL:
						p = rng ? new LesserBlockPotion() : new LesserEnergyPotion();
						break;
					case POWER:
						p = rng ? new LesserStrengthPotion() : new LesserDexterityPotion();
						break;
					default:
						p = rng ? new LesserWeakPotion() : new LesserFearPotion();
				}

				AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(p, true));
			}
		}
		this.updateDescription();
	}

	@Override
	public void updateDescription() {
		if (this.amount == 1) {
			this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + this.numPotion + DESCRIPTIONS[3];
		} else {
			this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[2] + this.numPotion + DESCRIPTIONS[3];
		}
	}
}
