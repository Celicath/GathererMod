package the_gatherer.modules;

import basemod.interfaces.ISubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.cards.AbstractCard;
import the_gatherer.interfaces.PostObtainCardSubscriber;

import java.util.ArrayList;
import java.util.Iterator;

import static basemod.BaseMod.logger;

// Parody of BaseMod; implements some fixed/missing interfaces.
@SpireInitializer
public class CaseMod {
	private static ArrayList<PostObtainCardSubscriber> postObtainCardSubscribers = new ArrayList();

	public static void publishPostObtainCard(AbstractCard c) {
		logger.info("publish on post obtain card");
		Iterator var1 = postObtainCardSubscribers.iterator();

		while (var1.hasNext()) {
			PostObtainCardSubscriber sub = (PostObtainCardSubscriber) var1.next();
			sub.receivePostObtainCard(c);
		}
	}

	// not actually unchecked because we do an isInstance check at runtime
	@SuppressWarnings("unchecked")
	private static <T> void subscribeIfInstance(ArrayList<T> list, ISubscriber sub, Class<T> clazz) {
		if (clazz.isInstance(sub)) {
			list.add((T) sub);
		}
	}

	public static void subscribe(ISubscriber sub) {
		subscribeIfInstance(postObtainCardSubscribers, sub, PostObtainCardSubscriber.class);
	}
}
