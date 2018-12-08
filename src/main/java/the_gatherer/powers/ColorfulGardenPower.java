package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.GathererMod;
import the_gatherer.cards.ColorfulGarden;

import java.util.HashSet;

public class ColorfulGardenPower extends AbstractPower {
	private static final String RAW_ID = "ColorfulGarden";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	private int power;

	private HashSet<Class<? extends AbstractCard>> playedCards = new HashSet<>();

	public ColorfulGardenPower(int power) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = AbstractDungeon.player;
		this.power = power;
		this.amount = 0;
		updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));

		addCard(ColorfulGarden.class);
	}

	@Override
	public void updateDescription() {
		this.amount = this.power * playedCards.size();
		this.description = DESCRIPTIONS[0] + this.power + DESCRIPTIONS[1] + DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3];
	}

	public void stackPower(int stackAmount) {
		this.fontScale = 8.0F;
		this.power += stackAmount;
		this.updateDescription();
	}

	@Override
	public void onPlayCard(AbstractCard card, AbstractMonster m) {
		addCard(card.getClass());
	}

	void addCard(Class<? extends AbstractCard> cardc) {
		playedCards.add(cardc);
		updateDescription();
	}

	@Override
	public void atStartOfTurn() {
		playedCards.clear();
		updateDescription();
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (isPlayer) {
			this.flash();
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(owner, owner, this.amount));
		}
	}
}
