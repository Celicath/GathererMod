package the_gatherer.cards;

import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.cards.Helper.AbstractNumberedCard;
import the_gatherer.interfaces.ColoredTextCard;
import the_gatherer.patches.CardColorEnum;

public class WoolGloves extends AbstractNumberedCard implements ColoredTextCard {
	private static final String RAW_ID = "WoolGloves";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int POWER = 8;
	private static final int UPGRADE_BONUS = 3;

	public WoolGloves() {
		super(ID, NAME, IMG, COST, DESCRIPTION, EXTENDED_DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.baseBlock = POWER;
		updateDescription();
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

		if (playCount == 0 || upgraded && playCount == 1) {
			AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, 3));
			AbstractDungeon.actionManager.addToBottom(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, 3, false));
		}
		addPlayCount();
	}

	public AbstractCard makeCopy() {
		return new WoolGloves();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeBlock(UPGRADE_BONUS);
			upgraded = true;
			updateDescription();
		}
	}
}
