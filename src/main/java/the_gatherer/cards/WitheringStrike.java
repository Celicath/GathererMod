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
import com.megacrit.cardcrawl.powers.StrengthPower;
import the_gatherer.GathererMod;
import the_gatherer.patches.CardColorEnum;

public class WitheringStrike extends CustomCard {
	private static final String RAW_ID = "WitheringStrike";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final AbstractCard.CardType TYPE = CardType.ATTACK;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 8;
	private static final int UPGRADE_BONUS = 3;
	private static final int THRESHOLD = -2;
	private static final int THRESHOLD_BONUS = -1;

	public WitheringStrike() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = POWER;
		this.baseMagicNumber = THRESHOLD;
		this.magicNumber = this.baseMagicNumber;
		this.tags.add(CardTags.STRIKE);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m,
				new DamageInfo(p, this.damage, this.damageTypeForTurn),
				AbstractGameAction.AttackEffect.SLASH_VERTICAL));
		StrengthPower sp = (StrengthPower) m.getPower(StrengthPower.POWER_ID);
		if (sp == null || sp.amount > this.magicNumber)
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, p, new StrengthPower(m, -1), -1));
	}

	public AbstractCard makeCopy() {
		return new WitheringStrike();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
			this.upgradeMagicNumber(THRESHOLD_BONUS);
		}
	}
}