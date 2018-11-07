package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import the_gatherer.GathererMod;
import the_gatherer.patches.CardColorEnum;

import java.lang.reflect.Method;

public class Flexibility extends CustomCard {
	private static final String RAW_ID = "Flexibility";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 3;
	private static final int UPGRADE_BONUS = 1;

	public Flexibility() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;
	}

	private void calculateBlock(AbstractMonster m) {
		this.baseBlock = 0;
		for (DamageInfo di : m.damage) {
			this.baseBlock += di.output;
		}
		this.baseBlock = this.baseBlock * this.magicNumber / 6;
		try {
			Method method = AbstractCard.class.getDeclaredMethod("applyPowersToBlock");
			method.setAccessible(true);
			method.invoke(this);
		} catch (Exception e) {
			GathererMod.logger.info(e.getMessage());
			this.block = -1;
		}
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);
		if (mo == null || mo.intent != AbstractMonster.Intent.ATTACK && mo.intent != AbstractMonster.Intent.ATTACK_BUFF && mo.intent != AbstractMonster.Intent.ATTACK_DEBUFF && mo.intent != AbstractMonster.Intent.ATTACK_DEFEND) {
			calculateBlock(mo);
			this.rawDescription = (upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION) + EXTENDED_DESCRIPTION[0];
			this.initializeDescription();
		}
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		calculateBlock(m);
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new DrawCardNextTurnPower(p, 2), 2));

		this.rawDescription = (upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION);
		this.initializeDescription();
	}

	public AbstractCard makeCopy() {
		return new Flexibility();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_BONUS);

			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}
}
