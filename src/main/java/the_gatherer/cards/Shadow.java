package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.powers.LightPower;
import the_gatherer.powers.ShadowPower;

public class Shadow extends CustomCard {
	private static final String RAW_ID = "Shadow";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.SPECIAL;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 9;
	private static final int UPGRADE_BONUS = 4;

	public Shadow() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.baseDamage = POWER;
		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;
	}

	@Override
	public void applyPowers() {
		this.baseDamage = this.magicNumber;
		if (AbstractDungeon.player.hasPower(LightPower.POWER_ID)) {
			this.baseDamage *= 2;
		}
		super.applyPowers();
		if (AbstractDungeon.player.hasPower(LightPower.POWER_ID)) {
			this.isDamageModified = true;
		}
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new ShadowPower(p)));
		this.baseDamage = this.magicNumber;
	}

	public AbstractCard makeCopy() {
		return new Shadow();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
			this.upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
