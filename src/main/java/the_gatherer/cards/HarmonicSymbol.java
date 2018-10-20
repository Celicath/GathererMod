package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.unique.SkewerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import the_gatherer.GathererMod;
import the_gatherer.actions.HarmonicSymbolAction;
import the_gatherer.patches.AbstractCardEnum;

public class HarmonicSymbol extends CustomCard {
	private static final String CardID = "HarmonicSymbol";
	public static final String ID = GathererMod.makeID(CardID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = "img/cards/" + CardID + ".png";
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardColor COLOR = AbstractCardEnum.LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int POWER = 3;
	private static final int UPGRADE_BONUS = 1;
	private static final int BLOCK = 3;
	private static final int BLOCK_UPGRADE = 1;

	public HarmonicSymbol() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseDamage = POWER;
		this.baseBlock = BLOCK;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		if (this.energyOnUse < EnergyPanel.totalCount) {
			this.energyOnUse = EnergyPanel.totalCount;
		}

		AbstractDungeon.actionManager.addToBottom(new HarmonicSymbolAction(p, m, this.damage, this.block, this.damageTypeForTurn, this.freeToPlayOnce, this.energyOnUse));
	}

	public AbstractCard makeCopy() {
		return new HarmonicSymbol();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			this.upgradeDamage(UPGRADE_BONUS);
			this.upgradeBlock(BLOCK_UPGRADE);
		}
	}
}
