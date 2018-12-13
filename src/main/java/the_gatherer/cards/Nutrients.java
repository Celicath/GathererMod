package the_gatherer.cards;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import the_gatherer.GathererMod;
import the_gatherer.interfaces.OnObtainEffect;
import the_gatherer.patches.CardColorEnum;
import the_gatherer.patches.CustomTags;

public class Nutrients extends CustomCard implements OnObtainEffect {
	private static final String RAW_ID = "Nutrients";
	public static final String ID = GathererMod.makeID(RAW_ID);
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.GATHERER_LIME;
	private static final AbstractCard.CardRarity RARITY = CardRarity.UNCOMMON;
	private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

	private static final int POWER = 12;
	private static final int UPGRADE_BONUS = 4;

	public Nutrients() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
		this.baseBlock = POWER;
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, block));
	}

	public AbstractCard makeCopy() {
		return new Nutrients();
	}

	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			this.upgradeBlock(UPGRADE_BONUS);
		}
	}

	@Override
	public void onObtain() {
		int effectCount = 0;
		for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
			if (c.hasTag(CustomTags.FLOWER) && c.canUpgrade()) {
				if (c.canUpgrade()) {
					if (effectCount < 20) {
						float x = MathUtils.random(0.1F, 0.9F) * Settings.WIDTH;
						float y = MathUtils.random(0.2F, 0.8F) * Settings.HEIGHT;

						AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c
								.makeStatEquivalentCopy(), x, y));
						AbstractDungeon.topLevelEffectsQueue.add(new UpgradeShineEffect(x, y));

						effectCount++;
					}
					c.upgrade();
				}
			}
		}
	}
}