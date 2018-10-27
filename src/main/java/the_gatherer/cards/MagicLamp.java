package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.interfaces.OnObtainEffect;
import the_gatherer.patches.CardColorEnum;

public class MagicLamp extends CustomCard implements OnObtainEffect {
	private static final String RAW_ID = "MagicLamp";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardColor COLOR = CardColorEnum.LIME;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int POWER = 1;
	private static final int UPGRADE_BONUS = 1;

	public MagicLamp() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseMagicNumber = POWER;
		this.magicNumber = this.baseMagicNumber;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, this.magicNumber));
	}

	public AbstractCard makeCopy() {
		return new MagicLamp();
	}

	public void upgrade() {
		if (!this.upgraded) {
			this.upgradeName();
			upgradeMagicNumber(UPGRADE_BONUS);
			this.rawDescription = UPGRADE_DESCRIPTION;
			this.initializeDescription();
		}
	}

	@Override
	public void onObtain() {
		AbstractDungeon.topLevelEffectsQueue.add(new com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect(new Shadow(), Settings.WIDTH / 3.0F, Settings.HEIGHT / 2.0F, false));
		AbstractDungeon.topLevelEffectsQueue.add(new com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect(new Light(), Settings.WIDTH * 2 / 3.0F, Settings.HEIGHT / 2.0F, false));
	}
}
