package the_gatherer.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import the_gatherer.GathererMod;
import the_gatherer.actions.ObtainLesserPotionAction;

public class Leftovers extends CustomRelic {
	private static final String RelicID = "Leftovers";
	public static final String ID = GathererMod.makeID(RelicID);

	public Leftovers() {
		super(ID, new Texture(GathererMod.GetRelicPath(RelicID)),
				RelicTier.RARE, LandingSound.MAGICAL);
	}

	@Override
	public void onUsePotion() {
		if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
			if (AbstractDungeon.cardRandomRng.random(99) < 20) {
				this.flash();
				AbstractDungeon.actionManager.addToBottom(new ObtainLesserPotionAction(GathererMod.returnRandomLesserPotion(), true));
			}
		}
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new Leftovers();
	}
}
