package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.actions.ModifyMagicNumberAction;
import the_gatherer.actions.PotionUsedAction;
import the_gatherer.patches.CardColorEnum;

public class SalvePotion extends CustomCard {
	private static final String RAW_ID = "SalvePotion";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int POWER = 3;
	private static final int UPGRADE_BONUS = 2;

	public SalvePotion() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;
		this.tags.add(CardTags.HEALING);
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new HealAction(p, p, this.magicNumber));
		AbstractDungeon.actionManager.addToBottom(new ModifyMagicNumberAction(this.uuid, -1));
		AbstractDungeon.actionManager.addToBottom(new PotionUsedAction());

		this.rawDescription = DESCRIPTION;
		this.initializeDescription();
	}

	public AbstractCard makeCopy() {
		return new SalvePotion();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_BONUS);
		}
	}
}
