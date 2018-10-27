package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import the_gatherer.GathererMod;
import the_gatherer.actions.ScrollOfPurityAction;
import the_gatherer.interfaces.OnUsePotionEffect;
import the_gatherer.patches.CardColorEnum;

public class ScrollOfPurity extends CustomCard implements OnUsePotionEffect {
	private static final String RAW_ID = "ScrollOfPurity";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = -2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.LIME;
	private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.NONE;

	private static final int POWER = 1;
	private static final int UPGRADE_BONUS = 1;

	public ScrollOfPurity() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
	}

	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		this.cantUseMessage = EXTENDED_DESCRIPTION[0];
		return false;
	}

	public AbstractCard makeCopy() {
		return new ScrollOfPurity();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeMagicNumber(UPGRADE_BONUS);
		}
	}

	@Override
	public void onUsePotion(AbstractPotion p) {
		AbstractDungeon.actionManager.addToBottom(new ScrollOfPurityAction(this.magicNumber));
	}
}
