package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.powers.GlowPower;

public class Light extends CustomCard {
	private static final String RAW_ID = "Light";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = CardColor.COLORLESS;
	private static final CardRarity RARITY = CardRarity.SPECIAL;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int POWER = 7;
	private static final int UPGRADE_BONUS = 3;
	private static final int BONUS = 1;

	public Light() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.baseBlock = POWER;
		this.magicNumber = BONUS;
		this.baseMagicNumber = this.magicNumber;
	}

	private int applyGlowPower(int b) {
		if (AbstractDungeon.player.hasPower(GlowPower.POWER_ID)) {
			b += this.magicNumber * AbstractDungeon.player.getPower(GlowPower.POWER_ID).amount;
		}
		return b;
	}

	@Override
	public void applyPowers() {
		int prev = baseBlock;
		baseBlock = applyGlowPower(baseBlock);
		super.applyPowers();
		this.baseBlock = prev;
		if (this.baseBlock != this.block)
			this.isBlockModified = true;
	}

	@Override
	public void calculateCardDamage(final AbstractMonster mo) {
		int prev = baseBlock;
		baseBlock = applyGlowPower(baseBlock);
		super.calculateCardDamage(mo);
		this.baseBlock = prev;
		if (this.baseBlock != this.block)
			this.isBlockModified = true;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new GlowPower(p, 1), 1));
	}

	public AbstractCard makeCopy() {
		return new Light();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBlock(UPGRADE_BONUS);
		}
	}
}
