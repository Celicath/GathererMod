package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.cards.Helper.GathererCardHelper;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.patches.CustomTags;

public class FlowerWhip extends CustomCard {
	private static final String RAW_ID = "FlowerWhip";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final CardRarity RARITY = CardRarity.BASIC;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 2;
	private static final int UPGRADE_BONUS = 1;
	private static final int MAGIC_POWER = 2;
	private static final int MAGIC_UPGRADE_BONUS = 1;

	public FlowerWhip() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = POWER;
		this.baseMagicNumber = MAGIC_POWER;
		this.magicNumber = this.baseMagicNumber;

		this.tags.add(CustomTags.FLOWER);

		this.rawDescription = GetRawDescription();
		this.initializeDescription();
	}

	private String GetRawDescription() {
		return DESCRIPTION + GathererCardHelper.FlowerSuffix(timesUpgraded);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		for (int i = 0; i < this.magicNumber; ++i) {
			AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
		}
	}

	public AbstractCard makeCopy() {
		return new FlowerWhip();
	}

	@Override
	public boolean canUpgrade() {
		return timesUpgraded < 3;
	}

	public void upgrade() {
		if (timesUpgraded < 3) {
			upgradeDamage(UPGRADE_BONUS);
			++this.timesUpgraded;
			if (timesUpgraded == 3)
				upgradeMagicNumber(MAGIC_UPGRADE_BONUS);
			this.upgraded = true;
			this.name = NAME + "+" + this.timesUpgraded;
			this.initializeTitle();
			this.rawDescription = GetRawDescription();
			this.initializeDescription();
		}
	}
}
