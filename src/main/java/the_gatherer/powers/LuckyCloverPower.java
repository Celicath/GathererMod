package the_gatherer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import the_gatherer.GathererMod;

public class LuckyCloverPower extends AbstractPower {
	private static final String RAW_ID = "LuckyClover";
	public static final String POWER_ID = GathererMod.makeID(RAW_ID);
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
	private static final int HP_LOSS = 4;
	private static final int HEAL = 3;

	public LuckyCloverPower(AbstractCreature owner) {
		this.name = NAME;
		this.ID = POWER_ID;
		this.owner = owner;
		this.amount = HP_LOSS;
		this.updateDescription();
		this.type = PowerType.BUFF;
		this.isTurnBased = false;
		this.img = new Texture(GathererMod.GetPowerPath(RAW_ID));
	}

	public void stackPower(int stackAmount) {
		// Does not stack
	}

	@Override
	public int onLoseHp(int damageAmount) {
		this.amount -= damageAmount;
		if (amount <= 0) {
			this.amount = HP_LOSS;
			if (!AbstractDungeon.player.hand.isEmpty()) {
				AbstractDungeon.actionManager.addToTop(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, 1, true));
				AbstractDungeon.actionManager.addToTop(new HealAction(AbstractDungeon.player, AbstractDungeon.player, HEAL));
			}
		}
		return damageAmount;
	}

	public void updateDescription() {
		this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1] + HEAL + DESCRIPTIONS[2];
	}
}
