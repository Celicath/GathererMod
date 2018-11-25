package the_gatherer.cards;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import the_gatherer.GathererMod;
import the_gatherer.actions.IncreaseGrowBookMiscAction;
import the_gatherer.cards.Helper.AbstractTaggedCard;
import the_gatherer.patches.CardColorEnum;

public class GrowBook extends AbstractTaggedCard {
	private static final String RAW_ID = "GrowBook";
	public static final String ID = GathererMod.makeID(RAW_ID);

	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String IMG = GathererMod.GetCardPath(RAW_ID);
	public static final String[] IMGS = new String[] {
			GathererMod.GetCardPath(RAW_ID + "_R"),
			GathererMod.GetCardPath(RAW_ID + "_G"),
			GathererMod.GetCardPath(RAW_ID + "_B")
	};
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
	private static final AbstractCard.CardType TYPE = AbstractCard.CardType.SKILL;
	private static final AbstractCard.CardColor COLOR = CardColorEnum.LIME;
	private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.RARE;
	private static final AbstractCard.CardTarget TARGET = AbstractCard.CardTarget.SELF;

	private static final int POWER = 8;
	private static final int UPGRADE_BONUS = 3;
	public static final int TRANSFORM_PLAYS = 3;

	@Override
	public void setTag(int tagNo) {
		if (this.misc >= 0) {
			int played = this.misc % TRANSFORM_PLAYS;
			int classNo = this.misc / TRANSFORM_PLAYS;
			this.rawDescription = EXTENDED_DESCRIPTION[0] + EXTENDED_DESCRIPTION[1 + played] + EXTENDED_DESCRIPTION[4 + classNo];
			this.initializeDescription();
			this.loadCardImage(GathererMod.GetCardPath(RAW_ID));

			if (classNo < IMGS.length) {
				this.loadCardImage(IMGS[classNo]);
			}
		}

		super.setTag(tagNo);
	}

	@Override
	public String getTagName(int tag) {
		switch (tag / TRANSFORM_PLAYS) {
			case -1:
				return "";
			default:
				return "<" + GathererMod.growBookCharacter.get(tag / TRANSFORM_PLAYS) + ">";
		}
	}

	public GrowBook() {
		super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

		this.misc = -1;
		this.baseBlock = POWER;
		this.rawDescription = DESCRIPTION;
		this.exhaust = true;

		this.tags.add(CardTags.HEALING);

		this.initializeDescription();
	}

	@Override
	public void update() {
		if (this.misc == -1 && AbstractDungeon.miscRng != null && AbstractDungeon.player != null) {
			GathererMod.logger.debug(GathererMod.growBookCharacter.size());
			this.misc = AbstractDungeon.miscRng.random(GathererMod.growBookCharacter.size() - 1) * TRANSFORM_PLAYS;
			setTag(this.misc);
		}
		super.update();
	}

	@Override
	public void initializeDescription() {
		super.initializeDescription();
		if (this.misc / TRANSFORM_PLAYS >= 0) {
			keywords.add("grow book tooltip " + this.misc / TRANSFORM_PLAYS);
		}
	}

	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new IncreaseGrowBookMiscAction(this.uuid, this.misc, 1, upgraded));
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));
	}

	public AbstractCard makeCopy() {
		return new GrowBook();
	}

	public void upgradeEffect() {
		this.upgradeBlock(UPGRADE_BONUS);
	}
}
