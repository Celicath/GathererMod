package the_gatherer.relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import the_gatherer.GathererMod;
import the_gatherer.cards.Helper.AbstractTaggedCard;

import java.util.HashSet;

public class FlyingFruit extends CustomRelic implements CustomSavable<HashSet<String>> {
	private static final String RelicID = "FlyingFruit";
	public static final String ID = GathererMod.makeID(RelicID);
	public HashSet<String> exhaustedUniqueID = new HashSet<>();

	private final int AMOUNT = 5;

	public FlyingFruit() {
		super(ID, new Texture(GathererMod.GetRelicPath(RelicID)),
				RelicTier.RARE, LandingSound.FLAT);
		this.counter = 0;
	}

	@Override
	public void onExhaust(AbstractCard card) {
		if (exhaustedUniqueID.size() < 10 && !exhaustedUniqueID.contains(GathererMod.getUniqueID(card))) {
			exhaustedUniqueID.add(GathererMod.getUniqueID(card));
			switch (exhaustedUniqueID.size()) {
				case 1:
				case 3:
				case 6:
				case 10:
					this.flash();
					AbstractDungeon.player.increaseMaxHp(AMOUNT, true);
			}
			this.counter = exhaustedUniqueID.size();
			this.description = this.getUpdatedDescription();
			this.tips.clear();
			this.tips.add(new PowerTip(this.name, this.description));
			this.initializeTips();
		}
	}

	@Override
	public void onLoad(HashSet<String> data) {
		exhaustedUniqueID = data;
		this.description = this.getUpdatedDescription();
		this.tips.clear();
		this.tips.add(new PowerTip(this.name, this.description));
		this.initializeTips();
	}

	@Override
	public HashSet<String> onSave() {
		return exhaustedUniqueID;
	}

	@Override
	public String getUpdatedDescription() {
		if (exhaustedUniqueID == null || exhaustedUniqueID.isEmpty()) {
			return DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1];
		} else {
			boolean first = true;
			StringBuilder result = new StringBuilder(DESCRIPTIONS[0] + AMOUNT + DESCRIPTIONS[1] + DESCRIPTIONS[2]);
			for (String id : exhaustedUniqueID) {
				if (!first) {
					result.append(", ");
				} else {
					first = false;
				}

				try {
					int index = id.indexOf("|Tag=");
					if (index >= 0) {
						AbstractCard c = CardLibrary.getCard(id.substring(0, index)).makeCopy();
						AbstractTaggedCard atc = (AbstractTaggedCard) c;
						atc.setTag(Integer.parseInt(id.substring(index + 5)));
						result.append(atc.name);
					} else {
						AbstractCard c = CardLibrary.getCard(id);
						result.append(c.name);
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.append(GathererMod.stripPrefix(id));
				}
			}
			return result.toString();
		}
	}

	@Override
	public AbstractRelic makeCopy() {
		return new FlyingFruit();
	}
}
